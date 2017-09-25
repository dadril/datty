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
package io.datty.spring.converter.embedded;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageIO;
import io.datty.msgpack.core.reader.LongReader;
import io.datty.spring.mapping.Embedded;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * SimpleEntityTest
 * 
 * @author Alex Shvid
 *
 */

public class SimpleEntityTest {

	@Test
	public void testAnnotation() {
		
		Class<?> clazz = io.datty.spring.converter.embedded.EmbeddedEntity.class;
		
		boolean present = clazz.isAnnotationPresent(Embedded.class);
		
		Assert.assertTrue(present);
		
	}
	
	@Test
	public void testCrossNull() {
		
		SimpleEntity entity = new SimpleEntity();
		entity.setId(123L);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		Assert.assertNull(row.get("embedded"));
		
		SimpleEntity actual = DattyConverterUtil.read(SimpleEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNull(actual.getEmbedded());
	}
	
	@Test
	public void testCrossEmbeddedNull() {
		
		SimpleEntity entity = new SimpleEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		entity.setEmbedded(embedded);		
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded").asByteBuf();
		Assert.assertNotNull(bb);

		Object embeddedObj = MessageIO.readValue(bb.duplicate(), true);
		Assert.assertNotNull(embeddedObj);
		Assert.assertTrue(embeddedObj instanceof Map);
		
		Map embeddedMap = (Map) embeddedObj;
		Assert.assertTrue(embeddedMap.isEmpty());
		
		SimpleEntity actual = DattyConverterUtil.read(SimpleEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertNull(actual.getEmbedded().getInnerField());
		
	}
		
	@Test
	public void testCrossEmbedded() {
		
		SimpleEntity entity = new SimpleEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		embedded.setInnerField("inner");
		entity.setEmbedded(embedded);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded").asByteBuf();
		Assert.assertNotNull(bb);
		
		Object embeddedObj = MessageIO.readValue(bb.duplicate(), true);
		Assert.assertNotNull(embeddedObj);
		
		Assert.assertTrue(embeddedObj instanceof Map);
		
		Map embeddedMap = (Map) embeddedObj;
		Assert.assertFalse(embeddedMap.isEmpty());
		Object innerField = embeddedMap.get("innerField");
		Assert.assertNotNull(innerField);
		Assert.assertTrue(innerField instanceof String);
		Assert.assertEquals("inner", innerField);
		
		SimpleEntity actual = DattyConverterUtil.read(SimpleEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(embedded.getInnerField(), actual.getEmbedded().getInnerField());
		
	}
	
}
