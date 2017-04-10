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

import io.datty.api.operation.GetOperation;
import io.datty.api.operation.Version;
import io.netty.buffer.ByteBuf;

/**
 * GetResult
 * 
 * @author dadril
 *
 */

public final class GetResult extends AbstractResult<GetOperation, GetResult> {

	/**
	 * Record version if exists
	 */
	
	private final Version version;
	
	/**
	 * key is the minorKey, value is payload
	 */
	
	private final Map<String, ByteBuf> values;
	
	public GetResult() {
		this(null, null);
	}
	
	public GetResult(Version version, Map<String, ByteBuf> values) {
		this.version = version;
		this.values = values;
	}
	
	public static GetResult absent() {
		return new GetResult();
	}
	
	public static GetResult of(Version version, Map<String, ByteBuf> values) {
		return new GetResult(version, values);
	}
	
	public boolean hasVersion() {
		return version != null;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public boolean exists() {
		return values != null;
	}

	public boolean isEmpty() {
		
		if (!exists()) {
			return true;
		}
		
		return values.isEmpty();
	}
	
	public int size() {
		
		if (!exists()) {
			return 0;
		}
		
		return values.size();
	}
	
	public Set<String> minorKeys() {
		
		if (!exists()) {
			return Collections.emptySet();
		}
		
		return values.keySet();
	}
	
	public ByteBuf get(String minorKey) {
		
		if (!exists()) {
			return null;
		}
		
		return values.get(minorKey);
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.GET;
	}

	@Override
	public String toString() {
		return "GetResult [version=" + version + ", values=" + values + "]";
	}

}
