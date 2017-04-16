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
package io.datty.aerospike;

import java.util.Enumeration;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyError;
import io.datty.api.DattyKey;
import io.datty.api.DattyStream;
import io.datty.support.exception.DattyStreamException;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * AerospikeDattyStream
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeDattyStream implements DattyStream {

	private final AerospikeCacheManager cacheManager;
	
	public AerospikeDattyStream(AerospikeCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Observable<ByteBuf> streamOut(DattyKey key) {
		
		AerospikeCache cache = cacheManager.getAerospikeCache(key.getCacheName());
		if (cache == null) {
			return Observable.error(new DattyStreamException(DattyError.ErrCode.CACHE_NOT_FOUND, key.getCacheName(), key));
		}
		
		String majorKey = key.getMajorKey();
		final String minorKey = key.getMinorKey();
		String[] binNames = new String[] { minorKey };
		
		if (majorKey == null || minorKey == null) {
			return Observable.error(new DattyStreamException(DattyError.ErrCode.BAD_ARGUMENTS, key));
		}
		
		final QueryPolicy queryPolicy = cache.getConfig().getQueryPolicy(false);
		
		ChunkEnumeration chunkEnumeration = new ChunkEnumeration(cacheManager.getConfig().getNamespace(), cache.getCacheName(), majorKey);
		Observable<Record> stream = cacheManager.getClient().streamGet(queryPolicy, chunkEnumeration, binNames, cache.streamExceptionTransformer(key));

		return stream.map(new Func1<Record, ByteBuf>() {

			@Override
			public ByteBuf call(Record rec) {
				
				if (rec != null && rec.bins != null) {
					Object aerospikeValue = rec.bins.get(minorKey);
					if (aerospikeValue != null) {
						return AerospikeValueUtil.toByteBuf(aerospikeValue);
					}
				}
				
				return null;
			}
			
		});
		
	}
	
	public final class ChunkEnumeration implements Enumeration<Key> {

		private final String namespace;
		private final String cacheName;
		private final String majorKey;
		private int chunkNumber = 1;
		
		public ChunkEnumeration(String namespace, String cacheName, String majorKey) {
			this.namespace = namespace;
			this.cacheName = cacheName;
			this.majorKey = majorKey;
		}
		
		@Override
		public boolean hasMoreElements() {
			return true;
		}

		@Override
		public Key nextElement() {
			Key key = new Key(namespace, cacheName, majorKey + chunkNumber);
			chunkNumber++;
			return key;
		}
		
	}

	@Override
	public Single<Long> streamIn(final DattyKey key, Observable<ByteBuf> value) {

		final AerospikeCache cache = cacheManager.getAerospikeCache(key.getCacheName());
		if (cache == null) {
			return Single.error(new DattyStreamException(DattyError.ErrCode.CACHE_NOT_FOUND, key.getCacheName(), key));
		}
		
		final String majorKey = key.getMajorKey();
		final String minorKey = key.getMinorKey();
		
		if (majorKey == null || minorKey == null) {
			return Single.error(new DattyStreamException(DattyError.ErrCode.BAD_ARGUMENTS, key));
		}
		
		final WritePolicy writePolicy = cache.getConfig().getWritePolicy(true);
		
		if (key.hasTtlSeconds()) {
			writePolicy.expiration = key.getTtlSeconds();
		}
		
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		
		final ChunkEnumeration chunkEnumeration = new ChunkEnumeration(cacheManager.getConfig().getNamespace(), cache.getCacheName(), majorKey);
		
		Observable<Long> stream = value.flatMap(new Func1<ByteBuf, Observable<Long>>() {

			@Override
			public Observable<Long> call(ByteBuf buffer) {

				AerospikeBins bins = new AerospikeBins(minorKey, buffer);
				return cacheManager.getClient().put(writePolicy, chunkEnumeration.nextElement(), bins, cache.streamExceptionTransformer(key)).toObservable();
				
			}
			
		});
		
		return stream.reduce(0L, LongSumFunc.INSTANCE).toSingle()
				.flatMap(new Func1<Long, Single<Long>>() {

					@Override
					public Single<Long> call(final Long writtenBytes) {

						return cacheManager.getClient().remove(writePolicy, chunkEnumeration.nextElement(), cache.streamExceptionTransformer(key))
						.map(new Func1<Boolean, Long>() {

							@Override
							public Long call(Boolean removed) {
								return writtenBytes;
							}
							
						});
						
					}
					
				});
		
	}

	public enum LongSumFunc implements Func2<Long, Long, Long> {

		INSTANCE;
		
		@Override
		public Long call(Long counter, Long written) {
			return counter += written;
		}
		
	}
	
}
