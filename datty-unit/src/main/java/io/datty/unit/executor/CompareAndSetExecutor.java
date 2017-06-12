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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.DattyError;
import io.datty.api.DattyRow;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.result.CompareAndSetResult;
import io.datty.api.version.Version;
import io.datty.api.version.VersionType;
import io.datty.support.exception.DattyOperationException;
import io.datty.unit.UnitRecord;
import io.netty.buffer.ByteBuf;
import rx.Single;

/**
 * CompareAndSetExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum CompareAndSetExecutor implements OperationExecutor<CompareAndSetOperation, CompareAndSetResult> {

	INSTANCE;

	@Override
	public Single<CompareAndSetResult> execute(ConcurrentMap<String, UnitRecord> recordMap, CompareAndSetOperation operation) {
		
		DattyRow row = operation.getRow();
		Map<String, ByteBuf> values = row != null ? row.getValues() : Collections.<String, ByteBuf>emptyMap();
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		if (record == null) {
			
			if (isZeroVersion(operation.getVersion())) {
				record = new UnitRecord(values);
				
				if (record.isEmpty()) {
					// do nothing
					return Single.just(new CompareAndSetResult().set(true));
				}
				
				boolean updated = null == recordMap.putIfAbsent(operation.getMajorKey(), record);
				
				if (!updated) {
					return Single.error(new DattyOperationException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
				}
				else {
					return Single.just(new CompareAndSetResult().set(updated));
				}
				
			}
			
		}
		else if (operation.hasVersion() && operation.getVersion().equals(record.getVersion())) {

			UnitRecord newRecord = new UnitRecord(record, values, operation.getUpdatePolicy());
			
			boolean updated = newRecord.isEmpty() ?
					recordMap.remove(operation.getMajorKey(), record) :
					recordMap.replace(operation.getMajorKey(), record, newRecord);
			
			if (!updated) {
				return Single.error(new DattyOperationException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
			}
			else {
				return Single.just(new CompareAndSetResult().set(updated));
			}
			
		}
		
		return Single.just(new CompareAndSetResult().set(false));

	}
	
	private boolean isZeroVersion(Version version) {
		
		if (version == null) {
			return true;
		}
		
		if (version.getType() == VersionType.LONG && version.asLong() == 0L) {
			return true;
		}
		
		return false;
	}
	
}
