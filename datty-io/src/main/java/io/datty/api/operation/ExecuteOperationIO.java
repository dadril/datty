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
import io.datty.msgpack.MessageReader;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * ExecuteOperationIO
 * 
 * @author Alex Shvid
 *
 */

public class ExecuteOperationIO extends AbstractUpdateOperationIO<ExecuteOperation> {

	@Override
	public ExecuteOperation newOperation() {
		return new ExecuteOperation();
	}
	
	@Override
	public boolean readField(ExecuteOperation operation, DattyField field, MessageReader reader, ByteBuf source) {
		
		boolean read = super.readField(operation, field, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(field) {
		
			case PACKAGE_NAME:
				operation.setPackageName((String) reader.readValue(source, true));
				return true;

			case FUNCTION_NAME:
				operation.setFunctionName((String) reader.readValue(source, true));
				return true;

			case ARGUMENTS:
				operation.setArguments((ByteBuf) reader.readValue(source, false));
				return true;
			
			default:
				return false;
				
		}
		
	}
	
	@Override
	protected void writeFields(ExecuteOperation operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);
		
		if (operation.hasPackageName()) {
			fieldWriter.writeField(DattyField.PACKAGE_NAME, operation.getPackageName());
		}

		if (operation.hasFunctionName()) {
			fieldWriter.writeField(DattyField.FUNCTION_NAME, operation.getFunctionName());
		}
		
		if (operation.hasArguments()) {
			fieldWriter.writeField(DattyField.ARGUMENTS, operation.getArguments());
		}
		
	}
	
}
