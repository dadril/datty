package io.datty.msgpack.lite;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteEvent;
import io.datty.msgpack.lite.LiteTable;
import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.impl.LiteTableImpl;


/**
 * LiteEventTest
 * 
 * Unit test for MessageBox
 * 
 * @author Alex Shvid
 *
 */

public class LiteEventTest {

	@Test
	public void testEmpty() {
		
		LiteEvent message = LiteValueFactory.newEvent();
		
		Assert.assertTrue(message.isEmpty());

		byte[] msgpack = message.toByteArray();
		Assert.assertTrue(Arrays.equals(new byte[] { -128 }, msgpack));
		
		String json = message.toJson();
		Assert.assertEquals("{}", json);
		
		LiteEvent actual = LiteValueFactory.parseEvent(msgpack);
		Assert.assertTrue(actual.isEmpty());
		
	}
	
	@Test
	public void testNotEmpty() {
		
		String payload = "{}";
		
		LiteEvent message = LiteValueFactory.newEvent();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayloadUtf8("json", payload);
		
		Assert.assertFalse(message.isEmpty());
		
		byte[] msgpack = message.toByteArray();
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		LiteEvent actual = LiteValueFactory.parseEvent(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		Assert.assertEquals(payload, message.getPayloadUtf8("json"));
		Assert.assertEquals(payload, actual.getPayloadUtf8("json"));
		
	}
	
	@Test
	public void testNotEmptyBlob() {
		
		byte[] payload = "{}".getBytes(StandardCharsets.UTF_8);
		
		LiteEvent message = LiteValueFactory.newEvent();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayload("json", payload, false);
		
		Assert.assertFalse(message.isEmpty());
		
		byte[] msgpack = message.toByteArray();
		//System.out.println("msgpack = " + Arrays.toString(msgpack));
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		LiteEvent actual = LiteValueFactory.parseEvent(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		Assert.assertTrue(Arrays.equals(payload, message.getPayload("json", false)));
		Assert.assertTrue(Arrays.equals(payload, actual.getPayload("json", false)));
		
	}
	
	@Test
	public void testMessageValue() {
		
		LiteTable payload = new LiteTableImpl();
		
		payload.put("name", "Bob");
		payload.put("age", "45");
		
		LiteEvent message = LiteValueFactory.newEvent();
		
		message
		.addHeader("id", "123")
		.addHeader("url", "www")
		.addPayload("payload", payload);
		
		byte[] msgpack = message.toByteArray();
		//System.out.println("msgpack = " + Arrays.toString(msgpack));
		
		String json = message.toJson();
		//System.out.println("json = " + json);
		
		LiteEvent actual = LiteValueFactory.parseEvent(msgpack);
		Assert.assertFalse(actual.isEmpty());
		
		Assert.assertEquals(message.getHeader("id"), actual.getHeader("id"));
		Assert.assertEquals(message.getHeader("url"), actual.getHeader("url"));
		
		LiteTable actualPayload = actual.getTypedPayload("payload");

		Assert.assertEquals(payload.get("name"), actualPayload.get("name"));
		Assert.assertEquals(payload.get("age"), actualPayload.get("age"));

	}
	
	
}
