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
	
	public <K, V> Map<K, V> read(Class<K> keyType, Class<V> valueType, ByteBuf buffer, boolean copy) {
		
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
    	return readArray(keyType, valueType, buffer, copy);      	
    	
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return readMap(keyType, valueType, buffer, copy);
    	
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
	
	private <K, V> Map<K, V> readArray(Class<K> keyType, Class<V> valueType, ByteBuf source, boolean copy) {
		
		int length = readArrayHeader(source);
		MessageReader<Integer> reader = new ArrayMessageReader(length);

		Map<K, V> map = new HashMap<>();

		for (int i = 0; i != length; ++i) {
			K key = reader.readValue(keyType, source, true);
			V value = reader.readValue(valueType, source, copy);
			map.put(key, value);
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
	
	private <K, V> Map<K, V> readMap(Class<K> keyType, Class<V> valueType, ByteBuf source, boolean copy) {
		
		int length = readMapHeader(source);
		MessageReader<Object> reader = new MapMessageReader(length);

		Map<K, V> map = new HashMap<>();

		for (int i = 0; i != length; ++i) {
			K key = reader.readValue(keyType, source, true);
			V value = reader.readValue(valueType, source, copy);
			map.put(key, value);
		}
		
		return map;
	}
	
	
}
