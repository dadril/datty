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
package io.datty.msgpack.core.writer;

import java.util.Map;

import io.datty.msgpack.core.AbstractMessageWriter;
import io.datty.msgpack.core.MapMessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * MapWriter
 * 
 * @author Alex Shvid
 *
 */

public class MapWriter extends AbstractMessageWriter {

	public static final MapWriter INSTANCE = new MapWriter();
	
	public ByteBuf write(ValueWriter<?> keyWriter, ValueWriter<?> componentWriter, Object value, ByteBuf sink, boolean copy, boolean numeric) {
		
		if (value == null) {
			writeNull(sink);
			return null;
		}
		
		Map<Object, Object> map = (Map<Object, Object>) value;
		
		int size = map.size();
		
		MapMessageWriter writer = MapMessageWriter.INSTANCE;
		
		writer.writeHeader(size, sink);
		
		for (Map.Entry<Object, Object> e : map.entrySet()) {
			
			Object key = e.getKey();
			
			if (key != null) {
				sink = ((ValueWriter<Object>) keyWriter).write(key, sink, true, numeric);
			}
			else {
				writeNull(sink);
			}
			
			Object element = e.getValue();
			
			if (element != null) {
				sink = ((ValueWriter<Object>) componentWriter).write(element, sink, copy, numeric);
			}
			else {
				writeNull(sink);
			}

		}
		
		return sink;
	}
	
	
}
