package io.datty.msgpack.lite;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.impl.LiteBooleanImpl;



/**
 * LiteBooleanImplTest
 * 
 * @author Alex Shvid
 *
 */

public class LiteBooleanImplTest extends AbstractLiteTest {

	@Test
	public void testNull() {

		LiteBooleanImpl bool = new LiteBooleanImpl(null);

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    LiteBooleanImpl actual = LiteValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testEmpty() {

		LiteBooleanImpl bool = new LiteBooleanImpl("");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    LiteBooleanImpl actual = LiteValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testFalse() {

		LiteBooleanImpl bool = new LiteBooleanImpl("false");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    LiteBooleanImpl actual = LiteValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	@Test
	public void testUnknown() {

		LiteBooleanImpl bool = new LiteBooleanImpl("unknown");

		Assert.assertEquals(false, bool.asBoolean());
    Assert.assertEquals("c2", bool.toHexString());
    Assert.assertEquals("c2", toHexString(bool));
    
    LiteBooleanImpl actual = LiteValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
		
	@Test
	public void testTrue() {

		LiteBooleanImpl bool = new LiteBooleanImpl("true");

		Assert.assertEquals(true, bool.asBoolean());
    Assert.assertEquals("c3", bool.toHexString());
    Assert.assertEquals("c3", toHexString(bool));
		
    LiteBooleanImpl actual = LiteValueFactory.newTypedValue(bool.toByteArray());
    Assert.assertEquals(bool, actual);
	}
	
	
}
