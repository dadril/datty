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

import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyResult;
import io.datty.api.UpdatePolicy;
import io.datty.api.operation.SetOperation;
import io.datty.api.result.ErrorResult;
import io.datty.api.result.VoidResult;
import io.datty.unit.UnitRecord;
import io.netty.buffer.ByteBuf;
import rx.Single;

public enum SetExecutor implements OperationExecutor<SetOperation> {

	INSTANCE;
	
	@Override
	public Single<DattyResult> execute(ConcurrentMap<String, UnitRecord> recordMap, SetOperation operation) {

		String majorKey = operation.getMajorKey();
		if (majorKey == null) {
			return Single.just(ErrorResult.of(ErrCode.BAD_ARGUMENTS, "empty majorKey"));
		}
		
		UnitRecord record = recordMap.get(majorKey);
		if (record == null) {
			record = new UnitRecord();
			UnitRecord c = recordMap.putIfAbsent(majorKey, record);
			if (c != null) {
				record = c;
			}
		}

		if (operation.getUpdatePolicy() == UpdatePolicy.REPLACE) {
			record.clear();
		}
		
		for (Map.Entry<String, ByteBuf> e : operation.getValues().entrySet()) {
			record.setColumn(e.getKey(), e.getValue());
		}

		record.incrementVersion();
		
		return Single.just(new VoidResult());
	}
	
}