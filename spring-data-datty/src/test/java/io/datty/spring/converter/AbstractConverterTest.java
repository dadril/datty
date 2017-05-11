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

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Assert;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * AbstractConverterTest
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractConverterTest {

	public void assertEqualsMap(Object entity, Map<String, Object> map) throws Exception {

		for (Field f : entity.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			Object expected = f.get(entity);
			Object actual = map.get(f.getName());
			Assert.assertEquals(f.getName(), toString(expected), toString(actual));
		}
		
	}
	
	public String toString(Object val) {
		if (val instanceof ByteBuf) {
			ByteBuf bb  = (ByteBuf) val;
			return ByteBufUtil.hexDump(bb);
		}
		return String.valueOf(val);
	}
	
	
}
