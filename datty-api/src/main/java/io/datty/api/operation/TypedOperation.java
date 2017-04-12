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
package io.datty.api.operation;

import io.datty.api.DattyOperation;
import io.datty.api.result.TypedResult;
import rx.Single;

/**
 * TypedOperation
 * 
 * @author Alex Shvid
 *
 * @param <O> - datty operation type
 * @param <R> - datty return type
 */

public interface TypedOperation<O extends TypedOperation<O, R>, R extends TypedResult<O>> extends DattyOperation {

	/**
	 * Gets fallback result if exists
	 * 
	 * @return null or fallback
	 */
	
	R getFallback();
	
	/**
	 * Sets fallback result in case of error
	 * 
	 * @param fallback - fallback result
	 * @return this
	 */
	
	O onFallback(R fallback);
	
	/**
	 * Executes operation if is created from Cache
	 * 
	 * @param datty - datty instance
	 */

	Single<R> execute();
	
}
