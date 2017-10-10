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
package io.datty.msgpack.core.type;

import io.datty.msgpack.core.ValueMessageReader;
import io.datty.msgpack.core.ValueMessageWriter;
import io.datty.msgpack.core.reader.ValueReader;
import io.datty.msgpack.core.writer.ValueWriter;

/**
 * MapObjectTypeInfoAdapter
 * 
 * @author Alex Shvid
 *
 * @param <T> - map type
 */

public final class MapObjectTypeInfoAdapter<T> implements MapTypeInfo<Object, Object, T> {
	
	private final Class<T> mapType;
	
	public MapObjectTypeInfoAdapter(Class<T> mapType) {
		this.mapType = mapType;
	}

	@Override
	public Class<T> getType() {
		return mapType;
	}

	@Override
	public Class<Object> getKeyType() {
		return Object.class;
	}
	
	@Override
	public Class<Object> getComponentType() {
		return Object.class;
	}

	@Override
	public ValueReader<Object> getKeyValueReader() {
		return ValueMessageReader.OBJECT_KEY_READER;
	}

	@Override
	public ValueWriter<Object> getKeyValueWriter() {
		return ValueMessageWriter.OBJECT_VALUE_WRITER;
	}
	
	@Override
	public ValueReader<Object> getComponentValueReader() {
		return ValueMessageReader.OBJECT_VALUE_READER;
	}

	@Override
	public ValueWriter<Object> getComponentValueWriter() {
		return ValueMessageWriter.OBJECT_VALUE_WRITER;
	}

}