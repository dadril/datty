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
import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.ByteBufValue;
import io.datty.api.DattyError;
import io.datty.api.DattyRecord;
import io.datty.api.DattyValue;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.result.CompareAndSetResult;
import io.datty.api.version.LongVersion;
import io.datty.api.version.Version;
import io.datty.support.exception.DattyOperationException;
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
	public Single<CompareAndSetResult> execute(AerospikeSet set, CompareAndSetOperation operation) {

		DattyRecord rec = operation.getRecord();
		
		if (rec == null) {

			switch (operation.getUpdatePolicy()) {

			case MERGE:
				return Single.just(new CompareAndSetResult(true));

			case REPLACE:
				return removeRecord(set, operation);

			default:
				return Single
						.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));

			}

		}

		boolean hasNullBins = hasNullBins(rec.getValues());

		switch (operation.getUpdatePolicy()) {

		case MERGE:
			if (hasNullBins) {
				return mergeBins(set, operation);
			} else {
				return putBins(set, operation);
			}

		case REPLACE:
			return putBins(set, operation);

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

	private Single<CompareAndSetResult> mergeBins(final AerospikeSet set, final CompareAndSetOperation operation) {

		final DattyRecord rec = operation.getRecord();
		final AerospikeDattyManager manager = set.getParent();
		QueryPolicy queryPolicy = set.getConfig().getQueryPolicy(operation, false);
		
		final WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, true);
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		writePolicy.generation = getGenerationNumber(operation);

		final Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(),
				operation.getMajorKey());

		return manager.getClient()
				.get(queryPolicy, recordKey, set.singleExceptionTransformer(operation, false))

				.flatMap(new Func1<Record, Single<CompareAndSetResult>>() {

					@Override
					public Single<CompareAndSetResult> call(Record record) {

						if (record.generation != writePolicy.generation) {
							return Single.just(new CompareAndSetResult(false));
						}
						
						AerospikeBins mergedBins = new AerospikeBins(mergeMaps(record, rec.getValues()));
						return putBins(manager, set, writePolicy, recordKey, mergedBins, operation);
					}

				});


	}
	
	private Single<CompareAndSetResult> putBins(AerospikeDattyManager manager, AerospikeSet set, WritePolicy writePolicy, 
			final Key recordKey, final AerospikeBins bins, final CompareAndSetOperation operation) {
		
		Single<Long> result;
		
		if (!bins.isEmpty()) {
			result = manager.getClient().put(writePolicy, recordKey, bins, set.singleExceptionTransformer(operation, true));
		}
		else {
			result = manager.getClient().remove(writePolicy, recordKey, set.singleExceptionTransformer(operation, true))
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

	private Map<String, DattyValue> mergeMaps(Record record, Map<String, DattyValue> newValues) {

		Map<String, DattyValue> mergingMap = new HashMap<String, DattyValue>();

		if (record.bins != null) {
			for (Map.Entry<String, Object> e : record.bins.entrySet()) {
				Object value = e.getValue();
				if (value != null) {
					mergingMap.put(e.getKey(), new ByteBufValue(AerospikeValueUtil.toByteBuf(value)));
				}
			}
		}

		for (Map.Entry<String, DattyValue> e : newValues.entrySet()) {

			DattyValue value = e.getValue();
			if (!value.isNull()) {
				mergingMap.put(e.getKey(), value);
			} else {
				mergingMap.remove(e.getKey());
			}

		}

		return mergingMap;

	}

	private Single<CompareAndSetResult> putBins(AerospikeSet set, CompareAndSetOperation operation) {

		final DattyRecord rec = operation.getRecord();
		AerospikeDattyManager manager = set.getParent();
		
		WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, true);
		writePolicy.generation = getGenerationNumber(operation);
		
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());

		return putBins(manager, set, writePolicy, recordKey, new AerospikeBins(rec.getValues()), operation);

	}

	private Single<CompareAndSetResult> removeRecord(AerospikeSet set, CompareAndSetOperation operation) {

		AerospikeDattyManager cacheManager = set.getParent();
		
		WritePolicy writePolicy = set.getConfig().getWritePolicy(true);
		if (operation.hasTimeoutMillis()) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}
		writePolicy.generation = getGenerationNumber(operation);
		
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());

		Single<Boolean> result = cacheManager.getClient().remove(writePolicy, recordKey,
				set.singleExceptionTransformer(operation, true));

		return result.map(new Func1<Boolean, CompareAndSetResult>() {

			@Override
			public CompareAndSetResult call(Boolean updated) {
				return new CompareAndSetResult(updated);
			}

		});

	}

	public boolean hasNullBins(Map<String, DattyValue> values) {

		for (Map.Entry<String, DattyValue> entry : values.entrySet()) {
			if (entry.getValue().isNull()) {
				return true;
			}
		}

		return false;
	}

}
