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

import io.datty.api.result.FetchResult;

/**
 * Fetch operation
 * 
 * @author Alex Shvid
 *
 */

public final class Fetch extends AbstractRecordOperation<Fetch, FetchResult> {
	
	/**
	 * Fetch values also
	 */
	
	private boolean withValues = true;
	
	public Fetch() {
	}
	
	public Fetch(String setName) {
		setSetName(setName);
	}

	public Fetch(String setName, String majorKey) {
		setSetName(setName).setMajorKey(majorKey);
	}

	@Override
	public OpCode getCode() {
		return OpCode.FETCH;
	}
	
	public boolean isFetchValues() {
		return withValues;
	}

	public Fetch withValues(boolean flag) {
		this.withValues = flag;
		return this;
	}

	@Override
	public String toString() {
		return "Fetch [minorKeys=" + minorKeys + ", allMinorKeys=" + allMinorKeys + ", withValues=" + withValues + ", setName=" + setName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}


}
