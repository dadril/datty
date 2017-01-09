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
package com.dadril.datty.api;

import com.dadril.datty.api.payload.SingleResultPayload;

/**
 * All operation results in Datty are futures
 * 
 * Reactive programming
 * 
 * All results of operations are thread safe
 * 
 * @author dadril
 *
 */

public interface SingleResult<T> extends DattyResult {

	/**
	 * Gets result of the operation
	 * 
	 * @return result or throws runtime DattyException
	 */

	T get();

	/**
	 * Writes single result
	 * 
	 * @param payload
	 *            - output payload
	 */

	void writeTo(SingleResultPayload payload);

	/**
	 * Single instantiator for result
	 */

	interface SingleInstantiator {

		/**
		 * Instantiation method
		 * 
		 * @param payload
		 *            of the operation
		 * @return instance of operation
		 */

		SingleResult<?> parseFrom(SingleResultPayload payload);

	}

}
