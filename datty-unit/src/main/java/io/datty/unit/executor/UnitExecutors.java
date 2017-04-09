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

/**
 * UnitExecutors
 * 
 * @author dadril
 *
 */

public final class UnitExecutors {

	private final static OperationExecutor<?>[] codeList = new OperationExecutor<?>[OpCode.max() + 1];
	
	private UnitExecutors() {
		
		codeList[OpCode.EXISTS.getCode()] = ExistsExecutor.INSTANCE;
		codeList[OpCode.GET.getCode()] = GetExecutor.INSTANCE;
		codeList[OpCode.SET.getCode()] = SetExecutor.INSTANCE;
		codeList[OpCode.COMPARE_AND_SET.getCode()] = CompareAndSetExecutor.INSTANCE;
		codeList[OpCode.EXECUTE.getCode()] = ExecuteExecutor.INSTANCE;
		
	}
	
	public static OperationExecutor<?> findExecutor(OpCode opcode) {
		return codeList[opcode.getCode()];
	}
	
}
