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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.datty.msgpack.message.MessageWriter;
import io.datty.msgpack.message.core.ArrayMessageWriter;
import io.datty.msgpack.message.core.MapMessageWriter;
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
		
		int headerIndex = writer.skipHeader(255, sink);
		
		int fields = 0;
		
		writer.writeKey("boolean", sink);
		writer.writeValue(typeOf(boolean.class), true, sink, true);
		fields++;

		writer.writeKey("Boolean", sink);
		writer.writeValue(typeOf(Boolean.class), true, sink, true);
		fields++;
		
		writer.writeKey("byte", sink);
		writer.writeValue(typeOf(byte.class), (byte)1, sink, true);
		fields++;
		
		writer.writeKey("Byte", sink);
		writer.writeValue(typeOf(Byte.class), (byte)1, sink, true);
		fields++;
		
		writer.writeKey("short", sink);
		writer.writeValue(typeOf(short.class), (short)1, sink, true);
		fields++;
		
		writer.writeKey("Short", sink);
		writer.writeValue(typeOf(Short.class), (short)1, sink, true);
		fields++;
		
		writer.writeKey("int", sink);
		writer.writeValue(typeOf(int.class), (int)1, sink, true);
		fields++;
		
		writer.writeKey("Integer", sink);
		writer.writeValue(typeOf(Integer.class), (int)1, sink, true);
		fields++;
		
		writer.writeKey("long", sink);
		writer.writeValue(typeOf(long.class), (long)1, sink, true);
		fields++;
		
		writer.writeKey("Long", sink);
		writer.writeValue(typeOf(Long.class), (long)1, sink, true);
		fields++;
		
		writer.writeKey("float", sink);
		writer.writeValue(typeOf(float.class), (float)1, sink, true);
		fields++;
		
		writer.writeKey("Float", sink);
		writer.writeValue(typeOf(Float.class), (float)1, sink, true);
		fields++;
		
		writer.writeKey("double", sink);
		writer.writeValue(typeOf(double.class), (double)1, sink, true);
		fields++;
		
		writer.writeKey("Double", sink);
		writer.writeValue(typeOf(Double.class), (double)1, sink, true);
		fields++;
		
		writer.writeKey("String", sink);
		writer.writeValue(typeOf(String.class), "1", sink, true);
		fields++;
		
		writer.writeKey("ByteBuf", sink);
		writer.writeValue(typeOf(ByteBuf.class), Unpooled.wrappedBuffer(new byte[] { 1 }), sink, true);
		fields++;
		
		writer.writeHeader(fields, 255, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertPrimitivesExample(bytes);
		
	}
	
	@Test
	public void testComplexStringMap() {

		ByteBuf sink = Unpooled.buffer();

		MessageWriter writer = MapMessageWriter.INSTANCE;
		writer.writeHeader(5, sink);
		
		// 1
		writer.writeKey("id", sink);
		writer.writeValue(typeOf(Long.class), Long.valueOf(1000), sink, true);
		
		// 2
		writer.writeKey("balance", sink);
		writer.writeValue(typeOf(Double.class), Double.valueOf(33.45), sink, true);
		
		// 3
		writer.writeKey("withdrawals", sink);
		writeDoubleList(Arrays.asList(10.45, 44.30), sink);
		
		// 4
		writer.writeKey("names", sink);
		Map<String, String> map = new HashMap<String, String>();
		map.put("first", "John");
		map.put("second", "Dow");
		writeMap(map, sink);
		
		// 5
		writer.writeKey("logins", sink);
		writer.writeValue(typeOf(long[].class), new long[] { 34, 56 }, sink, true);

		
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
	
	private void writeMap(Map<String, String> map, ByteBuf sink) {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		
		writer.writeHeader(map.size(), sink);
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			
			writer.writeKey(entry.getKey(), sink);
			writer.writeValue(entry.getValue(), sink);
			
		}
		
	}
	
}
