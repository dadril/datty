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
import io.datty.api.UpdatePolicy;
import io.datty.util.DattyIO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * PutOperationIOTest
 * 
 * @author Alex Shvid
 *
 */

public class PutOperationIOTest extends AbstractDattyIOTest {

	PutOperation newOperation() {
		return new PutOperation();
	}
	
	void assertEmptyFields(PutOperation operation) {
	}
	
	void addOtherFields(PutOperation operation) {
	}
	
	void assertOtherFields(PutOperation expected, PutOperation actual) {
	}
	
	@Test
	public void testEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		PutOperation empty = newOperation();
		
		DattyIO.writeOperation(empty, sink, numeric);
		
		DattyOperation read = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(empty.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		PutOperation actual = (PutOperation) read;
		
		Assert.assertNull(actual.getSetName());
		Assert.assertNull(actual.getSuperKey());
		Assert.assertNull(actual.getMajorKey());
		Assert.assertFalse(actual.hasTtlSeconds());
		Assert.assertEquals(UpdatePolicy.MERGE, actual.getUpdatePolicy());
		Assert.assertFalse(actual.hasTimeoutMillis());
		Assert.assertFalse(actual.hasFallback());
		Assert.assertFalse(actual.hasUpstreamContext());
		
		assertEmptyFields(actual);
		
	}
	
	@Test
	public void testNonEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		PutOperation operation = newOperation();
		
		operation.setSetName(setName);
		operation.setSuperKey(superKey);
		operation.setMajorKey(majorKey);
		operation.setTtlSeconds(ttlSeconds);
		operation.setUpdatePolicy(updatePolicy);
		operation.setTimeoutMillis(timeoutMillis);
		
		addOtherFields(operation);
		
		DattyIO.writeOperation(operation, sink, numeric);
		
		// System.out.println("bytes = " + Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyOperation read = DattyIO.readOperation(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(operation.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		PutOperation actual = (PutOperation) read;
		
		Assert.assertEquals(setName, actual.getSetName());
		Assert.assertEquals(superKey, actual.getSuperKey());
		Assert.assertEquals(ttlSeconds, actual.getTtlSeconds());
		Assert.assertEquals(updatePolicy, actual.getUpdatePolicy());
		Assert.assertEquals(timeoutMillis, actual.getTimeoutMillis());
		Assert.assertFalse(actual.hasFallback());
		Assert.assertFalse(actual.hasUpstreamContext());
		
		assertOtherFields(operation, actual);
		
	}
	
}
