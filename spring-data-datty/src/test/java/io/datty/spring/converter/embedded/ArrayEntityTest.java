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
package io.datty.spring.converter.embedded;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRecord;
import io.datty.msgpack.MessageIO;
import io.datty.msgpack.core.reader.LongReader;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * ArrayEntityTest
 * 
 * @author Alex Shvid
 *
 */

public class ArrayEntityTest {

	@Test
	public void testNull() {
		
		ArrayEntity entity = new ArrayEntity();
		entity.setId(123L);
		
		DattyRecord rec = new DattyRecord();
		
		DattyConverterUtil.write(entity, rec);
		
		ByteBuf bb = rec.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		Assert.assertNull(rec.get("embedded"));

		ArrayEntity actual = DattyConverterUtil.read(ArrayEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNull(actual.getEmbedded());
		
	}
	
	@Test
	public void testEmbeddedEmpty() {
		
		ArrayEntity entity = new ArrayEntity();
		entity.setId(123L);
		entity.setEmbedded(new EmbeddedEntity[] {});
		
		DattyRecord rec = new DattyRecord();
		
		DattyConverterUtil.write(entity, rec);
		
		ByteBuf bb = rec.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = rec.get("embedded").asByteBuf();
		Assert.assertNotNull(bb);
		
		Object value = MessageIO.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof List);
		
		List<Map> list = (List<Map>) value;
		Assert.assertEquals(0, list.size());

		ArrayEntity actual = DattyConverterUtil.read(ArrayEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(0, actual.getEmbedded().length);
		
	}
	
	@Test
	public void testEmbeddedNull() {
		
		ArrayEntity entity = new ArrayEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		entity.setEmbedded(new EmbeddedEntity[] {embedded});
		
		DattyRecord rec = new DattyRecord();
		
		DattyConverterUtil.write(entity, rec);
		
		ByteBuf bb = rec.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = rec.get("embedded").asByteBuf();
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageIO.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof List);
		
		List<Map> list = (List<Map>) value;
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertNull(list.get(0).get("innerField"));

		ArrayEntity actual = DattyConverterUtil.read(ArrayEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(1, actual.getEmbedded().length);
		Assert.assertNull(actual.getEmbedded()[0].getInnerField());
		
	}
		
	@Test
	public void testEmbedded() {
		
		ArrayEntity entity = new ArrayEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		embedded.setInnerField("inner");
		entity.setEmbedded(new EmbeddedEntity[] {embedded});
		
		DattyRecord rec = new DattyRecord();
		
		DattyConverterUtil.write(entity, rec);
		
		ByteBuf bb = rec.get("id").asByteBuf();
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = rec.get("embedded").asByteBuf();
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageIO.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof List);
		
		List<Map> list = (List<Map>) value;
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals("inner", list.get(0).get("innerField"));

		ArrayEntity actual = DattyConverterUtil.read(ArrayEntity.class, rec);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(1, actual.getEmbedded().length);
		Assert.assertEquals("inner", actual.getEmbedded()[0].getInnerField());
		
	}
	
}
