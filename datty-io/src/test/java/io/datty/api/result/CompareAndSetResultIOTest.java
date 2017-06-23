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

/**
 * CompareAndSetResultIOTest
 * 
 * @author Alex Shvid
 *
 */

public class CompareAndSetResultIOTest extends AbstractDattyResultIOTest<CompareAndSetResult> {

	@Override
	CompareAndSetResult newResult() {
		return new CompareAndSetResult();
	}

	@Override
	void assertEmptyFields(CompareAndSetResult result) {
		Assert.assertFalse(result.get());
	}

	@Override
	void addFields(CompareAndSetResult result) {
		result.set(true);
	}

	@Override
	void assertFields(CompareAndSetResult expected, CompareAndSetResult actual) {
		Assert.assertEquals(expected.get(), actual.get());
	}

}
