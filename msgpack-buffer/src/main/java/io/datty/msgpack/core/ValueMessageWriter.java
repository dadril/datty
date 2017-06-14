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
package io.datty.msgpack.core;


import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.type.ArrayTypeInfo;
import io.datty.msgpack.core.type.DefaultTypeInfoProvider;
import io.datty.msgpack.core.type.ListTypeInfo;
import io.datty.msgpack.core.type.MapTypeInfo;
import io.datty.msgpack.core.type.SimpleTypeInfo;
import io.datty.msgpack.core.type.TypeInfo;
import io.datty.msgpack.core.writer.ArrayWriter;
import io.datty.msgpack.core.writer.ListWriter;
import io.datty.msgpack.core.writer.MapWriter;
import io.datty.msgpack.core.writer.ValueWriter;
import io.datty.msgpack.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ValueMessageWriter
 * 
 * @author Alex Shvid
 *
 */

public class ValueMessageWriter extends AbstractMessageWriter implements MessageWriter {

	public static final ValueMessageWriter INSTANCE = new ValueMessageWriter();
	
	public static final ValueWriter<Object> OBJECT_VALUE_WRITER = new ValueWriter<Object>() {

		@Override
		public ByteBuf write(Object value, ByteBuf sink, boolean copy) {
			
			if (value == null) {
				return INSTANCE.writeNull(sink);
			}
			else {
				Class<?> type = value.getClass();
				TypeInfo<?> typeInfo = DefaultTypeInfoProvider.INSTANCE.getTypeInfo(type);
				return INSTANCE.writeValue((TypeInfo<Object>) typeInfo, value, sink, copy);
			}
			
		}

		
	};
	
	@Override
	public int skipHeader(int maxSize, ByteBuf sink) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public void writeHeader(int mapSize, int maxSize, int headerIndex, ByteBuf sink) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public void writeHeader(int size, ByteBuf sink) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public void writeKey(String key, ByteBuf sink) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public void writeKey(int key, ByteBuf sink) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public void writeValue(boolean value, ByteBuf sink) {
		writeBoolean(value, sink);
	}

	@Override
	public void writeValue(long value, ByteBuf sink) {
		writeVLong(value, sink);
	}

	@Override
	public void writeValue(double value, ByteBuf sink) {
		writeDouble(value, sink);
	}

	@Override
	public void writeValue(String value, ByteBuf sink) {
		writeString(value, sink);
	}

	@Override
	public ByteBuf writeValue(ByteBuf value, ByteBuf sink, boolean copy) {

		writeBinaryHeader(value.readableBytes(), sink);
		sink.writeBytes(value, value.readerIndex(), value.readableBytes());
		return sink;

		/**
		if (copy) {
			writeBinaryHeader(value.readableBytes(), sink);
			sink.writeBytes(value, value.readerIndex(), value.readableBytes());
			return sink;
		}
		else if (sink instanceof CompositeByteBuf) {
			writeBinaryHeader(value.readableBytes(), sink);
			CompositeByteBuf compositeSink = (CompositeByteBuf) sink;
			compositeSink.addComponent(true, value.duplicate());
			return compositeSink;
		}
		else {
			writeBinaryHeader(value.readableBytes(), sink);
			CompositeByteBuf result = sink.alloc().compositeBuffer();
			result.addComponent(true, sink);
			result.addComponent(true, value);
			return result;
		}
		*/
		
	}

	@Override
	public <V extends T, T> ByteBuf writeValue(TypeInfo<T> type, V value, ByteBuf sink, boolean copy) {

		if (type instanceof SimpleTypeInfo) {
			
			SimpleTypeInfo<T> simpleType = (SimpleTypeInfo<T>) type;
			
			return simpleType.getValueWriter().write(value, sink, copy);
			
		}
		
		else if (type instanceof ArrayTypeInfo) {
			
			ArrayTypeInfo<Object, T> arrayType = (ArrayTypeInfo<Object, T>) type;
			
			return ArrayWriter.INSTANCE.write(arrayType.getComponentValueWriter(), value, sink, copy);
			
		}
		
		else if (type instanceof ListTypeInfo) {
			
			ListTypeInfo<Object, T> listType = (ListTypeInfo<Object, T>) type;
			
			return ListWriter.INSTANCE.write(listType.getComponentValueWriter(), value, sink, copy);
			
		}
		
		else if (type instanceof MapTypeInfo) {
			
			MapTypeInfo<Object, Object, T> mapType = (MapTypeInfo<Object, Object, T>) type;
			
			return MapWriter.INSTANCE.write(mapType.getKeyValueWriter(), mapType.getComponentValueWriter(), value, sink, copy);
			
		}
		
		else {
			throw new MessageParseException("unknown type info: " + type);
		}
		
	}

	
}
