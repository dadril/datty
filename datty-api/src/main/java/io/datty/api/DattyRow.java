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
	
	private final Map<String, ByteBuf> values = new HashMap<String, ByteBuf>();
	
	public DattyRow() {
		this(DattyConstants.ALLOC);
	}
	
	public DattyRow(ByteBufAllocator alloc) {
		this.alloc = alloc;
	}
	
	public void release() {
		for (ByteBuf value : values.values()) {
			value.release();
		}
		values.clear();
	}
	
	public void clear() {
		for (ByteBuf value : values.values()) {
			value.resetReaderIndex().resetWriterIndex();
		}
	}
	
	public int size() {
		return values.size();
	}
	
	public ByteBuf addValue(String minorKey) {
		ByteBuf buffer = values.get(minorKey);
		if (buffer == null) {
			buffer = alloc.buffer();
			values.put(minorKey, buffer);
		}
		else {
			buffer.resetReaderIndex().resetWriterIndex();
		}
		return buffer;
	}
	
	public DattyRow removeValue(String minorKey, boolean release) {
		ByteBuf prev = values.remove(minorKey);
		if (prev != null && release) {
			prev.release();
		}
		return this;
	}
	
	public DattyRow putValue(String minorKey, ByteBuf value, boolean release) {
		ByteBuf prev = values.put(minorKey, value);
		if (prev != null && release) {
			prev.release();
		}
		return this;
	}
	
	public boolean isEmpty() {
		for (ByteBuf value : values.values()) {
			if (value != null && value.readableBytes() > 0) {
				return false;
			}
		}
		return true;
	}
	
	public Set<String> minorKeys() {
		return this.values.keySet();
	}
	
	public ByteBuf get(String minorKey) {
		return this.values.get(minorKey);
	}

	public Map<String, ByteBuf> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "DattyRow [values=" + values + "]";
	}
	
}
