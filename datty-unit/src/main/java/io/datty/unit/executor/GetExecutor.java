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

import io.datty.api.operation.GetOperation;
import io.datty.api.result.GetResult;
import io.datty.unit.UnitRecord;
import io.datty.unit.UnitValue;
import io.netty.buffer.ByteBuf;
import rx.Single;

public enum GetExecutor implements OperationExecutor<GetOperation, GetResult> {

	INSTANCE;
	
	@Override
	public Single<GetResult> execute(ConcurrentMap<String, UnitRecord> recordMap, GetOperation operation) {
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		if (record == null) {
			return Single.just(GetResult.absent());
		}

		if (operation.isAllMinorKeys()) {
			
			Map<String, ByteBuf> map = new HashMap<String, ByteBuf>();
			for (Map.Entry<String, UnitValue> e : record.getColumnMap().entrySet()) {
				map.put(e.getKey(), e.getValue().asByteBuf());
			}
			
			return Single.just(GetResult.of(record.getVersion(), map));
		}
		else {
			
			Map<String, ByteBuf> map = new HashMap<String, ByteBuf>();
			for (String minorKey : operation.getMinorKeys()) {
				UnitValue value = record.getColumn(minorKey);
				if (value != null) {
					map.put(minorKey, value.asByteBuf());
				}
			}
			
			return Single.just(GetResult.of(record.getVersion(), map));
		}

	}
	
}