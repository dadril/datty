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

import io.datty.api.DattyCode;
import io.datty.api.DattyResultIO;
import io.datty.api.DattyRowIO;
import io.datty.api.version.VersionIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * GetResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum GetResultIO implements DattyResultIO<GetResult> {

	INSTANCE;

	@Override
	public GetResult newResult() {
		return new GetResult();
	}

	@Override
	public boolean readField(GetResult result, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {
		
		switch(fieldCode) {
		
		case DattyCode.FIELD_VERSION:
			result.setVersion(VersionIO.readVersion(reader, source));
			return true;
		
		case DattyCode.FIELD_ROW:
			result.setRow(DattyRowIO.readRow(reader, source));
			return true;
			
		}
		
		return false;
	}

	@Override
	public ByteBuf write(GetResult result, MessageWriter writer, ByteBuf sink) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink);
		
		fieldWriter.writeField(DattyCode.FIELD_RESCODE, result.getCode());
		
		if (result.hasVersion()) {
			fieldWriter.writeField(DattyCode.FIELD_VERSION, result.getVersion());
		}
		
		if (result.hasRow()) {
			fieldWriter.writeField(DattyCode.FIELD_ROW, result.getRow());
		}
		
		return fieldWriter.writeEnd();
		
	}
	
}