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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import io.datty.api.operation.Version;
import io.datty.support.LongVersion;
import io.netty.buffer.ByteBuf;

/**
 * Unit implementation of record
 * 
 * @author dadril
 *
 */

public final class UnitRecord {

	private final ConcurrentMap<String, ByteBuf> columnMap = new ConcurrentHashMap<String, ByteBuf>();
	
	private final AtomicLong version = new AtomicLong(-1L);
	
	public void incrementVersion() {
		version.incrementAndGet();
	}
	
	public Version getVersion() {
		return new LongVersion(version.get());
	}
	
	public void clear() {
		columnMap.clear();
	}
	
	public boolean hasColumn(String minorKey) {
		return columnMap.containsKey(minorKey);
	}
	
	public ByteBuf getColumn(String minorKey) {
		return columnMap.get(minorKey);
	}
	
	public void addColumn(String minorKey, ByteBuf value) {
		ByteBuf old = columnMap.put(minorKey, value != null ? value.copy() : null);
		if (old != null) {
			old.release();
		}
	}
	
	public ByteBuf createColumn(String minorKey) {
		ByteBuf empty = UnitConstants.ALLOC.buffer();
		ByteBuf old = columnMap.put(minorKey, empty);
		if (old != null) {
			old.release();
		}
		return empty;
	}

	public Map<String, ByteBuf> getColumnMap() {
		return columnMap;
	}
	
}
