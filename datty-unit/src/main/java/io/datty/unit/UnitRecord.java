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
package io.datty.unit;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.datty.api.UpdatePolicy;
import io.datty.api.operation.Version;
import io.datty.support.LongVersion;
import io.netty.buffer.ByteBuf;

/**
 * Unit implementation of record
 * 
 * Immutable record
 * 
 * @author dadril
 *
 */

public final class UnitRecord {

	private final Map<String, UnitValue> columnMap;
	
	private final long version;

	public UnitRecord(String minorKey, UnitValue value) {
		ImmutableMap.Builder<String, UnitValue> builder = ImmutableMap.builder();
		if (value != null) {
			builder.put(minorKey, value);
		}
		this.columnMap = builder.build();
		this.version = 1L;
	}
	
	public UnitRecord(UnitRecord previous, String minorKey, UnitValue value, UpdatePolicy updatePolicy) {
		ImmutableMap.Builder<String, UnitValue> builder = ImmutableMap.builder();
		
		for (Map.Entry<String, UnitValue> e : previous.columnMap.entrySet()) {
			if (e.getKey().equals(minorKey)) {
				if (value != null) {
					builder.put(minorKey, value);
				}
				else {
					builder.put(e);
				}
			}
		}
		
		this.columnMap = builder.build();
		this.version = 1L;
	}
	
	public UnitRecord(Map<String, ByteBuf> map) {
		this.columnMap = toImmutableBuilder(map).build();
		this.version = 1L;
	}
	
	public UnitRecord(UnitRecord previous, Map<String, ByteBuf> map, UpdatePolicy updatePolicy) {
		
		switch(updatePolicy) {
		
		case REPLACE:
			this.columnMap = toImmutableBuilder(map).build();
			break;
			
		case MERGE:
			ImmutableMap.Builder<String, UnitValue> builder = toImmutableBuilder(map);
			for (Map.Entry<String, UnitValue> e : previous.columnMap.entrySet()) {
				if (!map.containsKey(e.getKey())) {
					builder.put(e);
				}
			}
			this.columnMap = builder.build();
			break;
			
		default:
			throw new IllegalArgumentException("unknown update policy: " + updatePolicy);	
		}
				
		this.version = previous.version + 1;
	}
	
	private ImmutableMap.Builder<String, UnitValue> toImmutableBuilder(Map<String, ByteBuf> map) {
		ImmutableMap.Builder<String, UnitValue> builder = ImmutableMap.builder();
		for (Map.Entry<String, ByteBuf> e : map.entrySet()) {
			ByteBuf valueOrNull = e.getValue();
			if (valueOrNull != null) {
				builder.put(e.getKey(), new UnitValue(e.getValue()));
			}
		}
		return builder;
	}
	
	public Version getVersion() {
		return new LongVersion(version);
	}
	
	public boolean hasColumn(String minorKey) {
		return columnMap.containsKey(minorKey);
	}
	
	public UnitValue getColumn(String minorKey) {
		return columnMap.get(minorKey);
	}
	
	public Map<String, UnitValue> getColumnMap() {
		return columnMap;
	}
	
	public boolean isEmpty() {
		return columnMap.isEmpty();
	}
	
}
