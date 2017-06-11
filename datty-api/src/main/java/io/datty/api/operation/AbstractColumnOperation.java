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

import io.datty.api.result.TypedResult;

/**
 * Abstract column operation
 * 
 * @author Alex Shvid
 *
 */

abstract class AbstractColumnOperation<O extends TypedOperation<O, R>, R extends TypedResult<O>> extends AbstractOperation<O, R> implements TypedOperation<O, R> {

	/**
	 * If allMinorKeys flag set, then retrieve all list of minor keys in record
	 */
	
	protected boolean allMinorKeys;
	
	/**
	 * Retrieve a specific list of minor keys (checks them for existence)
	 */
	
	protected Set<String> minorKeys;
	
	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	public O allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return castThis();
	}
	
	public O allMinorKeys() {
		this.allMinorKeys = true;
		return castThis();
	}
	
	public O addMinorKey(String minorKey) {
		if (this.minorKeys == null) {
			this.minorKeys = Collections.singleton(minorKey);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}			
			this.minorKeys.add(minorKey);
		}
		return castThis();
	}
	
	public O addMinorKeys(Collection<String> minorKeys) {
		if (this.minorKeys == null) {
			this.minorKeys = new HashSet<String>(minorKeys);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}
			this.minorKeys.addAll(minorKeys);
		}
		return castThis();
	}
	
	public Set<String> getMinorKeys() {
		return minorKeys != null ? minorKeys : Collections.<String>emptySet();
	}

}
