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
package io.datty.unit.test;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class DattyCruidTest extends AbstractDattyUnitTest {

	@Test
	public void testNull() {
		
		Assert.assertNotNull(cache);
		
	}
	
	@Test
	public void testCruid() {
		
		String majorKey = UUID.randomUUID().toString();
		String minorKey = "def";
		ByteBuf value = Unpooled.wrappedBuffer("hello".getBytes());
		
		boolean exists = cache.exists(majorKey).anyMinorKey().execute().toBlocking().value().get();
		Assert.assertFalse(exists);
		
		cache.put(majorKey).addValue(minorKey, value).execute().toBlocking();
		
		
		
	}
	
}
