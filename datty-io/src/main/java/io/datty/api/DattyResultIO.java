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

import io.datty.msgpack.message.MessageReader;
import io.datty.msgpack.message.MessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * DattyResultIO
 * 
 * Util class that reads or writes message with result
 * 
 * @author Alex Shvid
 *
 */

public interface DattyResultIO<R extends DattyResult> {

	/**
	 * Creates a new result
	 * 
	 * @return new result instance
	 */
	
	R newResult();
	
	/**
	 * Sets field to result
	 * 
	 * @param result - result instance
	 * @param field - field code
	 * @param reader - message reader
	 * @param source - input buffer
	 * @return true if field successfully read
	 */
	
	boolean readField(R result, DattyField field, MessageReader reader, ByteBuf source);
	
	/**
	 * Writes result to the writer
	 *  
	 * @param operation - datty operation
	 * @param writer - message writer
	 * @param sink - output buffer
	 * @param numeric - use numeric keys
	 * @return new or old sink 
	 */
	
	ByteBuf write(R result, MessageWriter writer, ByteBuf sink, boolean numeric);
	
}
