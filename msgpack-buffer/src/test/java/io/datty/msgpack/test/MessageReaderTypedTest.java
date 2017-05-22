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

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.StringMapMessageReader;
import io.datty.msgpack.core.ValueMessageReader;
import io.datty.msgpack.core.type.DefaultTypeInfoProvider;
import io.datty.msgpack.core.type.TypeInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;


/**
 * MessageReaderTypedTest
 * 
 * @author Alex Shvid
 *
 */

public class MessageReaderTypedTest {

	private final static Map<String, Class<?>> PR_SCHEMA = new HashMap<String, Class<?>>();

	private final static Map<String, Class<?>> ACC_SCHEMA = new HashMap<String, Class<?>>();
	
	static {
		
		PR_SCHEMA.put("boolean", boolean.class);
		PR_SCHEMA.put("Boolean", Boolean.class);
		PR_SCHEMA.put("byte", byte.class);
		PR_SCHEMA.put("Byte", Byte.class);
		PR_SCHEMA.put("short", short.class);
		PR_SCHEMA.put("Short", Short.class);
		PR_SCHEMA.put("int", int.class);
		PR_SCHEMA.put("Integer", Integer.class);
		PR_SCHEMA.put("long", long.class);
		PR_SCHEMA.put("Long", Long.class);
		PR_SCHEMA.put("float", float.class);
		PR_SCHEMA.put("Float", Float.class);
		PR_SCHEMA.put("double", double.class);
		PR_SCHEMA.put("Double", Double.class);
		PR_SCHEMA.put("String", String.class);
		PR_SCHEMA.put("ByteBuf", ByteBuf.class);
		
		ACC_SCHEMA.put("id", Long.class);
		ACC_SCHEMA.put("balance", Double.class);
		ACC_SCHEMA.put("withdrawals", List.class);
		ACC_SCHEMA.put("names", Map.class);
		ACC_SCHEMA.put("logins", long[].class);
	}
	
	@Test
	public void testReadPrimitives() {
		
		final byte[] example = new byte[] { -34, 0, 16, -89, 98, 111, 111, 108, 101, 97, 110, -61, -89, 66, 111, 111, 108, 101, 97, 110, -61, -92, 98, 121, 116, 101, 1, -92, 66, 121, 116, 101, 1, -91, 115, 104, 111, 114, 116, -47, 0, 1, -91, 83, 104, 111, 114, 116, -47, 0, 1, -93, 105, 110, 116, 1, -89, 73, 110, 116, 101, 103, 101, 114, 1, -92, 108, 111, 110, 103, 1, -92, 76, 111, 110, 103, 1, -91, 102, 108, 111, 97, 116, -54, 63, -128, 0, 0, -91, 70, 108, 111, 97, 116, -54, 63, -128, 0, 0, -90, 100, 111, 117, 98, 108, 101, -53, 63, -16, 0, 0, 0, 0, 0, 0, -90, 68, 111, 117, 98, 108, 101, -53, 63, -16, 0, 0, 0, 0, 0, 0, -90, 83, 116, 114, 105, 110, 103, -95, 49, -89, 66, 121, 116, 101, 66, 117, 102, -60, 1, 1 };

		assertPrimitivesExample(example);
		
	}
	
	@Test
	public void testReadComplexStringMap() {
		
		final byte[] example = new byte[] { -123, -94, 105, 100, -51, 3, -24, -89, 98, 97, 108, 97, 110, 99, 101, -53, 64, 64, -71, -103, -103, -103, -103, -102, -85, 119, 105, 116, 104, 100, 114, 97, 119, 97, 108, 115, -110, -53, 64, 36, -26, 102, 102, 102, 102, 102, -53, 64, 70, 38, 102, 102, 102, 102, 102, -91, 110, 97, 109, 101, 115, -126, -91, 102, 105, 114, 115, 116, -92, 74, 111, 104, 110, -90, 115, 101, 99, 111, 110, 100, -93, 68, 111, 119, -90, 108, 111, 103, 105, 110, 115, -110, 34, 56 };
		
		assertComplexStringMapExample(example);
		
	}
	
	protected void assertPrimitivesExample(byte[] example) {
		
		Map<String, Object> map = readWithSchema(example, PR_SCHEMA);
		
		Assert.assertEquals(map.get("boolean"), true);
		Assert.assertEquals(map.get("Boolean"), true);
		Assert.assertEquals(map.get("byte"), (byte) 1);
		Assert.assertEquals(map.get("Byte"), (byte) 1);
		Assert.assertEquals(map.get("short"), (short) 1);
		Assert.assertEquals(map.get("Short"), (short) 1);
		Assert.assertEquals(map.get("int"), (int) 1);
		Assert.assertEquals(map.get("Integer"), (int) 1);
		Assert.assertEquals(map.get("long"), (long) 1);
		Assert.assertEquals(map.get("Long"), (long) 1);
		Assert.assertEquals(map.get("float"), (float) 1);
		Assert.assertEquals(map.get("Float"), (float) 1);
		Assert.assertEquals(map.get("double"), (double) 1);
		Assert.assertEquals(map.get("Double"), (double) 1);
		Assert.assertEquals(map.get("String"), "1");
		
		ByteBuf bb = (ByteBuf) map.get("ByteBuf");
		byte[] bytes = ByteBufUtil.getBytes(bb);
		
		Assert.assertTrue(Arrays.equals(bytes, new byte[] { 1 }));
		
	}
	
	protected Map<String, Object> readWithSchema(byte[] example, Map<String, Class<?>> schema) {
		
		ByteBuf source = Unpooled.wrappedBuffer(example);
		
		Object value = ValueMessageReader.INSTANCE.readValue(source, false);
		Assert.assertTrue(value instanceof StringMapMessageReader);
		
		MessageReader<String> reader = (MessageReader<String>) value;
		
		Map<String, Object> map = new HashMap<>();
		
		for (int i = 0; i != reader.size(); i++) {
			
			String key = reader.readKey(source);
			
			Class<?> type = schema.get(key);
			Assert.assertNotNull(type);
			
			Object field = reader.readValue(typeOf(type), source, false);
			Assert.assertNotNull(key, field);
			
			map.put(key, field);
		}
		
		return map;
		
	}
	
	protected void assertComplexStringMapExample(byte[] example) {
		
		Map<String, Object> map = readWithSchema(example, ACC_SCHEMA);
		
	  Assert.assertEquals(map.get("id"), Long.valueOf(1000));
	  Assert.assertEquals(map.get("balance"), Double.valueOf(33.45));
	  Assert.assertEquals(map.get("withdrawals"), Arrays.asList(10.45, 44.30));
	  
		
	}
	
	protected static <T> TypeInfo<T> typeOf(Class<T> type) {
		return DefaultTypeInfoProvider.INSTANCE.getTypeInfo(type);
	}
	
}
