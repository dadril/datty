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

import io.datty.api.result.BooleanMapResult;

/**
 * Exists operation
 * 
 * @author dadril
 *
 */

public class ExistsOperation extends AbstractOperation<ExistsOperation, BooleanMapResult> {

	private Set<String> minorKeys = null;
	private boolean anyMinorKey;
	
	public ExistsOperation(String storeName) {
		super(storeName);
	}

	public ExistsOperation(String storeName, String majorKey) {
		super(storeName, majorKey);
	}

	public boolean isAnyMinorKey() {
		return anyMinorKey;
	}

	public ExistsOperation anyMinorKey() {
		return anyMinorKey(true);
	}

	public ExistsOperation anyMinorKey(boolean anyMinorKey) {
		this.anyMinorKey = anyMinorKey;
		return this;
	}
	
	public ExistsOperation addMinorKey(String minorKey) {
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
		return minorKeys != null ? minorKeys : Collections.emptySet();
	}

	@Override
	public OpCode getCode() {
		return OpCode.EXISTS;
	}

	@Override
	public String toString() {
		return "ExistsOperation [minorKeys=" + minorKeys + ", anyMinorKey=" + anyMinorKey + ", cacheName=" + cacheName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
