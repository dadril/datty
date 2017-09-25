package io.datty.msgpack.lite;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteNumberType;
import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.impl.LiteNumberImpl;
import io.datty.msgpack.lite.support.LiteNumberFormatException;

/**
 * LiteNumberImplTest
 * 
 * @author Alex Shvid
 *
 */

public class LiteNumberImplTest extends AbstractLiteTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {

		new LiteNumberImpl(null);

	}

	@Test(expected = LiteNumberFormatException.class)
	public void testEmpty() {

		new LiteNumberImpl("");

	}

	@Test(expected = LiteNumberFormatException.class)
	public void testNAN() {

		new LiteNumberImpl("abc");

	}
	
	@Test
	public void testZeroLong() {

		LiteNumberImpl number = new LiteNumberImpl("0");

		Assert.assertEquals(LiteNumberType.LONG, number.getType());
		Assert.assertEquals(0L, number.asLong());
		Assert.assertTrue(Math.abs(0.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("0", number.asString());
		
    Assert.assertEquals("00", number.toHexString());
    Assert.assertEquals("00", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testZeroDouble() {

		LiteNumberImpl number = new LiteNumberImpl("0.0");

		Assert.assertEquals(LiteNumberType.DOUBLE, number.getType());
		Assert.assertEquals(0L, number.asLong());
		Assert.assertTrue(Math.abs(0.0 - number.asDouble()) < 0.001);		
		Assert.assertEquals("0.0", number.asString());
		
    Assert.assertEquals("cb0000000000000000", number.toHexString());
    Assert.assertEquals("cb0000000000000000", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testLong() {

		LiteNumberImpl number = new LiteNumberImpl("123456789");

		Assert.assertEquals(LiteNumberType.LONG, number.getType());
		Assert.assertEquals(123456789L, number.asLong());
		Assert.assertTrue(Math.abs(123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("123456789", number.asString());
		
    Assert.assertEquals("ce075bcd15", number.toHexString());
    Assert.assertEquals("ce075bcd15", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testDouble() {

		LiteNumberImpl number = new LiteNumberImpl("123456789.0");

		Assert.assertEquals(LiteNumberType.DOUBLE, number.getType());
		Assert.assertEquals(123456789L, number.asLong());
		Assert.assertTrue(Math.abs(123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("1.23456789E8", number.asString());
		
    Assert.assertEquals("cb419d6f3454000000", number.toHexString());
    Assert.assertEquals("cb419d6f3454000000", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinusLong() {

		LiteNumberImpl number = new LiteNumberImpl("-123456789");

		Assert.assertEquals(LiteNumberType.LONG, number.getType());
		Assert.assertEquals(-123456789L, number.asLong());
		Assert.assertTrue(Math.abs(-123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("-123456789", number.asString());
		
    Assert.assertEquals("d2f8a432eb", number.toHexString());
    Assert.assertEquals("d2f8a432eb", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinusDouble() {

		LiteNumberImpl number = new LiteNumberImpl("-123456789.0");

		Assert.assertEquals(LiteNumberType.DOUBLE, number.getType());
		Assert.assertEquals(-123456789L, number.asLong());
		Assert.assertTrue(Math.abs(-123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("-1.23456789E8", number.asString());
		
    Assert.assertEquals("cbc19d6f3454000000", number.toHexString());
    Assert.assertEquals("cbc19d6f3454000000", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMaxLong() {

		LiteNumberImpl number = new LiteNumberImpl(Long.MAX_VALUE);

		Assert.assertEquals(LiteNumberType.LONG, number.getType());
		Assert.assertEquals(Long.MAX_VALUE, number.asLong());
		Assert.assertTrue(Math.abs((double)Long.MAX_VALUE - number.asDouble()) < 0.001);
		Assert.assertEquals(Long.toString(Long.MAX_VALUE), number.asString());
		
    Assert.assertEquals("cf7fffffffffffffff", number.toHexString());
    Assert.assertEquals("cf7fffffffffffffff", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinLong() {

		LiteNumberImpl number = new LiteNumberImpl(Long.MIN_VALUE);

		Assert.assertEquals(LiteNumberType.LONG, number.getType());
		Assert.assertEquals(Long.MIN_VALUE, number.asLong());
		Assert.assertTrue(Math.abs((double)Long.MIN_VALUE - number.asDouble()) < 0.001);
		Assert.assertEquals(Long.toString(Long.MIN_VALUE), number.asString());
		
    Assert.assertEquals("d38000000000000000", number.toHexString());
    Assert.assertEquals("d38000000000000000", toHexString(number));
    
    LiteNumberImpl actual = LiteValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}

	@Test
	public void testAdd() {
		
		LiteNumber number = new LiteNumberImpl(123L);
		
		Assert.assertEquals(123L + 555L, number.add(555L).asLong());
		Assert.assertEquals(123L + 555L, number.add(new LiteNumberImpl(555L)).asLong());
		Assert.assertEquals(126L, number.add(3.0).asLong());
		Assert.assertEquals(126L, number.add(new LiteNumberImpl(3.0)).asLong());
		
		number = new LiteNumberImpl(123.0);
		
		Assert.assertEquals(123L + 555L, number.add(555L).asLong());
		Assert.assertEquals(123L + 555L, number.add(new LiteNumberImpl(555L)).asLong());
		Assert.assertEquals(126L, number.add(3.0).asLong());
		Assert.assertEquals(126L, number.add(new LiteNumberImpl(3.0)).asLong());
	}
	
	@Test
	public void testSubtruct() {
		
		LiteNumber number = new LiteNumberImpl(123L);
		
		Assert.assertEquals(123L - 555L, number.subtract(555L).asLong());
		Assert.assertEquals(123L - 555L, number.subtract(new LiteNumberImpl(555L)).asLong());
		Assert.assertEquals(120L, number.subtract(3.0).asLong());
		Assert.assertEquals(120L, number.subtract(new LiteNumberImpl(3.0)).asLong());
		
		number = new LiteNumberImpl(123.0);
		
		Assert.assertEquals(123L - 555L, number.subtract(555L).asLong());
		Assert.assertEquals(123L - 555L, number.subtract(new LiteNumberImpl(555L)).asLong());
		Assert.assertEquals(120L, number.subtract(3.0).asLong());
		Assert.assertEquals(120L, number.subtract(new LiteNumberImpl(3.0)).asLong());
	}
}
