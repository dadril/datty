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
 * TTL is only for streamIn operation
 * 
 * @author Alex Shvid
 *
 */

public final class DattyKey {

	private String setName;
	
	private String superKey;

	private String majorKey;
	
	private String minorKey;
	
	private int ttlSeconds = DattyConstants.UNSET_TTL;
	
	public DattyKey() {
	}
	
	public DattyKey(String setName) {
		
		if (setName == null) {
			throw new IllegalArgumentException("empty set name");
		}
		
		this.setName = setName;
	}

	public DattyKey(String setName, String majorKey) {
		
		if (setName == null) {
			throw new IllegalArgumentException("empty set name");
		}
		
		this.setName = setName;
		this.majorKey = majorKey;
	}

	public String getSetName() {
		return setName;
	}

	public DattyKey setSetName(String setName) {
		
		if (setName == null) {
			throw new IllegalArgumentException("empty set name");
		}
		
		this.setName = setName;
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
	
	public boolean hasTtlSeconds() {
		return ttlSeconds != DattyConstants.UNSET_TTL;
	}
	
	public int getTtlSeconds() {
		return ttlSeconds;
	}

	public DattyKey setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return this;
	}
	
	public DattyKey withTimeToLive(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((setName == null) ? 0 : setName.hashCode());
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
		if (setName == null) {
			if (other.setName != null)
				return false;
		} else if (!setName.equals(other.setName))
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
		return "DattyKey [setName=" + setName + ", superKey=" + superKey + ", majorKey=" + majorKey + ", minorKey="
				+ minorKey + "]";
	}
	
	
}
