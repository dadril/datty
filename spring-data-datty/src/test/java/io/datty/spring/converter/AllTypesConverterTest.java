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

import org.junit.Test;

import io.netty.buffer.Unpooled;

/**
 * AllTypesConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class AllTypesConverterTest extends AbstractConverterTest {

	@Test
	public void testEmpty() throws Exception {
		testNotEmpty(new AllTypesEntity());
	}
	
	@Test
	public void testOnes() throws Exception {
		
		AllTypesEntity entity = new AllTypesEntity();
		entity.setBooleanVal(true);
		entity.setBooleanWal(true);
		entity.setByteVal((byte)1);
		entity.setByteWal((byte)1);
		entity.setShortVal((short)1);
		entity.setShortWal((short)1);
		entity.setIntVal(1);
		entity.setIntWal(1);
		entity.setLongVal(1L);
		entity.setLongWal(1L);
		entity.setFloatVal(1.0f);
		entity.setFloatWal(1.0f);
		entity.setDoubleVal(1.0d);
		entity.setDoubleWal(1.0d);
		entity.setStringVal("1");
		entity.setBbVal(Unpooled.wrappedBuffer(new byte[] { 1 }));
		
		testNotEmpty(entity);
		
	}
	
}
