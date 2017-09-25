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

import io.datty.msgpack.MessageIO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * MessageReaderInnerTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageReaderInnerTest {

	@Test
	public void testReadInnerMap() {
		
		final byte[] example = new byte[] { -126, -94, 105, 100, -51, 3, 9, -92, 110, 97, 109, 101, -126, -91, 102, 105, 114, 115, 116, -92, 74, 111, 104, 110, -92, 108, 97, 115, 116, -93, 68, 111, 119 };
		
		assertInnerMapExample(example);
	}
	
	@Test
	public void testReadInnerArray() {
		
		final byte[] example = new byte[] { -126, -94, 105, 100, -51, 3, 9, -91, 110, 97, 109, 101, 115, -110, -92, 74, 111, 104, 110, -93, 68, 111, 119 };

		assertInnerArrayExample(example);
	}
	
	protected void assertInnerMapExample(byte[] example) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageIO.readValue(source, false);
	  Assert.assertTrue(message instanceof Map);
		
	  @SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) message;
	  
	  Assert.assertEquals(map.get("id"), Long.valueOf(777));

	  @SuppressWarnings("unchecked")
		Map<String, Object> innerMap = (Map<String, Object>) map.get("name");
	  Assert.assertNotNull(innerMap);
	  
	  Assert.assertEquals(innerMap.get("first"), "John");
	  Assert.assertEquals(innerMap.get("last"), "Dow");
	  
	}
	
	protected void assertInnerArrayExample(byte[] example) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageIO.readValue(source, false);
	  Assert.assertTrue(message instanceof Map);

	  @SuppressWarnings("unchecked")
	  Map<String, Object> map = (Map<String, Object>) message;
	  
	  Assert.assertEquals(map.get("id"), Long.valueOf(777));

	  @SuppressWarnings("unchecked")
	  List<Object> innerArray = (List<Object>) map.get("names");
	  Assert.assertNotNull(innerArray);
	  
	  Assert.assertEquals(innerArray.get(0), "John");
	  Assert.assertEquals(innerArray.get(1), "Dow");
	  
	}
	
}
