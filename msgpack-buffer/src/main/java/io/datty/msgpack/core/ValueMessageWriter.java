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
import io.datty.msgpack.core.writer.ValueWriter;
import io.datty.msgpack.core.writer.ValueWriters;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;

/**
 * ValueMessageWriter
 * 
 * @author Alex Shvid
 *
 */

public class ValueMessageWriter extends AbstractMessageWriter implements MessageWriter {

	public static final ValueMessageWriter INSTANCE = new ValueMessageWriter();
	
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
	public ByteBuf prependHeader(int mapSize, ByteBuf sink) {
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

		if (copy) {
			writeBinaryHeader(value.readableBytes(), sink);
			sink.writeBytes(value);
			return sink;
		}
		else if (sink instanceof CompositeByteBuf) {
			CompositeByteBuf compositeSink = (CompositeByteBuf) sink;
			writeBinaryHeader(value.readableBytes(), compositeSink);
			compositeSink.addComponent(true, value);
			return compositeSink;
		}
		else {
			CompositeByteBuf result = sink.alloc().compositeBuffer();
			result.addComponent(true, sink);
			writeBinaryHeader(value.readableBytes(), result);
			result.addComponent(true, value);
			return result;
		}
		
	}

	@Override
	public <V extends T, T> ByteBuf writeValue(Class<T> type, V value, ByteBuf sink, boolean copy) {

		ValueWriter<T> writer = ValueWriters.find(type);
		
		if (writer == null) {
			writeNull(sink);
			return sink;
		}
		
		return writer.write(value, sink, copy);
	}
	
}