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
package io.datty.msgpack.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.datty.msgpack.message.core.ArrayMessageReader;
import io.datty.msgpack.message.core.MapMessageReader;
import io.datty.msgpack.message.core.ValueMessageReader;
import io.datty.msgpack.message.core.type.TypeInfo;
import io.datty.msgpack.message.support.MessageIOException;
import io.netty.buffer.ByteBuf;

/**
 * MessageIO
 * 
 * @author Alex Shvid
 *
 */

public final class MessageIO {

	private MessageIO() {
	}
	
	/**
	 * Reads value from the source
	 * 
	 * It could be:
	 * StringMap
	 * IntMap
	 * Array
	 * 
	 * @param reader - known reader
	 * @param source - input buffer
	 * @param copy - always copy if true
	 * @return Map or List or null
	 */
	
	public static Object readFrom(MessageReader reader, ByteBuf source, boolean copy) {
		
		if (reader instanceof MapMessageReader) {
			return readMap((MapMessageReader) reader, source, copy);
		}
		else if (reader instanceof ArrayMessageReader) {
			return readArray((ArrayMessageReader) reader, source, copy);
		}
		else {
			throw new MessageIOException("unknown reader type: " + reader);
		}
	}
	
	/**
	 * Reads map from source
	 * 
	 * @param reader - string map reader
	 * @param source - input buffers
	 * @return Map or null
	 */
	
	public static Map<Object, Object> readMap(MessageReader reader, ByteBuf source, boolean copy) {
		
		int size = reader.size();
		
	  Map<Object, Object> map = new HashMap<>();
	  
	  for (int i = 0; i != size; ++i) {
	  	
	  	Object key = reader.readKey(source);
	  	Object value = reader.readValue(source, copy);
	  	
	  	if (value instanceof MessageReader) {
	  		value = readFrom((MessageReader) value, source, copy);
	  	}
	  	
	  	map.put(key, value);
	  	
	  }
	  
	  return map;
	}
	
	/**
	 * Reads array from source
	 * 
	 * @param reader - string int reader
	 * @param source - input buffers
	 * @return Map or null
	 */
	
	public static List<Object> readArray(MessageReader reader, ByteBuf source, boolean copy) {
		
		int size = reader.size();
		
	  List<Object> list = new ArrayList<>(size);
	  
	  for (int i = 0; i != size; ++i) {
	  	
	  	Object value = reader.readValue(source, copy);
	  	
	  	if (value instanceof MessageReader) {
	  		value = readFrom((MessageReader) value, source, copy);
	  	}
	  	
	  	list.add(value);
	  	
	  }
	  
	  return list;
	}
	
	/**
	 * Release complex object with ByteBuf values
	 * 
	 * @param value
	 */
	
	public static void release(Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof Map) {
			Map<Object, Object> map = (Map<Object, Object>) value;
			for (Object v : map.values()) {
				if (v != null && v instanceof ByteBuf) {
					ByteBuf bb = (ByteBuf) v;
					bb.release();
				}
			}
		}
		if (value instanceof List) {
			List<Object> list = (List<Object>) value;
			for (Object v : list) {
				if (v != null && v instanceof ByteBuf) {
					ByteBuf bb = (ByteBuf) v;
					bb.release();
				}
			}
		}
	}
	
	/**
	 * Reads value from the source
	 * 
	 * @param source - input buffer
	 * @param copy - always copy content if true
	 * @return value or null
	 */

	public static Object readValue(ByteBuf source, boolean copy) {
		
		Object value = ValueMessageReader.INSTANCE.readValue(source, copy);
		
		if (value instanceof MessageReader) {
  		value = readFrom((MessageReader) value, source, copy);
		}
		
		return value;
	}
	
	/**
	 * Skips value from the source
	 * 
	 * @param source - input buffer
	 * @param copy - always copy content if true
	 * @return ByteBuf of the value
	 */
	
	public static ByteBuf skipValue(ByteBuf source, boolean copy) {
		return ValueMessageReader.INSTANCE.skipValue(source, copy);
	}
	
	/**
	 * Read typed value from the source
	 * 
	 * @param type - expecting type
	 * @param source - input buffer
	 * @param copy - always copy content if true
	 * @return value or null
	 */
	
	public static <T> T readValue(TypeInfo<T> type, ByteBuf source, boolean copy) {
		return ValueMessageReader.INSTANCE.readValue(type, source, copy);
	}
	
}
