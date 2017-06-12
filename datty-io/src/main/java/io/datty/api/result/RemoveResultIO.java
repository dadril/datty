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
import io.datty.util.DattyCollectionIO;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * RemoveResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum RemoveResultIO implements DattyResultIO<RemoveResult> {

	INSTANCE;

	@Override
	public RemoveResult newResult() {
		return new RemoveResult();
	}

	@Override
	public boolean readField(RemoveResult result, DattyField field, MessageReader<Integer> reader, ByteBuf source) {
		
		switch(field) {
		
		case MINOR_KEYS:
			result.addMinorKeys(DattyCollectionIO.readStringArray(reader, source));
			return true;
			
		default:
			return false;			
			
		}

	}

	@Override
	public ByteBuf write(RemoveResult result, MessageWriter writer, ByteBuf sink) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink);
		
		fieldWriter.writeField(DattyField.RESCODE, result.getCode());
		
		if (!result.isEmpty()) {
			fieldWriter.writeField(DattyField.MINOR_KEYS, result.minorKeys());
		}
		
		return fieldWriter.writeEnd();
		
	}
	
}
