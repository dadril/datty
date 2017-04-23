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

import io.netty.buffer.ByteBuf;

/**
 * ValueReaders
 * 
 * @author Alex Shvid
 *
 */

public final class ValueReaders {

	private final static Map<Class<?>, ValueReader<?>> readers = new HashMap<>();
	
	static {
		readers.put(Boolean.class, BooleanReader.INSTANCE);
		readers.put(Byte.class, ByteReader.INSTANCE);
		readers.put(Short.class, ShortReader.INSTANCE);
		readers.put(Integer.class, IntegerReader.INSTANCE);
		readers.put(Long.class, LongReader.INSTANCE);
		readers.put(Float.class, FloatReader.INSTANCE);
		readers.put(Double.class, DoubleReader.INSTANCE);
		readers.put(String.class, StringReader.INSTANCE);
		readers.put(ByteBuf.class, ByteBufReader.INSTANCE);
	}
	
	private ValueReaders() {
	}

	@SuppressWarnings("unchecked")
	public static <T> ValueReader<T> find(Class<T> type) {
		return (ValueReader<T>) readers.get(type);
	}
	
}
