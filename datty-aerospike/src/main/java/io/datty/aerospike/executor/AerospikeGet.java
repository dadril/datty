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

import io.datty.aerospike.AerospikeCache;
import io.datty.aerospike.AerospikeCacheManager;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.operation.GetOperation;
import io.datty.api.result.GetResult;
import io.datty.support.LongVersion;
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
	public Single<GetResult> execute(AerospikeCache cache, final GetOperation operation) {
		
		AerospikeCacheManager cacheManager = cache.getParent();
		QueryPolicy queryPolicy = cache.getConfig().getQueryPolicy(operation.getTimeoutMillis());
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());
		
		Single<Record> result;
		if (operation.isAllMinorKeys()) {
			 result = cacheManager.getClient().get(queryPolicy, recordKey, cache.singleExceptionTransformer(operation));
		}
		else {
			Set<String> minorKeys = operation.getMinorKeys();
			String[] binNames = minorKeys.toArray(new String[minorKeys.size()]);
			 result = cacheManager.getClient().get(queryPolicy, recordKey, binNames, cache.singleExceptionTransformer(operation));
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
			
			result.setVersion(new LongVersion(record.generation));

			if (operation.isAllMinorKeys()) {
				
				for (Map.Entry<String, Object> e : record.bins.entrySet()) {
					Object value = e.getValue();
					if (value != null) {
						result.addValue(e.getKey(), AerospikeValueUtil.toByteBuf(value));
					}
				}
				
			}
			else {
				
				for (String minorKey : operation.getMinorKeys()) {
					Object value = record.bins.get(minorKey);
					if (value != null) {
						result.addValue(minorKey, AerospikeValueUtil.toByteBuf(value));
					}
				}
				
			}
		
		}
		
		return result;
	}
	

}