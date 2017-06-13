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

import io.datty.api.DattyField;
import io.datty.msgpack.MessageReader;
import io.datty.util.DattyCollectionIO;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * AbstractRecordOperaitonIO
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
abstract class AbstractRecordOperationIO<O extends AbstractRecordOperation> extends AbstractOperationIO<O> {

	@SuppressWarnings("unchecked")
	@Override
	public boolean readField(O operation, DattyField field, MessageReader reader, ByteBuf source) {
		
		boolean read = super.readField(operation, field, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(field) {
		
			case ALL_MINOR_KEYS:
				operation.allMinorKeys(true);
				return true;
		
			case MINOR_KEYS:
				operation.addMinorKeys(DattyCollectionIO.readStringArray(reader, source));
				return true;
				
			default:
				return false;
		}
		
	}

	@Override
	protected void writeFields(O operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);
		
		if (operation.isAllMinorKeys()) {
			fieldWriter.writeField(DattyField.ALL_MINOR_KEYS, true);
		}
		else {
			@SuppressWarnings("unchecked")
			Set<String> minorKeys = operation.getMinorKeys();
			fieldWriter.writeField(DattyField.MINOR_KEYS, minorKeys);
		}
		
	}

}
