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
 * ArrayTypeInfoAdapter
 * 
 * @author Alex Shvid
 *
 * @param <E> - component type
 * @param <T> - array type
 */

public final class ArrayTypeInfoAdapter<E, T> implements ArrayTypeInfo<E, T> {
	
	private final Class<T> arrayType;
	private final SimpleTypeInfo<E> component;
	
	public ArrayTypeInfoAdapter(Class<T> arrayType, SimpleTypeInfo<E> component) {
		this.arrayType = arrayType;
		this.component = component;
	}

	@Override
	public Class<T> getType() {
		return arrayType;
	}

	@Override
	public Class<E> getComponentType() {
		return component.getType();
	}

	@Override
	public ValueReader<E> getComponentValueReader() {
		return component.getValueReader();
	}

	@Override
	public ValueWriter<E> getComponentValueWriter() {
		return component.getValueWriter();
	}

}