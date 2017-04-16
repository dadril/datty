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
import io.datty.api.operation.ExistsOperation;
import io.datty.api.result.ExistsResult;
import io.datty.support.LongVersion;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikeExists
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeExists implements AerospikeOperation<ExistsOperation, ExistsResult> {

	INSTANCE;
	
	@Override
	public Single<ExistsResult> execute(AerospikeCache cache, final ExistsOperation operation) {
		
		AerospikeCacheManager cacheManager = cache.getParent();
		QueryPolicy queryPolicy = cache.getConfig().getQueryPolicy(operation.getTimeoutMillis());
		Key recordKey = new Key(cacheManager.getConfig().getNamespace(), cache.getCacheName(), operation.getMajorKey());
		Set<String> minorKeys = operation.getMinorKeys();
		
		Single<Record> result;
		if (operation.isAllMinorKeys()) {
			 result = cacheManager.getClient().get(queryPolicy, recordKey, cache.singleExceptionTransformer(operation, false));
		}
		else if (minorKeys.isEmpty()) {
			 result = cacheManager.getClient().getHeader(queryPolicy, recordKey, cache.singleExceptionTransformer(operation, false));
		}
		else {	
			 String[] binNames = minorKeys.toArray(new String[minorKeys.size()]);
			 result = cacheManager.getClient().get(queryPolicy, recordKey, binNames, cache.singleExceptionTransformer(operation, false));
		}
		
		return result.map(new Func1<Record, ExistsResult>() {

			@Override
			public ExistsResult call(Record rec) {
				return toExistsResult(rec, operation);
			}
			
		});
	}
	
	private ExistsResult toExistsResult(Record record, ExistsOperation operation) {
		
		ExistsResult result = new ExistsResult();
		
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