package io.datty.msgpack.table;

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
