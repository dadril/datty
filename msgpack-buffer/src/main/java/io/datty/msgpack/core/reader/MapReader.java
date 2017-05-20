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

import java.util.HashMap;
import java.util.Map;

import org.msgpack.core.MessageFormat;

import io.datty.msgpack.MessageConstants;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.AbstractMessageReader;
import io.datty.msgpack.core.ArrayMessageReader;
import io.datty.msgpack.core.MapMessageReader;
import io.datty.msgpack.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * MapReader
 * 
 * @author Alex Shvid
 *
 */

public class MapReader extends AbstractMessageReader implements ValueReader<Map<Object, Object>> {

	public static final MapReader INSTANCE = new MapReader();
	
	@Override
	public Map<Object, Object> read(ByteBuf buffer, boolean copy) {

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
	
	public <K, V> Map<K, V> read(ValueReader<K> keyReader, ValueReader<V> valueReader, ByteBuf buffer, boolean copy) {
		
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
    	return (Map<K, V>) readArray(valueReader, buffer, copy);      	
    	
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return readMap(keyReader, valueReader, buffer, copy);
    	
    default:
      throw new MessageParseException("expected collection but was another type: " + f.name());	
		
		}		
		
	}

	private Map<Object, Object> readArray(ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);
		MessageReader<Integer> reader = new ArrayMessageReader(length);

		Map<Object, Object> map = new HashMap<Object, Object>();

		for (int i = 0; i != length; ++i) {
			Integer key = reader.readKey(source);
			Object value = reader.readValue(source, copy);
			map.put(key, value);
		}
		
		return map;
	}
	
	private <V> Map<Integer, V> readArray(ValueReader<V> valueReader, ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);

		Map<Integer, V> map = new HashMap<>();

		for (int i = 0; i != length; ++i) {
			V value = valueReader.read(source, copy);
			map.put(i +  MessageConstants.ARRAY_START_INDEX, value);
		}
		
		return map;
	}
	
	private Map<Object, Object> readMap(ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);
		MessageReader<Object> reader = new MapMessageReader(length);

		Map<Object, Object> map = new HashMap<Object, Object>();

		for (int i = 0; i != length; ++i) {
			Object key = reader.readKey(source);
			Object value = reader.readValue(source, copy);
			map.put(key, value);
		}
		
		return map;
	}
	
	private <K, V> Map<K, V> readMap(ValueReader<K> keyReader, ValueReader<V> valueReader, ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);

		Map<K, V> map = new HashMap<>();

		for (int i = 0; i != length; ++i) {
			K key = keyReader.read(source, true);
			V value = valueReader.read(source, copy);
			map.put(key, value);
		}
		
		return map;
	}
	
	
}
