package io.datty.msgpack.table;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

/**
 * Base interface for all packable values
 * 
 * @author Alex Shvid
 *
 * @param <T> - child type
 */

public interface PackableValue<T> {

	/**
	 * Gets value as a string
	 * 
	 * @return string representation of the value
	 */
	
	String asString();
	
	/**
	 * Gets msgpack value
	 * 
	 * @return not null msgpack value
	 */
	
	Value toValue();

	/**
	 * Gets msgpack json value
	 * 
	 * @return not null json value
	 */
	
	String toJson();
	
	/**
	 * Writes payload to packer
	 * 
	 * This method is faster then toValue().writeTo(packer) 
	 * because does not create intermediate Value object
	 * 
	 * @param packer - output packer
	 * throws IOException
	 */
	
	void writeTo(MessagePacker packer) throws IOException;
	
	/**
	 * Gets hex string value representation
	 * 
	 * @return not null hex string
	 */
	
	String toHexString();
	
	/**
	 * Gets byte array representation of the value
	 * 
	 * @return serialized value
	 */
	
	byte[] toByteArray();
	
	/**
	 * Prints value to string
	 * 
	 * @param out - output string builder
	 * @param initialSpaces - initial number of spaces
	 * @param tabSpaces - tab number of spaces
	 */
	
	void print(StringBuilder out, int initialSpaces, int tabSpaces);
	
}
