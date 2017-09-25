package io.datty.msgpack.lite;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteString;
import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.impl.LiteStringImpl;



/**
 * LiteStringImplTest
 * 
 * @author Alex Shvid
 *
 */

public class LiteStringImplTest extends AbstractLiteTest {

	@Test(expected=IllegalArgumentException.class)
	public void testNullString() {
		
		new LiteStringImpl((String)null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullBytes() {
		
		new LiteStringImpl((byte[])null, false);
		
	}
	
	@Test
	public void testEmptyString() {
		
		LiteString string = new LiteStringImpl("");
		
		Assert.assertEquals("", string.asString());
		Assert.assertEquals("", string.toUtf8());
		Assert.assertTrue(Arrays.equals(new byte[0], string.getBytes(true)));
		
    Assert.assertEquals("a0", string.toHexString());
    Assert.assertEquals("a0", toHexString(string));
    
    LiteStringImpl actual = LiteValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
	}
	
	@Test
	public void testString() {
		
		LiteString string = new LiteStringImpl("hello");
		
		Assert.assertEquals("hello", string.asString());
		Assert.assertEquals("hello", string.toUtf8());
		Assert.assertTrue(Arrays.equals("hello".getBytes(), string.getBytes(true)));
		
    Assert.assertEquals("a568656c6c6f", string.toHexString());
    Assert.assertEquals("a568656c6c6f", toHexString(string));
    
    LiteStringImpl actual = LiteValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
		
	}
	
}
