package io.datty.msgpack.table;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableNumber;
import io.datty.msgpack.table.PackableNumberType;
import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableValue;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableNumberImpl;
import io.datty.msgpack.table.impl.PackableStringImpl;

/**
 * PackableFactoryTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableFactoryTest {

	@Test
	public void testStringifyNull() {
		
		PackableValueFactory.newStringifyValue((String) null);
		
	}
	
	@Test
	public void testStringifyEmpty() {
		
		PackableValue<?> value = PackableValueFactory.newStringifyValue("");
		
		Assert.assertTrue(value instanceof PackableString);
		
		PackableString string = (PackableString) value;
		
		Assert.assertEquals("", string.asString());
	}
	
	@Test
	public void testStringifyZeroLong() {

		PackableValue<?> value = PackableValueFactory.newStringifyValue("0");
		
		Assert.assertTrue(value instanceof PackableNumber);
		
		PackableNumber val = (PackableNumber) value;
		
		Assert.assertEquals(PackableNumberType.LONG, val.getType());
		Assert.assertEquals(0, val.asLong());

	}
	
	@Test
	public void testStringifyZeroDouble() {

		PackableValue<?> value = PackableValueFactory.newStringifyValue("0.0");
		
		Assert.assertTrue(value instanceof PackableNumber);
		
		PackableNumber val = (PackableNumber) value;
		
		Assert.assertEquals(PackableNumberType.DOUBLE, val.getType());
		Assert.assertTrue(Math.abs(0.0 - val.asDouble()) < 0.00001);

	}
	
	@Test
	public void testStringifyLong() {

		PackableValue<?> value = PackableValueFactory.newStringifyValue("123456789");
		
		Assert.assertTrue(value instanceof PackableNumber);
		
		PackableNumber val = (PackableNumber) value;
		
		Assert.assertEquals(PackableNumberType.LONG, val.getType());
		Assert.assertEquals(123456789L, val.asLong());

	}
	
	
	@Test
	public void testStringifyString() {

		PackableValue<?> value = PackableValueFactory.newStringifyValue("abc");
		
		Assert.assertTrue(value instanceof PackableString);
		
		PackableString string = (PackableString) value;
		
		Assert.assertEquals("abc", string.asString());

	}

	@Test(expected=IllegalArgumentException.class)
	public void testNullExample() {
		PackableValueFactory.newValue((byte[]) null);
	}
	
	@Test
	public void testEmptyExample() {
		
		final byte[] example =  new byte[] {};
		
		PackableValue<?> value = PackableValueFactory.newValue(example);
		
		Assert.assertNull(value);
		
	}
	
	@Test
	public void testExample() {
		
		final byte[] example =  new byte[] {-125, -93, 97, 99, 99, -93, 49, 50, 51, -90, 108, 111, 103, 105, 110, 115, -9, -92, 110, 97, 109, 101, -92, 65, 108, 101, 120};
	
		PackableValue<?> value = PackableValueFactory.newValue(example);
		
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof PackableTable);
		
		PackableTable table = (PackableTable) value;
		
		Assert.assertEquals("123", table.get("acc").asString());
		Assert.assertEquals(new PackableStringImpl("123"), table.getString("acc"));
		Assert.assertEquals(new PackableNumberImpl(123), table.getNumber("acc"));
		
		Assert.assertEquals("Alex", table.get("name").asString());
		Assert.assertEquals(new PackableStringImpl("Alex"), table.getString("name"));
		
		Assert.assertEquals("-9", table.get("logins").asString());
		Assert.assertEquals(new PackableNumberImpl(-9), table.getNumber("logins"));
		
		//System.out.println(table);
		
	}
	
	
}
