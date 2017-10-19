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

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRecord;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * NumericEntityTest
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
		
		DattyRecord rec = new DattyRecord();
		DattyConverterUtil.write(entity, rec, true);

		ByteBuf bb = rec.get("1").asByteBuf();
		Assert.assertNotNull(bb);
		
		bb = rec.get("2").asByteBuf();
		Assert.assertNotNull(bb);
		
		NumericMigratedEntity actual = DattyConverterUtil.read(NumericMigratedEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertEquals(entity.getName(), actual.getFirst());
		
		rec = new DattyRecord();
		DattyConverterUtil.write(actual, rec, true);

		bb = rec.get("1").asByteBuf();
		Assert.assertNotNull(bb);
				
		bb = rec.get("2").asByteBuf();
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		actual = DattyConverterUtil.read(NumericMigratedEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertEquals(entity.getName(), actual.getFirst());
		
	}
	
}
