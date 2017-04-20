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

import org.junit.Assert;
import org.junit.Test;

import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.Identifiable;
import io.datty.spring.support.DattyConverterUtil;


/**
 * DattyConverterTest
 * 
 * @author Alex Shvid
 *
 */

public class DattyConverterTest {

	@Test
	public void testLongId() {
		
		DattyId id = DattyConverterUtil.toDattyId(Long.valueOf(123));
		
		Assert.assertNull(id.getSuperKey());
		Assert.assertNotNull(id.getMajorKey());
		Assert.assertNull(id.getMinorKey());
		
		Assert.assertEquals("123", id.getMajorKey());
		
	}
	
	@Test
	public void testStringId() {
		
		DattyId id = DattyConverterUtil.toDattyId("123");
		
		Assert.assertNull(id.getSuperKey());
		Assert.assertNotNull(id.getMajorKey());
		Assert.assertNull(id.getMinorKey());
		
		Assert.assertEquals("123", id.getMajorKey());
		
	}
	
	
	@Test
	public void testIdentifiable() {
		
		DattyId id = DattyConverterUtil.toDattyId(new Identifiable() {

			@Override
			public DattyId getId() {
				return new DattyId().setSuperKey("us").setMajorKey("123");
			}
			
		});
		
		Assert.assertNotNull(id.getSuperKey());
		Assert.assertNotNull(id.getMajorKey());
		Assert.assertNull(id.getMinorKey());
		
		Assert.assertEquals("us", id.getSuperKey());
		Assert.assertEquals("123", id.getMajorKey());
		
	}
}
