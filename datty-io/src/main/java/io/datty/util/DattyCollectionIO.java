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
package io.datty.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.ArrayMessageReader;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * DattyCollectionIO
 * 
 * @author Alex Shvid
 *
 */

public final class DattyCollectionIO {

	private DattyCollectionIO() {
	}
	
	public static List<String> readStringArray(MessageReader reader, ByteBuf source) {
		
		Object value = reader.readValue(source, false);
		
		if (value == null) {
			return Collections.emptyList();
		}
		
		if (!(value instanceof ArrayMessageReader)) {
			throw new DattyException("expected array of strings in message, but was: " + value);
		}
		
		ArrayMessageReader arrayReader = (ArrayMessageReader) value;
		
		int size = arrayReader.size();
		
		List<String> list = new ArrayList<String>(size);
		
		for (int i = 0; i != size; ++i) {
			
			Object item = arrayReader.readValue(source, true);
			
			if (item == null) {
				continue;
			}
			
			if (!(item instanceof String)) {
				throw new DattyException("expected string item, but was:" + item);
			}
			
			list.add((String) item);
			
		}
		
		return list;
	}
	
	public static void writeStringArray(Collection<String> values, ByteBuf sink) {
		
		ArrayMessageWriter arrayWriter = ArrayMessageWriter.INSTANCE;
		
		arrayWriter.writeHeader(values.size(), sink); 
		for (String str : values) {
			arrayWriter.writeValue(str, sink);
		}
		
	}

}
