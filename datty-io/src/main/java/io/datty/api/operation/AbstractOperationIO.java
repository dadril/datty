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
import io.datty.util.FieldWriter;
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
	public boolean readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {

		switch(fieldCode) {
		
		case DattyCode.FIELD_SET_NAME:
			operation.setSetName((String) reader.readValue(source, true));
			return true;
			
		case DattyCode.FIELD_SUPER_KEY:
			operation.setSuperKey((String) reader.readValue(source, true));
			return true;
			
		case DattyCode.FIELD_MAJOR_KEY:
			operation.setMajorKey((String) reader.readValue(source, true));
			return true;	
		
		case DattyCode.FIELD_TIMEOUT_MLS:
			operation.setTimeoutMillis(((Long) reader.readValue(source, true)).intValue());
			return true;
			
		}
		
		return false;
	}

	@Override
	public ByteBuf write(O operation, MessageWriter writer, ByteBuf sink) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink);
		
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
		
		fieldWriter.writeField(DattyCode.FIELD_OPCODE, operation.getCode());
		
		String setName = operation.getSetName();
		if (setName != null) {
			fieldWriter.writeField(DattyCode.FIELD_SET_NAME, setName);
		}
		
		String superKey = operation.getSuperKey();
		if (superKey != null) {
			fieldWriter.writeField(DattyCode.FIELD_SUPER_KEY, superKey);
		}
		
		String majorKey = operation.getMajorKey();
		if (majorKey != null) {
			fieldWriter.writeField(DattyCode.FIELD_MAJOR_KEY, majorKey);
		}		
		
		if (operation.hasTimeoutMillis()) {
			fieldWriter.writeField(DattyCode.FIELD_TIMEOUT_MLS, operation.getTimeoutMillis());
		}		
		
	}

}
