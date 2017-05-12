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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AllArraysConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class AllArraysConverterTest extends AbstractConverterTest {

	@Test
	public void testEmpty() throws Exception {
		testEmpty(new AllArraysEntity());
	}

	@Test
	public void testOnes() throws Exception {
		
		ByteBuf one = Unpooled.wrappedBuffer(new byte[] { 1 });
		
		AllArraysEntity entity = new AllArraysEntity();
		entity.setBooleanVal(new boolean[] {true});
		entity.setBooleanWal(new Boolean[] {true});
		entity.setByteVal(new byte[] {1});
		entity.setByteWal(new Byte[] {1});
		entity.setShortVal(new short[] {1});
		entity.setShortWal(new Short[] {1});
		entity.setIntVal(new int[] {1});
		entity.setIntWal(new Integer[] {1});
		entity.setLongVal(new long[] {1});
		entity.setLongWal(new Long[] {1L});
		entity.setFloatVal(new float[] {1.0f});
		entity.setFloatWal(new Float[] {1.0f});
		entity.setDoubleVal(new double[] {1.0d});
		entity.setDoubleWal(new Double[] {1.0d});
		entity.setStringVal(new String[] {"1"});
		entity.setBbVal(new ByteBuf[] {one});
		
		testNotEmpty(entity);
	}
	
	
}
