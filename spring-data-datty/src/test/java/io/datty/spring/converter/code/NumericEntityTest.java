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
package io.datty.spring.converter.code;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageFactory;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * TaggedEntityTest
 * 
 * @author Alex Shvid
 *
 */

public class NumericEntityTest {

	@Test
	public void testNames() {
		
		NumericEntity entity = new NumericEntity();
		entity.setId(123L);
		entity.setName("Alex");
		
		DattyRow row = new DattyRow();
		DattyConverterUtil.write(entity, row, true);

		ByteBuf bb = row.get("def").asByteBuf();
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<Integer, Object> map = (Map<Integer, Object>) value;
		Assert.assertEquals(entity.getId(), map.get(1));
		Assert.assertEquals("Alex", map.get(2));
		
		NumericEntity actual = DattyConverterUtil.read(NumericEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertEquals(entity.getName(), actual.getName());
		
		row.clear();
		DattyConverterUtil.write(actual, row, true);

		bb = row.get("def").asByteBuf();
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		map = (Map<Integer, Object>) value;
		Assert.assertEquals(entity.getId(), map.get(1));
		Assert.assertEquals("Alex", map.get(2));
		
		actual = DattyConverterUtil.read(NumericEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertEquals(entity.getName(), actual.getName());
		
	}
	
}
