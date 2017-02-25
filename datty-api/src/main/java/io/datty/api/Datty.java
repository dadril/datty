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

import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

/**
 * Base interface to execute asynchronous operations
 * 
 * @author dadril
 *
 */

public interface Datty extends RegionManager {

	/**
	 * Executes single Datty operation
	 * 
	 * @param operation
	 *            - single operation
	 * @param timeoutMillis
	 *            - timeout milliseconds
	 */

	Single<DattyResult> execute(DattyOperation operation);

	/**
	 * Executes sequence of Datty operations
	 * 
	 * @param operations - sequence of operations
	 * @return sequence of results
	 */
	
	Observable<DattyResult> executeBatch(Observable<DattyOperation> operations);
	
	/**
	 * Gets large value by key
	 * 
	 * @param key - datty key
	 * @return stream value
	 */
	
	Observable<ByteBuf> streamOut(DattyKey key);

	/**
	 * Puts large value by key
	 * 
	 * @param key - datty key
	 * @param value - large value
	 * @return number or bytes written 
	 */
	
	Single<Long> streamIn(DattyKey key, Observable<ByteBuf> value);
	
}
