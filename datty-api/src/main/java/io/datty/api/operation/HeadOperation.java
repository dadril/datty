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

import io.datty.api.result.HeadResult;

/**
 * Head operation
 * 
 * This is the same as GetOperation but values will not be retrieved
 * 
 * Used to get version and to know what minor keys are in record.
 * Also used to check if record exists or not.
 * 
 * @author Alex Shvid
 *
 */

public class HeadOperation extends AbstractOperation<HeadOperation, HeadResult> {

	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	private boolean allMinorKeys;
	
	/**
	 * Retrieve a specific list of minor keys (checks them for existence)
	 */
	
	private Set<String> minorKeys;
	
	public HeadOperation() {
	}
	
	public HeadOperation(String setName) {
		setSetName(setName);
	}

	public HeadOperation(String setName, String majorKey) {
		setSetName(setName).setMajorKey(majorKey);
	}
	
	public boolean isAnyMinorKey() {
		return !allMinorKeys && (minorKeys == null || minorKeys.isEmpty());
	}

	/**
	 * If anyMinorKey flag set, then retrieve only version of record
	 * does not get list of minorKeys
	 */
	
	public HeadOperation anyMinorKey() {
		this.allMinorKeys = false;
		if (minorKeys != null) {
			minorKeys.clear();
		}
		return this;
	}

	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	public HeadOperation allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return this;
	}
	
	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	public HeadOperation allMinorKeys() {
		this.allMinorKeys = true;
		return this;
	}

	public HeadOperation addMinorKey(String minorKey) {
		this.allMinorKeys = false;
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
	
	public HeadOperation addMinorKeys(Collection<String> minorKeys) {
		this.allMinorKeys = false;
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
		return OpCode.HEAD;
	}

	@Override
	public String toString() {
		return "HeadOperation [allMinorKeys=" + allMinorKeys + ", minorKeys=" + minorKeys + ", setName=" + setName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
