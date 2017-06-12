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
 * ExecuteResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum ExecuteResultIO implements DattyResultIO<ExecuteResult> {

	INSTANCE;

	@Override
	public ExecuteResult newResult() {
		return new ExecuteResult();
	}

	@Override
	public boolean readField(ExecuteResult result, DattyField field, MessageReader<Integer> reader, ByteBuf source) {
		
		switch(field) {
		
		case BYTES_VALUE:
			result.set((ByteBuf) reader.readValue(source, true));
			return true;
			
		default:
			return false;			
			
		}
		
	}

	@Override
	public ByteBuf write(ExecuteResult result, MessageWriter writer, ByteBuf sink) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink);
		
		fieldWriter.writeField(DattyField.RESCODE, result.getCode());
		
		fieldWriter.writeField(DattyField.BYTES_VALUE, result.get());
		
		return fieldWriter.writeEnd();
		
	}
	
}
