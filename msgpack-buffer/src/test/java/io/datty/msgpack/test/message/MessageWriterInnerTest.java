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
package io.datty.msgpack.test.message;

import java.util.Arrays;

import org.junit.Test;

import io.datty.msgpack.message.MessageWriter;
import io.datty.msgpack.message.core.ArrayMessageWriter;
import io.datty.msgpack.message.core.MapMessageWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * MessageWriterInnerTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageWriterInnerTest extends MessageReaderInnerTest {

	@Test
	public void testWriteInnerMap() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();

		int headerIndex = writer.skipHeader(10, sink);
		
		writer.writeKey("id", sink);
		writer.writeValue(777, sink);
		writer.writeKey("name", sink);
		
		writeMapNames(sink);
		
		writer.writeHeader(2, 10, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertInnerMapExample(bytes);
		
	}
	
	@Test
	public void testWriteInnerArray() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();

		int headerIndex = writer.skipHeader(10, sink);
		
		writer.writeKey("id", sink);
		writer.writeValue(777, sink);
		writer.writeKey("names", sink);
		
		writeArrayNames(sink);
		
		writer.writeHeader(2, 10, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		System.out.println(Arrays.toString(bytes));
		
		assertInnerArrayExample(bytes);
		
	}
	
	private void writeMapNames(ByteBuf sink) {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		
		int headerIndex = writer.skipHeader(10, sink);
		
		writer.writeKey("first", sink);
		writer.writeValue("John", sink);
		writer.writeKey("last", sink);
		writer.writeValue("Dow", sink);
		
		writer.writeHeader(2, 10, headerIndex, sink);
		
	}
	
	private void writeArrayNames(ByteBuf sink) {
		
		MessageWriter writer = ArrayMessageWriter.INSTANCE;
		
		int headerIndex = writer.skipHeader(10, sink);
		
		writer.writeValue("John", sink);
		writer.writeValue("Dow", sink);
		
		writer.writeHeader(2, 10, headerIndex, sink);
		
	}
	
}
