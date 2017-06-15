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
import io.datty.api.DattyRow;
import io.datty.api.DattyValue;
import io.datty.api.operation.PutOperation;
import io.datty.api.result.PutResult;
import io.datty.support.exception.DattyOperationException;
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
	public Single<PutResult> execute(AerospikeSet set, PutOperation operation) {

		DattyRow row = operation.getRow();
		
		if (row == null) {

			switch(operation.getUpdatePolicy()) {
			
			case MERGE:
				return Single.just(new PutResult());
				
			case REPLACE:
				return removeRecord(set, operation);
				
			default:
				return Single.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));
				
			}
			
		}
		
		boolean hasNullBins = hasNullBins(row.getValues());
		
		switch(operation.getUpdatePolicy()) {
		
			case MERGE:
				if (hasNullBins) {
					return mergeBins(set, operation);
				}
				else {
					return putBins(set, operation);
				}
				
			case REPLACE:
				return putBins(set, operation);
				
			default:
				return Single.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));
				
		}
		
	}
		
	private Single<PutResult> mergeBins(final AerospikeSet set, final PutOperation operation) {
		
		final DattyRow row = operation.getRow();
		final AerospikeDattyManager manager = set.getParent();
		QueryPolicy queryPolicy = set.getConfig().getQueryPolicy(operation, false);

		final WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, true);
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		
		final Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());
		

		return manager.getClient().get(queryPolicy, recordKey, set.singleExceptionTransformer(operation, false))
		
		.flatMap(new Func1<Record, Single<PutResult>>() {

			@Override
			public Single<PutResult> call(Record record) {
				AerospikeBins mergedBins = new AerospikeBins(mergeMaps(record, row.getValues()));
				return putBins(manager, set, writePolicy, recordKey, mergedBins, operation);
			}
			
		});
		
	}
	
	private Single<PutResult> putBins(AerospikeDattyManager manager, AerospikeSet set, WritePolicy writePolicy, final Key recordKey, final AerospikeBins bins, final PutOperation operation) {
		
		Single<Long> result;
		
		if (!bins.isEmpty()) {
			result = manager.getClient().put(writePolicy, recordKey, bins, set.singleExceptionTransformer(operation, false));
		}
		else {
			result = manager.getClient().remove(writePolicy, recordKey, set.singleExceptionTransformer(operation, false))
					.map(new Func1<Boolean, Long>() {

						@Override
						public Long call(Boolean t) {
							return bins.getWrittableBytes();
						}
						
					});
		}
		
		return result.map(new Func1<Long, PutResult>() {

			@Override
			public PutResult call(Long writtenBytes) {
				return new PutResult();
			}
			
		});
	}
	
	private	Map<String, DattyValue> mergeMaps(Record record, Map<String, DattyValue> newValues) {
		
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
			if (!value.isEmpty()) {
				mergingMap.put(e.getKey(), value);
			}
			else {
				mergingMap.remove(e.getKey());
			}
			
		}
		
		return mergingMap;
		
	}
	
	private Single<PutResult> putBins(AerospikeSet set, PutOperation operation) {
		
		final DattyRow row = operation.getRow();
		AerospikeDattyManager manager = set.getParent();
		WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, false);
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());
		
		return putBins(manager, set, writePolicy, recordKey, new AerospikeBins(row.getValues()), operation);
		
	}
	
	private Single<PutResult> removeRecord(AerospikeSet set, PutOperation operation) {
		
		AerospikeDattyManager manager = set.getParent();
		WritePolicy writePolicy = set.getConfig().getWritePolicy(operation.hasTimeoutMillis());
		if (operation.hasTimeoutMillis()) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());

		Single<Boolean> result = manager.getClient().remove(writePolicy, recordKey, set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<Boolean, PutResult>() {

			@Override
			public PutResult call(Boolean t) {
				return new PutResult();
			}
			
		});
		
	}
	
	public boolean hasNullBins(Map<String, DattyValue> values) {
		
		for (Map.Entry<String, DattyValue> entry : values.entrySet()) {
			if (entry.getValue().isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
}