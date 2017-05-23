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

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageFactory;
import io.datty.msgpack.core.reader.LongReader;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;

/**
 * StringMapCrossEntityTest
 * 
 * @author Alex Shvid
 *
 */

public class StringMapCrossEntityTest {

	@Test
	public void testNull() {
		
		StringMapCrossEntity entity = new StringMapCrossEntity();
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

		StringMapCrossEntity actual = DattyConverterUtil.read(StringMapCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNull(actual.getEmbedded());
		
	}
	
	@Test
	public void testEmbeddedEmpty() {
		
		StringMapCrossEntity entity = new StringMapCrossEntity();
		entity.setId(123L);
		entity.setEmbedded(Collections.<String, EmbeddedEntity>emptyMap());
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNotNull(bb);
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Map> map = (Map<String, Map>) value;
		Assert.assertEquals(0, map.size());

		StringMapCrossEntity actual = DattyConverterUtil.read(StringMapCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(0, actual.getEmbedded().size());
		
	}
	
	@Test
	public void testEmbeddedNull() {
		
		StringMapCrossEntity entity = new StringMapCrossEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		entity.setEmbedded(Collections.singletonMap("k", embedded));
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNotNull(bb);
		
		//System.out.println(StringMaps.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Map> map = (Map<String, Map>) value;
		Assert.assertEquals(1, map.size());
		Assert.assertNotNull(map.get("k"));
		Assert.assertNull(map.get("k").get("innerField"));

		StringMapCrossEntity actual = DattyConverterUtil.read(StringMapCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(1, actual.getEmbedded().size());
		Assert.assertNull(actual.getEmbedded().get("k").getInnerField());
		
	}
		
	@Test
	public void testEmbedded() {
		
		StringMapCrossEntity entity = new StringMapCrossEntity();
		entity.setId(123L);
		
		EmbeddedEntity embedded = new EmbeddedEntity();
		embedded.setInnerField("inner");
		entity.setEmbedded(Collections.singletonMap("k", embedded));
		
		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("id");
		Assert.assertNotNull(bb);
		
		Long id = LongReader.INSTANCE.read(bb.duplicate(), true);
		Assert.assertNotNull(id);
		Assert.assertEquals(123L, id.longValue());
		
		bb = row.get("embedded");
		Assert.assertNotNull(bb);
		
		//System.out.println(StringMaps.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb.duplicate(), true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Map> map = (Map<String, Map>) value;
		Assert.assertEquals(1, map.size());
		Assert.assertNotNull(map.get("k"));
		Assert.assertEquals("inner", map.get("k").get("innerField"));

		StringMapCrossEntity actual = DattyConverterUtil.read(StringMapCrossEntity.class, row);
		Assert.assertEquals(entity.getId(), actual.getId());
		Assert.assertNotNull(actual.getEmbedded());
		Assert.assertEquals(1, actual.getEmbedded().size());
		Assert.assertEquals("inner", actual.getEmbedded().get("k").getInnerField());
		
	}
	
}
