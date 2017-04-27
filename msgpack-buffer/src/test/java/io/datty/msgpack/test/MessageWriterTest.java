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

import org.junit.Test;

import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.datty.msgpack.core.MapMessageWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * MessageWriterTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageWriterTest extends MessageReaderTest {

	private static final int FIELDS_COUNT = 3;

	@Test
	public void testIntMap() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writer.writeHeader(FIELDS_COUNT, sink);
		writeIntMapFields(writer, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertIntMapExample(bytes);
		
	}
	
	@Test
	public void testIntMapPrepend() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writeIntMapFields(writer, sink);
		
		sink = writer.prependHeader(FIELDS_COUNT, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertIntMapExample(bytes);
		
	}
	
	@Test
	public void testIntMapSkip() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		int headerIndex = writer.skipHeader(10, sink);
		
		writeIntMapFields(writer, sink);
		
		writer.writeHeader(FIELDS_COUNT, 10, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertIntMapExample(bytes);
		
	}
	
	@Test
	public void testStringMap() {

		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writer.writeHeader(FIELDS_COUNT, sink);
		writeStringMapFields(writer, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);

		assertStringMapExample(bytes);
		
	}
	
	@Test
	public void testStringMapPrepend() {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writeStringMapFields(writer, sink);
		
		sink = writer.prependHeader(FIELDS_COUNT, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertStringMapExample(bytes);
		
	}
	
	@Test
	public void testStringMapSkip() {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		int headerIndex = writer.skipHeader(10, sink);
		
		writeStringMapFields(writer, sink);
		
		writer.writeHeader(FIELDS_COUNT, 10, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertStringMapExample(bytes);
		
	}
	

	@Test
	public void testArray() {

		MessageWriter writer = ArrayMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writer.writeHeader(FIELDS_COUNT, sink);
		writeArrayFields(writer, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertArrayExample(bytes);
		
	}

	@Test
	public void testArrayPrepend() {

		MessageWriter writer = ArrayMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		writeArrayFields(writer, sink);
		
		sink = writer.prependHeader(FIELDS_COUNT, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		System.out.println(Arrays.toString(bytes));
		
		assertArrayExample(bytes);
		
	}
	
	@Test
	public void testArraySkip() {

		MessageWriter writer = ArrayMessageWriter.INSTANCE;
		ByteBuf sink = Unpooled.buffer();
		
		int headerIndex = writer.skipHeader(10, sink);
		
		writeArrayFields(writer, sink);
		
		writer.writeHeader(FIELDS_COUNT, 10, headerIndex, sink);
		
		byte[] bytes = ByteBufUtil.getBytes(sink);
		
		assertArrayExample(bytes);
		
	}
	
	private void writeIntMapFields(MessageWriter writer, ByteBuf sink) {
		writer.writeKey(1, sink);
		writer.writeValue("123", sink);
		writer.writeKey(2, sink);
		writer.writeValue(-9, sink);
		writer.writeKey(3, sink);
		writer.writeValue("Alex", sink);
	}
	
	private void writeStringMapFields(MessageWriter writer, ByteBuf sink) {
		writer.writeKey("acc", sink);
		writer.writeValue("123", sink);
		writer.writeKey("logins", sink);
		writer.writeValue(-9, sink);
		writer.writeKey("name", sink);
		writer.writeValue("Alex", sink);
	}
	
	private void writeArrayFields(MessageWriter writer, ByteBuf sink) {
		writer.writeValue("123", sink);
		writer.writeValue(-9, sink);
		writer.writeValue("Alex", sink);
	}
	
}
