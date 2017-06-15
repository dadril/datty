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
package io.datty.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * DattyRow
 * 
 * @author Alex Shvid
 *
 */

public final class DattyRow {

	private final ByteBufAllocator alloc;
	
	/**
	 * key is the minorKey, value is payload
	 */
	
	private final Map<String, DattyValue> values = new HashMap<String, DattyValue>();
	
	public DattyRow() {
		this(DattyConstants.ALLOC);
	}
	
	public DattyRow(ByteBufAllocator alloc) {
		
		if (alloc == null) {
			throw new IllegalArgumentException("null alloc");
		}
		
		this.alloc = alloc;
	}
	
	public void reset() {
		for (DattyValue value : values.values()) {
			value.reset();
		}
	}

	public void clear() {
		for (DattyValue value : values.values()) {
			value.clear();
		}
	}
	
	public void release() {
		for (DattyValue value : values.values()) {
			value.release();
		}
		values.clear();
	}
	
	public int size() {
		return values.size();
	}
	
	public ByteBuf getOrCreateValue(String minorKey) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		DattyValue prev = values.get(minorKey);
		if (prev != null && prev.hasByteBuf()) {
			prev.clear();
			return prev.asByteBuf();
		}
		ByteBuf buffer = alloc.buffer();
		values.put(minorKey, new ByteBufValue(buffer));
		return buffer;
	}
	
	public DattyRow addValue(String minorKey, DattyValue value, boolean release) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		if (value == null) {
			throw new IllegalArgumentException("null value");
		}
		
		DattyValue prev = values.put(minorKey, value);
		if (prev != null && release) {
			prev.release();
		}
		return this;
	}
	
	public DattyRow removeValue(String minorKey, boolean release) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		DattyValue prev = values.remove(minorKey);
		if (prev != null && release) {
			prev.release();
		}
		return this;
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public Set<String> minorKeys() {
		return this.values.keySet();
	}
	
	public DattyValue get(String minorKey) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		return this.values.get(minorKey);
	}

	public Map<String, DattyValue> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "DattyRow [values=" + values + "]";
	}
	
}
