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
package io.datty.msgpack.core.writer;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;

/**
 * ValueWriters
 * 
 * @author Alex Shvid
 *
 */

public final class ValueWriters {

	private final static Map<Class<?>, ValueWriter<?>> writers = new HashMap<>();
	
	static {
		writers.put(boolean.class, BooleanWriter.INSTANCE);
		writers.put(Boolean.class, BooleanWriter.INSTANCE);
		writers.put(byte.class, ByteWriter.INSTANCE);
		writers.put(Byte.class, ByteWriter.INSTANCE);
		writers.put(short.class, ShortWriter.INSTANCE);
		writers.put(Short.class, ShortWriter.INSTANCE);
		writers.put(int.class, IntegerWriter.INSTANCE);
		writers.put(Integer.class, IntegerWriter.INSTANCE);
		writers.put(long.class, LongWriter.INSTANCE);
		writers.put(Long.class, LongWriter.INSTANCE);
		writers.put(float.class, FloatWriter.INSTANCE);
		writers.put(Float.class, FloatWriter.INSTANCE);
		writers.put(double.class, DoubleWriter.INSTANCE);
		writers.put(Double.class, DoubleWriter.INSTANCE);
		writers.put(String.class, StringWriter.INSTANCE);
		writers.put(ByteBuf.class, ByteBufWriter.INSTANCE);
	}
	
	private ValueWriters() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ValueWriter<T> find(Class<T> type) {
		
		if (ByteBuf.class.isAssignableFrom(type)) {
			return  (ValueWriter<T>) ByteBufWriter.INSTANCE;
		}
		
		return (ValueWriter<T>) writers.get(type);
	}
	
}
