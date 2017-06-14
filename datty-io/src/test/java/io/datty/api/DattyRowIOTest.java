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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * DattyRowIOTest
 * 
 * @author Alex Shvid
 *
 */

public class DattyRowIOTest extends AbstractDattyIOTest {

	@Test
	public void testEmpty() {
		
		DattyRow row = new DattyRow();
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRowIO.writeRow(row, sink);
		
		DattyRow actual = DattyRowIO.readRow(sink);
		
		Assert.assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testOne() {
		
		ByteBuf value = Unpooled.wrappedBuffer("value".getBytes());
		
		DattyRow row = new DattyRow();
		row.putValue(minorKey, value, true);
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRowIO.writeRow(row, sink);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyRow actual = DattyRowIO.readRow(sink);
		
		Assert.assertFalse(actual.isEmpty());
		Assert.assertEquals(1, actual.size());
		
		assertValue("value", actual, minorKey);
	}
	
	@Test
	public void testTwo() {
		
		ByteBuf value = Unpooled.wrappedBuffer("value".getBytes());
		ByteBuf value2 = Unpooled.wrappedBuffer("value2".getBytes());
		
		DattyRow row = new DattyRow();
		row.putValue(minorKey, value, true);
		row.putValue("minorKey2", value2, true);
		
		ByteBuf sink = Unpooled.buffer();
		
		sink = DattyRowIO.writeRow(row, sink);
		
		System.out.println(Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyRow actual = DattyRowIO.readRow(sink);
		
		Assert.assertFalse(actual.isEmpty());
		Assert.assertEquals(2, actual.size());
		
		assertValue("value", actual, minorKey);
		assertValue("value2", actual, "minorKey2");
		
	}
	
	private void assertValue(String expected, DattyRow actual, String key) {
		
		ByteBuf actualValue = actual.get(key);
		Assert.assertNotNull(actualValue);
		
		byte[] actualValueBytes = ByteBufUtil.getBytes(actualValue);
		
		Assert.assertEquals(expected, new String(actualValueBytes));
		
	}
	
}
