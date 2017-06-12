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
 * DattyOperationIO
 * 
 * Util class that reads or writes message with operation
 * 
 * @author Alex Shvid
 *
 */

public interface DattyOperationIO<O extends DattyOperation> {

	/**
	 * Creates a new operation
	 * 
	 * @return creates a new operation
	 */
	
	O newOperation();

	/**
	 * Sets field to operation
	 * 
	 * @param operation - operation instance
	 * @param fieldCode - field code
	 * @param reader - message reader
	 * @param source - input buffer
	 * @return true if field successfully read
	 */
	
	boolean readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source);
	
	/**
	 * Writes operation to the writer
	 *  
	 * @param operation - datty operation
	 * @param writer - message writer
	 * @param sink - output buffer
	 * @return new or old sink
	 */
	
	ByteBuf write(O operation, MessageWriter writer, ByteBuf sink);
	
}
