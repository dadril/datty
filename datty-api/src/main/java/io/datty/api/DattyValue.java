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
package io.datty.api;

import io.netty.buffer.ByteBuf;

/**
 * DattyValue
 * 
 * @author Alex Shvid
 *
 */

public interface DattyValue {

	/**
	 * Null value
	 */
	
	public static final DattyValue NULL = NullDattyValue.NULL;
	
	/**
	 * Checks if value is null
	 * 
	 * @return true if value is null
	 */
	
	boolean isNull();
	
	/**
	 * Checks if it has ByteBuf
	 * 
	 * @return 
	 */
	
	boolean hasByteBuf();
	
	/**
	 * Gets ByteBuf if has
	 * 
	 * @return byte buf or null
	 */
	
	ByteBuf asByteBuf();
	
	/**
	 * Retain value
	 * 
	 * @return this
	 */
	
	DattyValue retain();
	
	/**
	 * Releases ByteBuf if it has
	 */
	
	void release();
	
	/**
	 * Writes value to the sink
	 * 
	 * @param sink - output buffer
	 * @return old or new sink
	 */
	
	ByteBuf write(ByteBuf sink);
	
	/**
	 * Writes value to byte array
	 * 
	 * @return byte array or null
	 */
	
	byte[] toByteArray();
	
}
