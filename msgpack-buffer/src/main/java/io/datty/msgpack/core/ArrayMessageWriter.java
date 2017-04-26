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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;

/**
 * ArrayMessageWriter
 * 
 * @author Alex Shvid
 *
 */

public class ArrayMessageWriter extends ValueMessageWriter implements MessageWriter {

	public static final ArrayMessageWriter INSTANCE = new ArrayMessageWriter();
	
	@Override
	public int skipHeader(int maxSize, ByteBuf sink) {
		int headerIndex = sink.writerIndex();
		int headerSize = getArrayHeaderSize(maxSize);
		for (int i = 0; i != headerSize; ++i) {
			sink.writeByte(0);
		}
		return headerIndex;
	}

	@Override
	public void writeHeader(int arraySize, int maxSize, int headerIndex, ByteBuf sink) {
		int checkpoint = sink.writerIndex();
		sink.writerIndex(headerIndex);
		writeArrayHeader(arraySize, maxSize, sink);
		sink.writerIndex(checkpoint);
	}

	@Override
	public void writeHeader(int size, ByteBuf sink) {
		writeArrayHeader(size, sink);
	}
	
	@Override
	public ByteBuf prependHeader(int arraySize, ByteBuf sink) {
		
		int headerSize = getArrayHeaderSize(arraySize);
		
		ByteBuf header = sink.alloc().buffer(headerSize);
		writeMapHeader(arraySize, header);
		
		if (sink instanceof CompositeByteBuf) {
			CompositeByteBuf compositeSink = (CompositeByteBuf) sink;
			compositeSink.addComponent(true, 0, header);
			return compositeSink;
		}
		else {
			CompositeByteBuf result = sink.alloc().compositeBuffer();
			result.addComponent(true, header);
			result.addComponent(true, sink);
			return result;
		}
	}

	@Override
	public void writeKey(String key, ByteBuf sink) {
		throw new UnsupportedOperationException("this method does not support in array");
	}

	@Override
	public void writeKey(int key, ByteBuf sink) {
		throw new UnsupportedOperationException("this method does not support in array");
	}
	
}
