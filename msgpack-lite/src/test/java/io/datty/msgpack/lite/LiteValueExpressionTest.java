package io.datty.msgpack.lite;

import org.junit.Assert;
import org.junit.Test;

import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteTable;
import io.datty.msgpack.lite.LiteValueExpression;
import io.datty.msgpack.lite.impl.LiteNumberImpl;
import io.datty.msgpack.lite.impl.LiteStringImpl;
import io.datty.msgpack.lite.impl.LiteTableImpl;
import io.datty.msgpack.lite.impl.LiteValueExpressionImpl;



/**
 * LiteValueExpressionTest
 * 
 * @author Alex Shvid
 *
 */

public class LiteValueExpressionTest {

	@Test(expected=IllegalArgumentException.class)
	public void testNull() {
		
		new LiteValueExpressionImpl(null);
		
	}
	
	@Test
	public void testEmpty() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("");
		Assert.assertTrue(ve.isEmpty());
		
	}
	
	@Test
	public void testSingle() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("logins");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(1, ve.size());
		Assert.assertEquals("logins", ve.get(0));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testSingleIndex() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("[4]");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(1, ve.size());
		Assert.assertEquals("4", ve.get(0));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testTwo() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("name.first");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(2, ve.size());
		Assert.assertEquals("name", ve.get(0));
		Assert.assertEquals("first", ve.get(1));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testTwoWithIndex() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("name[1]");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(2, ve.size());
		Assert.assertEquals("name", ve.get(0));
		Assert.assertEquals("1", ve.get(1));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testComplex() {
		
		LiteValueExpression ve = new LiteValueExpressionImpl("educations[2].name");
		Assert.assertFalse(ve.isEmpty());
		Assert.assertEquals(3, ve.size());
		Assert.assertEquals("educations", ve.get(0));
		Assert.assertEquals("2", ve.get(1));
		Assert.assertEquals("name", ve.get(2));
		
		//System.out.println(ve);
		
	}
	
	@Test
	public void testSimple() {
		
		LiteTable table = new LiteTableImpl();
		table.put("name", "John");
		
		LiteValueExpression ve = new LiteValueExpressionImpl("name");
		
		Assert.assertEquals(new LiteStringImpl("John"), table.get(ve));
		Assert.assertEquals(new LiteStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testInner() {
		
		LiteTable innerTable = new LiteTableImpl();
		innerTable.put("first", "John");
		innerTable.put("last", "Dow");
		
		LiteTable table = new LiteTableImpl();
		table.put("name", innerTable);
		
		LiteValueExpression ve = new LiteValueExpressionImpl("name.first");
		
		Assert.assertEquals(new LiteStringImpl("John"), table.get(ve));
		Assert.assertEquals(new LiteStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testInnerWithIndex() {
		
		LiteTable innerTable = new LiteTableImpl();
		innerTable.put(1, "John");
		innerTable.put(2, "Dow");
		
		LiteTable table = new LiteTableImpl();
		table.put("name", innerTable);
		
		LiteValueExpression ve = new LiteValueExpressionImpl("name[1]");
		
		Assert.assertEquals(new LiteStringImpl("John"), table.get(ve));
		Assert.assertEquals(new LiteStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testEmptyPut() {
		
		LiteTable table = new LiteTableImpl();
		LiteValueExpression ve = new LiteValueExpressionImpl("name");

		table.put(ve, "John");
		
		Assert.assertEquals(new LiteStringImpl("John"), table.get(ve));
		Assert.assertEquals(new LiteStringImpl("John"), table.getString(ve));
	}
	
	@Test
	public void testEmptyInnerPut() {
		
		LiteTable table = new LiteTableImpl();
		LiteValueExpression ve = new LiteValueExpressionImpl("name.first");

		table.put(ve, "John");
		
		Assert.assertEquals(new LiteStringImpl("John"), table.get(ve));
		Assert.assertEquals(new LiteStringImpl("John"), table.getString(ve));
		
	}
	
	@Test
	public void testIncrement() {
		
		LiteTable table = new LiteTableImpl();
		LiteValueExpression ve = new LiteValueExpressionImpl("logins");

		LiteNumber number = table.getNumber(ve);
		if (number == null) {
			number = new LiteNumberImpl(0l);
		}
		number = number.add(new LiteNumberImpl(1));
		
		table.put(ve, number);
		
		Assert.assertEquals(1, table.getNumber(ve).asLong());
		
	}
	
	@Test
	public void testDecrement() {
		
		LiteTable table = new LiteTableImpl();
		LiteValueExpression ve = new LiteValueExpressionImpl("logins");

		LiteNumber number = table.getNumber(ve);
		if (number == null) {
			number = new LiteNumberImpl(0l);
		}
		number = number.subtract(new LiteNumberImpl(1.0));
		
		table.put(ve, number);
		
		Assert.assertEquals(-1, table.getNumber(ve).asLong());
		
	}

	
}
