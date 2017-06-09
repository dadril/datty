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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.operation.ScanOperation;
import io.datty.api.result.RecordResult;
import io.datty.unit.UnitRecord;
import io.datty.unit.UnitValue;
import rx.Observable;

/**
 * ScanExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum ScanExecutor implements SetOperationExecutor<ScanOperation> {

	INSTANCE;

	@Override
	public Observable<RecordResult> execute(ConcurrentMap<String, UnitRecord> recordMap, ScanOperation operation) {
		
		List<RecordResult> list = new ArrayList<>(recordMap.size());
		
		for (Map.Entry<String, UnitRecord> entry : recordMap.entrySet()) {
			
			UnitRecord record = entry.getValue();
			
			RecordResult result = new RecordResult();
			result.setVersion(record.getVersion());
			result.setMajorKey(entry.getKey());
			
			for (Map.Entry<String, UnitValue> e : record.getColumnMap().entrySet()) {
				UnitValue value = e.getValue();
				if (value != null) {
					result.addValue(e.getKey(), value.asByteBuf());
				}
			}
			
			list.add(result);
			
		}

		return Observable.from(list);
		
	}
	
}
