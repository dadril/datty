package io.datty.msgpack.lite;

/**
 * Immutable Number interface
 * 
 * @author Alex Shvid
 *
 */

public interface LiteNumber extends LiteValue<LiteNumber> {

	/**
	 * Gets type of the Msg number
	 * 
	 * @return not null type
	 */
	
	LiteNumberType getType();
	
	/**
	 * Gets long, even if value is double, it will be converted to long
	 * 
	 * @return long value
	 */
	
	long asLong();
	
	/**
	 * Gets double, even if value is long, it will be converted to double
	 * 
	 * @return double value
	 */
	
	double asDouble();
	
	/**
	 * Add number
	 * 
	 * @param number - number to add
	 * @return new immutable Msg number
	 */
	
	LiteNumber add(LiteNumber otherNumber);
	LiteNumber add(long otherLongValue);
	LiteNumber add(double otherDoubleValue);

	/**
	 * Subtract number
	 * 
	 * @param number - number to add
	 * @return new immutable Msg number
	 */
	
	LiteNumber subtract(LiteNumber otherNumber);
	LiteNumber subtract(long otherLongValue);
	LiteNumber subtract(double otherDoubleValue);

}
