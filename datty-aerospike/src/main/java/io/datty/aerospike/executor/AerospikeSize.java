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

import io.datty.aerospike.AerospikeDattyManager;
import io.datty.aerospike.AerospikeSet;
import io.datty.aerospike.info.AerospikeInfoCallable;
import io.datty.aerospike.info.AerospikeInfoRequest;
import io.datty.aerospike.info.AerospikeInfoResponse;
import io.datty.api.operation.SizeOperation;
import io.datty.api.result.RecordResult;
import rx.Observable;
import rx.functions.Func1;

/**
 * AerospikeSize
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeSize implements AerospikeSetOperation<SizeOperation> {

	INSTANCE;

	@Override
	public Observable<RecordResult> execute(AerospikeSet set, SizeOperation operation) {
		
		AerospikeDattyManager manager = set.getParent();
		AerospikeInfoRequest request = new AerospikeInfoRequest(manager.getConfig().getNamespace(), set.getName());
		Observable<AerospikeInfoResponse> response = new AerospikeInfoCallable(manager, request).toOservable();
		
		return response.map(new Func1<AerospikeInfoResponse, RecordResult>() {

			@Override
			public RecordResult call(AerospikeInfoResponse response) {
				RecordResult result = new RecordResult();
				result.setCount(response.getNObjects());
				return result;
			}
			
		});
	}

}
