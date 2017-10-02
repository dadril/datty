package io.datty.msgpack.table.util;

import io.datty.msgpack.table.PackableBoolean;
import io.datty.msgpack.table.PackableNumber;
import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableValue;
import io.datty.msgpack.table.impl.PackableBooleanImpl;
import io.datty.msgpack.table.impl.PackableNumberImpl;
import io.datty.msgpack.table.impl.PackableStringImpl;

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
				return new PackableBooleanImpl(value.asString());
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
				return new PackableNumberImpl(value.asString());
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
				return new PackableStringImpl(value.asString());
			}
		}
		return null;
	}
	
}
