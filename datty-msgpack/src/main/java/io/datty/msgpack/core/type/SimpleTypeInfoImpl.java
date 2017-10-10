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

import io.datty.msgpack.core.reader.ValueReader;
import io.datty.msgpack.core.writer.ValueWriter;

/**
 * SimpleTypeInfoImpl
 * 
 * @author Alex Shvid
 *
 * @param <T> - type
 */

public final class SimpleTypeInfoImpl<T> implements SimpleTypeInfo<T> {

	private final Class<T> type; 
	private final ValueReader<T> reader;
	private final ValueWriter<T> writer;
	
	public SimpleTypeInfoImpl(Class<T> type, ValueReader<T> reader, ValueWriter<T> writer) {
		this.type = type;
		this.reader = reader;
		this.writer = writer;
	}
	
	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public ValueReader<T> getValueReader() {
		return reader;
	}

	@Override
	public ValueWriter<T> getValueWriter() {
		return writer;
	}

	@Override
	public String toString() {
		return "SimpleTypeInfoImpl [type=" + type + "]";
	}
	
	
}