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

import java.util.concurrent.ConcurrentMap;

import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.DattySingle;
import io.datty.support.exception.CacheNotFoundException;
import io.datty.support.exception.DattyErrorException;
import io.datty.unit.executor.OperationExecutor;
import io.datty.unit.executor.UnitExecutors;
import rx.Single;

public final class UnitDattySingle implements DattySingle {

	private final ConcurrentMap<String, UnitCache> cacheMap;
	
	public UnitDattySingle(ConcurrentMap<String, UnitCache> cacheMap) {
		this.cacheMap = cacheMap;
	}
	
	@Override
	public <O extends DattyOperation<O, R>, R extends DattyResult<O>> Single<R> execute(O operation) {
		
		String cacheName = operation.getCacheName();
		UnitCache cache = cacheMap.get(cacheName);
		
		if (cache == null) {
			return Single.error(new CacheNotFoundException(cacheName, operation));
		}
		
		String majorKey = operation.getMajorKey();
		if (majorKey == null) {
			return Single.error(new DattyErrorException(ErrCode.BAD_ARGUMENTS, "empty majorKey", operation));
		}
		
		OperationExecutor<O, R> executor = UnitExecutors.findExecutor(operation.getCode());
		
		if (executor == null) {
			return Single.error(new DattyErrorException(ErrCode.UNKNOWN_OPERATION, "unknown operation: " + operation.getCode().name(), operation));
		}
		
		return executor.execute(cache.getRecordMap(), operation);

	}

}
