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

/**
 * DattyRecord
 * 
 * @author Alex Shvid
 *
 */

public final class DattyRecord {

	/**
	 * Own values or not
	 */
	
	private final boolean ownValues;
	
	/**
	 * key is the minorKey, value is payload
	 */
	
	private final Map<String, DattyValue> values = new HashMap<String, DattyValue>();
	
	public DattyRecord() {
		this.ownValues = false;
	}

	public DattyRecord(boolean ownValues) {
		this.ownValues = ownValues;
	}

	public void clear() {
		if (ownValues) {
			for (DattyValue value : values.values()) {
				value.release();
			}
		}
		values.clear();
	}
	
	public int size() {
		return values.size();
	}
	
	public DattyRecord put(String minorKey, DattyValue value) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		if (value == null) {
			throw new IllegalArgumentException("null value");
		}
		
		DattyValue prev = values.put(minorKey, ownValues ? value.retain() : value);
		if (prev != null && ownValues) {
			prev.release();
		}
		return this;
	}
	
	public DattyRecord remove(String minorKey) {
		
		if (minorKey == null) {
			throw new IllegalArgumentException("null minorKey");
		}
		
		DattyValue prev = values.remove(minorKey);
		if (prev != null && ownValues) {
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
		return "DattyRecord [values=" + values + "]";
	}
	
}
