package io.datty.msgpack.lite;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteNumberType;
import io.datty.msgpack.lite.LiteString;
import io.datty.msgpack.lite.LiteTable;
import io.datty.msgpack.lite.LiteValue;
import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.impl.LiteNumberImpl;
import io.datty.msgpack.lite.impl.LiteStringImpl;

/**
 * LiteFactoryTest
 * 
 * @author Alex Shvid
 *
 */

public class LiteFactoryTest {

	@Test
	public void testStringifyNull() {
		
		LiteValueFactory.newStringifyValue((String) null);
		
	}
	
	@Test
	public void testStringifyEmpty() {
		
		LiteValue<?> value = LiteValueFactory.newStringifyValue("");
		
		Assert.assertTrue(value instanceof LiteString);
		
		LiteString string = (LiteString) value;
		
		Assert.assertEquals("", string.asString());
	}
	
	@Test
	public void testStringifyZeroLong() {

		LiteValue<?> value = LiteValueFactory.newStringifyValue("0");
		
		Assert.assertTrue(value instanceof LiteNumber);
		
		LiteNumber val = (LiteNumber) value;
		
		Assert.assertEquals(LiteNumberType.LONG, val.getType());
		Assert.assertEquals(0, val.asLong());

	}
	
	@Test
	public void testStringifyZeroDouble() {

		LiteValue<?> value = LiteValueFactory.newStringifyValue("0.0");
		
		Assert.assertTrue(value instanceof LiteNumber);
		
		LiteNumber val = (LiteNumber) value;
		
		Assert.assertEquals(LiteNumberType.DOUBLE, val.getType());
		Assert.assertTrue(Math.abs(0.0 - val.asDouble()) < 0.00001);

	}
	
	@Test
	public void testStringifyLong() {

		LiteValue<?> value = LiteValueFactory.newStringifyValue("123456789");
		
		Assert.assertTrue(value instanceof LiteNumber);
		
		LiteNumber val = (LiteNumber) value;
		
		Assert.assertEquals(LiteNumberType.LONG, val.getType());
		Assert.assertEquals(123456789L, val.asLong());

	}
	
	
	@Test
	public void testStringifyString() {

		LiteValue<?> value = LiteValueFactory.newStringifyValue("abc");
		
		Assert.assertTrue(value instanceof LiteString);
		
		LiteString string = (LiteString) value;
		
		Assert.assertEquals("abc", string.asString());

	}

	@Test(expected=IllegalArgumentException.class)
	public void testNullExample() {
		LiteValueFactory.newValue((byte[]) null);
	}
	
	@Test
	public void testEmptyExample() {
		
		final byte[] example =  new byte[] {};
		
		LiteValue<?> value = LiteValueFactory.newValue(example);
		
		Assert.assertNull(value);
		
	}
	
	@Test
	public void testExample() {
		
		final byte[] example =  new byte[] {-125, -93, 97, 99, 99, -93, 49, 50, 51, -90, 108, 111, 103, 105, 110, 115, -9, -92, 110, 97, 109, 101, -92, 65, 108, 101, 120};
	
		LiteValue<?> value = LiteValueFactory.newValue(example);
		
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof LiteTable);
		
		LiteTable table = (LiteTable) value;
		
		Assert.assertEquals("123", table.get("acc").asString());
		Assert.assertEquals(new LiteStringImpl("123"), table.getString("acc"));
		Assert.assertEquals(new LiteNumberImpl(123), table.getNumber("acc"));
		
		Assert.assertEquals("Alex", table.get("name").asString());
		Assert.assertEquals(new LiteStringImpl("Alex"), table.getString("name"));
		
		Assert.assertEquals("-9", table.get("logins").asString());
		Assert.assertEquals(new LiteNumberImpl(-9), table.getNumber("logins"));
		
		//System.out.println(table);
		
	}
	
	
}
