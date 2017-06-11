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

import io.datty.api.DattyCode;
import io.datty.api.DattyOperationIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.AbstractMessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * AbstractSetOperationIO
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractSetOperationIO<O extends AbstractSetOperation<O>> implements DattyOperationIO<O> {

	@Override
	public void readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {
		
		Object value = reader.readValue(source, true);
		
		switch(fieldCode) {
		
		case DattyCode.FIELD_SET_NAME:
			operation.setSetName((String) value);
			break;
			
		case DattyCode.FIELD_SUPER_KEY:
			operation.setSuperKey((String) value);
			break;			
		
		case DattyCode.FIELD_TIMEOUT_MLS:
			operation.setTimeoutMillis(((Long) value).intValue());
			break;
			
		}
		
	}

	@Override
	public ByteBuf write(O operation, MessageWriter writer, ByteBuf sink) {
		
		int headerIndex = writer.skipHeader(AbstractMessageWriter.MAX_BYTE_HEADER_SIZE, sink);
		
		int size = 0;
		
		writer.writeValue(DattyCode.FIELD_OPCODE, sink); 
		writer.writeValue(operation.getCode().getCode(), sink);
		size++;
		
		String setName = operation.getSetName();
		if (setName != null) {
			writer.writeValue(DattyCode.FIELD_SET_NAME, sink);
			writer.writeValue(setName, sink);
			size++;
		}
		
		String superKey = operation.getSuperKey();
		if (superKey != null) {
			writer.writeValue(DattyCode.FIELD_SUPER_KEY, sink);
			writer.writeValue(superKey, sink);
			size++;
		}
		
		if (operation.hasTimeoutMillis()) {
			writer.writeValue(DattyCode.FIELD_TIMEOUT_MLS, sink);
			writer.writeValue(operation.getTimeoutMillis(), sink);
			size++;
		}		
		
		writer.writeHeader(size, AbstractMessageWriter.MAX_BYTE_HEADER_SIZE, headerIndex, sink); 
		
		return sink;
	}
	
}
