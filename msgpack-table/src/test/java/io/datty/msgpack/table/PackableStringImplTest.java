package io.datty.msgpack.table;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.impl.PackableStringImpl;



/**
 * PackableStringImplTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableStringImplTest extends AbstractPackableTest {

	@Test(expected=IllegalArgumentException.class)
	public void testNullString() {
		
		new PackableStringImpl((String)null);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullBytes() {
		
		new PackableStringImpl((byte[])null, false);
		
	}
	
	@Test
	public void testEmptyString() {
		
		PackableString string = new PackableStringImpl("");
		
		Assert.assertEquals("", string.asString());
		Assert.assertEquals("", string.toUtf8());
		Assert.assertTrue(Arrays.equals(new byte[0], string.getBytes(true)));
		
    Assert.assertEquals("a0", string.toHexString());
    Assert.assertEquals("a0", toHexString(string));
    
    PackableStringImpl actual = PackableValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
	}
	
	@Test
	public void testString() {
		
		PackableString string = new PackableStringImpl("hello");
		
		Assert.assertEquals("hello", string.asString());
		Assert.assertEquals("hello", string.toUtf8());
		Assert.assertTrue(Arrays.equals("hello".getBytes(), string.getBytes(true)));
		
    Assert.assertEquals("a568656c6c6f", string.toHexString());
    Assert.assertEquals("a568656c6c6f", toHexString(string));
    
    PackableStringImpl actual = PackableValueFactory.newTypedValue(string.toByteArray());
    Assert.assertEquals(string, actual);
		
	}
	
}
