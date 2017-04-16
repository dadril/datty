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

import io.datty.api.operation.GetOperation;
import io.datty.api.result.GetResult;
import io.datty.unit.UnitRecord;
import io.datty.unit.UnitValue;
import rx.Single;

/**
 * GetExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum GetExecutor implements OperationExecutor<GetOperation, GetResult> {

	INSTANCE;
	
	@Override
	public Single<GetResult> execute(ConcurrentMap<String, UnitRecord> recordMap, GetOperation operation) {
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		GetResult result = new GetResult();
		
		if (record != null) {
			
			result.setVersion(record.getVersion());

			if (operation.isAllMinorKeys()) {
				
				for (Map.Entry<String, UnitValue> e : record.getColumnMap().entrySet()) {
					result.addValue(e.getKey(), e.getValue().asByteBuf());
				}
				
			}
			else if (!operation.getMinorKeys().isEmpty()) {
				
				for (String minorKey : operation.getMinorKeys()) {
					UnitValue value = record.getColumn(minorKey);
					if (value != null) {
						result.addValue(minorKey, value.asByteBuf());
					}
				}
				
			}
		
		}

		return Single.just(result);
	}
	
}