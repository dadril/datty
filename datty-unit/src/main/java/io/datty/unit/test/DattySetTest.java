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
package io.datty.unit.test;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.operation.Clear;
import io.datty.api.operation.Push;
import io.datty.api.operation.Scan;
import io.datty.api.operation.Size;
import io.datty.api.result.RecordResult;
import io.netty.buffer.ByteBuf;

/**
 * DattySetTest
 * 
 * @author Alex Shvid
 *
 */

public class DattySetTest extends AbstractDattyUnitTest {

	private void clear() {
		dattyManager.getDatty().execute(new Clear(SET_NAME)).toBlocking().single();
	}
	
	@Test
	public void testEmpty() {
		
		clear();

		List<RecordResult> all = toList(dattyManager.getDatty().execute(new Scan(SET_NAME)).toBlocking().toIterable());
		Assert.assertTrue(all.isEmpty());
		
		long count = dattyManager.getDatty().execute(new Size(SET_NAME)).toBlocking().single().count();
		
		Assert.assertEquals(0, count);
		
		
		
	}
	
	@Test
	public void testOne() {

		clear();
		
		String majorKey = UUID.randomUUID().toString();
		
		dattyManager.getDatty().execute(new Push(SET_NAME, majorKey).addValue(minorKey, value())).toBlocking().value();
		
		List<RecordResult> all = toList(dattyManager.getDatty().execute(new Scan(SET_NAME)).toBlocking().toIterable());
		Assert.assertEquals(1, all.size());
		
		RecordResult result = all.get(0);
		Assert.assertEquals(majorKey, result.getMajorKey());

		ByteBuf bb = result.get(minorKey).asByteBuf();
		Assert.assertEquals(value, bb);
		
		long count = dattyManager.getDatty().execute(new Size(SET_NAME)).toBlocking().single().count();
		
		Assert.assertEquals(1, count);
		
	}
	
	@Test
	public void testTwo() {

		clear();
		
		String majorKey1 = UUID.randomUUID().toString();
		String majorKey2 = UUID.randomUUID().toString();
		
		dattyManager.getDatty().execute(new Push(SET_NAME, majorKey1).addValue(minorKey, value())).toBlocking().value();
		dattyManager.getDatty().execute(new Push(SET_NAME, majorKey2).addValue(minorKey, value())).toBlocking().value();
		
		long count = dattyManager.getDatty().execute(new Size(SET_NAME)).toBlocking().single().count();
		
		Assert.assertEquals(2, count);
		
	}
	
	
}
