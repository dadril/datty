package io.datty.msgpack.lite.util;

/**
 * PackableStringifyUtil
 * 
 * @author Alex Shvid
 *
 */

public final class PackableStringifyUtil {
	
	private final static char[] HEX_ARRAY = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	
	private PackableStringifyUtil() {
	}

	/**
	 * Number Type
	 *
	 */
	
	public enum NumberType {
		LONG, DOUBLE, NAN;
	}
	
	/**
	 * Detects number from string
	 * 
	 * @param stringifyValue - null or string representation of number
	 * @return not null number type
	 */
	
	public static NumberType detectNumber(String stringifyValue) {
		
		if (stringifyValue == null) {
			return NumberType.NAN;
		}
		
		int length = stringifyValue.length();
		if (length == 0) {
			return NumberType.NAN;
		}
		boolean first = true;
		boolean haveDot = false;
		boolean haveE = false;		
		for (int i = 0; i != length; ++i) {
			char ch = stringifyValue.charAt(i);
			if (first && ch == '-') {
				first = false;
				continue;
			}
			first = false;
			if (ch == '.') {
				if (haveDot) {
					return NumberType.NAN;
				}
				haveDot = true;
				continue;
			}
			if (ch == 'E') {
				if (haveE) {
					return NumberType.NAN;
				}
				haveE = true;
				continue;				
			}
			if (ch >= '0' && ch <= '9') {
				continue;
			}
			return NumberType.NAN;
		}
		return (haveDot || haveE) ? NumberType.DOUBLE : NumberType.LONG;
	}
	
	/**
	 * Converts bytes to hex string
	 * 
	 * @param bytes - incoming bytes or null
	 * @return string or null
	 */
	
	public static String toHex(byte[] bytes) {
		
		if (bytes == null) {
			return null;
		}
		
		int capacity = bytes.length << 1;
		
		char[] hexChars = new char[capacity];
		
		for (int i = 0, j = 0; i != bytes.length; ++i) {
			
			int v = bytes[i] & 0xFF;
			hexChars[j++] = HEX_ARRAY[v >>> 4];
			hexChars[j++] = HEX_ARRAY[v & 0x0F];
			
		}
		
		return new String(hexChars);
	}
	
}
