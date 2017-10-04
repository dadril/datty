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
package io.datty.msgpack.message.core.reader;

import java.lang.reflect.Array;

import org.msgpack.core.MessageFormat;

import io.datty.msgpack.message.MessageReader;
import io.datty.msgpack.message.core.AbstractMessageReader;
import io.datty.msgpack.message.core.MapMessageReader;
import io.datty.msgpack.message.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ArrayReader
 * 
 * @author Alex Shvid
 *
 */

public class ArrayReader extends AbstractMessageReader {

	public static final ArrayReader INSTANCE = new ArrayReader();
	
	public Object read(Class<?> elementType, ValueReader<?> elementReader, ByteBuf buffer, boolean copy) {
		
		if (!hasNext(buffer)) {
			return null;
		}
		
		MessageFormat f = getNextFormat(buffer);
		
		switch(f) {
		
    case NIL:
      return readNull(buffer);
      
    case FIXARRAY:
    case ARRAY16:
    case ARRAY32:
    	return readArray(elementType, elementReader, buffer, copy);      	
    	
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return readMap(elementType, elementReader, buffer, copy);
    	
    default:
      throw new MessageParseException("expected collection but was another type: " + f.name());	
		
		}
		
	}

	private Object readArray(Class<?> elementType, ValueReader<?> elementReader, ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);

		Object array = Array.newInstance(elementType, length);

		for (int i = 0; i != length; ++i) {
			Object value = elementReader.read(source, copy);
			Array.set(array, i, value);
		}
		
		return array;
	}
	
	private Object readMap(Class<?> elementType, ValueReader<?> elementReader, ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);
		MessageReader reader = new MapMessageReader(length);

		Object array = Array.newInstance(elementType, length);

		for (int i = 0; i != length; ++i) {
			reader.readKey(source);
			Object value = elementReader.read(source, copy);
			Array.set(array, i, value);
		}
		
		return array;
	}
	
}
