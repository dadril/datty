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

import io.datty.msgpack.core.AbstractMessageWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;

/**
 * ByteBufWriter
 * 
 * @author Alex Shvid
 *
 */

public class ByteBufWriter extends AbstractMessageWriter implements ValueWriter<ByteBuf> {

	public static final ByteBufWriter INSTANCE = new ByteBufWriter();

	@Override
	public ByteBuf write(ByteBuf value, ByteBuf sink, boolean copy) {
		
		if (value == null) {
			writeNull(sink);
			return sink;
		}
		
		else if (copy) {
			writeBinaryHeader(value.readableBytes(), sink);
			sink.writeBytes(value, value.readerIndex(), value.readableBytes());
			return sink;
		}
		
		else if (sink instanceof CompositeByteBuf) {
			writeBinaryHeader(value.readableBytes(), sink);
			CompositeByteBuf compositeSink = (CompositeByteBuf) sink;
			compositeSink.addComponent(true, value);
			return compositeSink;
		}
		
		else {
			writeBinaryHeader(value.readableBytes(), sink);
			CompositeByteBuf result = sink.alloc().compositeBuffer();
			result.addComponent(true, sink);
			result.addComponent(true, value);
			return result;
		}
		
	}
	
}
