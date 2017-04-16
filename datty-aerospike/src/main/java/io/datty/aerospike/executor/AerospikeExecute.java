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

import io.datty.aerospike.AerospikeCache;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.result.ExecuteResult;
import rx.Single;

/**
 * AerospikeExecute
 * 
 * @author Alex Shvid
 *
 */

public enum AerospikeExecute implements AerospikeOperation<ExecuteOperation, ExecuteResult> {

	INSTANCE;

	@Override
	public Single<ExecuteResult> execute(AerospikeCache cache, ExecuteOperation operation) {
		
		return Single.just(new ExecuteResult().set(operation.getArguments()));
		
	}

	
}
