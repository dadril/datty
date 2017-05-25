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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.datty.api.result.RemoveResult;

/**
 * Remove Operation
 * 
 * @author Alex Shvid
 *
 */

public class RemoveOperation extends AbstractOperation<RemoveOperation, RemoveResult> {

	private boolean allMinorKeys;
	private Set<String> minorKeys;
	
	public RemoveOperation(String storeName) {
		super(storeName);
	}

	public RemoveOperation(String storeName, String majorKey) {
		super(storeName, majorKey);
	}

	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	public RemoveOperation allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return this;
	}
	
	public RemoveOperation allMinorKeys() {
		this.allMinorKeys = true;
		return this;
	}

	public RemoveOperation addMinorKey(String minorKey) {
		if (this.minorKeys == null) {
			this.minorKeys = Collections.singleton(minorKey);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}
			this.minorKeys.add(minorKey);
		}
		return this;
	}
	
	public RemoveOperation addMinorKeys(Collection<String> minorKeys) {
		if (this.minorKeys == null) {
			this.minorKeys = new HashSet<String>(minorKeys);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}
			this.minorKeys.addAll(minorKeys);
		}
		return this;
	}
	
	public Set<String> getMinorKeys() {
		return minorKeys != null ? minorKeys : Collections.<String>emptySet();
	}

	@Override
	public OpCode getCode() {
		return OpCode.REMOVE;
	}



}
