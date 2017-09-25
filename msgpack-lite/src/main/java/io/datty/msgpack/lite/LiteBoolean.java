package io.datty.msgpack.lite;

/**
 * Immutable Boolean type 
 * 
 * @author Alex Shvid
 *
 */

public interface LiteBoolean extends LiteValue<LiteBoolean> {

	/**
	 * Gets boolean value
	 * 
	 * @return bool value
	 */
	
	boolean asBoolean();

	
}
