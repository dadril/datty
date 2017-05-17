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
import io.datty.msgpack.MessageFactory;
import io.datty.msgpack.core.reader.LongReader;
import io.datty.spring.mapping.Embedded;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * EmbeddedEntityTest
 * 
 * @author Alex Shvid
 *
 */

public class EmbeddedEntityTest {

	@Test
	public void testAnnotation() {
		
		Class<?> clazz = io.datty.spring.converter.embedded.EmbeddedEntity.class;
		
		boolean present = clazz.isAnnotationPresent(Embedded.class);
		
		Assert.assertTrue(present);
		
	}
	
	@Test
	public void testNull() {
		
		ContainerEntity entity = new ContainerEntity();
		entity.setId(123L);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		Assert.assertEquals(entity.getId(), map.get("id"));

		ContainerEntity actual = DattyConverterUtil.read(ContainerEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNull(actual.getEmbedded());
		
	}
	
	@Test
	public void testCrossNull() {
		
		ContainerCrossEntity entity = new ContainerCrossEntity();
		entity.setId(123L);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNull(bb);
		
		ContainerCrossEntity actual = DattyConverterUtil.read(ContainerCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNull(actual.getEmbedded());
	}
	
	@Test
	public void testEmbeddedNull() {
		
		ContainerEntity entity = new ContainerEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		entity.setEmbedded(embedded);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		Assert.assertEquals(entity.getId(), map.get("id"));

		ContainerEntity actual = DattyConverterUtil.read(ContainerEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertNull(actual.getEmbedded().getInnerField());
		
	}
	
	@Test
	public void testCrossEmbeddedNull() {
		
		ContainerCrossEntity entity = new ContainerCrossEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		entity.setEmbedded(embedded);		
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNotNull(bb);

		Object embeddedObj = MessageFactory.readValue(bb, true);
		Assert.assertNotNull(embeddedObj);
		Assert.assertTrue(embeddedObj instanceof Map);
		
		Map embeddedMap = (Map) embeddedObj;
		Assert.assertTrue(embeddedMap.isEmpty());
		
		ContainerCrossEntity actual = DattyConverterUtil.read(ContainerCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertNull(actual.getEmbedded().getInnerField());
		
	}
	
	@Test
	public void testEmbedded() {
		
		ContainerEntity entity = new ContainerEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		embedded.setInnerField("inner");
		entity.setEmbedded(embedded);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
				
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		Assert.assertEquals(entity.getId(), map.get("id"));

		ContainerEntity actual = DattyConverterUtil.read(ContainerEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(embedded.getInnerField(), actual.getEmbedded().getInnerField());
		
	}
	
	@Test
	public void testCrossEmbedded() {
		
		ContainerCrossEntity entity = new ContainerCrossEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		embedded.setInnerField("inner");
		entity.setEmbedded(embedded);
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNotNull(bb);
		
		Object embeddedObj = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(embeddedObj);
		Assert.assertTrue(embeddedObj instanceof Map);
		
		Map embeddedMap = (Map) embeddedObj;
		Assert.assertFalse(embeddedMap.isEmpty());
		Object innerField = embeddedMap.get("innerField");
		Assert.assertNotNull(innerField);
		Assert.assertTrue(innerField instanceof String);
		Assert.assertEquals("inner", innerField);
		
		ContainerCrossEntity actual = DattyConverterUtil.read(ContainerCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(embedded.getInnerField(), actual.getEmbedded().getInnerField());
		
	}
	
}
