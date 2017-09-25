package io.datty.msgpack.lite.util;

import io.datty.msgpack.lite.LiteBoolean;
import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteString;
import io.datty.msgpack.lite.LiteTable;
import io.datty.msgpack.lite.LiteValue;
import io.datty.msgpack.lite.impl.LiteBooleanImpl;
import io.datty.msgpack.lite.impl.LiteNumberImpl;
import io.datty.msgpack.lite.impl.LiteStringImpl;

/**
 * Base util class for explicit conversion from one simple type to another
 * 
 * This is the central point of all explicit conversions
 * 
 * @author Alex Shvid
 *
 */

public final class LiteValueUtil {

	private LiteValueUtil() {
	}
	
	/**
	 * Casts Msg value to Msg table if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg boolean or null
	 */
	
	public static LiteTable toTable(LiteValue<?> value) {
		if (value != null && value instanceof LiteTable) {
			return (LiteTable) value;
		}
		return null;
	}

	/**
	 * Converts Msg value to Msg boolean if possible and if needed
	 * 
	 * @param value - Msg value with unknown type or null
	 * @return Msg boolean or null
	 */
	
	public static LiteBoolean toBoolean(LiteValue<?> value) {
		if (value != null) {
			if (value instanceof LiteBoolean) {
				return (LiteBoolean) value;
			}
			else {
				return new LiteBooleanImpl(value.asString());
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
	
	public static LiteNumber toNumber(LiteValue<?> value) {
		if (value != null) {
			if (value instanceof LiteNumber) {
				return (LiteNumber) value;
			}
			else {
				return new LiteNumberImpl(value.asString());
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
	
	public static LiteString toString(LiteValue<?> value) {
		if (value != null) {
			if (value instanceof LiteString) {
				return (LiteString) value;
			}
			else {
				return new LiteStringImpl(value.asString());
			}
		}
		return null;
	}
	
}
