/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.api.operation;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.AbstractDattyIOTest;
import io.datty.api.DattyOperation;
import io.datty.util.DattyIO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AbstractRecordOperationIOTest
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
public abstract class AbstractRecordOperationIOTest<O extends AbstractRecordOperation> extends AbstractDattyIOTest {

	abstract O newOperation();
	
	@Test
	public void testEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		O empty = newOperation();
		
		DattyIO.writeOperation(empty, sink, numeric);
		
		DattyOperation read = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(empty.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		O actual = (O) read;
		
		Assert.assertNull(actual.getSetName());
		Assert.assertNull(actual.getSuperKey());
		Assert.assertNull(actual.getMajorKey());
		Assert.assertFalse(actual.isAllMinorKeys());
		Assert.assertTrue(actual.getMinorKeys().isEmpty());
		Assert.assertFalse(actual.hasTimeoutMillis());
		Assert.assertFalse(actual.hasFallback());
		Assert.assertFalse(actual.hasUpstreamContext());
		
	}
	
	@Test
	public void testNonEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		O operation = newOperation();
		
		operation.setSetName(setName);
		operation.setSuperKey(superKey);
		operation.setMajorKey(majorKey);
		operation.addMinorKey(minorKey);
		operation.setTimeoutMillis(timeoutMillis);
		
		DattyIO.writeOperation(operation, sink, numeric);
		
		// System.out.println("bytes = " + Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyOperation read = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(operation.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		O actual = (O) read;
		
		Assert.assertEquals(setName, actual.getSetName());
		Assert.assertEquals(superKey, actual.getSuperKey());
		Assert.assertFalse(actual.isAllMinorKeys());
		Assert.assertEquals(1, actual.getMinorKeys().size());
		Assert.assertEquals(minorKey, actual.getMinorKeys().iterator().next());
		Assert.assertEquals(timeoutMillis, actual.getTimeoutMillis());
		Assert.assertFalse(actual.hasFallback());
		Assert.assertFalse(actual.hasUpstreamContext());
		
		
	}
	
}
