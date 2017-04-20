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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;

/**
 * DattyRow
 * 
 * @author Alex Shvid
 *
 */

public final class DattyRow {

	/**
	 * key is the minorKey, value is payload
	 */
	
	private Map<String, ByteBuf> values;
	
	public DattyRow addValue(String minorKey, ByteBuf valueOrNull) {
		if (this.values == null) {
			this.values = Collections.singletonMap(minorKey, valueOrNull);
		}
		else {
			if (this.values.size() == 1) {
				this.values = new HashMap<>(this.values);
			}
			this.values.put(minorKey, valueOrNull);
		}
		return this;
	}
	
	public DattyRow addValues(Map<String, ByteBuf> map) {
		if (this.values == null) {
			this.values = new HashMap<String, ByteBuf>(map);
		}
		else {
			if (this.values.size() == 1) {
				this.values = new HashMap<String, ByteBuf>(this.values);
			}
			this.values.putAll(map);
		}
		return this;
	}
	
	public void clear() {
		this.values = null;
	}
	
	public boolean isEmpty() {
		
		if (this.values == null) {
			return true;
		}
		
		return this.values.isEmpty();
	}
	
	public int size() {
		
		if (this.values == null) {
			return 0;
		}
		
		return this.values.size();
	}
	
	public Set<String> minorKeys() {
		
		if (this.values == null) {
			return Collections.emptySet();
		}
		
		return this.values.keySet();
	}
	
	public ByteBuf get(String minorKey) {
		
		if (this.values == null) {
			return null;
		}
		
		return this.values.get(minorKey);
	}

	public Map<String, ByteBuf> getValues() {
		return values != null ? values : Collections.<String, ByteBuf>emptyMap();
	}

	@Override
	public String toString() {
		return "DattyRow [values=" + values + "]";
	}
	
}
