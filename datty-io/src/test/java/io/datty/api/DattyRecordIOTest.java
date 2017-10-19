/*
 * Copyright (C) 2016 Datty.io Authors
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
package io.datty.api;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * DattyRecordIOTest
 * 
 * @author Alex Shvid
 *
 */

public class DattyRecordIOTest extends AbstractDattyIOTest {

	@Test
	public void testEmpty() {
		
		DattyRecord rec = new DattyRecord();
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRecordIO.writeRecord(rec, sink);
		
		DattyRecord actual = DattyRecordIO.readRecord(sink);
		
		Assert.assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testNull() {
		
		DattyRecord rec = new DattyRecord();
		rec.put(minorKey, NullDattyValue.NULL);
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRecordIO.writeRecord(rec, sink);
		
		DattyRecord actual = DattyRecordIO.readRecord(sink);
		
		Assert.assertFalse(actual.isEmpty());
		Assert.assertEquals(1, actual.size());
		
		DattyValue actualValue = actual.get(minorKey);
		Assert.assertNotNull(actualValue);
		Assert.assertTrue(actualValue.isNull());
		
		Assert.assertEquals(NullDattyValue.NULL, actualValue);
	}
	
	@Test
	public void testOne() {
		
		ByteBuf value = Unpooled.wrappedBuffer("value".getBytes());
		
		DattyRecord rec = new DattyRecord();
		rec.put(minorKey, new ByteBufValue(value));
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRecordIO.writeRecord(rec, sink);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyRecord actual = DattyRecordIO.readRecord(sink);
		
		Assert.assertFalse(actual.isEmpty());
		Assert.assertEquals(1, actual.size());
		
		assertValue("value", actual, minorKey);
	}
	
	@Test
	public void testTwo() {
		
		ByteBuf value = Unpooled.wrappedBuffer("value".getBytes());
		ByteBuf value2 = Unpooled.wrappedBuffer("value2".getBytes());
		
		DattyRecord rec = new DattyRecord();
		rec.put(minorKey, new ByteBufValue(value));
		rec.put("minorKey2", new ByteBufValue(value2));
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRecordIO.writeRecord(rec, sink);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyRecord actual = DattyRecordIO.readRecord(sink);
		
		Assert.assertFalse(actual.isEmpty());
		Assert.assertEquals(2, actual.size());
		
		assertValue("value", actual, minorKey);
		assertValue("value2", actual, "minorKey2");
		
	}
	
	private void assertValue(String expected, DattyRecord actual, String key) {
		
		DattyValue actualValue = actual.get(key);
		Assert.assertNotNull(actualValue);
		
		byte[] actualValueBytes = actualValue.toByteArray();
		
		Assert.assertNotNull(actualValueBytes);
		
		Assert.assertEquals(expected, new String(actualValueBytes));
		
	}
	
}
