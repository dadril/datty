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
package io.datty.aerospike;

import java.util.Map;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.info.AerospikeCommandCallable;
import io.datty.aerospike.info.AerospikeDeleteSetRequest;
import io.datty.aerospike.info.AerospikeInfoCallable;
import io.datty.aerospike.info.AerospikeInfoRequest;
import io.datty.aerospike.info.AerospikeInfoResponse;
import io.datty.aerospike.info.AerospikeTruncateRequest;
import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyQuery;
import io.datty.api.DattyRow;
import io.datty.api.operation.SizeOperation;
import io.datty.api.operation.ClearOperation;
import io.datty.api.operation.QueryOperation;
import io.datty.api.operation.ScanOperation;
import io.datty.api.result.QueryResult;
import io.datty.support.LongVersion;
import io.datty.support.exception.DattyOperationException;
import rx.Observable;
import rx.functions.Func1;

/**
 * AerospikeDattyQuery
 * 
 * @author Alex Shvid
 *
 */


public class AerospikeDattyQuery implements DattyQuery {

	private final static String[] EMPTY_BIN_NAMES = new String[] {};

	private final static AerospikeVersion VERSION_3_12 = AerospikeVersion.of(3, 12);
	
	private final AerospikeDattyManager manager;
	
	public AerospikeDattyQuery(AerospikeDattyManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Observable<QueryResult> executeQuery(QueryOperation operation) {
		
		String setName = operation.getSetName();
		if (setName == null) {
			return Observable.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty setName", operation));
		}
		
		AerospikeSet set = manager.getAerospikeSet(setName);
		if (set == null) {
			return Observable.error(new DattyOperationException(ErrCode.SET_NOT_FOUND, setName, operation));
		}
		
		if (operation instanceof SizeOperation) {
			return doCount(set, (SizeOperation) operation);
		}
		if (operation instanceof ScanOperation) {
			return doScan(set, (ScanOperation) operation);
		}
		if (operation instanceof ClearOperation) {
			return doDelete(set, (ClearOperation) operation);
		}
		else {
			return Observable.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, setName, operation));
		}
		
	}
	
	protected Observable<QueryResult> doScan(AerospikeSet set, ScanOperation operation) {
		
		Observable<AerospikeRecord> result = manager.getClient().scan(
				manager.getConfig().getClientPolicy().scanPolicyDefault, 
				manager.getConfig().getNamespace(),
				set.getName(), EMPTY_BIN_NAMES, 
				set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<AerospikeRecord, QueryResult>() {

			@Override
			public QueryResult call(AerospikeRecord record) {
				return toQueryResult(record);
			}
			
		});
	}
	
	private QueryResult toQueryResult(AerospikeRecord aeroRecord) {
		
		QueryResult result = new QueryResult();

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
	
	
	protected Observable<QueryResult> doCount(AerospikeSet set, SizeOperation operation) {
		
		AerospikeInfoRequest request = new AerospikeInfoRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<AerospikeInfoResponse> response = new AerospikeInfoCallable(manager, request).toOservable();
		
		return response.map(new Func1<AerospikeInfoResponse, QueryResult>() {

			@Override
			public QueryResult call(AerospikeInfoResponse response) {
				QueryResult result = new QueryResult();
				result.setCount(response.getNObjects());
				return result;
			}
			
		});
	}
	
	protected Observable<QueryResult> doDelete(AerospikeSet set, ClearOperation operation) {
		
		if (manager.getConfig().isScanAndDelete()) {
			return doScanAndDelete(set, operation);
		}
		
		AerospikeVersion version = manager.getVersion();
		if (version.compareTo(VERSION_3_12) >= 0) {
			return doTruncate(set);
		}
		else {				
			return doDeleteAll(set);
		}
		
	}
	
	protected Observable<QueryResult> doTruncate(AerospikeSet set) {
		
		AerospikeTruncateRequest request = new AerospikeTruncateRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<String> response = new AerospikeCommandCallable(manager, request.toCommand()).toOservable();

		return response.map(new Func1<String, QueryResult>() {

			@Override
			public QueryResult call(String response) {
				return new QueryResult();
			}
			
		});
		
	}
	
	protected Observable<QueryResult> doDeleteAll(AerospikeSet set) {
		
		AerospikeDeleteSetRequest request = new AerospikeDeleteSetRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<String> response = new AerospikeCommandCallable(manager, request.toCommand()).toOservable();

		return response.map(new Func1<String, QueryResult>() {

			@Override
			public QueryResult call(String response) {
				return new QueryResult();
			}
			
		});
		
	}
	
	protected Observable<QueryResult> doScanAndDelete(AerospikeSet set, ClearOperation operation) {
				
		WritePolicy deletePolity = set.getConfig().getWritePolicy(true);
		deletePolity.sendKey = false;
		
		Observable<Long> result = manager.getClient().scanAndDelete(
				manager.getConfig().getClientPolicy().scanPolicyDefault, 
				deletePolity, 
				manager.getConfig().getNamespace(),
				set.getName(), 
				set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<Long, QueryResult>() {

			@Override
			public QueryResult call(Long cnt) {
				QueryResult res = new QueryResult();
				res.setCount(cnt);
				return res;
			}
			
			
		});
		
	}
	
}
