package io.datty.msgpack.table;

/**
 * Immutable boolean type 
 * 
 * @author Alex Shvid
 *
 */

public interface PackableBoolean extends PackableValue<PackableBoolean> {

	/**
	 * Gets boolean value
	 * 
	 * @return bool value
	 */
	
	boolean asBoolean();

	
}
