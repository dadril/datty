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

import io.datty.api.DattyError;
import io.datty.api.operation.PutOperation;
import io.datty.api.result.PutResult;
import io.datty.support.exception.DattySingleException;
import io.datty.unit.UnitRecord;
import rx.Single;

/**
 * PutExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum PutExecutor implements OperationExecutor<PutOperation, PutResult> {

	INSTANCE;
	
	@Override
	public Single<PutResult> execute(ConcurrentMap<String, UnitRecord> recordMap, PutOperation operation) {

		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		if (record == null) {
			
			record = new UnitRecord(operation.getValues());
			UnitRecord c = recordMap.putIfAbsent(operation.getMajorKey(), record);
			if (c != null) {
				return Single.error(new DattySingleException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
			}
			else {
				return Single.just(new PutResult());
			}
		}
		else {
			
			UnitRecord newRecord = new UnitRecord(record, operation.getValues(), operation.getUpdatePolicy());
			
			boolean updated = newRecord.isEmpty() ? 
					recordMap.remove(operation.getMajorKey(), record) :
					recordMap.replace(operation.getMajorKey(), record, newRecord);
			
			if (!updated) {
				return Single.error(new DattySingleException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
			}
			
			return Single.just(new PutResult());
		}
	}
	
}