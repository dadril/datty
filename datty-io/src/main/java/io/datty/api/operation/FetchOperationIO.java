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
 * FetchOperationIO
 * 
 * @author Alex Shvid
 *
 */

public class FetchOperationIO extends AbstractRecordOperationIO<Fetch> {

	@Override
	public Fetch newOperation() {
		return new Fetch();
	}
	
	@Override
	public boolean readField(Fetch operation, DattyField field, MessageReader reader, ByteBuf source) {
		
		boolean read = super.readField(operation, field, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(field) {
		
			case WITH_VALUES:
				operation.withValues(((Boolean) reader.readValue(source, true)));
				return true;

			default:
				return false;
		}
		
	}
	
	@Override
	protected void writeFields(Fetch operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);

		fieldWriter.writeField(DattyField.WITH_VALUES, operation.isFetchValues());
		
	}


}
