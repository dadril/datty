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

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AllMapsConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class AllMapsConverterTest extends AbstractConverterTest {

	@Test
	public void testEmpty() throws Exception {
		testEmpty(new AllMapsEntity());
	}

	@Test
	public void testOnes() throws Exception {
		
		ByteBuf one = Unpooled.wrappedBuffer(new byte[] { 1 });
		
		AllMapsEntity entity = new AllMapsEntity();
		entity.setBooleanVal(mapOf(true));
		entity.setByteVal(mapOf((byte)1));
		entity.setShortVal(mapOf((short)1));
		entity.setIntVal(mapOf(1));
		entity.setLongVal(mapOf(1L));
		entity.setFloatVal(mapOf(1.0f));
		entity.setDoubleVal(mapOf(1.0d));
		entity.setStringVal(mapOf("1"));
		entity.setBbVal(mapOf(one));
		
		testNotEmpty(entity);
		
	}
	
	public static <T> Map<String, T> mapOf(T val) {
		return Collections.singletonMap("1", val);
	}
	
	
}
