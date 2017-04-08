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
package io.datty.api.operation;

import io.datty.api.result.PayloadResult;
import io.netty.buffer.ByteBuf;

/**
 * ExecuteOperation
 * 
 * @author dadril
 *
 */

public class ExecuteOperation extends AbstractUpdateOperation<ExecuteOperation, PayloadResult> implements UpdateOperation {

	private String packageName;
	private String functionName;
	private ByteBuf arguments;
	
	public ExecuteOperation(String storeName) {
		super(storeName);
	}

	public ExecuteOperation(String storeName, String majorKey) {
		super(storeName, majorKey);
	}

	public String getPackageName() {
		return packageName;
	}

	public ExecuteOperation setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public String getFunctionName() {
		return functionName;
	}

	public ExecuteOperation setFunctionName(String functionName) {
		this.functionName = functionName;
		return this;
	}

	public ByteBuf getArguments() {
		return arguments;
	}

	public ExecuteOperation setArguments(ByteBuf arguments) {
		this.arguments = arguments;
		return this;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.EXECUTE;
	}	
	
}
