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

import java.util.HashMap;
import java.util.Map;

import io.datty.msgpack.core.reader.BooleanReader;
import io.datty.msgpack.core.reader.ByteBufReader;
import io.datty.msgpack.core.reader.ByteReader;
import io.datty.msgpack.core.reader.DoubleReader;
import io.datty.msgpack.core.reader.FloatReader;
import io.datty.msgpack.core.reader.IntegerReader;
import io.datty.msgpack.core.reader.LongReader;
import io.datty.msgpack.core.reader.ShortReader;
import io.datty.msgpack.core.reader.StringReader;
import io.datty.msgpack.core.writer.BooleanWriter;
import io.datty.msgpack.core.writer.ByteBufWriter;
import io.datty.msgpack.core.writer.ByteWriter;
import io.datty.msgpack.core.writer.DoubleWriter;
import io.datty.msgpack.core.writer.FloatWriter;
import io.datty.msgpack.core.writer.IntegerWriter;
import io.datty.msgpack.core.writer.LongWriter;
import io.datty.msgpack.core.writer.ShortWriter;
import io.datty.msgpack.core.writer.StringWriter;
import io.netty.buffer.ByteBuf;

/**
 * TypeRegistry
 * 
 * @author Alex Shvid
 *
 */

public final class TypeRegistry {

	private final static Map<Class<?>, SimpleTypeInfo<?>> simpleMap = new HashMap<>();	

	private final static SimpleTypeInfoImpl<ByteBuf> BYTEBUF_TYPE = new SimpleTypeInfoImpl<ByteBuf>(ByteBuf.class, ByteBufReader.INSTANCE, ByteBufWriter.INSTANCE);
	
	static {
		
		simpleMap.put(boolean.class, new SimpleTypeInfoImpl<Boolean>(boolean.class, BooleanReader.INSTANCE, BooleanWriter.INSTANCE));
		simpleMap.put(Boolean.class, new SimpleTypeInfoImpl<Boolean>(Boolean.class, BooleanReader.INSTANCE, BooleanWriter.INSTANCE));

		simpleMap.put(byte.class, new SimpleTypeInfoImpl<Byte>(byte.class, ByteReader.INSTANCE, ByteWriter.INSTANCE));
		simpleMap.put(Byte.class, new SimpleTypeInfoImpl<Byte>(Byte.class, ByteReader.INSTANCE, ByteWriter.INSTANCE));

		simpleMap.put(short.class, new SimpleTypeInfoImpl<Short>(short.class, ShortReader.INSTANCE, ShortWriter.INSTANCE));
		simpleMap.put(Short.class, new SimpleTypeInfoImpl<Short>(Short.class, ShortReader.INSTANCE, ShortWriter.INSTANCE));

		simpleMap.put(int.class, new SimpleTypeInfoImpl<Integer>(int.class, IntegerReader.INSTANCE, IntegerWriter.INSTANCE));
		simpleMap.put(Integer.class, new SimpleTypeInfoImpl<Integer>(Integer.class, IntegerReader.INSTANCE, IntegerWriter.INSTANCE));

		simpleMap.put(long.class, new SimpleTypeInfoImpl<Long>(long.class, LongReader.INSTANCE, LongWriter.INSTANCE));
		simpleMap.put(Long.class, new SimpleTypeInfoImpl<Long>(Long.class, LongReader.INSTANCE, LongWriter.INSTANCE));

		simpleMap.put(float.class, new SimpleTypeInfoImpl<Float>(float.class, FloatReader.INSTANCE, FloatWriter.INSTANCE));
		simpleMap.put(Float.class, new SimpleTypeInfoImpl<Float>(Float.class, FloatReader.INSTANCE, FloatWriter.INSTANCE));

		simpleMap.put(double.class, new SimpleTypeInfoImpl<Double>(double.class, DoubleReader.INSTANCE, DoubleWriter.INSTANCE));
		simpleMap.put(Double.class, new SimpleTypeInfoImpl<Double>(Double.class, DoubleReader.INSTANCE, DoubleWriter.INSTANCE));

		simpleMap.put(String.class, new SimpleTypeInfoImpl<String>(String.class, StringReader.INSTANCE, StringWriter.INSTANCE));
		
		simpleMap.put(ByteBuf.class, BYTEBUF_TYPE);
		
	}
	
	private TypeRegistry() {
	}
	
	/**
	 * Finds simple type info
	 * 
	 * @param type - type class
	 * @return null or simple type info
	 */
	
	public static <T> SimpleTypeInfo<T> findSimple(Class<T> type) {
		
		if (ByteBuf.class.isAssignableFrom(type)) {
			return (SimpleTypeInfo<T>) BYTEBUF_TYPE;
		}

		return (SimpleTypeInfo<T>) simpleMap.get(type);
	}
	

	
}
