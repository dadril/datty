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

import io.datty.api.DattyConstants;
import io.datty.api.result.ExecuteResult;
import io.netty.buffer.ByteBuf;

/**
 * ExecuteOperation
 * 
 * @author Alex Shvid
 *
 */

public class ExecuteOperation extends AbstractOperation<ExecuteOperation, ExecuteResult> {

	private String packageName;
	private String functionName;
	private ByteBuf arguments;
	
	private int ttlSeconds = DattyConstants.UNSET_TTL;
	
	public ExecuteOperation() {
	}
	
	public ExecuteOperation(String setName) {
		setSetName(setName);
	}

	public ExecuteOperation(String setName, String majorKey) {
		setSetName(setName).setMajorKey(majorKey);
	}
	
	public boolean hasPackageName() {
		return packageName != null;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public ExecuteOperation setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}
	
	public boolean hasFunctionName() {
		return functionName != null;
	}

	public String getFunctionName() {
		return functionName;
	}

	public ExecuteOperation setFunctionName(String functionName) {
		this.functionName = functionName;
		return this;
	}

	public boolean hasArguments() {
		return arguments != null;
	}
	
	public ByteBuf getArguments() {
		return arguments;
	}

	public ExecuteOperation setArguments(ByteBuf arguments) {
		this.arguments = arguments;
		return this;
	}
	
	public boolean hasTtlSeconds() {
		return ttlSeconds != DattyConstants.UNSET_TTL;
	}

	public int getTtlSeconds() {
		return ttlSeconds;
	}
	
	public ExecuteOperation setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return this;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.EXECUTE;
	}

	@Override
	public String toString() {
		return "ExecuteOperation [packageName=" + packageName + ", functionName=" + functionName + ", arguments="
				+ arguments + ", setName=" + setName + ", superKey=" + superKey + ", majorKey=" + majorKey
				+ ", ttlSeconds=" + ttlSeconds
				+ ", timeoutMillis=" + timeoutMillis + "]";
	}	
	
	
}
