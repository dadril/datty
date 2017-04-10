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

import java.util.List;

import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

/**
 * Base interface to execute asynchronous operations
 * 
 * @author dadril
 *
 */

public interface Datty extends DattySingle {
	
	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @return sequence of results
	 */
	
	Single<List<DattyResult>> executeBatch(List<DattyOperation> operations);
	
	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @param timeoutMillis - timeout in milliseconds or 0
	 * @return sequence of results
	 */
	
	Single<List<DattyResult>> executeBatch(List<DattyOperation> operations, int timeoutMillis);
	
	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @return sequence of results
	 */
	
	Observable<DattyResult> executeSequence(Observable<DattyOperation> operations);
	
	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @param totalTimeoutMillis - total timeout milliseconds for the whole batch
	 * @return sequence of results
	 */
	
	Observable<DattyResult> executeSequence(Observable<DattyOperation> operations, int totalTimeoutMillis);
	
	/**
	 * Gets large value by key
	 * 
	 * @param key - datty key
	 * @return stream ValueResult with value bytes or return ErrorResult 
	 */
	
	Observable<DattyResult> streamOut(DattyKey key);
	
	/**
	 * Gets large value by key
	 * 
	 * @param key - datty key
	 * @param totalTimeoutMillis - total timeout milliseconds for the whole stream
	 * @return stream ValueResult with value bytes or return ErrorResult 
	 */
	
	Observable<DattyResult> streamOut(DattyKey key, int totalTimeoutMillis);

	/**
	 * Puts large value by key
	 * 
	 * @param key - datty key
	 * @param value - large value
	 * @return number or bytes written in LongResult or ErrorResult
	 */
	
	Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value);
	
	/**
	 * Puts large value by key
	 * 
	 * @param key - datty key
	 * @param value - large value
	 * @param totalTimeoutMillis - total timeout milliseconds for the whole stream
	 * @return number or bytes written in LongResult or ErrorResult
	 */
	
	Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value, int totalTimeoutMillis);
	
}
