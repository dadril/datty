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
package io.datty.api.version;

import io.datty.api.DattyField;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.MapMessageReader;
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * VersionIO
 * 
 * @author Alex Shvid
 *
 */

public final class VersionIO {

	private VersionIO() {
	}
	
	
	public static Version readVersion(MessageReader<Integer> reader, ByteBuf source) {
		
		Object rowMap = reader.readValue(source, false);
		
		if (rowMap == null) {
			return null;
		}
		
		if (!(rowMap instanceof MapMessageReader)) {
			throw new DattyException("expected MapMessageReader for DattyRow object");
		}
		
		MapMessageReader mapReader = (MapMessageReader) rowMap;
		
		int size = mapReader.size();

		VersionType versionType = null;
		Long longValue = null;
		String stringValue = null;
		
		for (int i = 0; i != size; ++i) {
			
			Object fieldKey = mapReader.readKey(source);
			if (fieldKey == null) {
				throw new DattyException("expected not null fieldNum in  object");
			}
			
			DattyField field = DattyField.findByKey(fieldKey);
			if (field == null) {
				throw new DattyException("field not found for fieldNum: " + fieldKey);
			}

			switch(field) {
			
			case VERSION_TYPE:
				int code = ((Long) mapReader.readValue(source, true)).intValue();
				versionType = VersionType.findByCode(code);
				if (versionType == null) {
					throw new DattyException("versionType not found for code: " + code);
				}
				break;
				
			case VERSION_LONG:
				longValue = (Long) mapReader.readValue(source, true);
				break;
				
			case VERSION_STRING:
				stringValue = (String) mapReader.readValue(source, true);
				break;
				
			default:
				mapReader.skipValue(source, false);
				break;
			}
			
		}
		
		switch(versionType) {
		
		case LONG:
			return new LongVersion(longValue != null ? longValue.longValue() : 0L);
		
		case STRING:	
			return new StringVersion(stringValue);
			
		default:
			throw new DattyException("unknown version type: " + versionType);
			
		}
		
	}
	
	public static void writeVersion(MessageWriter writer, Version version, ByteBuf sink) {
		
		VersionType type = version.getType();
		
		switch(type) {
		
		case LONG:
			writeLongVersion(writer, version, sink);
			break;
			
		case STRING:
			writeStringVersion(writer, version, sink);
			break;
		
		default:
			throw new DattyException("unknown version type: " + type);
			
		}
		
	}
	
	public static void writeLongVersion(MessageWriter writer, Version version, ByteBuf sink) {
		
		writer.writeHeader(2, sink);
		
		writer.writeKey(DattyField.VERSION_TYPE.getFieldCode(), sink);
		writer.writeValue(version.getType().getCode(), sink);
		
		writer.writeKey(DattyField.VERSION_LONG.getFieldCode(), sink);
		writer.writeValue(version.asLong(), sink);
		
	}
	
	public static void writeStringVersion(MessageWriter writer, Version version, ByteBuf sink) {
		
		String stringVersion = version.asString();
		
		writer.writeHeader(stringVersion != null ? 2 : 1, sink);
		
		writer.writeKey(DattyField.VERSION_TYPE.getFieldCode(), sink);
		writer.writeValue(version.getType().getCode(), sink);
		
		if (stringVersion != null) {
			writer.writeKey(DattyField.VERSION_STRING.getFieldCode(), sink);
			writer.writeValue(stringVersion, sink);
		}
		
	}
	
}
