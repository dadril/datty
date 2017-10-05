/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table.util;

import io.datty.msgpack.table.PackableBoolean;
import io.datty.msgpack.table.PackableNumber;
import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableValue;

/**
 * Base util class for explicit conversion from one simple type to another
 * 
 * This is the central point of all explicit conversions
 * 
 * @author Alex Shvid
 *
 */

public final class PackableValueUtil {

	private PackableValueUtil() {
	}
	
	/**
	 * Casts Msg value to Msg table if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg boolean or null
	 */
	
	public static PackableTable toTable(PackableValue<?> value) {
		if (value != null && value instanceof PackableTable) {
			return (PackableTable) value;
		}
		return null;
	}

	/**
	 * Converts Msg value to Msg boolean if possible and if needed
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg boolean or null
	 */
	
	public static PackableBoolean toBoolean(PackableValue<?> value) {
		if (value != null) {
			if (value instanceof PackableBoolean) {
				return (PackableBoolean) value;
			}
			else {
				return new PackableBoolean(value.asString());
			}
		}
		return null;
	}
	
	/**
	 * Converts Msg value to Msg number if possible and if needed
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg number or null
	 */
	
	public static PackableNumber toNumber(PackableValue<?> value) {
		if (value != null) {
			if (value instanceof PackableNumber) {
				return (PackableNumber) value;
			}
			else {
				return new PackableNumber(value.asString());
			}
		}
		return null;
	}
	
	/**
	 * Converts Msg value to Msg string if possible and if needed
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg string or null
	 */
	
	public static PackableString toString(PackableValue<?> value) {
		if (value != null) {
			if (value instanceof PackableString) {
				return (PackableString) value;
			}
			else {
				return new PackableString(value.asString());
			}
		}
		return null;
	}
	
}
