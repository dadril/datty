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
 * Supported versions
 * 
 * @author Alex Shvid
 *
 */

public enum VersionType {

	LONG(1), 
	
	STRING(2);
	
	private final int code;
	
	private VersionType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static VersionType findByCode(int code) {
		for (VersionType v : values()) {
			if (v.getCode() == code) {
				return v;
			}
		}
		return null;
	}
	
}
