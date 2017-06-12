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
import io.datty.api.DattyRowIO;
import io.datty.api.version.VersionIO;
import io.datty.msgpack.MessageReader;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * CompareAndSetOperationIO
 * 
 * @author Alex Shvid
 *
 */

public class CompareAndSetOperationIO extends AbstractUpdateOperationIO<CompareAndSetOperation> {

	@Override
	public CompareAndSetOperation newOperation() {
		return new CompareAndSetOperation();
	}
	
	@Override
	public boolean readField(CompareAndSetOperation operation, DattyField field, MessageReader<Integer> reader, ByteBuf source) {
		
		boolean read = super.readField(operation, field, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(field) {

			case VERSION:
				operation.setVersion(VersionIO.readVersion(reader, source));
				return true;
			
			case ROW:
				operation.setRow(DattyRowIO.readRow(reader, source));
				return true;

			default:
				return false;
		}
		
	}
	
	@Override
	protected void writeFields(CompareAndSetOperation operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);
		
		if (operation.hasVersion()) {
			fieldWriter.writeField(DattyField.VERSION, operation.getVersion());
		}
		
		if (operation.hasRow()) {
			fieldWriter.writeField(DattyField.ROW, operation.getRow());
		}
		
	}
	
}
