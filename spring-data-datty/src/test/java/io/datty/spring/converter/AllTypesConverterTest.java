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

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageFactory;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;
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
		
		AllTypesEntity entity = new AllTypesEntity();

		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb, true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		assertEqualsMap(entity, map);
		
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
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb, true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		//System.out.println(map);
		assertEqualsMap(entity, map);
		
	}
	
}
