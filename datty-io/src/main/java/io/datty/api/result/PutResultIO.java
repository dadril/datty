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
package io.datty.api.result;

import io.datty.api.DattyField;
import io.datty.api.DattyResultIO;
import io.datty.msgpack.message.MessageReader;
import io.datty.msgpack.message.MessageWriter;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * PutResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum PutResultIO implements DattyResultIO<PutResult> {

	INSTANCE;

	@Override
	public PutResult newResult() {
		return new PutResult();
	}

	@Override
	public boolean readField(PutResult result, DattyField field, MessageReader reader, ByteBuf source) {
		
		switch(field) {
		
		case LONG_VALUE:
			result.setWrittenBytes((Long) reader.readValue(source, true));
			return true;
			
		default:
			return false;			
			
		}

	}

	@Override
	public ByteBuf write(PutResult result, MessageWriter writer, ByteBuf sink, boolean numeric) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink, numeric);
		
		fieldWriter.writeField(DattyField.RESCODE, result.getCode());
		
		if (result.hasWrittenBytes()) {
			fieldWriter.writeField(DattyField.LONG_VALUE, result.getWrittenBytes());
		}
		
		return fieldWriter.writeEnd();
		
	}
	
}
