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

import io.datty.api.result.ExistsResult;

/**
 * Exists operation
 * 
 * @author Alex Shvid
 *
 */

public class ExistsOperation extends AbstractOperation<ExistsOperation, ExistsResult> {

	/**
	 * If anyMinorKey flag set, then retrieve only version of record
	 * does not get list of minorKeys
	 */
	
	private boolean anyMinorKey;
	
	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	private boolean allMinorKeys;
	
	/**
	 * Retrieve a specific list of minor keys (checks them for existance)
	 */
	
	private Set<String> minorKeys;
	
	public ExistsOperation(String storeName) {
		super(storeName);
	}

	public ExistsOperation(String storeName, String majorKey) {
		super(storeName, majorKey);
	}
	
	public boolean isAnyMinorKey() {
		return anyMinorKey;
	}

	/**
	 * If anyMinorKey flag set, then retrieve only version of record
	 * does not get list of minorKeys
	 */
	
	public ExistsOperation anyMinorKey(boolean any) {
		this.anyMinorKey = any;
		if (any) {
			this.allMinorKeys = false;
		}
		return this;
	}
	
	/**
	 * If anyMinorKey flag set, then retrieve only version of record
	 * does not get list of minorKeys
	 */
	
	public ExistsOperation anyMinorKey() {
		this.anyMinorKey = true;
		this.allMinorKeys = false;
		return this;
	}

	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	public ExistsOperation allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		if (all) {
			this.anyMinorKey = false;
		}
		return this;
	}
	
	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	public ExistsOperation allMinorKeys() {
		this.allMinorKeys = true;
		this.anyMinorKey = false;
		return this;
	}

	public ExistsOperation addMinorKey(String minorKey) {
		this.allMinorKeys = false;
		this.anyMinorKey = false;
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
	
	public ExistsOperation addMinorKeys(Collection<String> minorKeys) {
		this.allMinorKeys = false;
		this.anyMinorKey = false;
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
		return OpCode.EXISTS;
	}

	@Override
	public String toString() {
		return "ExistsOperation [allMinorKeys=" + allMinorKeys + ", minorKeys=" + minorKeys + ", cacheName=" + cacheName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
