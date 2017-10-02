package io.datty.msgpack.table;

/**
 * Immutable number interface
 * 
 * @author Alex Shvid
 *
 */

public interface PackableNumber extends PackableValue<PackableNumber> {

	/**
	 * Gets type of the Msg number
	 * 
	 * @return not null type
	 */
	
	PackableNumberType getType();
	
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
	
	PackableNumber add(PackableNumber otherNumber);
	PackableNumber add(long otherLongValue);
	PackableNumber add(double otherDoubleValue);

	/**
	 * Subtract number
	 * 
	 * @param number - number to add
	 * @return new immutable Msg number
	 */
	
	PackableNumber subtract(PackableNumber otherNumber);
	PackableNumber subtract(long otherLongValue);
	PackableNumber subtract(double otherDoubleValue);

}
