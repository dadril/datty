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
import io.datty.api.operation.Fetch;
import io.datty.api.operation.Push;
import io.datty.api.operation.RecordOperation;
import io.datty.api.result.FetchResult;
import io.datty.api.result.PushResult;
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
		
		Observable<RecordOperation> input = Observable.empty();
		
		Iterable<DattyResult> result = dattyManager.getDatty().executeSequence(input).toBlocking().toIterable();
		
		List<DattyResult> list = Lists.newArrayList(result);
		
		Assert.assertTrue(list.isEmpty());
		
	}
	
	@Test
	public void testSingle() {
		
		String majorKey = UUID.randomUUID().toString();
		
		RecordOperation fetchOp = new Fetch(SET_NAME, majorKey).allMinorKeys();
		
		Observable<RecordOperation> input = Observable.just(fetchOp);
		
		Iterable<DattyResult> results = dattyManager.getDatty().executeSequence(input).toBlocking().toIterable();
		List<DattyResult> list = Lists.newArrayList(results);
		
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(1, list.size());
		
		FetchResult result = (FetchResult) list.get(0);
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
		
		RecordOperation put1 = new Push(SET_NAME, majorKey).addValue(minorKey, value());
		RecordOperation put2 = new Push(SET_NAME, majorKeyOther).addValue(minorKey, value());
		
		Observable<RecordOperation> input = Observable.just(put1, put2);
		
		Iterable<DattyResult> iterable = dattyManager.getDatty().executeSequence(input).toBlocking().toIterable();
		List<DattyResult> results = Lists.newArrayList(iterable);
		
		Assert.assertFalse(results.isEmpty());
		Assert.assertEquals(2, results.size());
		
		Assert.assertNotNull(results.get(0));
		Assert.assertTrue(results.get(0) instanceof PushResult);
		
		Assert.assertNotNull(results.get(1));
		Assert.assertTrue(results.get(1) instanceof PushResult);
		
		/*
		 * Get
		 */
		
		RecordOperation fetch1 = new Fetch(SET_NAME, majorKey).addMinorKey(minorKey);
		RecordOperation fetch2 = new Fetch(SET_NAME, majorKeyOther).addMinorKey(minorKey);
		
		input = Observable.just(fetch1, fetch2);
		
		iterable = dattyManager.getDatty().executeSequence(input).toBlocking().toIterable();
		results = Lists.newArrayList(iterable);
		
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
