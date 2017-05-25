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

import java.util.HashMap;
import java.util.Map;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.AerospikeBins;
import io.datty.aerospike.AerospikeCache;
import io.datty.aerospike.AerospikeCacheManager;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyError;
import io.datty.api.DattyRow;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.Version;
import io.datty.api.result.CompareAndSetResult;
import io.datty.support.LongVersion;
import io.datty.support.exception.DattyOperationException;
import io.netty.buffer.ByteBuf;
import rx.Single;
import rx.functions.Func1;

/**
 * CompareAndSetExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeCompareAndSet implements AerospikeOperation<CompareAndSetOperation, CompareAndSetResult> {

	INSTANCE;

	@Override
	public Single<CompareAndSetResult> execute(AerospikeCache cache, CompareAndSetOperation operation) {

		DattyRow row = operation.getRow();
		
		if (row == null) {

			switch (operation.getUpdatePolicy()) {

			case MERGE:
				return Single.just(new CompareAndSetResult(true));

			case REPLACE:
				return removeRecord(cache, operation);

			default:
				return Single
						.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));

			}

		}

		boolean hasNullBins = hasNullBins(row.getValues());

		switch (operation.getUpdatePolicy()) {

		case MERGE:
			if (hasNullBins) {
				return mergeBins(cache, operation);
			} else {
				return putBins(cache, operation);
			}

		case REPLACE:
			return putBins(cache, operation);

		default:
			return Single
					.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));

		}

	}
	
	private int getGenerationNumber(CompareAndSetOperation operation) {
		
		Version version = operation.getVersion();
		if (version != null && version instanceof LongVersion) {
			
			LongVersion longVersion = (LongVersion) version;
			
			return (int) longVersion.asLong();
			
		}
		
		return 0;
	}

	private Single<CompareAndSetResult> mergeBins(final AerospikeCache cache, final CompareAndSetOperation operation) {

		final DattyRow row = operation.getRow();
		final AerospikeCacheManager cacheManager = cache.getParent();
		QueryPolicy queryPolicy = cache.getConfig().getQueryPolicy(operation, false);
		
		final WritePolicy writePolicy = cache.getConfig().getWritePolicy(operation, true);
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		writePolicy.generation = getGenerationNumber(operation);

		final Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(),
				operation.getMajorKey());

		return cacheManager.getClient()
				.get(queryPolicy, recordKey, cache.singleExceptionTransformer(operation, false))

				.flatMap(new Func1<Record, Single<CompareAndSetResult>>() {

					@Override
					public Single<CompareAndSetResult> call(Record record) {

						if (record.generation != writePolicy.generation) {
							return Single.just(new CompareAndSetResult(false));
						}
						
						AerospikeBins mergedBins = new AerospikeBins(mergeMaps(record, row.getValues()));
						return putBins(cacheManager, cache, writePolicy, recordKey, mergedBins, operation);
					}

				});


	}
	
	private Single<CompareAndSetResult> putBins(AerospikeCacheManager cacheManager, AerospikeCache cache, WritePolicy writePolicy, 
			final Key recordKey, final AerospikeBins bins, final CompareAndSetOperation operation) {
		
		Single<Long> result;
		
		if (!bins.isEmpty()) {
			result = cacheManager.getClient().put(writePolicy, recordKey, bins, cache.singleExceptionTransformer(operation, true));
		}
		else {
			result = cacheManager.getClient().remove(writePolicy, recordKey, cache.singleExceptionTransformer(operation, true))
					.map(new Func1<Boolean, Long>() {

						@Override
						public Long call(Boolean updated) {
							return updated ? bins.getWrittableBytes() : null;
						}
						
					});
		}
		
		return result.map(new Func1<Long, CompareAndSetResult>() {

			@Override
			public CompareAndSetResult call(Long writtenBytes) {
				return new CompareAndSetResult(writtenBytes != null);
			}
			
		});
	}

	private Map<String, ByteBuf> mergeMaps(Record record, Map<String, ByteBuf> newValues) {

		Map<String, ByteBuf> mergingMap = new HashMap<String, ByteBuf>();

		if (record.bins != null) {
			for (Map.Entry<String, Object> e : record.bins.entrySet()) {
				Object value = e.getValue();
				if (value != null) {
					mergingMap.put(e.getKey(), AerospikeValueUtil.toByteBuf(value));
				}
			}
		}

		for (Map.Entry<String, ByteBuf> e : newValues.entrySet()) {

			ByteBuf value = e.getValue();
			if (value != null) {
				mergingMap.put(e.getKey(), value);
			} else {
				mergingMap.remove(e.getKey());
			}

		}

		return mergingMap;

	}

	private Single<CompareAndSetResult> putBins(AerospikeCache cache, CompareAndSetOperation operation) {

		final DattyRow row = operation.getRow();
		AerospikeCacheManager cacheManager = cache.getParent();
		
		WritePolicy writePolicy = cache.getConfig().getWritePolicy(operation, true);
		writePolicy.generation = getGenerationNumber(operation);
		
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());

		return putBins(cacheManager, cache, writePolicy, recordKey, new AerospikeBins(row.getValues()), operation);

	}

	private Single<CompareAndSetResult> removeRecord(AerospikeCache cache, CompareAndSetOperation operation) {

		AerospikeCacheManager cacheManager = cache.getParent();
		
		WritePolicy writePolicy = cache.getConfig().getWritePolicy(true);
		if (operation.hasTimeoutMillis()) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}
		writePolicy.generation = getGenerationNumber(operation);
		
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());

		Single<Boolean> result = cacheManager.getClient().remove(writePolicy, recordKey,
				cache.singleExceptionTransformer(operation, true));

		return result.map(new Func1<Boolean, CompareAndSetResult>() {

			@Override
			public CompareAndSetResult call(Boolean updated) {
				return new CompareAndSetResult(updated);
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

}
