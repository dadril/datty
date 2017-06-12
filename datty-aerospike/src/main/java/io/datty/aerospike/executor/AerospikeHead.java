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

import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.AerospikeDattyManager;
import io.datty.api.operation.HeadOperation;
import io.datty.api.result.HeadResult;
import io.datty.api.version.LongVersion;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikeHead
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeHead implements AerospikeOperation<HeadOperation, HeadResult> {

	INSTANCE;
	
	@Override
	public Single<HeadResult> execute(AerospikeSet set, final HeadOperation operation) {
		
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
		
		return result.map(new Func1<Record, HeadResult>() {

			@Override
			public HeadResult call(Record rec) {
				return toHeadResult(rec, operation);
			}
			
		});
	}
	
	private HeadResult toHeadResult(Record record, HeadOperation operation) {
		
		HeadResult result = new HeadResult();
		
		if (record == null) {
			return result;
		}
			
		result.setVersion(new LongVersion(record.generation));

		if (record.bins == null) {
			return result;
		}
				
		if (operation.isAllMinorKeys()) {
			
			for (Map.Entry<String, Object> e : record.bins.entrySet()) {
				Object value = e.getValue();
				if (value != null) {
					result.addMinorKey(e.getKey());
				}
			}
			
		}
		else if (!operation.getMinorKeys().isEmpty()) {
			
			for (String minorKey : operation.getMinorKeys()) {
				Object value = record.bins.get(minorKey);
				if (value != null) {
					result.addMinorKey(minorKey);
				}
			}
			
		}
		
		return result;
	}
	
}