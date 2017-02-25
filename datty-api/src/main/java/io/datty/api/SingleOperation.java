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
package io.datty.api;

import io.datty.api.operation.BatchOperation;
import io.datty.api.payload.SingleOperationPayload;

/**
 * All operations in Datty are asynchronous
 * 
 * Reactive programming
 * 
 * All operations are thread-safe
 * 
 * @author dadril
 *
 */

public interface SingleOperation<O extends SingleOperation<O>> extends
		DattyOperation {

	/**
	 * Adds this operation to the batch
	 * 
	 * @param batch
	 *            - batch that will be executed
	 */

	O addToBatch(BatchOperation batch);

	/**
	 * Writes single operation
	 * 
	 * @param payload
	 *            - output payload
	 */

	void writeTo(SingleOperationPayload payload);

	/**
	 * Single instantiator for operation
	 */

	interface SingleInstantiator {

		/**
		 * Instantiation method
		 * 
		 * @param payload
		 *            of the operation
		 * @return instance of operation
		 */

		SingleOperation<?> parseFrom(SingleOperationPayload payload);

	}

}
