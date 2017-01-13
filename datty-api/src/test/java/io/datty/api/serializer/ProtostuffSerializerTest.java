/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package io.datty.api.serializer;

import io.datty.api.serializer.ProtostuffSerializer;

import java.io.IOException;

import org.junit.Test;

public class ProtostuffSerializerTest extends AbstractSerializerTest {

	@Test
	public void testSingle() throws IOException {

		testSingle(ProtostuffSerializer.INSTANCE);

	}

	@Test
	public void testBatch() throws IOException {

		testBatch(ProtostuffSerializer.INSTANCE);

	}

	@Test
	public void testPerformace() throws IOException {

		long ser = testSerializePerformace(ProtostuffSerializer.INSTANCE,
				DEFAULT_ITERS);
		long deser = testDeserializePerformace(ProtostuffSerializer.INSTANCE,
				DEFAULT_ITERS);
		System.out.println("ProtostuffSerializer performance " + ser + "/"
				+ deser);

	}

}
