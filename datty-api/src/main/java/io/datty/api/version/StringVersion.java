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
package io.datty.api.version;

/**
 * StringVersion
 * 
 * @author Alex Shvid
 *
 */

public class StringVersion implements Version {

	private final String value;
	
	public StringVersion(String value) {
		this.value = value;
	}
	
	@Override
	public VersionType type() {
		return VersionType.STRING;
	}

	@Override
	public long asLong() {
		try {
			return Long.parseLong(value);
		}
		catch(NumberFormatException e) {
			return 0L;
		}
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		StringVersion other = (StringVersion) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StringVersion [value=" + value + "]";
	}

}
