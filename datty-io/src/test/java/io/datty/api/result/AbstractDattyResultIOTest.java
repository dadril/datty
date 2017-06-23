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
package io.datty.api.result;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.AbstractDattyIOTest;
import io.datty.api.DattyResult;
import io.datty.util.DattyIO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AbstractDattyResultIOTest
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractDattyResultIOTest<R extends DattyResult> extends AbstractDattyIOTest {

	abstract R newResult();
	
	void assertEmptyFields(R result) {
	}
	
	void addFields(R result) {
	}
	
	void assertFields(R expected, R actual) {
	}
	
	@Test
	public void testEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		R empty = newResult();
		
		DattyIO.writeResult(empty, sink, numeric);
		
		DattyResult read = DattyIO.readResult(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(empty.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		R actual = (R) read;
		
		assertEmptyFields(actual);
		
	}
	
	@Test
	public void testNonEmpty() {
		
		ByteBuf sink = Unpooled.buffer();
		
		R result = newResult();
		
		addFields(result);
		
		DattyIO.writeResult(result, sink, numeric);
		
		//System.out.println("bytes = " + Arrays.toString(ByteBufUtil.getBytes(sink)));
		
		DattyResult read = DattyIO.readResult(sink);
		
		Assert.assertNotNull(read);
		Assert.assertEquals(result.getCode(), read.getCode());
		
		@SuppressWarnings("unchecked")
		R actual = (R) read;
		
		assertFields(result, actual);
		
	}
	
}
