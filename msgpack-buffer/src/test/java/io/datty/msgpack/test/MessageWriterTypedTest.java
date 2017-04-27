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
package io.datty.msgpack.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.datty.msgpack.core.MapMessageWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * MessageWriterTypedTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageWriterTypedTest extends MessageReaderTypedTest {

	@Test
	public void testPrimitives() {
		
		ByteBuf sink = Unpooled.buffer();

		MessageWriter writer = MapMessageWriter.INSTANCE;
		
		int fields = 0;
		
		writer.writeKey("boolean", sink);
		writer.writeValue(boolean.class, true, sink, true);
		fields++;

		writer.writeKey("Boolean", sink);
		writer.writeValue(Boolean.class, true, sink, true);
		fields++;
		
		writer.writeKey("byte", sink);
		writer.writeValue(byte.class, (byte)1, sink, true);
		fields++;
		
		writer.writeKey("Byte", sink);
		writer.writeValue(Byte.class, (byte)1, sink, true);
		fields++;
		
		writer.writeKey("short", sink);
		writer.writeValue(short.class, (short)1, sink, true);
		fields++;
		
		writer.writeKey("Short", sink);
		writer.writeValue(Short.class, (short)1, sink, true);
		fields++;
		
		writer.writeKey("int", sink);
		writer.writeValue(int.class, (int)1, sink, true);
		fields++;
		
		writer.writeKey("Integer", sink);
		writer.writeValue(Integer.class, (int)1, sink, true);
		fields++;
		
		writer.writeKey("long", sink);
		writer.writeValue(long.class, (long)1, sink, true);
		fields++;
		
		writer.writeKey("Long", sink);
		writer.writeValue(Long.class, (long)1, sink, true);
		fields++;
		
		writer.writeKey("float", sink);
		writer.writeValue(float.class, (float)1, sink, true);
		fields++;
		
		writer.writeKey("Float", sink);
		writer.writeValue(Float.class, (float)1, sink, true);
		fields++;
		
		writer.writeKey("double", sink);
		writer.writeValue(double.class, (double)1, sink, true);
		fields++;
		
		writer.writeKey("Double", sink);
		writer.writeValue(Double.class, (double)1, sink, true);
		fields++;
		
		writer.writeKey("String", sink);
		writer.writeValue(String.class, "1", sink, true);
		fields++;
		
		writer.writeKey("ByteBuf", sink);
		writer.writeValue(ByteBuf.class, Unpooled.wrappedBuffer(new byte[] { 1 }), sink, true);
		fields++;
		
		sink = writer.prependHeader(fields, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertPrimitivesExample(bytes);
		
	}
	
	@Test
	public void testComplexStringMap() {

		ByteBuf sink = Unpooled.buffer();

		MessageWriter writer = MapMessageWriter.INSTANCE;
		writer.writeHeader(3, sink);
		
		// 1
		writer.writeKey("id", sink);
		writer.writeValue(Long.class, Long.valueOf(1000), sink, true);
		
		// 2
		writer.writeKey("balance", sink);
		writer.writeValue(Double.class, Double.valueOf(33.45), sink, true);
		
		// 3
		writer.writeKey("withdrawals", sink);
		writeDoubleList(Arrays.asList(10.45, 44.30), sink);
		
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		//System.out.println(Arrays.toString(bytes));
		
		assertComplexStringMapExample(bytes);
		
	}
	
	private void writeDoubleList(List<Double> list, ByteBuf sink) {
		
		MessageWriter writer = ArrayMessageWriter.INSTANCE;
		
		writer.writeHeader(list.size(), sink);
		
		for (Double value : list) {
			writer.writeValue(value, sink);
		}
		
	}
	
}
