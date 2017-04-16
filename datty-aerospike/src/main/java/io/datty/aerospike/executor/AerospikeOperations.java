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

import io.datty.api.DattyOperation.OpCode;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.TypedResult;

/**
 * AerospikeOperations
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeOperations {

	private final static AerospikeOperation<?, ?>[] codeList = new AerospikeOperation<?, ?>[OpCode.max() + 1];
	
	private AerospikeOperations() {
	}
	
	static {
		
		codeList[OpCode.EXISTS.getCode()] = AerospikeExists.INSTANCE;
		codeList[OpCode.GET.getCode()] = AerospikeGet.INSTANCE;
		codeList[OpCode.PUT.getCode()] = AerospikePut.INSTANCE;
		codeList[OpCode.COMPARE_AND_SET.getCode()] = AerospikeCompareAndSet.INSTANCE;
		codeList[OpCode.EXECUTE.getCode()] = AerospikeExecute.INSTANCE;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends TypedOperation<O, R>, R extends TypedResult<O>> AerospikeOperation<O, R> find(OpCode opcode) {
		return (AerospikeOperation<O, R>) codeList[opcode.getCode()];
	}
	
}
