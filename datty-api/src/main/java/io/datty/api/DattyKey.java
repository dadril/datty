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

/**
 * Datty key for streaming operations
 * 
 * @author dadril
 *
 */

public final class DattyKey {

	private String cacheName;
	
	private String superKey;

	private String majorKey;
	
	private String minorKey;
	
	public DattyKey() {
	}
	
	public DattyKey(String cacheName) {
		this.cacheName = cacheName;
	}

	public DattyKey(String cacheName, String majorKey) {
		this.cacheName = cacheName;
		this.majorKey = majorKey;
	}

	public String getCacheName() {
		return cacheName;
	}

	public DattyKey setCacheName(String cacheName) {
		this.cacheName = cacheName;
		return this;
	}

	public String getSuperKey() {
		return superKey;
	}

	public DattyKey setSuperKey(String superKey) {
		this.superKey = superKey;
		return this;
	}

	public String getMajorKey() {
		return majorKey;
	}

	public DattyKey setMajorKey(String majorKey) {
		this.majorKey = majorKey;
		return this;
	}

	public String getMinorKey() {
		return minorKey;
	}

	public DattyKey setMinorKey(String minorKey) {
		this.minorKey = minorKey;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheName == null) ? 0 : cacheName.hashCode());
		result = prime * result + ((majorKey == null) ? 0 : majorKey.hashCode());
		result = prime * result + ((minorKey == null) ? 0 : minorKey.hashCode());
		result = prime * result + ((superKey == null) ? 0 : superKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DattyKey other = (DattyKey) obj;
		if (cacheName == null) {
			if (other.cacheName != null)
				return false;
		} else if (!cacheName.equals(other.cacheName))
			return false;
		if (majorKey == null) {
			if (other.majorKey != null)
				return false;
		} else if (!majorKey.equals(other.majorKey))
			return false;
		if (minorKey == null) {
			if (other.minorKey != null)
				return false;
		} else if (!minorKey.equals(other.minorKey))
			return false;
		if (superKey == null) {
			if (other.superKey != null)
				return false;
		} else if (!superKey.equals(other.superKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DattyKey [cacheName=" + cacheName + ", superKey=" + superKey + ", majorKey=" + majorKey + ", minorKey="
				+ minorKey + "]";
	}
	
	
}
