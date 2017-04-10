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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.datty.api.result.GetResult;

/**
 * Get operation
 * 
 * @author dadril
 *
 */

public class GetOperation extends AbstractOperation<GetOperation, GetResult> {

	private Set<String> minorKeys = null;
	private boolean allMinorKeys;
	
	public GetOperation(String cacheName) {
		super(cacheName);
	}

	public GetOperation(String cacheName, String majorKey) {
		super(cacheName, majorKey);
	}

	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	public GetOperation allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return this;
	}
	
	public GetOperation allMinorKeys() {
		this.allMinorKeys = true;
		return this;
	}
	
	public GetOperation addMinorKey(String minorKey) {
		if (this.minorKeys == null) {
			this.minorKeys = Collections.singleton(minorKey);
		}
		else if (this.minorKeys.size() == 1) {
			this.minorKeys = new HashSet<String>(this.minorKeys);
		}
		else {
			this.minorKeys.add(minorKey);
		}
		return this;
	}
	
	public Set<String> getMinorKeys() {
		return minorKeys != null ? minorKeys : Collections.<String>emptySet();
	}

	@Override
	public OpCode getCode() {
		return OpCode.GET;
	}

	@Override
	public String toString() {
		return "GetOperation [minorKeys=" + minorKeys + ", allMinorKeys=" + allMinorKeys + ", cacheName=" + cacheName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}


}
