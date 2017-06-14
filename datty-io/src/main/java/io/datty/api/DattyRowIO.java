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
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * DattyRowIO
 * 
 * @author Alex Shvid
 *
 */

public final class DattyRowIO {

	private final static MessageReader reader = MapMessageReader.INSTANCE;
	private final static MessageWriter writer = MapMessageWriter.INSTANCE;
	
	private DattyRowIO() {
	}
	
	public static DattyRow readRow(ByteBuf source) {
		
		DattyRow row = new DattyRow();
		
		Object rowMap = reader.readValue(source, false);
		
		if (rowMap == null) {
			return row;
		}
		
		if (!(rowMap instanceof MapMessageReader)) {
			throw new DattyException("expected MapMessageReader for DattyRow object");
		}

		MapMessageReader mapReader = (MapMessageReader) rowMap;
		
		int size = mapReader.size();
		
		for (int i = 0; i != size; ++i) {
			
			Object minorKey = mapReader.readKey(source);
			if (minorKey == null) {
				throw new DattyException("expected not null key in DattyRow object");
			}
			
			if (!(minorKey instanceof String)) {
				throw new DattyException("expected string instance for minor key DattyRow object");
			}
			
			Object value = mapReader.readValue(source, false);
			if (value != null) {
				
				if (!(value instanceof ByteBuf)) {
					throw new DattyException("expected ByteBuf value in DattyRow object: " + value);
				}
				
				row.putValue((String) minorKey, (ByteBuf) value, true);
				
			}
			
		}
		
		return row;
	}
	
	public static ByteBuf writeRow(DattyRow row, ByteBuf sink) {
		
		Map<String, ByteBuf> values = row.getValues();
		
		int headerIndex = writer.skipHeader(values.size(), sink);
		
		int size = 0;
		for (Map.Entry<String, ByteBuf> entry : values.entrySet()) {
			
			ByteBuf value = entry.getValue();
			if (value != null && value.readableBytes() > 0) {

				writer.writeKey(entry.getKey(), sink);
				sink = writer.writeValue(value, sink, false);
				size++;
				
			}
			
		}

		writer.writeHeader(size, values.size(), headerIndex, sink);
		
		return sink;
		
	}
	
}
