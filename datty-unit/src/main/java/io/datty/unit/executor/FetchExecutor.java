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

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.operation.FetchOperation;
import io.datty.api.result.FetchResult;
import io.datty.support.NullDattyValue;
import io.datty.unit.UnitRecord;
import io.datty.unit.UnitValue;
import rx.Single;

/**
 * FetchExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum FetchExecutor implements OperationExecutor<FetchOperation, FetchResult> {

	INSTANCE;
	
	@Override
	public Single<FetchResult> execute(ConcurrentMap<String, UnitRecord> recordMap, FetchOperation operation) {
		
		boolean fetchValues = operation.isFetchValues();
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		FetchResult result = new FetchResult();
		
		if (record != null) {
			
			result.setVersion(record.getVersion());

			if (operation.isAllMinorKeys()) {
				
				for (Map.Entry<String, UnitValue> e : record.getColumnMap().entrySet()) {
					result.addValue(e.getKey(), fetchValues ? e.getValue().dublicate() : NullDattyValue.NULL);
				}
				
			}
			else if (!operation.getMinorKeys().isEmpty()) {
				
				for (String minorKey : operation.getMinorKeys()) {
					UnitValue value = record.getColumn(minorKey);
					if (value != null) {
						result.addValue(minorKey, fetchValues ? value.dublicate() : NullDattyValue.NULL);
					}
				}
				
			}
		
		}

		return Single.just(result);
	}
	
}