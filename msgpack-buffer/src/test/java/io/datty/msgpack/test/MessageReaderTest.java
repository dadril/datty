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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.MessageFactory;
import io.datty.msgpack.MessageReader;
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
	public void testExample() {
		
		final byte[] example =  new byte[] {-125, -93, 97, 99, 99, -93, 49, 50, 51, -90, 108, 111, 103, 105, 110, 115, -9, -92, 110, 97, 109, 101, -92, 65, 108, 101, 120};
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object message = MessageFactory.readValue(source, false);
	  Assert.assertTrue(message instanceof MessageReader);
		
	  MessageReader<String> reader = (MessageReader<String>) message;

	  Map<String, Object> map = new HashMap<>();
	  
	  for (int i = 0; i != reader.size(); ++i) {
	  	
	  	String key = reader.readKey(source);
	  	Object value = reader.readValue(source, false);
	  	
	  	map.put(key, value);
	  	
	  }
	  
	  Assert.assertEquals(map.get("acc"), "123");
	  Assert.assertEquals(map.get("logins"), Long.valueOf(-9));
	  Assert.assertEquals(map.get("name"), "Alex");

	  Assert.assertNull(reader.readKey(source));
		
	}
	
}
