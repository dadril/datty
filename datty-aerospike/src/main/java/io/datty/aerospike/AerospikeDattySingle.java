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
package io.datty.aerospike;

import io.datty.aerospike.executor.AerospikeOperation;
import io.datty.aerospike.executor.AerospikeOperations;
import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattySingle;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.TypedResult;
import io.datty.support.exception.DattySingleException;
import rx.Single;

/**
 * AerospikeDattySingle
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeDattySingle implements DattySingle {

	private final AerospikeCacheManager cacheManager;
	
	public AerospikeDattySingle(AerospikeCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Override
	public <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(O operation) {

		String cacheName = operation.getCacheName();
		AerospikeCache cache = cacheManager.getAerospikeCache(cacheName);
		
		if (cache == null) {
			return Single.error(new DattySingleException(ErrCode.CACHE_NOT_FOUND, cacheName, operation));
		}
		
		String majorKey = operation.getMajorKey();
		if (majorKey == null) {
			return Single.error(new DattySingleException(ErrCode.BAD_ARGUMENTS, "empty majorKey", operation));
		}
		
		AerospikeOperation<O, R> aerospikeOperation = AerospikeOperations.find(operation.getCode());
		
		if (aerospikeOperation == null) {
			return Single.error(new DattySingleException(ErrCode.UNKNOWN_OPERATION, "unknown operation: " + operation.getCode().name(), operation));
		}
		
		return aerospikeOperation.execute(cache, operation);
		
	}

}
