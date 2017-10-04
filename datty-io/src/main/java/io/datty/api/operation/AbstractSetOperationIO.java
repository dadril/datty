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

import io.datty.api.DattyField;
import io.datty.api.DattyOperationIO;
import io.datty.msgpack.message.MessageReader;
import io.datty.msgpack.message.MessageWriter;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * AbstractSetOperationIO
 * 
 * @author Alex Shvid
 *
 */

abstract class AbstractSetOperationIO<O extends AbstractSetOperation<O>> implements DattyOperationIO<O> {

	@Override
	public boolean readField(O operation, DattyField field, MessageReader reader, ByteBuf source) {
		
		switch(field) {
		
		case SET_NAME:
			operation.setSetName((String) reader.readValue(source, true));
			return true;
			
		case SUPER_KEY:
			operation.setSuperKey((String) reader.readValue(source, true));
			return true;		
		
		case TIMEOUT_MLS:
			operation.setTimeoutMillis(((Long) reader.readValue(source, true)).intValue());
			return true;
			
		default:
			return false;
			
		}
		
	}

	@Override
	public ByteBuf write(O operation, MessageWriter writer, ByteBuf sink, boolean numeric) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink, numeric);
		
		writeFields(operation, fieldWriter);
		
		return fieldWriter.writeEnd();
		
	}
	
	/**
	 * This method must be called first, because opcode must be first
	 * 
	 * @param operation - operation
	 * @param fieldWriter - field writer
	 */
	
	protected void writeFields(O operation, FieldWriter fieldWriter) {
		
		fieldWriter.writeField(DattyField.OPCODE, operation.getCode());
		
		String setName = operation.getSetName();
		if (setName != null) {
			fieldWriter.writeField(DattyField.SET_NAME, setName);
		}
		
		String superKey = operation.getSuperKey();
		if (superKey != null) {
			fieldWriter.writeField(DattyField.SUPER_KEY, superKey);
		}
		
		if (operation.hasTimeoutMillis()) {
			fieldWriter.writeField(DattyField.TIMEOUT_MLS, operation.getTimeoutMillis());
		}		
		
	}
	
}
