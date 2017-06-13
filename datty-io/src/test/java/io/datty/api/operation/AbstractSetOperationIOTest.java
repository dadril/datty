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
package io.datty.api.operation;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.AbstractDattyIOTest;
import io.datty.api.DattyOperation;
import io.datty.util.DattyIO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AbstractSetOperationIOTest
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
public abstract class AbstractSetOperationIOTest<O extends AbstractSetOperation> extends AbstractDattyIOTest {

	abstract O newOperation();
	
	@Test
	public void testEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		O empty = newOperation();
		
		DattyIO.writeOperation(empty, sink, numeric);
		
		DattyOperation actual = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(actual);
		Assert.assertEquals(empty.getCode(), actual.getCode());
		
		Assert.assertNull(actual.getSetName());
		Assert.assertNull(actual.getSuperKey());
		Assert.assertFalse(actual.hasTimeoutMillis());
		
	}
	
	@Test
	public void testNonEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		O operation = newOperation();
		
		operation.setSetName(setName);
		operation.setSuperKey(superKey);
		operation.setTimeoutMillis(timeoutMillis);
		
		DattyIO.writeOperation(operation, sink, numeric);
		
		//System.out.println("bytes = " + Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyOperation actual = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(actual);
		Assert.assertEquals(operation.getCode(), actual.getCode());
		
		Assert.assertEquals(setName, actual.getSetName());
		Assert.assertEquals(superKey, actual.getSuperKey());
		Assert.assertEquals(timeoutMillis, actual.getTimeoutMillis());
		
		
	}
	
}
