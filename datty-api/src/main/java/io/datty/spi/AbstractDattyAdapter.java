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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.datty.api.Datty;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.operation.TypedOperation;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func0;
import rx.functions.Func1;

public abstract class AbstractDattyAdapter implements Datty {
	
	@Override
	public Single<List<DattyResult>> executeBatch(List<DattyOperation> operations) {
		
		if (operations.isEmpty()) {
			return Single.just(Collections.<DattyResult>emptyList());
		}
		
		int size = operations.size();
		
		final List<DattyResult> resultList = new ArrayList<DattyResult>(size); 
		
		final List<Completable> joinList = new ArrayList<Completable>(size);
		
		for (int i = 0; i != size; ++i) {
			
			final int sequenceNumber = i;
			
			@SuppressWarnings("rawtypes")
			TypedOperation op = (TypedOperation) operations.get(i);
			
			resultList.add(op.getFallback());
			
			Single<DattyResult> singleResult = doExecute(op).map(new Func1<DattyResult, DattyResult>() {
				
				public DattyResult call(DattyResult res) {
					resultList.set(sequenceNumber, res);
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
		
		
		return result;
	}

	@Override
	public Observable<DattyResult> executeSequence(Observable<DattyOperation> operations) {
		
		Observable<DattyResult> results = operations.flatMap(new Func1<DattyOperation, Observable<DattyResult>>() {
			
			public Observable<DattyResult> call(DattyOperation op) {
				return doExecute(op).toObservable();
			}
			
		});
		
		return results;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Single<DattyResult> doExecute(DattyOperation operation) {
		TypedOperation op = (TypedOperation) operation;
		return (Single<DattyResult>) execute(op);
	}
	
}
