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
package io.datty.msgpack.core.reader;

import java.util.ArrayList;
import java.util.List;

import org.msgpack.core.MessageFormat;

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.AbstractMessageReader;
import io.datty.msgpack.core.ArrayMessageReader;
import io.datty.msgpack.core.MapMessageReader;
import io.datty.msgpack.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ListReader
 * 
 * @author Alex Shvid
 *
 */

public class ListReader extends AbstractMessageReader implements ValueReader<List<Object>> {

	public static final ListReader INSTANCE = new ListReader();
	
	@Override
	public List<Object> read(ByteBuf buffer, boolean copy) {
		
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
    	return readArray(buffer, copy);      	
    	
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return readMap(buffer, copy);
    	
    default:
      throw new MessageParseException("expected collection but was another type: " + f.name());	
		
		}
		
	}

	public <T> List<T> read(ValueReader<T> elementReader, ByteBuf buffer, boolean copy) {

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
    	return readArray(elementReader, buffer, copy);      	
    	
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return readMap(elementReader, buffer, copy);
    	
    default:
      throw new MessageParseException("expected collection but was another type: " + f.name());	
		
		}

	}

	private List<Object> readArray(ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);
		MessageReader reader = new ArrayMessageReader(length);

		List<Object> list = new ArrayList<Object>(length);

		for (int i = 0; i != length; ++i) {
			Object value = reader.readValue(source, copy);
			list.add(value);
		}
		
		return list;
	}
	
	private <T> List<T> readArray(ValueReader<T> elementReader, ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);

		List<T> list = new ArrayList<T>(length);

		for (int i = 0; i != length; ++i) {
			T value = elementReader.read(source, copy);
			list.add(value);
		}
		
		return list;
	}
	
	private List<Object> readMap(ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);
		MessageReader reader = new MapMessageReader(length);

		List<Object> list = new ArrayList<Object>(length);

		for (int i = 0; i != length; ++i) {
			reader.readKey(source);
			Object value = reader.readValue(source, copy);
			list.add(value);
		}
		
		return list;
	}
	
	private <T> List<T> readMap(ValueReader<T> elementReader, ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);
		MessageReader reader = new MapMessageReader(length);

		List<T> list = new ArrayList<T>(length);

		for (int i = 0; i != length; ++i) {
			reader.readKey(source);
			T value = elementReader.read(source, copy);
			list.add(value);
		}
		
		return list;
	}
	
}
