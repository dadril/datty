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

import io.datty.api.operation.SetOperation;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.RecordResult;
import io.datty.api.result.TypedResult;
import rx.Observable;
import rx.Single;

/**
 * DattySingle
 * 
 * @author Alex Shvid
 *
 */

public interface DattySingle {

	/**
	 * Executes operation under the whole Set
	 * 
	 * @param query - datty query operation
	 * @return not null observable of results
	 */
	
	<O extends SetOperation> Observable<RecordResult> execute(O operation);
	
	/**
	 * Executes single datty operation
	 * 
	 * @param operation - not null datty operation
	 */

	<O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(O operation);
	
	/**
	 * Executes single datty operation
	 * 
	 * @param operation - not null datty operation single
	 */

	<O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(Single<O> operation);
	
}
