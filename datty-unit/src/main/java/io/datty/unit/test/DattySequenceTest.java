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

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.PutOperation;
import io.datty.api.result.GetResult;
import io.datty.api.result.PutResult;
import rx.Observable;

/**
 * DattySequenceTest
 * 
 * @author Alex Shvid
 *
 */

public class DattySequenceTest extends AbstractDattyUnitTest {

	@Test
	public void testEmpty() {
		
		Observable<DattyOperation> input = Observable.empty();
		
		Iterable<DattyResult> result = cacheManager.getDatty().executeSequence(input).toBlocking().toIterable();
		
		List<DattyResult> list = Lists.newArrayList(result);
		
		Assert.assertTrue(list.isEmpty());
		
	}
	
	@Test
	public void testSingle() {
		
		String majorKey = UUID.randomUUID().toString();
		
		DattyOperation getOp = new GetOperation(CACHE_NAME, majorKey).allMinorKeys();
		
		Observable<DattyOperation> input = Observable.just(getOp);
		
		Iterable<DattyResult> results = cacheManager.getDatty().executeSequence(input).toBlocking().toIterable();
		List<DattyResult> list = Lists.newArrayList(results);
		
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(1, list.size());
		
		GetResult result = (GetResult) list.get(0);
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
		
		DattyOperation put1 = new PutOperation(CACHE_NAME, majorKey).addValue(minorKey, value);
		DattyOperation put2 = new PutOperation(CACHE_NAME, majorKeyOther).addValue(minorKey, value);
		
		Observable<DattyOperation> input = Observable.just(put1, put2);
		
		Iterable<DattyResult> iterable = cacheManager.getDatty().executeSequence(input).toBlocking().toIterable();
		List<DattyResult> results = Lists.newArrayList(iterable);
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(2, results.size());
		
		Assert.assertNotNull(results.get(0));
		Assert.assertTrue(results.get(0) instanceof PutResult);
		
		Assert.assertNotNull(results.get(1));
		Assert.assertTrue(results.get(1) instanceof PutResult);
		
		/*
		 * Get
		 */
		
		DattyOperation get1 = new GetOperation(CACHE_NAME, majorKey).addMinorKey(minorKey);
		DattyOperation get2 = new GetOperation(CACHE_NAME, majorKeyOther).addMinorKey(minorKey);
		
		input = Observable.just(get1, get2);
		
		iterable = cacheManager.getDatty().executeSequence(input).toBlocking().toIterable();
		results = Lists.newArrayList(iterable);
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(2, results.size());
		
		GetResult result = (GetResult) results.get(0);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.exists());
		Assert.assertEquals(value, result.get(minorKey));
		
		result = (GetResult) results.get(1);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.exists());
		Assert.assertEquals(value, result.get(minorKey));
		
	}
	
}
