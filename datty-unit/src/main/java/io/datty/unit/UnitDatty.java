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
import io.datty.api.DattyKey;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.spi.AbstractDatty;
import io.datty.support.exception.CacheExistsException;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

/**
 * Unit implementation for Datty API
 * 
 * @author dadril
 *
 */

public class UnitDatty extends AbstractDatty implements Datty, CacheManager {

	private final String name;
	private final ConcurrentMap<String, UnitCache> cacheMap = new ConcurrentHashMap<String, UnitCache>();
	private Datty currentDatty;
	
	public UnitDatty() {
		this(new Properties());
	}

	public UnitDatty(Properties props) {
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


	@Override
	public Single<DattyResult> doExecute(DattyOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> doStreamOut(DattyKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<DattyResult> doStreamIn(DattyKey key, Observable<ByteBuf> value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString() {
		return "UnitDatty [name=" + name + "]";
	}

}
