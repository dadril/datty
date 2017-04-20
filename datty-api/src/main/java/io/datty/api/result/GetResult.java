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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import io.datty.api.DattyRow;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.Version;
import io.netty.buffer.ByteBuf;

/**
 * GetResult
 * 
 * @author Alex Shvid
 *
 */

public class GetResult extends AbstractResult<GetOperation, GetResult> {

	/**
	 * Record version if exists
	 */
	
	private Version version;
	
	private final DattyRow row = new DattyRow();
	
	public GetResult() {
	}
	
	public DattyRow getRow() {
		return row;
	}
	
	public GetResult addValue(String minorKey, ByteBuf valueOrNull) {
		row.addValue(minorKey, valueOrNull);
		return this;
	}
	
	public GetResult addValues(Map<String, ByteBuf> map) {
		row.addValues(map);
		return this;
	}
	
	public boolean hasVersion() {
		return version != null;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public GetResult setVersion(Version version) {
		this.version = version;
		return this;
	}
	
	public boolean exists() {
		return this.version != null;
	}

	public boolean isEmpty() {
		
		if (!exists()) {
			return true;
		}
		
		return row.isEmpty();
	}
	
	public int size() {
		
		if (!exists()) {
			return 0;
		}
		
		return row.size();
	}
	
	public Set<String> minorKeys() {
		
		if (!exists()) {
			return Collections.emptySet();
		}
		
		return row.minorKeys();
	}
	
	public ByteBuf get(String minorKey) {
		
		if (!exists()) {
			return null;
		}
		
		return row.get(minorKey);
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.GET;
	}

	@Override
	public String toString() {
		return "GetResult [version=" + version + ", row=" + row + "]";
	}

}
