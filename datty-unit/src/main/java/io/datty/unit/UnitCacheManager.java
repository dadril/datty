/*
 * Copyright (C) 2016 Datty.io Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.datty.unit;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.Cache;
import io.datty.api.CacheExistsAction;
import io.datty.api.CacheManager;
import io.datty.api.Datty;
import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyKey;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.DattySingle;
import io.datty.api.result.ErrorResult;
import io.datty.api.result.ExecuteResult;
import io.datty.api.result.LongResult;
import io.datty.spi.AbstractDattyDriver;
import io.datty.support.exception.CacheExistsException;
import io.datty.support.exception.CacheNotFoundException;
import io.datty.unit.executor.OperationExecutor;
import io.datty.unit.executor.UnitExecutors;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Unit implementation for Datty API
 * 
 * @author dadril
 *
 */

public class UnitCacheManager extends AbstractDattyDriver implements DattySingle, CacheManager {

	private final String name;
	private final ConcurrentMap<String, UnitCache> cacheMap = new ConcurrentHashMap<String, UnitCache>();
	private Datty currentDatty;
	
	public UnitCacheManager() {
		this(new Properties());
	}

	public UnitCacheManager(Properties props) {
		this.name = props.getProperty(UnitPropertyKeys.NAME);
		this.currentDatty = this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Cache getCache(String cacheName) {
		return cacheMap.get(cacheName);
	}

	private UnitCache createAndPut(String cacheName, Properties cacheProperties) {
		UnitCache cache = new UnitCache(this, cacheName, cacheProperties);
		UnitCache c = cacheMap.putIfAbsent(cacheName, cache);
		if (c != null) {
			cache = c;
		}
		return cache;
	}
	
	@Override
	public Cache getCache(String cacheName, Properties cacheProperties, CacheExistsAction action) {
		UnitCache cache = cacheMap.get(cacheName);
		
		switch(action) {
		
		case CREATE_ONLY:
			if (cache != null) {
				throw new CacheExistsException(cacheName);
			}
			break;
		
		case CREATE_IF_NOT_EXISTS:
			if (cache == null) {
				cache = createAndPut(cacheName, cacheProperties);
			}
 			break;
 			
		case UPDATE:
			if (cache != null) {
				cache.setCacheProperties(cacheProperties);
			}
			else {
				cache = createAndPut(cacheName, cacheProperties);
			}
 			break;
 			
		}

		return cache;
	}

	@Override
	public Datty getDatty() {
		return currentDatty;
	}

	@Override
	public void setDatty(Datty newDatty) {

		if (newDatty == null) {
			throw new IllegalArgumentException("empty new datty");
		}
		
		this.currentDatty = newDatty;
	}
	
	/*
	 * -------------------------
	 * 
	 *          DATTY
	 * 
	 * -------------------------
	 */


	@SuppressWarnings("unchecked")
	@Override
	public Single<DattyResult> doExecute(DattyOperation operation) {

		UnitCache cache = cacheMap.get(operation.getCacheName());
		if (cache == null) {
			return Single.just(ErrorResult.of(ErrCode.CACHE_NOT_FOUND));
		}
		
		@SuppressWarnings("rawtypes")
		OperationExecutor executor = UnitExecutors.findExecutor(operation.getCode());
		
		if (executor == null) {
			return Single.just(ErrorResult.of(ErrCode.UNKNOWN_OPERATION));
		}
		
		return executor.execute(cache.getRecordMap(), operation);
	}

	@Override
	public Observable<DattyResult> doStreamOut(DattyKey key) {
		
		UnitCache cache = cacheMap.get(key.getCacheName());
		if (cache == null) {
			return Observable.error(new CacheNotFoundException(key.getCacheName()));
		}
		
		String majorKey = key.getMajorKey();
		if (majorKey == null) {
			return Observable.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty majorKey"));
		}
		
		UnitRecord record = cache.getRecordMap().get(majorKey);
		if (record == null) {
			return Observable.just(ExecuteResult.ofNull());
		}
		
		String minorKey = key.getMinorKey();
		if (minorKey == null) {
			return Observable.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty minorKey"));
		}
		
		ByteBuf value = record.getColumn(minorKey);
		
		return Observable.just(ExecuteResult.of(value));
	}

	@Override
	public Single<DattyResult> doStreamIn(DattyKey key, Observable<ByteBuf> value) {
		
		UnitCache cache = cacheMap.get(key.getCacheName());
		if (cache == null) {
			return Single.just(ErrorResult.of(ErrCode.CACHE_NOT_FOUND));
		}
		
		String majorKey = key.getMajorKey();
		if (majorKey == null) {
			return Single.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty majorKey"));
		}
		
		String minorKey = key.getMinorKey();
		if (minorKey == null) {
			return Single.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty minorKey"));
		}
		
		UnitRecord record = cache.getRecordMap().get(majorKey);
		if (record == null) {
			record = new UnitRecord();
			UnitRecord c = cache.getRecordMap().putIfAbsent(majorKey, record);
			if (c != null) {
				record = c;
			}
		}
		
		final ByteBuf destBuffer = record.createColumn(minorKey);
		
		record.incrementVersion();
		
		Observable<ByteBuf> result = value.reduce(destBuffer, new Func2<ByteBuf, ByteBuf, ByteBuf>() {

			@Override
			public ByteBuf call(ByteBuf dest, ByteBuf chunk) {
				dest.writeBytes(chunk);
				return dest;
			}
			
		});
		
		return result.map(new Func1<ByteBuf, DattyResult>() {

			@Override
			public DattyResult call(ByteBuf buf) {
				return new LongResult(buf.readableBytes());
			}
			
		}).toSingle();

		
	}

	@Override
	public String toString() {
		return "UnitDatty [name=" + name + "]";
	}

}
