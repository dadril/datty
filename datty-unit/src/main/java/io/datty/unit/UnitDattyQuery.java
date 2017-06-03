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

	private final ConcurrentMap<String, UnitCache> cacheMap;

	public UnitDattyQuery(ConcurrentMap<String, UnitCache> cacheMap) {
		this.cacheMap = cacheMap;
	}

	@Override
	public Observable<QueryResult> executeQuery(QueryOperation operation) {

		String cacheName = operation.getCacheName();
		if (cacheName == null) {
			return Observable.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty cacheName", operation));
		}

		UnitCache cache = cacheMap.get(operation.getCacheName());

		if (cache == null) {
			return Observable.error(new DattyOperationException(ErrCode.CACHE_NOT_FOUND, cacheName, operation));
		}

		if (operation instanceof CountOperation) {
			return doCount(cache, (CountOperation) operation);
		}
		if (operation instanceof ScanOperation) {
			return doScan(cache, (ScanOperation) operation);
		}
		if (operation instanceof DeleteOperation) {
			return doDelete(cache, (DeleteOperation) operation);
		}
		else {
			return Observable.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, cacheName, operation));
		}

	}

	protected Observable<QueryResult> doScan(UnitCache cache, ScanOperation operation) {

		ConcurrentMap<String, UnitRecord> recordMap = cache.getRecordMap();
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
	
	protected Observable<QueryResult> doCount(UnitCache cache, CountOperation operation) {

		QueryResult result = new QueryResult();
		result.setCount(cache.getRecordMap().size());

		return Observable.just(result);
	}
	
	protected Observable<QueryResult> doDelete(UnitCache cache, DeleteOperation operation) {

		QueryResult result = new QueryResult();
		
		if (operation.isAllMinorKeys()) {
			result.setCount(cache.getRecordMap().size());
			cache.getRecordMap().clear();
		}
		else if (!operation.getMinorKeys().isEmpty()) {
			int count = 0;
			Map<String, UnitRecord> recordMap = new HashMap<>(cache.getRecordMap());
			for (Map.Entry<String, UnitRecord> entry : recordMap.entrySet()) {
				UnitRecord record = entry.getValue();
				UnitRecord newRecord = new UnitRecord(record, operation.getMinorKeys());
				if (newRecord.isEmpty()) {
					cache.getRecordMap().remove(entry.getKey());
					count++;
				}
				else if (newRecord.columns() != record.columns()) {
					cache.getRecordMap().put(entry.getKey(), newRecord);
					count++;
				}
			}
			result.setCount(count);
		}
		
		
		return Observable.just(result);
	}

}
