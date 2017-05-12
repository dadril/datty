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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageFactory;
import io.datty.spring.support.DattyConverterUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * AbstractConverterTest
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractConverterTest {

	public void testEmpty(Object entity) throws Exception {
		
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
	
	public void testNotEmpty(Object entity) throws Exception {
		
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
	
	public void assertEqualsMap(Object entity, Map<String, Object> map) throws Exception {

		for (Field f : entity.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			Object expected = f.get(entity);
			Object actual = map.get(f.getName());
			Assert.assertEquals(f.getName(), toString(expected), toString(actual));
		}
		
	}
	
	public String toString(Object val) {
		if (val == null) {
			return "null";
		}
		else if (val instanceof ByteBuf) {
			ByteBuf bb  = (ByteBuf) val;
			return ByteBufUtil.hexDump(bb);
		}
		else if (val instanceof List) {
			List<Object> list = (List<Object>) val;
			StringBuilder str = new StringBuilder();
			str.append("[");
			int size = list.size();
			for (int i = 0; i != size; ++i) {
				Object element = list.get(i);
				if (str.length() != 1) {
					str.append(", ");
				}
				str.append(toString(element));
			}
			str.append("]");
			return str.toString();
		}
		else if (val instanceof Map) {
			Map<Object, Object> map = (Map<Object, Object>) val;
			StringBuilder str = new StringBuilder();
			str.append("{");
			for (Map.Entry<Object, Object> e : map.entrySet()) {
				Object key = e.getKey();
				Object element = e.getValue();
				if (str.length() != 1) {
					str.append(", ");
				}
				str.append(toString(key));
				str.append("=");
				str.append(toString(element));
			}
			str.append("}");
			return str.toString();
		}
		else if (val.getClass().isArray()) {
			StringBuilder str = new StringBuilder();
			str.append("[");
			int size = Array.getLength(val);
			for (int i = 0; i != size; ++i) {
				Object element = Array.get(val, i);
				if (str.length() != 1) {
					str.append(", ");
				}
				str.append(toString(element));
			}
			str.append("]");
			return str.toString();
		}
		else {
			return String.valueOf(val);
		}
	}
	
	
}
