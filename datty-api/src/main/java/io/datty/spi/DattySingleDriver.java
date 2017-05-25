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
package io.datty.spi;

import java.util.concurrent.TimeUnit;

import io.datty.api.DattyConstants;
import io.datty.api.DattyError;
import io.datty.api.DattySingle;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.TypedResult;
import io.datty.support.exception.DattyOperationException;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Server side for correct exceptions handling and SLA
 * 
 * @author Alex Shvid
 *
 */

public class DattySingleDriver implements DattySingle {
	
	private final DattySingle single;
	
	public DattySingleDriver(DattySingle single) {
		this.single = single;
	}
	
	/**
	 * Configurable parameters
	 */
	
	public int getMaxConcurrentTries() {
		return DattyConstants.MAX_CONCURRENT_TRIES;
	}
	
	@Override
	public <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(final O operation) {
		
		Single<R> result = single.execute(operation);
		
		return addPostProcessing(operation, result);
		
	}

	private <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> addPostProcessing(final O operation,
			Single<R> result) {
		
		if (operation.hasTimeoutMillis()) {
			
			result = result.timeout(operation.getTimeoutMillis(), TimeUnit.MILLISECONDS, 
					Single.<R>error(new DattyOperationException(DattyError.ErrCode.TIMEOUT, operation)));
			
		}

		result = result.retry(new Func2<Integer, Throwable, Boolean>() {

			public Boolean call(Integer attempts, Throwable e) {

				if (e instanceof DattyError && ((DattyError) e).getErrorCode() == DattyError.ErrCode.CONCURRENT_UPDATE) {
					return attempts < getMaxConcurrentTries();
				}

				return false;
			}
			
		});
		
		result = result.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable t) {

				if (!(t instanceof DattyOperationException)) {
					throw new DattyOperationException(DattyError.ErrCode.UNKNOWN, operation, t);
				}
				
			}
			
		});
		
		return result;
	}

	@Override
	public <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(Single<O> operation) {

		return operation.flatMap(new Func1<O, Single<R>>() {

			@Override
			public Single<R> call(O op) {
				return execute(op);
			}
			
		});
		
	}
	
	
	
}
