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

import java.util.Map;

import com.aerospike.client.Key;
import com.aerospike.client.Record;

import io.datty.aerospike.AerospikeConstants;
import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.AerospikeRecord;
import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyRow;
import io.datty.api.operation.ScanOperation;
import io.datty.api.result.RecordResult;
import io.datty.support.LongVersion;
import rx.Observable;
import rx.functions.Func1;

/**
 * AerospikeScan
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeScan implements AerospikeSetOperation<ScanOperation> {

	INSTANCE;

	@Override
	public Observable<RecordResult> execute(AerospikeSet set, ScanOperation operation) {
		
		AerospikeDattyManager manager = set.getParent();
		
		Observable<AerospikeRecord> result = manager.getClient().scan(
				manager.getConfig().getClientPolicy().scanPolicyDefault, 
				manager.getConfig().getNamespace(),
				set.getName(), AerospikeConstants.EMPTY_STRING_ARRAY, 
				set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<AerospikeRecord, RecordResult>() {

			@Override
			public RecordResult call(AerospikeRecord record) {
				return toRecordResult(record);
			}
			
		});
		
	}
	
	private RecordResult toRecordResult(AerospikeRecord aeroRecord) {
		
		RecordResult result = new RecordResult();

		Key key = aeroRecord.getKey();
		if (key != null && key.userKey != null) {
			result.setMajorKey(key.userKey.toString());
		}
		
		Record record = aeroRecord.getRecord();
		if (record != null) {
			
			DattyRow row = new DattyRow();
			result.setRow(row);
			
			result.setVersion(new LongVersion(record.generation));

			for (Map.Entry<String, Object> e : record.bins.entrySet()) {
				Object value = e.getValue();
				if (value != null) {
					row.putValue(e.getKey(), AerospikeValueUtil.toByteBuf(value), true);
				}
			}
		
		}
		
		return result;
	}

}
