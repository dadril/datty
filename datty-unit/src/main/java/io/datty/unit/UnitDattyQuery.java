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
package io.datty.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyQuery;
import io.datty.api.operation.CountOperation;
import io.datty.api.operation.DeleteOperation;
import io.datty.api.operation.QueryOperation;
import io.datty.api.operation.ScanOperation;
import io.datty.api.result.QueryResult;
import io.datty.support.exception.DattyOperationException;
import rx.Observable;

/**
 * UnitDattyQuery
 * 
 * @author Alex Shvid
 *
 */

public class UnitDattyQuery implements DattyQuery {

	private final ConcurrentMap<String, UnitSet> setMap;

	public UnitDattyQuery(ConcurrentMap<String, UnitSet> setMap) {
		this.setMap = setMap;
	}

	@Override
	public Observable<QueryResult> executeQuery(QueryOperation operation) {

		String setName = operation.getSetName();
		if (setName == null) {
			return Observable.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty setName", operation));
		}

		UnitSet set = setMap.get(operation.getSetName());

		if (set == null) {
			return Observable.error(new DattyOperationException(ErrCode.SET_NOT_FOUND, setName, operation));
		}

		if (operation instanceof CountOperation) {
			return doCount(set, (CountOperation) operation);
		}
		if (operation instanceof ScanOperation) {
			return doScan(set, (ScanOperation) operation);
		}
		if (operation instanceof DeleteOperation) {
			return doDelete(set, (DeleteOperation) operation);
		}
		else {
			return Observable.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, setName, operation));
		}

	}

	protected Observable<QueryResult> doScan(UnitSet set, ScanOperation operation) {

		ConcurrentMap<String, UnitRecord> recordMap = set.getRecordMap();
		List<QueryResult> list = new ArrayList<>(recordMap.size());
		
		for (Map.Entry<String, UnitRecord> entry : recordMap.entrySet()) {
			
			UnitRecord record = entry.getValue();
			
			QueryResult result = new QueryResult();
			result.setVersion(record.getVersion());
			result.setMajorKey(entry.getKey());
			
			if (operation.isAllMinorKeys()) {
				for (Map.Entry<String, UnitValue> e : record.getColumnMap().entrySet()) {
					UnitValue value = e.getValue();
					if (value != null) {
						result.addValue(e.getKey(), value.asByteBuf());
					}
				}
			}
			else {
				for (String minorKey : operation.getMinorKeys()) {
					UnitValue value = record.getColumn(minorKey);
					if (value != null) {
						result.addValue(minorKey, value.asByteBuf());
					}
				}
			}
			
			list.add(result);
			
		}

		return Observable.from(list);
		
	}
	
	protected Observable<QueryResult> doCount(UnitSet set, CountOperation operation) {

		QueryResult result = new QueryResult();
		result.setCount(set.getRecordMap().size());

		return Observable.just(result);
	}
	
	protected Observable<QueryResult> doDelete(UnitSet set, DeleteOperation operation) {

		QueryResult result = new QueryResult();
		
		if (operation.isAllMinorKeys()) {
			result.setCount(set.getRecordMap().size());
			set.getRecordMap().clear();
		}
		else if (!operation.getMinorKeys().isEmpty()) {
			int count = 0;
			Map<String, UnitRecord> recordMap = new HashMap<>(set.getRecordMap());
			for (Map.Entry<String, UnitRecord> entry : recordMap.entrySet()) {
				UnitRecord record = entry.getValue();
				UnitRecord newRecord = new UnitRecord(record, operation.getMinorKeys());
				if (newRecord.isEmpty()) {
					set.getRecordMap().remove(entry.getKey());
					count++;
				}
				else if (newRecord.columns() != record.columns()) {
					set.getRecordMap().put(entry.getKey(), newRecord);
					count++;
				}
			}
			result.setCount(count);
		}
		
		
		return Observable.just(result);
	}

}
