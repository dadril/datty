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

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.MessageFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * MessageReaderTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageReaderTest {

	@Test
	public void testIntMapExample() {
		
		final byte[] example =  new byte[] {-125, 1, -93, 49, 50, 51, 2, -9, 3, -92, 65, 108, 101, 120};
		assertIntMapExample(example);
		
	}
	
	@Test
	public void testStringMapExample() {
		
		final byte[] example =  new byte[] {-125, -93, 97, 99, 99, -93, 49, 50, 51, -90, 108, 111, 103, 105, 110, 115, -9, -92, 110, 97, 109, 101, -92, 65, 108, 101, 120};
		assertStringMapExample(example);
		
	}
	
	public void testArrayExample() {
		
		final byte[] example = new byte[] { -109, -93, 49, 50, 51, -9, -92, 65, 108, 101, 120 };
		assertArrayExample(example);
		
	}
	
	protected void assertIntMapExample(byte[] example) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageFactory.readValue(source, false);
	  Assert.assertTrue(message instanceof Map);
		
	  @SuppressWarnings("unchecked")
	  Map<Integer, Object> map = (Map<Integer, Object>) message;
	  
	  Assert.assertEquals(map.get(1), "123");
	  Assert.assertEquals(map.get(2), Long.valueOf(-9));
	  Assert.assertEquals(map.get(3), "Alex");
		
	}
	
	protected void assertStringMapExample(byte[] example) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageFactory.readValue(source, false);
	  Assert.assertTrue(message instanceof Map);
		
	  @SuppressWarnings("unchecked")
	  Map<Object, Object> map = (Map<Object, Object>) message;
	  
	  Assert.assertEquals(map.get("acc"), "123");
	  Assert.assertEquals(map.get("logins"), Long.valueOf(-9));
	  Assert.assertEquals(map.get("name"), "Alex");

	}
	
	protected void assertArrayExample(byte[] example) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageFactory.readValue(source, false);
	  Assert.assertTrue(message instanceof List);
	  
	  @SuppressWarnings("unchecked")
	  List<Object> array = (List<Object>) message;
	 
	  Assert.assertEquals(array.get(0), "123");
	  Assert.assertEquals(array.get(1), Long.valueOf(-9));
	  Assert.assertEquals(array.get(2), "Alex");
	  
	}
	
}
