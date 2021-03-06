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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import io.datty.api.DattyResult;
import io.datty.api.operation.Fetch;
import io.datty.api.operation.Push;
import io.datty.api.operation.RecordOperation;
import io.datty.api.result.FetchResult;
import io.datty.api.result.PushResult;

/**
 * DattyBatchTest
 * 
 * @author Alex Shvid
 *
 */

public class DattyBatchTest extends AbstractDattyUnitTest {

	@Test
	public void testEmpty() {
		
		List<DattyResult> result = dattyManager.getDatty().executeBatch(Collections.<RecordOperation>emptyList()).toBlocking().value();
		
		Assert.assertTrue(result.isEmpty());
		
	}
	
	@Test
	public void testSingle() {
		
		String majorKey = UUID.randomUUID().toString();
		
		List<RecordOperation> batch = new ArrayList<RecordOperation>();
		batch.add(new Fetch(SET_NAME, majorKey).allMinorKeys());
		
		List<DattyResult> results = dattyManager.getDatty().executeBatch(batch).toBlocking().value();
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(1, results.size());
		
		FetchResult result = (FetchResult) results.get(0);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.exists());
		
	}
	
	@Test
	public void testTwo() {
		
		String majorKey = UUID.randomUUID().toString();
		String majorKeyOther = UUID.randomUUID().toString();

		/*
		 * Put
		 */
		
		List<RecordOperation> batch = new ArrayList<RecordOperation>();
		batch.add(new Push(SET_NAME, majorKey).addValue(minorKey, value()));
		batch.add(new Push(SET_NAME, majorKeyOther).addValue(minorKey, value()));
		
		List<DattyResult> results = dattyManager.getDatty().executeBatch(batch).toBlocking().value();
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(2, results.size());
		
		Assert.assertNotNull(results.get(0));
		Assert.assertTrue(results.get(0) instanceof PushResult);
		
		Assert.assertNotNull(results.get(1));
		Assert.assertTrue(results.get(1) instanceof PushResult);
		
		/*
		 * Get
		 */
		
		batch = new ArrayList<RecordOperation>();
		batch.add(new Fetch(SET_NAME, majorKey).addMinorKey(minorKey));
		batch.add(new Fetch(SET_NAME, majorKeyOther).addMinorKey(minorKey));
		
		results = dattyManager.getDatty().executeBatch(batch).toBlocking().value();
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(2, results.size());
		
		FetchResult result = (FetchResult) results.get(0);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.exists());
		assertEquals(value(), result.get(minorKey));
		
		result = (FetchResult) results.get(1);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.exists());
		assertEquals(value(), result.get(minorKey));
		
	}
	
}
