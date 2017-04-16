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
package io.datty.aerospike.executor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.AerospikeCache;
import io.datty.aerospike.AerospikeCacheManager;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyConstants;
import io.datty.api.DattyError;
import io.datty.api.operation.PutOperation;
import io.datty.api.result.PutResult;
import io.datty.support.exception.DattySingleException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikePut
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikePut implements AerospikeOperation<PutOperation, PutResult> {

	INSTANCE;
	
	@Override
	public Single<PutResult> execute(AerospikeCache cache, PutOperation operation) {

		if (operation.getValues().isEmpty()) {

			switch(operation.getUpdatePolicy()) {
			
			case MERGE:
				return Single.just(new PutResult());
				
			case REPLACE:
				return removeRecord(cache, operation);
				
			default:
				return Single.error(new DattySingleException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));
				
			}
			
		}
		
		boolean hasNullBins = hasNullBins(operation.getValues());
		
		switch(operation.getUpdatePolicy()) {
		
			case MERGE:
				if (hasNullBins) {
					return mergeBins(cache, operation);
				}
				else {
					return putBins(cache, operation);
				}
				
			case REPLACE:
				return putBins(cache, operation);
				
			default:
				return Single.error(new DattySingleException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));
				
		}
		
	}
		
	private Single<PutResult> mergeBins(final AerospikeCache cache, final PutOperation operation) {
		
		final AerospikeCacheManager cacheManager = cache.getParent();
		QueryPolicy queryPolicy = cache.getConfig().getQueryPolicy(operation.getTimeoutMillis());
		final WritePolicy writePolicy = new WritePolicy(cache.getConfig().getWritePolicy());
		final Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());
		
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		
		if (operation.getTtlSeconds() != DattyConstants.UNSET_TTL) {
			writePolicy.expiration = operation.getTtlSeconds();
		}
		
		if (operation.getTimeoutMillis() != DattyConstants.UNSET_TIMEOUT) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}

		Single<Key> result = cacheManager.getClient().get(queryPolicy, recordKey, cache.singleExceptionTransformer(operation))
		
		.flatMap(new Func1<Record, Single<Key>>() {

			@Override
			public Single<Key> call(Record record) {
				
				Bin[] mergedBins = notNullBins(mergeMaps(record, operation.getValues()));
				return cacheManager.getClient().put(writePolicy, recordKey, mergedBins, cache.singleExceptionTransformer(operation));
			}
			
		});
		
		return result.map(new Func1<Key, PutResult>() {

			@Override
			public PutResult call(Key t) {
				return new PutResult();
			}
			
		});
		
	}
	
	private	Map<String, ByteBuf> mergeMaps(Record record, Map<String, ByteBuf> newValues) {
		
		Map<String, ByteBuf> mergingMap = new HashMap<String, ByteBuf>();
			
		for (Map.Entry<String, Object> e : record.bins.entrySet()) {
			Object value = e.getValue();
			if (value != null) {
				mergingMap.put(e.getKey(), AerospikeValueUtil.toByteBuf(value));
			}
		}
		
		for (Map.Entry<String, ByteBuf> e : newValues.entrySet()) {
			
			ByteBuf value = e.getValue();
			if (value != null) {
				mergingMap.put(e.getKey(), value);
			}
			else {
				mergingMap.remove(e.getKey());
			}
			
		}
		
		return mergingMap;
		
	}
	
	private Single<PutResult> putBins(AerospikeCache cache, PutOperation operation) {
		
		AerospikeCacheManager cacheManager = cache.getParent();
		WritePolicy writePolicy = new WritePolicy(cache.getConfig().getWritePolicy());
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());
		
		switch(operation.getUpdatePolicy()) {
			case MERGE:
				writePolicy.recordExistsAction = RecordExistsAction.UPDATE;
				break;
			case REPLACE:
				writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
				break;
		}
		
		if (operation.getTtlSeconds() != DattyConstants.UNSET_TTL) {
			writePolicy.expiration = operation.getTtlSeconds();
		}
		
		if (operation.getTimeoutMillis() != DattyConstants.UNSET_TIMEOUT) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}
		
		Bin[] notNullBins = notNullBins(operation.getValues());
		Single<Key> result = cacheManager.getClient().put(writePolicy, recordKey, notNullBins, cache.singleExceptionTransformer(operation));
		
		return result.map(new Func1<Key, PutResult>() {

			@Override
			public PutResult call(Key t) {
				return new PutResult();
			}
			
		});
		
	}
	
	private Single<PutResult> removeRecord(AerospikeCache cache, PutOperation operation) {
		
		AerospikeCacheManager cacheManager = cache.getParent();
		WritePolicy writePolicy = cache.getConfig().getWritePolicy(operation.getTimeoutMillis());
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());

		Single<Boolean> result = cacheManager.getClient().remove(writePolicy, recordKey, cache.singleExceptionTransformer(operation));
		
		return result.map(new Func1<Boolean, PutResult>() {

			@Override
			public PutResult call(Boolean t) {
				return new PutResult();
			}
			
		});
		
	}
	
	public boolean hasNullBins(Map<String, ByteBuf> values) {
		
		for (Map.Entry<String, ByteBuf> entry : values.entrySet()) {
			if (entry.getValue() == null) {
				return true;
			}
		}
		
		return false;
	}
	
	private Bin[] notNullBins(Map<String, ByteBuf> values) {
		
		Bin[] bins = new Bin[values.size()];
		
		int i = 0;
		for (Map.Entry<String, ByteBuf> entry : values.entrySet()) {
			
			String binName = entry.getKey();
			ByteBuf value = entry.getValue();
			
			if (value != null) {
				bins[i++] = new Bin(binName, ByteBufUtil.getBytes(value));
			}
			
		}
		
		if (i != values.size()) {
			bins = Arrays.copyOf(bins, i);
		}
		
		return bins;
	}
	
}