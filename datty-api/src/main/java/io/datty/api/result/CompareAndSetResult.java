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

import io.datty.api.operation.CompareAndSetOperation;

/**
 * CompareAndSetResult
 * 
 * @author Alex Shvid
 *
 */

public final class CompareAndSetResult extends AbstractResult<CompareAndSetOperation, CompareAndSetResult> {

	private boolean value;

	public CompareAndSetResult() {
	}

	public boolean get() {
		return value;
	}

	public CompareAndSetResult set(boolean val) {
		this.value = val;
		return this;
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.COMPARE_AND_SET;
	}

	@Override
	public String toString() {
		return "CompareAndSetResult [value=" + value + "]";
	}
	
}
