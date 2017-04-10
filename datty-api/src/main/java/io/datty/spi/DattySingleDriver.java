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
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.DattySingle;
import io.datty.support.exception.DattyConcurrentException;
import io.datty.support.exception.DattyErrorException;
import io.datty.support.exception.DattyTimeoutException;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Server side
 * 
 * @author dadril
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
	public <O extends DattyOperation<O, R>, R extends DattyResult<O>> Single<R> execute(final O operation) {
		
		Single<R> result = single.execute(operation);
		
		if (operation.hasTimeoutMillis()) {
			
			result = result.timeout(operation.getTimeoutMillis(), TimeUnit.MILLISECONDS, 
					Single.<R>error(new DattyTimeoutException(operation)));
			
		}

		result = result.retry(new Func2<Integer, Throwable, Boolean>() {

			public Boolean call(Integer attempts, Throwable e) {

				if (e instanceof DattyConcurrentException) {
					return attempts < getMaxConcurrentTries();
				}

				return false;
			}
			
		});
		
		result.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable t) {
				if (!(t instanceof DattyErrorException)) {
					throw new DattyErrorException(DattyError.ErrCode.UNKNOWN, operation, t);
				}
			}
			
		});
		
		return result;
		
	}
	
	/*

	@Override
	public Single<List<DattyResult>> executeBatch(List<DattyOperation> operations) {
		return executeBatch(operations, DattyConstants.UNSET_TIMEOUT);
	}

	@Override
	public Single<List<DattyResult>> executeBatch(final List<DattyOperation> operations, int timeoutMillis) {

		if (operations.isEmpty()) {
			return Single.just(Collections.<DattyResult>emptyList());
		}
		
		int size = operations.size();
		
		final List<DattyResult> resultList = new ArrayList<DattyResult>(size); 
		final List<Completable> joinList = new ArrayList<Completable>(size);
		
		for (int i = 0; i != size; ++i) {
			
			Single<DattyResult> singleResult = execute(operations.get(i).setSequenceNumber(i));
			
			singleResult = singleResult.map(new Func1<DattyResult, DattyResult>() {
				
				public DattyResult call(DattyResult res) {
					resultList.add(res.getOperation().getSequenceNumber(), res);
					return res;
				}
				
			});
			
			joinList.add(singleResult.toCompletable());
			
		}
		
		Single<List<DattyResult>> result = Completable.merge(joinList).toSingle(new Func0<List<DattyResult>> () {
			
			public List<DattyResult> call() {
				return resultList;
			}
			
		});
		
		if (timeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			
			result = result.timeout(timeoutMillis, TimeUnit.MILLISECONDS, 
					Single.just(timeoutsOf(operations.size())));
			
		}
		
		return result;
	}

	private List<DattyResult> timeoutsOf(int size) {
		List<DattyResult> list = new ArrayList<DattyResult>(size);
		for (int i = 0; i != size; ++i) {
			list.add(ErrorResult.of(ErrCode.TIMEOUT));
		}
		return list;
	}
	
	@Override
	public Observable<DattyResult> executeSequence(Observable<DattyOperation> operations) {
		return executeSequence(operations, DattyConstants.UNSET_TIMEOUT);
	}
	
	@Override
	public Observable<DattyResult> executeSequence(Observable<DattyOperation> operations, int totalTimeoutMillis) {
		
		Observable<DattyResult> results = operations.flatMap(new Func1<DattyOperation, Observable<DattyResult>>() {
			
			public Observable<DattyResult> call(DattyOperation op) {
				return execute(op).toObservable();
			}
			
		});
		
		if (totalTimeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			
			results = results.timeout(totalTimeoutMillis, TimeUnit.MILLISECONDS, 
					Observable.just(ErrorResult.of(ErrCode.TIMEOUT)));
			
		}
		
		return results;
	}

	@Override
	public Observable<DattyResult> streamOut(DattyKey key) {
		return streamOut(key, DattyConstants.UNSET_TIMEOUT);
	}
	
	@Override
	public Observable<DattyResult> streamOut(DattyKey key, int totalTimeoutMillis) {
		
		Observable<DattyResult> results = doStreamOut(key);
		
		results = results.onErrorReturn(new Func1<Throwable, DattyResult>() {

			public DattyResult call(Throwable e) {
				return exceptionToError(e);
			}
			
		});
		
		if (totalTimeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			
			results = results.timeout(totalTimeoutMillis, TimeUnit.MILLISECONDS, 
					Observable.just(ErrorResult.of(ErrCode.TIMEOUT)));
			
		}
		
		return results;
				
	}
	@Override
	public Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value) {
		return streamIn(key, value, DattyConstants.UNSET_TIMEOUT);
	}
	
	@Override
	public Single<DattyResult> streamIn(DattyKey key, Observable<ByteBuf> value, int totalTimeoutMillis) {
		
		Single<DattyResult> result = doStreamIn(key, value);
		
		result = result.onErrorReturn(new Func1<Throwable, DattyResult>() {

			public DattyResult call(Throwable e) {
				return exceptionToError(e);
			}
			
		});
		
		if (totalTimeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			
			result = result.timeout(totalTimeoutMillis, TimeUnit.MILLISECONDS, 
					Single.just(ErrorResult.of(ErrCode.TIMEOUT)));
			
		}
		
		return result;
	}

	private static DattyResult exceptionToError(Throwable e) {
		
		if (e instanceof DattyTimeoutException) {
			return ErrorResult.of(ErrCode.TIMEOUT);
		}
		
		if (e instanceof DattyConcurrentException) {
			return ErrorResult.of(ErrCode.CONCURRENT_RETIRES);
		}

		return new ErrorResult(ErrCode.UNKNOWN, e);
	}
	
	*/
	
}
