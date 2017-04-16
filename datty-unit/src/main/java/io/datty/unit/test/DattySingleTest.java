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

import io.datty.api.UpdatePolicy;
import io.datty.api.operation.GetOperation;
import io.datty.api.result.ExistsResult;
import io.datty.api.result.GetResult;
import io.datty.support.exception.DattySingleException;

/**
 * DattySingleTest
 * 
 * @author Alex Shvid
 *
 */

public class DattySingleTest extends AbstractDattyUnitTest {

	@Test
	public void testNull() {
		
		Assert.assertNotNull(cache);
		
	}
	
	@Test(expected=DattySingleException.class)
	public void testCacheNotFound() {
		
		cacheManager.getDatty().execute(new GetOperation("unknownCache", "majorKey")).toBlocking().value();
		
	}
	
	@Test
	public void testFallback() {
		
		GetResult fallback = new GetResult();
		
		GetResult actual = cacheManager.getDatty().execute(new GetOperation("unknownCache", "majorKey").withFallback(fallback)).toBlocking().value();
		
		Assert.assertFalse(actual.exists());
		
	}
	
	
	@Test
	public void testNotExists() {
		
		String majorKey = UUID.randomUUID().toString();
		
		boolean exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		exists = cache.exists(majorKey).execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		GetResult result = cache.get(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertFalse(result.hasVersion());
		Assert.assertNull(result.getVersion());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testExists() {
		
		String majorKey = UUID.randomUUID().toString();
		
		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		// record
		ExistsResult exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertTrue(exists.exists(minorKey));
		
		exists = cache.exists(majorKey).execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertFalse(exists.exists(minorKey));
		
		// column
		exists = cache.exists(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertTrue(exists.exists(minorKey));
		
	  // record
		GetResult get = cache.get(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(get.exists());
		Assert.assertTrue(get.hasVersion());
		Assert.assertNotNull(get.getVersion());
		Assert.assertEquals(1, get.size());
		Assert.assertEquals(value(), get.get(minorKey));
		
		// column
		get = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertTrue(get.exists());
		Assert.assertTrue(get.hasVersion());
		Assert.assertNotNull(get.getVersion());
		Assert.assertEquals(1, get.size());
		Assert.assertEquals(value(), get.get(minorKey));
		
	}
	
	@Test
	public void testReplace() {
		
		String majorKey = UUID.randomUUID().toString();
		
		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking();
		
		GetResult result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertEquals(value(), result.get(minorKey));
		
		cache.put(majorKey).withPolicy(UpdatePolicy.REPLACE).execute().toBlocking().value();
		
		boolean exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = cache.get(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testReplaceNull() {
		
		String majorKey = UUID.randomUUID().toString();
		
		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking();
		
		GetResult result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertEquals(value(), result.get(minorKey));
		
		cache.put(majorKey).addValue(minorKey, null).withPolicy(UpdatePolicy.REPLACE).execute().toBlocking().value();
		
		boolean exists = cache.exists(majorKey).execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = cache.get(majorKey).execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testMergeEmpty() {
		
		String majorKey = UUID.randomUUID().toString();
		
		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking();
		
		GetResult result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertEquals(value(), result.get(minorKey));
		
		cache.put(majorKey).withPolicy(UpdatePolicy.MERGE).execute().toBlocking().value();
		
		boolean exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertTrue(exists);
		
		result = cache.get(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(result.exists());
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(value(), result.get(minorKey));
		
	}
	
	@Test
	public void testMergeNull() {
		
		String majorKey = UUID.randomUUID().toString();
		
		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking();
		
		GetResult result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertEquals(value(), result.get(minorKey));
		
		cache.put(majorKey).addValue(minorKey, null).withPolicy(UpdatePolicy.MERGE).execute().toBlocking().value();
		
		boolean exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = cache.get(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testPutIfAbsent() {
		
		String majorKey = UUID.randomUUID().toString();
		
		boolean updated = cache.compareAndSet(majorKey).addValue(majorKey, value()).withVersion(null).execute().toBlocking().value().get();
		Assert.assertTrue(updated);
		
		boolean exists = cache.exists(majorKey).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertTrue(exists);
		
	}
	
	@Test
	public void testCompareAndSet() {
		
		String majorKey = UUID.randomUUID().toString();

		cache.put(majorKey).addValue(minorKey, value()).execute().toBlocking();
		
		GetResult result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		
		boolean updated = cache.compareAndSet(majorKey).addValue(minorKey, newValue()).withVersion(result.getVersion()).execute().toBlocking().value().get();
		Assert.assertTrue(updated);

		result = cache.get(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertEquals(newValue(), result.get(minorKey));

	}
}
