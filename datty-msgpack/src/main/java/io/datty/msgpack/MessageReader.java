/*
 * Copyright (C) 2016 Datty.io Authors
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
package io.datty.msgpack;

import io.datty.msgpack.core.type.TypeInfo;
import io.netty.buffer.ByteBuf;

/**
 * MessageReader
 * 
 * @author Alex Shvid
 *
 */

public interface MessageReader {

	/**
	 * Gets number of entries
	 * 
	 * @return size
	 */
	
	int size();
	
	/**
	 * Reads the key of the message entry
	 * 
	 * Return types:
	 * null
	 * Integer
	 * String
	 * 
	 * @param source - input buffer
	 * @return Integer or String or null if no entries
	 */
	
	Object readKey(ByteBuf source);
	
	/**
	 * Reads value of the message entry
	 * 
	 * Return types:
	 * null
	 * Boolean
	 * Long
	 * Double
	 * String
	 * ByteBuf
	 * MessageReader
	 * 
	 * @param source - input buffer
	 * @param copy - always copy content if true
	 * @return java value object or null, could be a MessageReader object if value is the Map or Array
	 */
	
	Object readValue(ByteBuf source, boolean copy);
	
	/**
	 * Does not parse value, only returns ByteBuf pointing to the value
	 * 
	 * @param source - input buffer
	 * @param copy - always copy content if true
	 * @return buffer or null
	 */
	
	ByteBuf skipValue(ByteBuf source, boolean copy);
	
	/**
	 * Reads value from the source with expecting value type
	 * 
	 * Automatically converts value to the expecting type
	 * 
	 * @param type - expecting type
	 * @param source - source
	 * @param copy - always copy content if true
	 * @return java object or null
	 */
	
	<T> T readValue(TypeInfo<T> type, ByteBuf source, boolean copy);
	
}
