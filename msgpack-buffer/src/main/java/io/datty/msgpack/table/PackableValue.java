/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import io.netty.buffer.ByteBuf;

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
	 * Writes payload to buffer
	 * 
	 * @param not null buffer
	 * @throws IOException
	 */
	
	ByteBuf pack(ByteBuf buffer)  throws IOException;
	
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
