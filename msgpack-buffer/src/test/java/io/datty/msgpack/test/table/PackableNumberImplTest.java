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

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableNumber;
import io.datty.msgpack.table.PackableNumberType;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableNumberImpl;
import io.datty.msgpack.table.support.PackableNumberFormatException;

/**
 * PackableNumberImplTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableNumberImplTest extends AbstractPackableTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {

		new PackableNumberImpl(null);

	}

	@Test(expected = PackableNumberFormatException.class)
	public void testEmpty() {

		new PackableNumberImpl("");

	}

	@Test(expected = PackableNumberFormatException.class)
	public void testNAN() {

		new PackableNumberImpl("abc");

	}
	
	@Test
	public void testZeroLong() {

		PackableNumberImpl number = new PackableNumberImpl("0");

		Assert.assertEquals(PackableNumberType.LONG, number.getType());
		Assert.assertEquals(0L, number.asLong());
		Assert.assertTrue(Math.abs(0.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("0", number.asString());
		
    Assert.assertEquals("00", number.toHexString());
    Assert.assertEquals("00", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testZeroDouble() {

		PackableNumberImpl number = new PackableNumberImpl("0.0");

		Assert.assertEquals(PackableNumberType.DOUBLE, number.getType());
		Assert.assertEquals(0L, number.asLong());
		Assert.assertTrue(Math.abs(0.0 - number.asDouble()) < 0.001);		
		Assert.assertEquals("0.0", number.asString());
		
    Assert.assertEquals("cb0000000000000000", number.toHexString());
    Assert.assertEquals("cb0000000000000000", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testLong() {

		PackableNumberImpl number = new PackableNumberImpl("123456789");

		Assert.assertEquals(PackableNumberType.LONG, number.getType());
		Assert.assertEquals(123456789L, number.asLong());
		Assert.assertTrue(Math.abs(123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("123456789", number.asString());
		
    Assert.assertEquals("ce075bcd15", number.toHexString());
    Assert.assertEquals("ce075bcd15", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testDouble() {

		PackableNumberImpl number = new PackableNumberImpl("123456789.0");

		Assert.assertEquals(PackableNumberType.DOUBLE, number.getType());
		Assert.assertEquals(123456789L, number.asLong());
		Assert.assertTrue(Math.abs(123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("1.23456789E8", number.asString());
		
    Assert.assertEquals("cb419d6f3454000000", number.toHexString());
    Assert.assertEquals("cb419d6f3454000000", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinusLong() {

		PackableNumberImpl number = new PackableNumberImpl("-123456789");

		Assert.assertEquals(PackableNumberType.LONG, number.getType());
		Assert.assertEquals(-123456789L, number.asLong());
		Assert.assertTrue(Math.abs(-123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("-123456789", number.asString());
		
    Assert.assertEquals("d2f8a432eb", number.toHexString());
    Assert.assertEquals("d2f8a432eb", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinusDouble() {

		PackableNumberImpl number = new PackableNumberImpl("-123456789.0");

		Assert.assertEquals(PackableNumberType.DOUBLE, number.getType());
		Assert.assertEquals(-123456789L, number.asLong());
		Assert.assertTrue(Math.abs(-123456789.0 - number.asDouble()) < 0.001);
		Assert.assertEquals("-1.23456789E8", number.asString());
		
    Assert.assertEquals("cbc19d6f3454000000", number.toHexString());
    Assert.assertEquals("cbc19d6f3454000000", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMaxLong() {

		PackableNumberImpl number = new PackableNumberImpl(Long.MAX_VALUE);

		Assert.assertEquals(PackableNumberType.LONG, number.getType());
		Assert.assertEquals(Long.MAX_VALUE, number.asLong());
		Assert.assertTrue(Math.abs((double)Long.MAX_VALUE - number.asDouble()) < 0.001);
		Assert.assertEquals(Long.toString(Long.MAX_VALUE), number.asString());
		
    Assert.assertEquals("cf7fffffffffffffff", number.toHexString());
    Assert.assertEquals("cf7fffffffffffffff", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}
	
	@Test
	public void testMinLong() {

		PackableNumberImpl number = new PackableNumberImpl(Long.MIN_VALUE);

		Assert.assertEquals(PackableNumberType.LONG, number.getType());
		Assert.assertEquals(Long.MIN_VALUE, number.asLong());
		Assert.assertTrue(Math.abs((double)Long.MIN_VALUE - number.asDouble()) < 0.001);
		Assert.assertEquals(Long.toString(Long.MIN_VALUE), number.asString());
		
    Assert.assertEquals("d38000000000000000", number.toHexString());
    Assert.assertEquals("d38000000000000000", toHexString(number));
    
    PackableNumberImpl actual = PackableValueFactory.newTypedValue(number.toByteArray());
    Assert.assertEquals(number, actual);
	}

	@Test
	public void testAdd() {
		
		PackableNumber number = new PackableNumberImpl(123L);
		
		Assert.assertEquals(123L + 555L, number.add(555L).asLong());
		Assert.assertEquals(123L + 555L, number.add(new PackableNumberImpl(555L)).asLong());
		Assert.assertEquals(126L, number.add(3.0).asLong());
		Assert.assertEquals(126L, number.add(new PackableNumberImpl(3.0)).asLong());
		
		number = new PackableNumberImpl(123.0);
		
		Assert.assertEquals(123L + 555L, number.add(555L).asLong());
		Assert.assertEquals(123L + 555L, number.add(new PackableNumberImpl(555L)).asLong());
		Assert.assertEquals(126L, number.add(3.0).asLong());
		Assert.assertEquals(126L, number.add(new PackableNumberImpl(3.0)).asLong());
	}
	
	@Test
	public void testSubtruct() {
		
		PackableNumber number = new PackableNumberImpl(123L);
		
		Assert.assertEquals(123L - 555L, number.subtract(555L).asLong());
		Assert.assertEquals(123L - 555L, number.subtract(new PackableNumberImpl(555L)).asLong());
		Assert.assertEquals(120L, number.subtract(3.0).asLong());
		Assert.assertEquals(120L, number.subtract(new PackableNumberImpl(3.0)).asLong());
		
		number = new PackableNumberImpl(123.0);
		
		Assert.assertEquals(123L - 555L, number.subtract(555L).asLong());
		Assert.assertEquals(123L - 555L, number.subtract(new PackableNumberImpl(555L)).asLong());
		Assert.assertEquals(120L, number.subtract(3.0).asLong());
		Assert.assertEquals(120L, number.subtract(new PackableNumberImpl(3.0)).asLong());
	}
}
