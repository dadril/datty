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
import io.datty.api.DattyRecord;
import io.datty.api.DattyValue;
import io.datty.api.operation.Fetch;
import io.datty.api.result.FetchResult;
import io.datty.api.version.LongVersion;
import io.netty.buffer.ByteBuf;
import rx.Single;
import rx.functions.Func1;

/**
 * AerospikeFetch
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeFetch implements AerospikeOperation<Fetch, FetchResult> {

	INSTANCE;
	
	@Override
	public Single<FetchResult> execute(AerospikeSet set, final Fetch operation) {
		
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
		
		return result.map(new Func1<Record, FetchResult>() {

			@Override
			public FetchResult call(Record rec) {
				return toFetchResult(rec, operation);
			}
			
		});
		
	}
	
	private FetchResult toFetchResult(Record record, Fetch operation) {
		
		boolean fetchValues = operation.isFetchValues();
		
		FetchResult result = new FetchResult();
		
		if (record != null) {
			
			DattyRecord rec = new DattyRecord();
			result.setRecord(rec);
			
			result.setVersion(new LongVersion(record.generation));

			if (operation.isAllMinorKeys()) {
				
				for (Map.Entry<String, Object> e : record.bins.entrySet()) {
					Object value = e.getValue();
					if (value != null && fetchValues) {
						ByteBuf buffer = AerospikeValueUtil.toByteBuf(value);
						rec.put(e.getKey(), new ByteBufValue(buffer));
					}
					else {
						rec.put(e.getKey(), DattyValue.NULL);
					}
				}
				
			}
			else {
				
				for (String minorKey : operation.getMinorKeys()) {
					Object value = record.bins.get(minorKey);
					if (value != null && fetchValues) {
						ByteBuf buffer = AerospikeValueUtil.toByteBuf(value);
						rec.put(minorKey, new ByteBufValue(buffer));
					}
					else {
						rec.put(minorKey, DattyValue.NULL);
					}
				}
				
			}
		
		}
		
		return result;
	}
	

}