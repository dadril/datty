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

import com.aerospike.client.policy.WritePolicy;

import io.datty.aerospike.AerospikeConstants;
import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.AerospikeVersion;
import io.datty.aerospike.info.AerospikeCommandCallable;
import io.datty.aerospike.info.AerospikeDeleteSetRequest;
import io.datty.aerospike.info.AerospikeTruncateRequest;
import io.datty.api.operation.Clear;
import io.datty.api.result.RecordResult;
import rx.Observable;
import rx.functions.Func1;

/**
 * AerospikeClear
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeClear implements AerospikeSetOperation<Clear> {

	INSTANCE;

	@Override
	public Observable<RecordResult> execute(AerospikeSet set, Clear operation) {
		
		AerospikeDattyManager manager = set.getParent();
		
		if (manager.getConfig().isScanAndDelete()) {
			return doScanAndDelete(set, operation);
		}
		
		AerospikeVersion version = manager.getVersion();
		if (version.compareTo(AerospikeConstants.VERSION_3_12) >= 0) {
			return doTruncate(set);
		}
		else {				
			return doDeleteAll(set);
		}
		
	}
	
	protected Observable<RecordResult> doTruncate(AerospikeSet set) {
		
		AerospikeDattyManager manager = set.getParent();
		
		AerospikeTruncateRequest request = new AerospikeTruncateRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<String> response = new AerospikeCommandCallable(manager, request.toCommand()).toOservable();

		return response.map(new Func1<String, RecordResult>() {

			@Override
			public RecordResult call(String response) {
				return new RecordResult();
			}
			
		});
		
	}
	
	protected Observable<RecordResult> doDeleteAll(AerospikeSet set) {
		
		AerospikeDattyManager manager = set.getParent();
		
		AerospikeDeleteSetRequest request = new AerospikeDeleteSetRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<String> response = new AerospikeCommandCallable(manager, request.toCommand()).toOservable();

		return response.map(new Func1<String, RecordResult>() {

			@Override
			public RecordResult call(String response) {
				return new RecordResult();
			}
			
		});
		
	}
	
	protected Observable<RecordResult> doScanAndDelete(AerospikeSet set, Clear operation) {
				
		AerospikeDattyManager manager = set.getParent();
		
		WritePolicy deletePolity = set.getConfig().getWritePolicy(true);
		deletePolity.sendKey = false;
		
		Observable<Long> result = manager.getClient().scanAndDelete(
				manager.getConfig().getClientPolicy().scanPolicyDefault, 
				deletePolity, 
				manager.getConfig().getNamespace(),
				set.getName(), 
				set.singleExceptionTransformer(operation, false));
		
		return result.map(new Func1<Long, RecordResult>() {

			@Override
			public RecordResult call(Long cnt) {
				RecordResult res = new RecordResult();
				res.setCount(cnt);
				return res;
			}
			
			
		});
		
	}

}
