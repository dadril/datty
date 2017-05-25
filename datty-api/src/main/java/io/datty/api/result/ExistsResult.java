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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.datty.api.operation.ExistsOperation;
import io.datty.api.operation.Version;

/**
 * ExistsResult
 * 
 * @author Alex Shvid
 *
 */

public class ExistsResult extends AbstractResult<ExistsOperation, ExistsResult> {

	/**
	 * Record exists only if version exists too
	 * 
	 * For anyMinorKey returns only version
	 * For allMinorKeys returns version and all minorKeys set
	 * For selected minorKeys returns version and only existing minorKeys set
	 */
	
	private Version version;
	
	/**
	 * Retrieved minorKeys from the record (all or selected)
	 */
	
	private Set<String> minorKeys;
	
	public ExistsResult() {
	}
	
	public boolean hasVersion() {
		return version != null;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public ExistsResult setVersion(Version version) {
		this.version = version;
		return this;
	}
	
	public boolean exists() {
		return this.version != null;
	}
	
	public boolean isEmpty() {
		
		if (this.minorKeys == null) {
			return true;
		}
		
		return this.minorKeys.isEmpty();
	}
	
	public int size() {
		
		if (this.minorKeys == null) {
			return 0;
		}
		
		return this.minorKeys.size();
	}
	
	public Set<String> minorKeys() {
		
		if (this.minorKeys == null) {
			return Collections.emptySet();
		}
		
		return this.minorKeys;
	}
	
	public boolean exists(String minorKey) {
		
		if (this.minorKeys == null) {
			return false;
		}
		
		return this.minorKeys.contains(minorKey);
	}
	
	public ExistsResult addMinorKey(String minorKey) {
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
	
	public ExistsResult addMinorKeys(Collection<String> minorKeys) {
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
	
	@Override
	public ResCode getCode() {
		return ResCode.EXISTS;
	}

	@Override
	public String toString() {
		return "ExistsResult [version=" + version + ", minorKeys=" + minorKeys + "]";
	}

}
