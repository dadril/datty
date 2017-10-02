package io.datty.msgpack.table;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.table.util.PackableStringifyUtil;
import io.datty.msgpack.table.util.PackableStringifyUtil.NumberType;


/**
 * PackableStringifyUtilTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableStringifyUtilTest {

	@Test
	public void testNull() {
		
		Assert.assertEquals(NumberType.NAN, PackableStringifyUtil.detectNumber(null));
		
	}
	
	@Test
	public void testEmpty() {
		
		Assert.assertEquals(NumberType.NAN, PackableStringifyUtil.detectNumber(""));
		
	}
	
	@Test
	public void testLong() {
		
		Assert.assertEquals(NumberType.LONG, PackableStringifyUtil.detectNumber("123456789"));
		
	}
	
	@Test
	public void testMinusLong() {
		
		Assert.assertEquals(NumberType.LONG, PackableStringifyUtil.detectNumber("-123456789"));
		
	}
	
	@Test
	public void testInvalidMinusLong() {
		
		Assert.assertEquals(NumberType.NAN, PackableStringifyUtil.detectNumber("123-456789"));
		
	}
	
	@Test
	public void testDouble() {
		
		Assert.assertEquals(NumberType.DOUBLE, PackableStringifyUtil.detectNumber("123456789.0"));
		
	}
	
	@Test
	public void testDoubleE() {
		
		Assert.assertEquals(NumberType.DOUBLE, PackableStringifyUtil.detectNumber("1.23456789E8"));
		
	}
	
	@Test
	public void testInvalidDoubleE() {
		
		Assert.assertEquals(NumberType.NAN, PackableStringifyUtil.detectNumber("1.2345E789E8"));
		
	}
	
	@Test
	public void testInvalidDouble() {
		
		Assert.assertEquals(NumberType.NAN, PackableStringifyUtil.detectNumber("12345.6789.0"));
		
	}
	
	@Test
	public void testMinusDouble() {
		
		Assert.assertEquals(NumberType.DOUBLE, PackableStringifyUtil.detectNumber("-123456789.0"));
		
	}
	
	@Test
	public void testMinusDoubleE() {
		
		Assert.assertEquals(NumberType.DOUBLE, PackableStringifyUtil.detectNumber("-1.23456789E8"));
		
	}
	
	@Test
	public void testHexNull() {
		
		String hex = PackableStringifyUtil.toHex(null);
		
		Assert.assertNull(hex);
	}
	
	@Test
	public void testHexEmpty() {
		
		String hex = PackableStringifyUtil.toHex(new byte[] {});
		
		Assert.assertEquals("", hex);
		
	}
	
	@Test
	public void testHexSingle() {
		
		String hex = PackableStringifyUtil.toHex(new byte[] { 1 });
		
		Assert.assertEquals("01", hex);
		
	}
	
	@Test
	public void testHexAll() {
		
		byte[] all = new byte[256];
		for (int i = 0; i != 256; ++i) {
			all[i] = (byte) i;
		}
		
		String hex = PackableStringifyUtil.toHex(all);
		
		Assert.assertEquals("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedfe0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff", hex);
		
	}
	
}
