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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableTableType;
import io.datty.msgpack.table.PackableValue;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableNumberImpl;
import io.datty.msgpack.table.impl.PackableStringImpl;
import io.datty.msgpack.table.impl.PackableTableImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;



/**
 * PackableTableImplTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableTableImplTest extends AbstractPackableTest {

	@Test
	public void testNull() {

		PackableTable table = new PackableTableImpl();

		Assert.assertEquals(0, table.size());
		
		table.put(1, (PackableValue<?>) null);
		Assert.assertEquals(0, table.size());

		table.put(1, (String) null);
		Assert.assertEquals(0, table.size());

		table.put("1", (PackableValue<?>) null);
		Assert.assertEquals(0, table.size());

		table.put("1", (String) null);
		Assert.assertEquals(0, table.size());
		
	}
	
	@Test
	public void testEmpty() {
		
		PackableTable table = new PackableTableImpl();
		
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		Assert.assertEquals(0, table.size());
		
		PackableTable actual = PackableValueFactory.newTypedValue(table.toByteArray());
		Assert.assertEquals(PackableTableType.INT_KEY, actual.getType());
		Assert.assertEquals(0, actual.size());
		
	}
	
	private byte[] getAndCheck(PackableTable table) throws IOException {
		
    byte[] core = table.toByteArray();
    
    ByteBuf buffer = Unpooled.buffer();
    table.pack(buffer);
    byte[] raw = ByteBufUtil.getBytes(buffer);
    Assert.assertTrue(Arrays.equals(core, raw));
    
    return core;
	}
	
	@Test
	public void testSingleInt() throws IOException {
		
		PackableTable table = new PackableTableImpl();
		
		table.put(123, new PackableNumberImpl(123));
		
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		Assert.assertEquals(1, table.size());
		Assert.assertEquals("123", table.keySet().iterator().next());
		Assert.assertEquals(Integer.valueOf(123), table.intKeys().get(0));
		
    Assert.assertEquals("817b7b", table.toHexString());
    Assert.assertEquals("817b7b", toHexString(table));
		
		PackableTable actual = PackableValueFactory.newTypedValue(getAndCheck(table));
		
		Assert.assertEquals(PackableTableType.INT_KEY, actual.getType());
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals("123", actual.keySet().iterator().next());
		Assert.assertEquals(Integer.valueOf(123), actual.intKeys().get(0));
		
	}
	
	@Test
	public void testIntKey() {
		
		PackableTable table = new PackableTableImpl();
		table.put(123, "stringValue");
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		table.put("123", "newStringValue");
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		Assert.assertEquals(1, table.size());
		Assert.assertEquals("newStringValue", table.get(123).asString());
	}
	
	@Test
	public void testIntToStringKey() {
		
		PackableTable table = new PackableTableImpl();
		table.put(123, "stringValue");
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		table.put("abc", "newStringValue");
		Assert.assertEquals(PackableTableType.STRING_KEY, table.getType());
		Assert.assertEquals(2, table.size());
		Assert.assertEquals("stringValue", table.get("123").asString());
		Assert.assertEquals("newStringValue", table.get("abc").asString());
		
	}
	
	@Test
	public void testIntArray() {
		
		PackableTable table = new PackableTableImpl();
		Assert.assertNull(table.maxIntKey());
		Assert.assertTrue(table.keySet().isEmpty());
		Assert.assertTrue(table.intKeys().isEmpty());
		
		for (int i = 1; i != 11; ++i) {
			table.put(i, "value" + i);
		}
		
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		Assert.assertEquals(10, table.size());
		
		List<Integer> keys = table.intKeys();
		for (int i = 1, j = 0; i != 11; ++i, ++j) {
			Assert.assertEquals(Integer.valueOf(i), keys.get(j));
		}
		
		Assert.assertEquals(Integer.valueOf(10), table.maxIntKey());
		
	}
	
	@Test
	public void testIntToStringTable() throws IOException {
		
		PackableTable table = new PackableTableImpl();
		table.put("1", "one");
		table.put(2, "two");
		table.put("3", "three");
		
		Assert.assertEquals(PackableTableType.INT_KEY, table.getType());
		Assert.assertEquals(3, table.size());
		Assert.assertEquals(3, table.maxIntKey().intValue());
		
		byte[] msgpack = getAndCheck(table);
		PackableTable actual = PackableValueFactory.newTypedValue(msgpack);

		Assert.assertEquals(PackableTableType.INT_KEY, actual.getType());
		Assert.assertEquals(3, actual.size());
		Assert.assertEquals(3, actual.maxIntKey().intValue());

		table.put("abc", "abc");

		Assert.assertEquals(PackableTableType.STRING_KEY, table.getType());
		Assert.assertEquals(4, table.size());
		Assert.assertEquals(3, table.maxIntKey().intValue());
		
		msgpack = table.toByteArray();
		actual = PackableValueFactory.newTypedValue(msgpack);
		
		Assert.assertEquals(PackableTableType.STRING_KEY, actual.getType());
		Assert.assertEquals(4, actual.size());
		Assert.assertEquals(3, actual.maxIntKey().intValue());

	}
	
	@Test
	public void testStringGetters() {
		
		PackableTable table = new PackableTableImpl();
		table.put("abc", "123.0");
		
		Assert.assertEquals(new PackableNumberImpl(123.0), table.get("abc"));
		
		Assert.assertEquals(false, table.getBoolean("abc"));
		
		Assert.assertEquals(new PackableNumberImpl(123.0), table.getNumber("abc"));
		Assert.assertEquals(Long.valueOf(123L), table.getLong("abc"));
		Assert.assertEquals(Double.valueOf(123.0), table.getDouble("abc"));
		
		Assert.assertEquals(new PackableStringImpl("123.0"), table.getString("abc"));
		Assert.assertEquals("123.0", table.getStringUtf8("abc"));
		Assert.assertTrue(Arrays.equals("123.0".getBytes(), table.getBytes("abc", false)));
		
	}
	
	@Test
	public void testIntGetters() {
		
		PackableTable table = new PackableTableImpl();
		table.put(5, "123.0");
		
		Assert.assertEquals(new PackableNumberImpl(123.0), table.get(5));
		
		Assert.assertEquals(false, table.getBoolean(5));
		
		Assert.assertEquals(new PackableNumberImpl(123.0), table.getNumber(5));
		Assert.assertEquals(Long.valueOf(123L), table.getLong(5));
		Assert.assertEquals(Double.valueOf(123.0), table.getDouble(5));
		
		Assert.assertEquals(new PackableStringImpl("123.0"), table.getString(5));
		Assert.assertEquals("123.0", table.getStringUtf8(5));
		Assert.assertTrue(Arrays.equals("123.0".getBytes(), table.getBytes(5, false)));
		

		
	}
	
	@Test
	public void testInnerTable() throws IOException {

		PackableTable innerTable = new PackableTableImpl();
		innerTable.put("first", "Alex");
		
		PackableTable table = new PackableTableImpl();
		table.put("name", innerTable);
		
		//System.out.println(table.toJson());
		
		byte[] msgpack = getAndCheck(table);
		PackableTable actual = PackableValueFactory.newTypedValue(msgpack);
		
		// one entry in table guarantee the order
		Assert.assertEquals(table.toJson(), actual.toJson());
		
		PackableTable name = actual.getTable("name");
		Assert.assertNotNull(name);
		Assert.assertEquals("Alex", name.getString("first").asString());
		
	}
	
}
