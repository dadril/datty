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

import java.util.Map;
import java.util.Set;

import io.datty.api.DattyResult;
import io.datty.api.operation.Version;
import io.datty.support.exception.RecordNotExistsException;
import io.netty.buffer.ByteBuf;

/**
 * RecordResult
 * 
 * @author dadril
 *
 */

public final class RecordResult extends AbstractResult {

	/**
	 * Record version if exists
	 */
	
	private final Version version;
	
	/**
	 * key is the minorKey, value is payload
	 */
	
	private final Map<String, ByteBuf> values;
	
	public RecordResult() {
		this(null, null);
	}
	
	public RecordResult(Version version, Map<String, ByteBuf> values) {
		this.version = version;
		this.values = values;
	}
	
	public static DattyResult absent() {
		return new RecordResult();
	}
	
	public static DattyResult of(Version version, Map<String, ByteBuf> values) {
		return new RecordResult(version, values);
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
			throw new RecordNotExistsException();
		}
		
		return values.isEmpty();
	}
	
	public int size() {
		
		if (!exists()) {
			throw new RecordNotExistsException();
		}
		
		return values.size();
	}
	
	public Set<String> minorKeys() {
		
		if (!exists()) {
			throw new RecordNotExistsException();
		}
		
		return values.keySet();
	}
	
	public ByteBuf get(String minorKey) {
		
		if (!exists()) {
			throw new RecordNotExistsException();
		}
		
		return values.get(minorKey);
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.RECORD;
	}

	@Override
	public String toString() {
		return "RecordResult [version=" + version + ", values=" + values + "]";
	}

}
