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
import io.datty.api.operation.Push;
import io.datty.api.result.PushResult;
import io.datty.api.version.LongVersion;
import io.datty.api.version.Version;
import io.datty.support.exception.DattyOperationException;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikePut
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikePush implements AerospikeOperation<Push, PushResult> {

	INSTANCE;
	
	@Override
	public Single<PushResult> execute(AerospikeSet set, Push operation) {

		DattyRecord rec = operation.getRecord();
		
		if (rec == null) {

			switch(operation.getUpdatePolicy()) {
			
			case MERGE:
				return Single.just(new PushResult().setUpdated(true));
				
			case REPLACE:
				return removeRecord(set, operation);
				
			default:
				return Single.error(new DattyOperationException(DattyError.ErrCode.BAD_ARGUMENTS, "unknown updatePolicy", operation));
				
			}
			
		}
		
		boolean hasNullBins = hasNullBins(rec.getValues());
		
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
		
	private int getGenerationNumber(Push operation) {
		
		Version version = operation.getVersion();
		if (version != null && version instanceof LongVersion) {
			
			LongVersion longVersion = (LongVersion) version;
			
			return (int) longVersion.asLong();
			
		}
		
		return 0;
	}
	
	
	private Single<PushResult> mergeBins(final AerospikeSet set, final Push operation) {
		
		final DattyRecord rec = operation.getRecord();
		final AerospikeDattyManager manager = set.getParent();
		QueryPolicy queryPolicy = set.getConfig().getQueryPolicy(operation, false);

		final WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, true);
		writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
		if (operation.useVersion()) {
			writePolicy.generation = getGenerationNumber(operation);
		}
		
		final Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());
		

		return manager.getClient().get(queryPolicy, recordKey, set.singleExceptionTransformer(operation, false))
		
		.flatMap(new Func1<Record, Single<PushResult>>() {

			@Override
			public Single<PushResult> call(Record record) {
				
				if (operation.useVersion() && record.generation != writePolicy.generation) {
					return Single.just(new PushResult().setUpdated(false));
				}
				
				AerospikeBins mergedBins = new AerospikeBins(mergeMaps(record, rec.getValues()));
				return putBins(manager, set, writePolicy, recordKey, mergedBins, operation);
			}
			
		});
		
	}
	
	private Single<PushResult> putBins(AerospikeDattyManager manager, AerospikeSet set, WritePolicy writePolicy, final Key recordKey, final AerospikeBins bins, final Push operation) {
		
		Single<Long> result;
		
		if (!bins.isEmpty()) {
			result = manager.getClient().put(writePolicy, recordKey, bins, set.singleExceptionTransformer(operation, false));
		}
		else {
			result = manager.getClient().remove(writePolicy, recordKey, set.singleExceptionTransformer(operation, false))
					.map(new Func1<Boolean, Long>() {

						@Override
						public Long call(Boolean updated) {
							return updated ? bins.getWrittableBytes() : null;
						}
						
					});
		}
		
		return result.map(new Func1<Long, PushResult>() {

			@Override
			public PushResult call(Long writtenBytes) {
				PushResult res = new PushResult().setUpdated(writtenBytes != null);
				if (writtenBytes != null) {
					res.setWrittenBytes(writtenBytes.longValue());
				}
				return res;
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
			if (!value.isNull()) {
				mergingMap.put(e.getKey(), value);
			}
			else {
				mergingMap.remove(e.getKey());
			}
			
		}
		
		return mergingMap;
		
	}
	
	private Single<PushResult> putBins(AerospikeSet set, Push operation) {
		
		final DattyRecord rec = operation.getRecord();
		AerospikeDattyManager manager = set.getParent();
		
		WritePolicy writePolicy = set.getConfig().getWritePolicy(operation, false);
		if (operation.useVersion()) {
			writePolicy.generation = getGenerationNumber(operation);
		}
		
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());
		
		return putBins(manager, set, writePolicy, recordKey, new AerospikeBins(rec.getValues()), operation);
		
	}
	
	private Single<PushResult> removeRecord(AerospikeSet set, Push operation) {
		
		AerospikeDattyManager manager = set.getParent();
		WritePolicy writePolicy = set.getConfig().getWritePolicy(operation.hasTimeoutMillis());
		
		if (operation.hasTimeoutMillis()) {
			writePolicy.timeout = operation.getTimeoutMillis();
		}
		if (operation.useVersion()) {
			writePolicy.generation = getGenerationNumber(operation);
		}
		
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());

		Single<Boolean> result = manager.getClient().remove(writePolicy, recordKey, set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<Boolean, PushResult>() {

			@Override
			public PushResult call(Boolean updated) {
				return new PushResult().setUpdated(updated);
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