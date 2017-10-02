package io.datty.msgpack.lite;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.PackableNumber;
import io.datty.msgpack.lite.PackableTable;
import io.datty.msgpack.lite.PackableValueExpression;
import io.datty.msgpack.lite.impl.PackableNumberImpl;
import io.datty.msgpack.lite.impl.PackableStringImpl;
import io.datty.msgpack.lite.impl.PackableTableImpl;
import io.datty.msgpack.lite.impl.PackableValueExpressionImpl;



/**
 * PackableValueExpressionTest
 * 
 * @author Alex Shvid
 *
 */

public class PackableValueExpressionTest {

	@Test(expected=IllegalArgumentException.class)
	public void testNull() {
		
		new PackableValueExpressionImpl(null);
		
	}
	
	@Test
	public void testEmpty() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("");
		Assert.assertTrue(ve.isEmpty());
		
	}
	
	@Test
	public void testSingle() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("logins");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(1, ve.size());
		Assert.assertEquals("logins", ve.get(0));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testSingleIndex() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("[4]");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(1, ve.size());
		Assert.assertEquals("4", ve.get(0));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testTwo() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("name.first");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(2, ve.size());
		Assert.assertEquals("name", ve.get(0));
		Assert.assertEquals("first", ve.get(1));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testTwoWithIndex() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("name[1]");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(2, ve.size());
		Assert.assertEquals("name", ve.get(0));
		Assert.assertEquals("1", ve.get(1));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testComplex() {
		
		PackableValueExpression ve = new PackableValueExpressionImpl("educations[2].name");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(3, ve.size());
		Assert.assertEquals("educations", ve.get(0));
		Assert.assertEquals("2", ve.get(1));
		Assert.assertEquals("name", ve.get(2));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testSimple() {
		
		PackableTable table = new PackableTableImpl();
		table.put("name", "John");
		
		PackableValueExpression ve = new PackableValueExpressionImpl("name");
		
		Assert.assertEquals(new PackableStringImpl("John"), table.get(ve));
		Assert.assertEquals(new PackableStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testInner() {
		
		PackableTable innerTable = new PackableTableImpl();
		innerTable.put("first", "John");
		innerTable.put("last", "Dow");
		
		PackableTable table = new PackableTableImpl();
		table.put("name", innerTable);
		
		PackableValueExpression ve = new PackableValueExpressionImpl("name.first");
		
		Assert.assertEquals(new PackableStringImpl("John"), table.get(ve));
		Assert.assertEquals(new PackableStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testInnerWithIndex() {
		
		PackableTable innerTable = new PackableTableImpl();
		innerTable.put(1, "John");
		innerTable.put(2, "Dow");
		
		PackableTable table = new PackableTableImpl();
		table.put("name", innerTable);
		
		PackableValueExpression ve = new PackableValueExpressionImpl("name[1]");
		
		Assert.assertEquals(new PackableStringImpl("John"), table.get(ve));
		Assert.assertEquals(new PackableStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testEmptyPut() {
		
		PackableTable table = new PackableTableImpl();
		PackableValueExpression ve = new PackableValueExpressionImpl("name");

		table.put(ve, "John");
		
		Assert.assertEquals(new PackableStringImpl("John"), table.get(ve));
		Assert.assertEquals(new PackableStringImpl("John"), table.getString(ve));
	}
	
	@Test
	public void testEmptyInnerPut() {
		
		PackableTable table = new PackableTableImpl();
		PackableValueExpression ve = new PackableValueExpressionImpl("name.first");

		table.put(ve, "John");
		
		Assert.assertEquals(new PackableStringImpl("John"), table.get(ve));
		Assert.assertEquals(new PackableStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testIncrement() {
		
		PackableTable table = new PackableTableImpl();
		PackableValueExpression ve = new PackableValueExpressionImpl("logins");

		PackableNumber number = table.getNumber(ve);
		if (number == null) {
			number = new PackableNumberImpl(0l);
		}
		number = number.add(new PackableNumberImpl(1));
		
		table.put(ve, number);
		
		Assert.assertEquals(1, table.getNumber(ve).asLong());
		
	}
	
	@Test
	public void testDecrement() {
		
		PackableTable table = new PackableTableImpl();
		PackableValueExpression ve = new PackableValueExpressionImpl("logins");

		PackableNumber number = table.getNumber(ve);
		if (number == null) {
			number = new PackableNumberImpl(0l);
		}
		number = number.subtract(new PackableNumberImpl(1.0));
		
		table.put(ve, number);
		
		Assert.assertEquals(-1, table.getNumber(ve).asLong());
		
	}

	
}
