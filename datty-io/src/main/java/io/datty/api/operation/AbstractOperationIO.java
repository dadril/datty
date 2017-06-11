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
package io.datty.api.operation;

import io.datty.api.DattyOperationIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * AbstractOperationIO
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
abstract class AbstractOperationIO<O extends AbstractOperation> implements DattyOperationIO<O> {

	@Override
	public void readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {

		
	}

	@Override
	public ByteBuf write(O operation, MessageWriter writer, ByteBuf sink) {
		
		
		
		return null;
	}

}
