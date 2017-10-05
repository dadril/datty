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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableValueFactory;



/**
 * PackableStringTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableStringTest extends AbstractPackableTest {

	@Test(expected=IllegalArgumentException.class)
	public void testNullString() {
		
		new PackableString((String)null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullBytes() {
		
		new PackableString((byte[])null, false);
		
	}
	
	@Test
	public void testEmptyString() {
		
		PackableString string = new PackableString("");
		
		Assert.assertEquals("", string.asString());
		Assert.assertEquals("", string.toUtf8());
		Assert.assertTrue(Arrays.equals(new byte[0], string.getBytes(true)));
		
    Assert.assertEquals("a0", string.toHexString());
    Assert.assertEquals("a0", toHexString(string));
    
    PackableString actual = PackableValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
	}
	
	@Test
	public void testString() {
		
		PackableString string = new PackableString("hello");
		
		Assert.assertEquals("hello", string.asString());
		Assert.assertEquals("hello", string.toUtf8());
		Assert.assertTrue(Arrays.equals("hello".getBytes(), string.getBytes(true)));
		
    Assert.assertEquals("a568656c6c6f", string.toHexString());
    Assert.assertEquals("a568656c6c6f", toHexString(string));
    
    PackableString actual = PackableValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
		
	}
	
}
