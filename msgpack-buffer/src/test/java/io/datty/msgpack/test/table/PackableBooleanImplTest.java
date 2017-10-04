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

import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableBooleanImpl;



/**
 * PackableBooleanImplTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableBooleanImplTest extends AbstractPackableTest {

	@Test
	public void testNull() {

		PackableBooleanImpl bool = new PackableBooleanImpl(null);

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    PackableBooleanImpl actual = PackableValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testEmpty() {

		PackableBooleanImpl bool = new PackableBooleanImpl("");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    PackableBooleanImpl actual = PackableValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testFalse() {

		PackableBooleanImpl bool = new PackableBooleanImpl("false");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    PackableBooleanImpl actual = PackableValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testUnknown() {

		PackableBooleanImpl bool = new PackableBooleanImpl("unknown");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    PackableBooleanImpl actual = PackableValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
		
	@Test
	public void testTrue() {

		PackableBooleanImpl bool = new PackableBooleanImpl("true");

		Assert.assertEquals(true, bool.asBoolean());
    Assert.assertEquals("c3", bool.toHexString());
    Assert.assertEquals("c3", toHexString(bool));
		
    PackableBooleanImpl actual = PackableValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	
}
