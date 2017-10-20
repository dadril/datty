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

import io.datty.api.DattyValue;
import io.datty.api.UpdatePolicy;
import io.datty.api.operation.FetchOperation;
import io.datty.api.result.FetchResult;
import io.datty.support.exception.DattyOperationException;

/**
 * DattySingleTest
 * 
 * @author Alex Shvid
 *
 */

public class DattySingleTest extends AbstractDattyUnitTest {

	@Test
	public void testNull() {
		
		Assert.assertNotNull(dattySet);
		
	}
	
	@Test(expected=DattyOperationException.class)
	public void testCacheNotFound() {
		
		dattyManager.getDatty().execute(new FetchOperation("unknownSet", "majorKey")).toBlocking().value();
		
	}
	
	@Test
	public void testFallback() {
		
		FetchResult fallback = new FetchResult();
		
		FetchResult actual = dattyManager.getDatty().execute(new FetchOperation("unknownSet", "majorKey").setFallback(fallback)).toBlocking().value();
		
		Assert.assertFalse(actual.exists());
		
	}
	
	
	@Test
	public void testNotExists() {
		
		String majorKey = UUID.randomUUID().toString();
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		exists = dattySet.fetch(majorKey).withValues(false).execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		FetchResult result = dattySet.fetch(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertFalse(result.hasVersion());
		Assert.assertNull(result.getVersion());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testExists() {
		
		String majorKey = UUID.randomUUID().toString();
		
		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		// record
		FetchResult exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertTrue(exists.exists(minorKey));
		
		exists = dattySet.fetch(majorKey).withValues(false).execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertFalse(exists.exists(minorKey));
		
		// column
		exists = dattySet.fetch(majorKey).withValues(false).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertTrue(exists.exists());
		Assert.assertTrue(exists.exists(minorKey));
		
	  // record
		FetchResult get = dattySet.fetch(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(get.exists());
		Assert.assertTrue(get.hasVersion());
		Assert.assertNotNull(get.getVersion());
		Assert.assertEquals(1, get.size());
		assertEquals(value(), get.get(minorKey));
		
		// column
		get = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		Assert.assertTrue(get.exists());
		Assert.assertTrue(get.hasVersion());
		Assert.assertNotNull(get.getVersion());
		Assert.assertEquals(1, get.size());
		assertEquals(value(), get.get(minorKey));
		
	}
	
	@Test
	public void testReplace() {
		
		String majorKey = UUID.randomUUID().toString();
		
		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		FetchResult result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		assertEquals(value(), result.get(minorKey));
		
		dattySet.push(majorKey).setUpdatePolicy(UpdatePolicy.REPLACE).execute().toBlocking().value();
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = dattySet.fetch(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testReplaceNull() {
		
		String majorKey = UUID.randomUUID().toString();
		
		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		FetchResult result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		assertEquals(value(), result.get(minorKey));
		
		dattySet.push(majorKey).addValue(minorKey, DattyValue.NULL).setUpdatePolicy(UpdatePolicy.REPLACE).execute().toBlocking().value();
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = dattySet.fetch(majorKey).execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testMergeEmpty() {
		
		String majorKey = UUID.randomUUID().toString();
		
		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		FetchResult result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		assertEquals(value(), result.get(minorKey));
		
		dattySet.push(majorKey).setUpdatePolicy(UpdatePolicy.MERGE).execute().toBlocking().value();
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertTrue(exists);
		
		result = dattySet.fetch(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertTrue(result.exists());
		Assert.assertEquals(1, result.size());
		assertEquals(value(), result.get(minorKey));
		
	}
	
	@Test
	public void testMergeNull() {
		
		String majorKey = UUID.randomUUID().toString();
		
		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();

		FetchResult result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		assertEquals(value(), result.get(minorKey));

		dattySet.push(majorKey).addValue(minorKey, DattyValue.NULL).setUpdatePolicy(UpdatePolicy.MERGE).execute().toBlocking().value();
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertFalse(exists);
		
		result = dattySet.fetch(majorKey).allMinorKeys().execute().toBlocking().value();
		Assert.assertFalse(result.exists());
		Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	public void testPutIfAbsent() {
		
		String majorKey = UUID.randomUUID().toString();
		
		boolean updated = dattySet.push(majorKey).addValue(minorKey, value()).withVersion(null).execute().toBlocking().value().isUpdated();
		Assert.assertTrue(updated);
		
		boolean exists = dattySet.fetch(majorKey).withValues(false).allMinorKeys().execute().toBlocking().value().exists();
		Assert.assertTrue(exists);
		
	}
	
	@Test
	public void testCompareAndSet() {
		
		String majorKey = UUID.randomUUID().toString();

		dattySet.push(majorKey).addValue(minorKey, value()).execute().toBlocking().value();
		
		FetchResult result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		
		boolean updated = dattySet.push(majorKey).addValue(minorKey, newValue()).withVersion(result.getVersion()).execute().toBlocking().value().isUpdated();
		Assert.assertTrue(updated);

		result = dattySet.fetch(majorKey).addMinorKey(minorKey).execute().toBlocking().value();
		assertEquals(newValue(), result.get(minorKey));

	}
}
