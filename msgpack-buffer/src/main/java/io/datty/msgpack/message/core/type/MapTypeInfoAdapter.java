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
package io.datty.msgpack.message.core.type;

import io.datty.msgpack.message.core.reader.ValueReader;
import io.datty.msgpack.message.core.writer.ValueWriter;

/**
 * MapTypeInfoAdapter
 * 
 * @author Alex Shvid
 *
 * @param <K> - key type
 * @param <V> - value type
 * @param <T> - array type
 */

public final class MapTypeInfoAdapter<K, V, T> implements MapTypeInfo<K, V, T> {
	
	private final Class<T> keyType;
	private final SimpleTypeInfo<K> key;
	private final SimpleTypeInfo<V> component;
	
	public MapTypeInfoAdapter(Class<T> keyType, SimpleTypeInfo<K> key, SimpleTypeInfo<V> component) {
		this.keyType = keyType;
		this.key = key;
		this.component = component;
	}

	@Override
	public Class<T> getType() {
		return keyType;
	}
	
	@Override
	public Class<K> getKeyType() {
		return key.getType();
	}

	@Override
	public ValueReader<K> getKeyValueReader() {
		return key.getValueReader();
	}

	@Override
	public ValueWriter<K> getKeyValueWriter() {
		return key.getValueWriter();
	}

	@Override
	public Class<V> getComponentType() {
		return component.getType();
	}

	@Override
	public ValueReader<V> getComponentValueReader() {
		return component.getValueReader();
	}

	@Override
	public ValueWriter<V> getComponentValueWriter() {
		return component.getValueWriter();
	}

}