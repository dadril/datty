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
package io.datty.api.result;

import io.datty.api.operation.Execute;
import io.netty.buffer.ByteBuf;

/**
 * ExecuteResult
 * 
 * @author Alex Shvid
 *
 */

public class ExecuteResult extends AbstractResult<Execute, ExecuteResult> {

	private ByteBuf value;
	
	public ExecuteResult() {
	}

	public ByteBuf get() {
		return value;
	}
	
	public ExecuteResult set(ByteBuf val) {
		this.value = val;
		return this;
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.EXECUTE;
	}

	@Override
	public String toString() {
		return "ExecuteResult [value=" + value + "]";
	}

	
}
