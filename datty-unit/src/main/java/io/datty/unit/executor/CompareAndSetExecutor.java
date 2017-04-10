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

import io.datty.api.UpdatePolicy;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.result.CompareAndSetResult;
import io.datty.unit.UnitRecord;
import io.netty.buffer.ByteBuf;
import rx.Single;

public enum CompareAndSetExecutor implements OperationExecutor<CompareAndSetOperation, CompareAndSetResult> {

	INSTANCE;

	@Override
	public Single<CompareAndSetResult> execute(ConcurrentMap<String, UnitRecord> recordMap, CompareAndSetOperation operation) {
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		if (record == null) {
			
			if (!operation.hasOldVersion()) {
				boolean updated = null == recordMap.putIfAbsent(operation.getMajorKey(), newRecord(operation));
				return Single.just(CompareAndSetResult.of(updated));
			}
			
		}
		else if (operation.hasOldVersion() && operation.getOldVersion().equals(record.getVersion())) {

			if (operation.getUpdatePolicy() == UpdatePolicy.REPLACE) {
				record.clear();
			}
			
			for (Map.Entry<String, ByteBuf> e : operation.getValues().entrySet()) {
				record.addColumn(e.getKey(), e.getValue());
			}
			
			record.incrementVersion();
			
			return Single.just(CompareAndSetResult.of(true));
			
		}
		
		return Single.just(CompareAndSetResult.of(false));

	}
	
	private static UnitRecord newRecord(CompareAndSetOperation operation) {
		
		UnitRecord record = new UnitRecord();
		
		for (Map.Entry<String, ByteBuf> e : operation.getValues().entrySet()) {
			record.addColumn(e.getKey(), e.getValue());
		}
		
		record.incrementVersion();
		
		return record;
	}
	
}
