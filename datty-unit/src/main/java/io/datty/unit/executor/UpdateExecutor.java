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
import io.datty.api.DattyRecord;
import io.datty.api.DattyValue;
import io.datty.api.operation.Push;
import io.datty.api.result.PushResult;
import io.datty.api.version.Version;
import io.datty.api.version.VersionType;
import io.datty.support.exception.DattyOperationException;
import io.datty.unit.UnitRecord;
import rx.Single;

/**
 * UpdateExecutor
 * 
 * @author Alex Shvid
 *
 */

public enum UpdateExecutor implements OperationExecutor<Push, PushResult> {

	INSTANCE;
	
	@Override
	public Single<PushResult> execute(ConcurrentMap<String, UnitRecord> recordMap, Push operation) {
		
		DattyRecord rec = operation.getRecord();
		
		UnitRecord record = recordMap.get(operation.getMajorKey());
		
		if (record == null) {
			
			if (operation.useVersion() && !isZeroVersion(operation.getVersion())) {
				return Single.just(new PushResult().setUpdated(false));
			}
			
			if (rec == null || rec.isEmpty()) {
				return Single.just(new PushResult().setUpdated(true));
			}
			
			record = new UnitRecord(rec.getValues());
			UnitRecord c = recordMap.putIfAbsent(operation.getMajorKey(), record);
			if (c != null) {
				return Single.error(new DattyOperationException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
			}
			else {
				return Single.just(new PushResult().setUpdated(true));
			}
		}
		else {
			
			if (operation.useVersion() && !isVersionMatch(operation, record)) {
				return Single.just(new PushResult().setUpdated(false));
			}
			
			Map<String, DattyValue> values = rec != null ? rec.getValues() : Collections.<String, DattyValue>emptyMap();
			UnitRecord newRecord = new UnitRecord(record, values, operation.getUpdatePolicy());
			
			boolean updated = newRecord.isEmpty() ? 
					recordMap.remove(operation.getMajorKey(), record) :
					recordMap.replace(operation.getMajorKey(), record, newRecord);
			
			if (!updated) {
				return Single.error(new DattyOperationException(DattyError.ErrCode.CONCURRENT_UPDATE, operation));
			}
			
			return Single.just(new PushResult().setUpdated(true));
		}
	}
	
	private boolean isVersionMatch(Push operation, UnitRecord record) {
		return operation.hasVersion() && operation.getVersion().equals(record.getVersion());
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