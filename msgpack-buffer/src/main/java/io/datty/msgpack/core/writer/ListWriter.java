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

import java.util.List;

import io.datty.msgpack.core.AbstractMessageWriter;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * ListWriter
 * 
 * @author Alex Shvid
 *
 */

public class ListWriter extends AbstractMessageWriter {

	public static final ListWriter INSTANCE = new ListWriter();
	
	public ByteBuf write(Class<?> listType, Object value, ByteBuf sink, boolean copy) {
		
		if (value == null) {
			writeNull(sink);
			return null;
		}
		
		List<Object> list = (List<Object>) value;
		
		int size = list.size();
		
		ArrayMessageWriter writer = ArrayMessageWriter.INSTANCE;
		
		writer.writeHeader(size, sink);
		
		for (int i = 0; i != size; ++i) {
			
			Object element = list.get(i);
			
			if (element != null) {
				Class<Object> elementType = (Class<Object>) element.getClass();
				sink = writer.writeValue((Class<Object>) elementType, element, sink, copy);
			}
			else {
				writeNull(sink);
			}

		}
		
		return sink;
	}
	
	
}
