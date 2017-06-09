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
package io.datty.unit.executor;

import java.util.concurrent.ConcurrentMap;

import io.datty.api.operation.SetOperation;
import io.datty.api.result.RecordResult;
import io.datty.unit.UnitRecord;
import rx.Observable;

/**
 * SetOperationExecutor
 * 
 * @author Alex Shvid
 *
 */

public interface SetOperationExecutor<O extends SetOperation> {

	/**
	 * Executes operation under the whole Set
	 * 
	 * @param operation - datty operation under the whole Set
	 * @return not null results
	 */
	
	Observable<RecordResult> execute(ConcurrentMap<String, UnitRecord> recordMap, O operation);
	
}
