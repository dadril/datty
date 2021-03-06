/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.api.result;

import org.junit.Assert;

import io.datty.api.version.LongVersion;
import io.datty.support.NullDattyValue;

/**
 * FetchResultIOTest
 * 
 * @author Alex Shvid
 *
 */

public class FetchResultIOTest extends AbstractDattyResultIOTest<FetchResult> {

	@Override
	FetchResult newResult() {
		return new FetchResult();
	}

	@Override
	void assertEmptyFields(FetchResult result) {
		Assert.assertFalse(result.exists());
		Assert.assertFalse(result.hasRecord());
		Assert.assertNull(result.getRecord());
	}

	@Override
	void addFields(FetchResult result) {
		result.setVersion(new LongVersion(1));
		result.addValue(minorKey, NullDattyValue.NULL);
	}

	@Override
	void assertFields(FetchResult expected, FetchResult actual) {
		Assert.assertTrue(actual.exists());
		Assert.assertTrue(actual.hasRecord());
		Assert.assertEquals(new LongVersion(1), actual.getVersion());
		Assert.assertEquals(NullDattyValue.NULL, actual.get(minorKey));
	}
	
}
