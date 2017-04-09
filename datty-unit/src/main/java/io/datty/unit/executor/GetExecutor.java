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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyResult;
import io.datty.api.operation.GetOperation;
import io.datty.api.result.ErrorResult;
import io.datty.api.result.RecordResult;
import io.datty.unit.UnitRecord;
import io.netty.buffer.ByteBuf;
import rx.Single;

public enum GetExecutor implements OperationExecutor<GetOperation> {

	INSTANCE;
	
	@Override
	public Single<DattyResult> execute(ConcurrentMap<String, UnitRecord> recordMap, GetOperation operation) {
		
		String majorKey = operation.getMajorKey();
		if (majorKey == null) {
			return Single.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty majorKey"));
		}
		
		UnitRecord record = recordMap.get(majorKey);
		if (record == null) {
			return Single.just(new RecordResult());
		}

		if (operation.isAllMinorKeys()) {
			return Single.just(new RecordResult(record.getVersion(), record.getColumnMap()));
		}
		else {
			
			Map<String, ByteBuf> map = new HashMap<String, ByteBuf>();
			for (String minorKey : operation.getMinorKeys()) {
				ByteBuf value = record.getColumn(minorKey);
				if (value != null) {
					map.put(minorKey, value);
				}
			}
			
			return Single.just(new RecordResult(record.getVersion(), map));
		}

	}
	
}