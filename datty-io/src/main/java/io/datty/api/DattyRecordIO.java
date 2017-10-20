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
package io.datty.api;

import java.util.Map;

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.MapMessageReader;
import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.ValueMessageReader;
import io.datty.support.NullDattyValue;
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * DattyRecordIO
 * 
 * @author Alex Shvid
 *
 */

public final class DattyRecordIO {

	private final static MessageReader reader = MapMessageReader.INSTANCE;
	private final static MessageWriter writer = MapMessageWriter.INSTANCE;
	
	private DattyRecordIO() {
	}
	
	public static DattyRecord readRecord(ByteBuf source) {
		
		DattyRecord rec = new DattyRecord();
		
		Object recMap = reader.readValue(source, false);
		
		if (recMap == null) {
			return rec;
		}
		
		if (!(recMap instanceof MapMessageReader)) {
			throw new DattyException("expected MapMessageReader for DattyRecord object");
		}

		MapMessageReader mapReader = (MapMessageReader) recMap;
		
		int size = mapReader.size();
		
		for (int i = 0; i != size; ++i) {
			
			Object minorKey = mapReader.readKey(source);
			if (minorKey == null) {
				throw new DattyException("expected not null key in DattyRow object");
			}
			
			if (!(minorKey instanceof String)) {
				throw new DattyException("expected string instance for minor key DattyRow object");
			}
			
			ByteBuf value = mapReader.skipValue(source, false);

			DattyValue dattyValue; 
			
			if (value == null || ValueMessageReader.INSTANCE.isNull(value)) {
				dattyValue = NullDattyValue.NULL;
			}
			else if (ValueMessageReader.INSTANCE.isBinary(value)) {
				dattyValue = new ByteBufValue(ValueMessageReader.INSTANCE.readBinary(value, false));
			}
			else {
				dattyValue = new ByteBufValue(value);
			}
			
			rec.put((String) minorKey, dattyValue);
		
		}
		
		return rec;
	}
	
	public static ByteBuf writeRecord(DattyRecord rec, ByteBuf sink) {
		
		Map<String, DattyValue> values = rec.getValues();
		
		writer.writeHeader(values.size(), sink);
		
		for (Map.Entry<String, DattyValue> entry : values.entrySet()) {
			
			DattyValue value = entry.getValue();

			writer.writeKey(entry.getKey(), sink);
			
			if (value.isNull()) {
				writer.writeNull(sink);
			}
			if (value.hasByteBuf()) {
				writer.writeValue(value.asByteBuf(), sink, false);
			}
			else {
				sink = value.write(sink);
			}
			
		}

		return sink;
		
	}
	
}
