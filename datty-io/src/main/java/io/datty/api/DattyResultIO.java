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

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
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
	 * Reads result from the reader
	 * 
	 * @param reader - message reader
	 * @param source - input buffer 
	 * @return not null instance
	 */
	
	R read(MessageReader<Integer> reader, ByteBuf source);
	
	/**
	 * Writes result to the writer
	 *  
	 * @param operation - datty operation
	 * @param writer - message writer
	 * @param sink - output buffer
	 * @return new or old sink 
	 */
	
	void write(R result, MessageWriter writer, ByteBuf sink);
	
}
