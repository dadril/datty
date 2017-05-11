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

/**
 * AllArraysConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class AllArraysConverterTest extends AbstractConverterTest {

	@Test
	public void testEmpty() throws Exception {
		
		AllArraysEntity entity = new AllArraysEntity();

		DattyRow row = new DattyRow();
		
		DattyConverterUtil.write(entity, row);
		
		ByteBuf bb = row.get("def");
		Assert.assertNotNull(bb);
		
		//System.out.println(Arrays.toString(ByteBufUtil.getBytes(bb)));
		
		Object value = MessageFactory.readValue(bb, true);
		Assert.assertNotNull(value);
		Assert.assertTrue(value instanceof Map);
		
		Map<String, Object> map = (Map<String, Object>) value;
		Assert.assertTrue(map.isEmpty());
		
	}

	
}
