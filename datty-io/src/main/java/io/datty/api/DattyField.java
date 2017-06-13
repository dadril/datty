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

/**
 * DattyField
 * 
 * Key fields
 * 
 * @author Alex Shvid
 *
 */

public enum DattyField {

	OPCODE(1, "opcode"), // Integer
	RESCODE(2, "rescode"), // Integer

	SET_NAME(10, "setName"), // String
	SUPER_KEY(11, "superKey"), // String
	MAJOR_KEY(12, "majorKey"), // String
	ALL_MINOR_KEYS(13, "allMinorKeys"), // Boolean
	MINOR_KEYS(14, "minorKeys"), // Collection of Strings
	TIMEOUT_MLS(15, "timeoutMillis"), // Integer
	TTL_SEC(16, "ttlSecods"), // Integer
	UPDATE_POLICY(17, "updatePolicy"), // Integer
	ROW(18, "row"), // DattyRow
	VERSION(19, "version"), // Version
	COUNT(20, "count"), // Long
	PACKAGE_NAME(21, "packageName"), // String
	FUNCTION_NAME(22, "functionName"), // String
	ARGUMENTS(23, "arguments"), // String

	VERSION_TYPE(24, "type"), // Integer
	VERSION_LONG(25, "long"), // Long
	VERSION_STRING(26, "string"), // String

	BOOL_VALUE(27, "bool"), // Boolean
	LONG_VALUE(28, "long"), // Long
	BYTES_VALUE(29, "bytes"); // ByteBuf

	private final int code;
	private final String name;

	private final static DattyField[] codeCache;
	private final static Map<String, DattyField> nameIndex;
	
	static {
		int maxCode = DattyField.max();
		codeCache = new DattyField[maxCode + 1];
		nameIndex = new HashMap<String, DattyField>();
		
		for (DattyField v : values()) {
			codeCache[v.getFieldCode()] = v;
			nameIndex.put(v.getFieldName(), v);
		}
		
	}
	
	private DattyField(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getFieldCode() {
		return code;
	}

	public String getFieldName() {
		return name;
	}
	
	public static DattyField findByKey(Object fieldKey) {
		if (fieldKey instanceof Integer) {
			return findByCode((Integer) fieldKey);
		}
		else if (fieldKey instanceof String) {
			return findByName((String) fieldKey);
		}
		else {
			return null;
		}
	}
	
	public static DattyField findByCode(int fieldCode) {
		if (fieldCode < 0 || fieldCode >= codeCache.length) {
			return null;
		}
		return codeCache[fieldCode];
	}
	
	public static DattyField findByName(String fieldName) {
		return nameIndex.get(fieldName);
	}
	
	public static int max() {
		int max = Integer.MIN_VALUE;
		for (DattyField v : values()) {
			if (max < v.getFieldCode()) {
				max = v.getFieldCode();
			}
		}
		return max;
	}

}
