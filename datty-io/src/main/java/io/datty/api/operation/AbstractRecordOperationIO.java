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

import java.util.Set;

import io.datty.api.DattyCode;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.ArrayMessageReader;
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * AbstractRecordOperaitonIO
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
abstract class AbstractRecordOperationIO<O extends AbstractRecordOperation> extends AbstractOperationIO<O> {

	@Override
	public boolean readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {
		
		boolean read = super.readField(operation, fieldCode, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(fieldCode) {
		
			case DattyCode.FIELD_ALL_MINOR_KEYS:
				operation.allMinorKeys(true);
				return true;
		
			case DattyCode.FIELD_MINOR_KEYS:
				readMinorKeys(operation, reader, source);
				return true;
				
		}
		
		return false;
		
	}

	private void readMinorKeys(O operation, MessageReader<Integer> reader, ByteBuf source) {
		
		Object value = reader.readValue(source, false);
		
		if (value == null) {
			return;
		}
		
		if (!(value instanceof ArrayMessageReader)) {
			throw new DattyException("expected array of strings in message, but was: " + value);
		}
		
		ArrayMessageReader arrayReader = (ArrayMessageReader) value;
		
		int size = arrayReader.size();
		
		for (int i = 0; i != size; ++i) {
			
			Object item = arrayReader.readValue(source, true);
			
			if (item == null) {
				continue;
			}
			
			if (!(item instanceof String)) {
				throw new DattyException("expected string item, but was:" + item);
			}
			
			operation.addMinorKey((String) item);
			
		}
		
	}
	
	@Override
	protected void writeFields(O operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);
		
		if (operation.isAllMinorKeys()) {
			fieldWriter.writeField(DattyCode.FIELD_ALL_MINOR_KEYS, true);
		}
		else {
			@SuppressWarnings("unchecked")
			Set<String> minorKeys = operation.getMinorKeys();
			fieldWriter.writeField(DattyCode.FIELD_MINOR_KEYS, minorKeys);
		}
		
	}

}
