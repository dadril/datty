/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.test.table;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableMessage;
import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableTableImpl;


/**
 * PackableMessageTest
 * 
 * Unit test for MessageBox
 * 
 * @author Alex Shvid
 *
 */

public class PackableMessageTest {

	@Test
	public void testEmpty() {
		
		PackableMessage message = PackableValueFactory.newMessage();
		
		Assert.assertTrue(message.isEmpty());

		byte[] msgpack = message.toByteArray();
		Assert.assertTrue(Arrays.equals(new byte[] { -128 }, msgpack));
		
		String json = message.toJson();
		Assert.assertEquals("{}", json);
		
		PackableMessage actual = PackableValueFactory.parseMessage(msgpack);
		Assert.assertTrue(actual.isEmpty());
		
	}
	
	@Test
	public void testNotEmpty() {
		
		String payload = "{}";
		
		PackableMessage message = PackableValueFactory.newMessage();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayloadUtf8("json", payload);
		
		Assert.assertFalse(message.isEmpty());
		
		byte[] msgpack = message.toByteArray();
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		PackableMessage actual = PackableValueFactory.parseMessage(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		Assert.assertEquals(payload, message.getPayloadUtf8("json"));
		Assert.assertEquals(payload, actual.getPayloadUtf8("json"));
		
	}
	
	@Test
	public void testNotEmptyBlob() {
		
		byte[] payload = "{}".getBytes(StandardCharsets.UTF_8);
		
		PackableMessage message = PackableValueFactory.newMessage();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayload("json", payload, false);
		
		Assert.assertFalse(message.isEmpty());
		
		byte[] msgpack = message.toByteArray();
		//System.out.println("msgpack = " + Arrays.toString(msgpack));
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		PackableMessage actual = PackableValueFactory.parseMessage(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		Assert.assertTrue(Arrays.equals(payload, message.getPayload("json", false)));
		Assert.assertTrue(Arrays.equals(payload, actual.getPayload("json", false)));
		
	}
	
	@Test
	public void testMessageValue() {
		
		PackableTable payload = new PackableTableImpl();
		
		payload.put("name", "Bob");
		payload.put("age", "45");
		
		PackableMessage message = PackableValueFactory.newMessage();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayload("payload", payload);
		
		byte[] msgpack = message.toByteArray();
		//System.out.println("msgpack = " + Arrays.toString(msgpack));
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		PackableMessage actual = PackableValueFactory.parseMessage(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		PackableTable actualPayload = actual.getTypedPayload("payload");

		Assert.assertEquals(payload.get("name"), actualPayload.get("name"));
		Assert.assertEquals(payload.get("age"), actualPayload.get("age"));

	}
	
	
}
