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
package io.datty.aerospike;

import java.util.StringTokenizer;

import io.datty.support.exception.DattyException;

/**
 * AerospikeVersion
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeVersion implements Comparable<AerospikeVersion> {

	private final int major;
	private final int minor;
	
	public AerospikeVersion(final int major, final int minor) {
		this.major = major;
		this.minor = minor;
	}

	public static AerospikeVersion of(int major, int minor) {
		return new AerospikeVersion(major, minor);
	}
	
	public static AerospikeVersion parse(String response) {
		
		StringTokenizer tokenizer = new StringTokenizer(response, ".");
		
		int major = 0;
		int minor = 0;
		
		if (tokenizer.hasMoreTokens()) {
			try {
				major = Integer.parseInt(tokenizer.nextToken());
			}
			catch(NumberFormatException e) {
				throw new DattyException("invalid aerospike version in response: " + response);
			}
		}
		
		if (tokenizer.hasMoreTokens()) {
			try {
				minor = Integer.parseInt(tokenizer.nextToken());
			}
			catch(NumberFormatException e) {
				throw new DattyException("invalid aerospike version in response: " + response);
			}
		}
		
		return new AerospikeVersion(major, minor);
	}
	
	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	@Override
	public int compareTo(AerospikeVersion o) {
		int c = Integer.compare(major, o.major);
		if (c == 0) {
			c = Integer.compare(minor, o.minor);
		}
		return c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
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
		AerospikeVersion other = (AerospikeVersion) obj;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + major + "." + minor;
	}
	
}
