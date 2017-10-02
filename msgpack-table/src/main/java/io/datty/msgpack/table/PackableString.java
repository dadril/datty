package io.datty.msgpack.table;

/**
 * Immutable String
 * 
 * That can be two types: UFT-8 and BYTES strings
 * 
 * @author Alex Shvid
 *
 */

public interface PackableString extends PackableValue<PackableString> {

	/**
	 * Gets type of the string
	 * 
	 * @return not null type, can by UTF8 or Bytes
	 */
	
	PackableStringType getType();

	/**
	 * Gets value as utf8 string
	 * 
	 * @return utf8 string
	 */
	
	String toUtf8();
	
	/**
	 * Gets value as byte array
	 * 
	 * @param copy - copy bytes or not
	 * @return not null bytes
	 */
	
	byte[] getBytes(boolean copy);
	
}
