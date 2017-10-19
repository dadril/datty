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
import io.datty.api.DattyRecordIO;
import io.datty.api.version.VersionIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.util.FieldWriter;
import io.netty.buffer.ByteBuf;

/**
 * RecordResultIO
 * 
 * @author Alex Shvid
 *
 */

public enum RecordResultIO implements DattyResultIO<RecordResult> {

	INSTANCE;

	@Override
	public RecordResult newResult() {
		return new RecordResult();
	}

	@Override
	public boolean readField(RecordResult result, DattyField field, MessageReader reader, ByteBuf source) {
		
		switch(field) {
		
		case MAJOR_KEY:
			result.setMajorKey((String) reader.readValue(source, true));
			return true;
		
		case VERSION:
			result.setVersion(VersionIO.readVersion(source));
			return true;
		
		case RECORD:
			result.setRecord(DattyRecordIO.readRecord(source));
			return true;
			
		case COUNT:
			result.setCount((Long) reader.readValue(source, true));
			return true;
			
		default:
			return false;			
			
		}
		
	}

	@Override
	public ByteBuf write(RecordResult result, MessageWriter writer, ByteBuf sink, boolean numeric) {
		
		FieldWriter fieldWriter = new FieldWriter(writer, sink, numeric);
		
		fieldWriter.writeField(DattyField.RESCODE, result.getCode());
		
		if (result.hasMajorKey()) {
			fieldWriter.writeField(DattyField.MAJOR_KEY, result.getMajorKey());
		}
		
		if (result.hasVersion()) {
			fieldWriter.writeField(DattyField.VERSION, result.getVersion());
		}
		
		if (result.hasRecord()) {
			fieldWriter.writeField(DattyField.RECORD, result.getRecord());
		}
		
		if (result.hasCount()) {
			fieldWriter.writeField(DattyField.COUNT, result.getCount());
		}
		
		return fieldWriter.writeEnd();
		
	}
	
}
