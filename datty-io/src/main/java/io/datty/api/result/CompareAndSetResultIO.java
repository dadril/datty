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
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * CompareAndSetResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum CompareAndSetResultIO implements DattyResultIO<CompareAndSetResult> {

	INSTANCE;

	@Override
	public CompareAndSetResult newResult() {
		return new CompareAndSetResult();
	}

	@Override
	public boolean readField(CompareAndSetResult result, DattyField field, MessageReader reader, ByteBuf source) {
		
		switch(field) {
		
		case BOOL_VALUE:
			result.set((Boolean) reader.readValue(source, true));
			return true;
			
		default:
			return false;
			
		}

	}

	@Override
	public ByteBuf write(CompareAndSetResult result, MessageWriter writer, ByteBuf sink, boolean numeric) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink, numeric);
		
		fieldWriter.writeField(DattyField.RESCODE, result.getCode());
		
		fieldWriter.writeField(DattyField.BOOL_VALUE, result.get());
		
		return fieldWriter.writeEnd();
		
	}
	
}
