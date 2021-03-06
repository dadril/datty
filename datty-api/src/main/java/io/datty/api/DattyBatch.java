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

import io.datty.api.operation.RecordOperation;
import rx.Observable;
import rx.Single;

/**
 * DattyBatch
 * 
 * Batch or bulk operations
 * 
 * @author Alex Shvid
 *
 */

public interface DattyBatch {

	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @return sequence of results
	 */
	
	Single<List<DattyResult>> executeBatch(List<RecordOperation> operations);
	
	/**
	 * Executes sequence of datty operations
	 * 
	 * @param operations - sequence of operations
	 * @return sequence of results
	 */
	
	Observable<DattyResult> executeSequence(Observable<RecordOperation> operations);
	
}
