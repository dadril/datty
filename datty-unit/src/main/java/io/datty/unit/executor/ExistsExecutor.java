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

import io.datty.api.operation.ExistsOperation;
import io.datty.api.result.ExistsResult;
import io.datty.unit.UnitRecord;
import rx.Single;

/**
 * ExistsExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum ExistsExecutor implements OperationExecutor<ExistsOperation, ExistsResult> {

	INSTANCE;
	
	@Override
	public Single<ExistsResult> execute(ConcurrentMap<String, UnitRecord> recordMap, ExistsOperation operation) {
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		ExistsResult result = new ExistsResult();
		if (record != null) {
		
			result.setVersion(record.getVersion());
			
			if (operation.isAllMinorKeys()) {
				result.addMinorKeys(operation.getMinorKeys());
			}
			else {
				for (String minorKey : operation.getMinorKeys()) {
					if (record.hasColumn(minorKey)) {
						result.addMinorKey(minorKey);
					}
				}
			}
			
		}
		
		return Single.just(result);
	}
	
}