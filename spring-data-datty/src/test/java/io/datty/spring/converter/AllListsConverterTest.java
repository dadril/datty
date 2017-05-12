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
package io.datty.spring.converter;

import java.util.Arrays;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AllListsConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class AllListsConverterTest extends AbstractConverterTest {

	@Test
	public void testEmpty() throws Exception {
		testEmpty(new AllListsEntity());
	}

	@Test
	public void testOnes() throws Exception {
		
		ByteBuf one = Unpooled.wrappedBuffer(new byte[] { 1 });
		
		AllListsEntity entity = new AllListsEntity();
		entity.setBooleanVal(Arrays.asList(true));
		entity.setByteVal(Arrays.asList((byte)1));
		entity.setShortVal(Arrays.asList((short)1));
		entity.setIntVal(Arrays.asList(1));
		entity.setLongVal(Arrays.asList(1L));
		entity.setFloatVal(Arrays.asList(1.0f));
		entity.setDoubleVal(Arrays.asList(1.0d));
		entity.setStringVal(Arrays.asList("1"));
		entity.setBbVal(Arrays.asList(one));
		
		testNotEmpty(entity);
		
	}
	
	
}
