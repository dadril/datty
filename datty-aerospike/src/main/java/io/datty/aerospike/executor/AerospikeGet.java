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
import java.util.Set;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.QueryPolicy;

import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.ByteBufValue;
import io.datty.api.DattyRow;
import io.datty.api.DattyValue;
import io.datty.api.operation.GetOperation;
import io.datty.api.result.GetResult;
import io.datty.api.version.LongVersion;
import io.netty.buffer.ByteBuf;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikeGet
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeGet implements AerospikeOperation<GetOperation, GetResult> {

	INSTANCE;
	
	@Override
	public Single<GetResult> execute(AerospikeSet set, final GetOperation operation) {
		
		AerospikeDattyManager manager = set.getParent();
		QueryPolicy queryPolicy = set.getConfig().getQueryPolicy(operation, false);
		Key recordKey = new Key(manager.getConfig().getNamespace(), set.getName(), operation.getMajorKey());
		Set<String> minorKeys = operation.getMinorKeys();
		
		Single<Record> result;
		if (operation.isAllMinorKeys()) {
			 result = manager.getClient().get(queryPolicy, recordKey, set.singleExceptionTransformer(operation, false));
		}
		else if (minorKeys.isEmpty()) {
			 result = manager.getClient().getHeader(queryPolicy, recordKey, set.singleExceptionTransformer(operation, false));
		}		
		else {
			String[] binNames = minorKeys.toArray(new String[minorKeys.size()]);
			 result = manager.getClient().get(queryPolicy, recordKey, binNames, set.singleExceptionTransformer(operation, false));
		}
		
		return result.map(new Func1<Record, GetResult>() {

			@Override
			public GetResult call(Record rec) {
				return toGetResult(rec, operation);
			}
			
		});
		
	}
	
	private GetResult toGetResult(Record record, GetOperation operation) {
		
		GetResult result = new GetResult();
		
		if (record != null) {
			
			DattyRow row = new DattyRow();
			result.setRow(row);
			
			result.setVersion(new LongVersion(record.generation));

			if (operation.isAllMinorKeys()) {
				
				for (Map.Entry<String, Object> e : record.bins.entrySet()) {
					Object value = e.getValue();
					if (value != null) {
						ByteBuf buffer = AerospikeValueUtil.toByteBuf(value);
						row.addValue(e.getKey(), new ByteBufValue(buffer), true);
					}
					else {
						row.addValue(e.getKey(), DattyValue.NULL, true);
					}
				}
				
			}
			else {
				
				for (String minorKey : operation.getMinorKeys()) {
					Object value = record.bins.get(minorKey);
					if (value != null) {
						ByteBuf buffer = AerospikeValueUtil.toByteBuf(value);
						row.addValue(minorKey, new ByteBufValue(buffer), true);
					}
					else {
						row.addValue(minorKey, DattyValue.NULL, true);
					}
				}
				
			}
		
		}
		
		return result;
	}
	

}