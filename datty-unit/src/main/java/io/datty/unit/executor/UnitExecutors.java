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
package io.datty.unit.executor;

import io.datty.api.DattyOperation.OpCode;
import io.datty.api.operation.SetOperation;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.TypedResult;

/**
 * UnitExecutors
 * 
 * @author Alex Shvid
 *
 */

public final class UnitExecutors {

	private final static SetOperationExecutor<?>[] setCodeList = new SetOperationExecutor<?>[OpCode.max() + 1];
	
	private final static OperationExecutor<?, ?>[] codeList = new OperationExecutor<?, ?>[OpCode.max() + 1];
	
	static {
		
		setCodeList[OpCode.CLEAR.getCode()] = ClearExecutor.INSTANCE;
		setCodeList[OpCode.SIZE.getCode()] = SizeExecutor.INSTANCE;
		setCodeList[OpCode.SCAN.getCode()] = ScanExecutor.INSTANCE;
		
		codeList[OpCode.FETCH.getCode()] = FetchExecutor.INSTANCE;
		codeList[OpCode.PUT.getCode()] = PutExecutor.INSTANCE;
		codeList[OpCode.EXECUTE.getCode()] = ExecuteExecutor.INSTANCE;
		
	}

	private UnitExecutors() {
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends SetOperation> SetOperationExecutor<O> findSetExecutor(OpCode opcode) {
		return (SetOperationExecutor<O>) setCodeList[opcode.getCode()];
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends TypedOperation<O, R>, R extends TypedResult<O>> OperationExecutor<O, R> findExecutor(OpCode opcode) {
		return (OperationExecutor<O, R>) codeList[opcode.getCode()];
	}
	
}
