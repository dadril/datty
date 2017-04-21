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

import io.netty.buffer.ByteBuf;

/**
 * MessageWriter
 * 
 * @author Alex Shvid
 *
 */

public interface MessageWriter {

	/**
	 * Skips header to reserve the space in the sink
	 * 
	 * @param maxSize - maximum number of entries in the map
	 * @param sink - output buffer
	 * @return writeIndex of the map header
	 */
	
	int skipHeader(int maxSize, ByteBuf sink);
	
	/**
	 * Writes header on the reserved slot in the sink
	 * 
	 * @param size - actual size of the map
	 * @param maxSize - maximum number of entries in the map
	 * @param sink - output buffer
	 */
	
	void writeHeader(int size, int maxSize, int headerIndex, ByteBuf sink);
	
	/**
	 * Writes message header with number of elements
	 * 
	 * @param size - number of entries in map
	 * @param sink - output buffer
	 * @return CompositeByteBuf of header and sink
	 */
	
	ByteBuf writeHeader(int size, ByteBuf sink);
	
	/**
	 * Writes the key of the map as a string
	 * 
	 * @param key - not null string
	 * @param sink - output buffer
	 */
	
	void writeKey(String key, ByteBuf sink);
	
	/**
	 * Writes the key of th map as a int
	 * 
	 * @param key - int key
	 * @param sink - output buffer
	 */
	
	void writeKey(int key, ByteBuf sink);
	
	/**
	 * Writes boolean value
	 * 
	 * @param value - boolean value
	 * @param sink - output buffer
	 */
	
	void writeValue(boolean value, ByteBuf sink);
	
	/**
	 * Writes long value
	 * 
	 * @param value - long value
	 * @param sink - output buffer
	 */
	
	void writeValue(long value, ByteBuf sink);
	
	/**
	 * Writes double value
	 * 
	 * @param value - double value
	 * @param sink - output buffer
	 */
	
	void writeValue(double value, ByteBuf sink);
	
	/**
	 * Writes string value
	 * 
	 * @param value - string value
	 * @param sink - output buffer
	 */
	
	void writeValue(String value, ByteBuf sink);
	
	/**
	 * Writes payload value
	 * 
	 * @param value - buffer value
	 * @param sink - output buffer
	 * @param copy - always copy value if true
	 * @return sink or CompositeByteBuf
	 */
	
	ByteBuf writeValue(ByteBuf value, ByteBuf sink, boolean copy);
	
	/**
	 * Writes value to the output buffer with specific value type
	 * 
	 * Does optimization for writing, creates CompositeByteBuf in case of big payload
	 * 
	 * @param type - value type
	 * @param value - value object
	 * @param sink - output buffer
	 * @param copy - always copy value if true
	 * @return sink or CompositeByteBuf
	 */
	
	<V extends T, T> ByteBuf writeValue(Class<T> type, V value, ByteBuf sink, boolean copy);
	
}
