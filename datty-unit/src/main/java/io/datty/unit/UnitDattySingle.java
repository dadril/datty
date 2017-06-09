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
import io.datty.api.DattySingle;
import io.datty.api.operation.SetOperation;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.RecordResult;
import io.datty.api.result.TypedResult;
import io.datty.support.exception.DattyOperationException;
import io.datty.unit.executor.OperationExecutor;
import io.datty.unit.executor.SetOperationExecutor;
import io.datty.unit.executor.UnitExecutors;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

/**
 * UnitDattySingle
 * 
 * @author Alex Shvid
 *
 */

public final class UnitDattySingle implements DattySingle {

	private final ConcurrentMap<String, UnitSet> setMap;
	
	public UnitDattySingle(ConcurrentMap<String, UnitSet> setMap) {
		this.setMap = setMap;
	}

	@Override
	public <O extends SetOperation> Observable<RecordResult> execute(O operation) {
		
		String setName = operation.getSetName();
		if (setName == null) {
			return Observable.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty setName", operation));
		}
		
		UnitSet set = setMap.get(setName);
		
		if (set == null) {
			return Observable.error(new DattyOperationException(ErrCode.SET_NOT_FOUND, setName, operation));
		}
		
		SetOperationExecutor<O> executor = UnitExecutors.findSetExecutor(operation.getCode());
		
		if (executor == null) {
			return Observable.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, "unknown operation: " + operation.getCode().name(), operation));
		}
		
		return executor.execute(set.getRecordMap(), operation);
		
	}



	@Override
	public <O extends TypedOperation<O, R>, R extends TypedResult<O>> Single<R> execute(O operation) {
		
		String setName = operation.getSetName();
		if (setName == null) {
			return Single.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty setName", operation));
		}
		
		UnitSet set = setMap.get(setName);
		
		if (set == null) {
			return Single.error(new DattyOperationException(ErrCode.SET_NOT_FOUND, setName, operation));
		}
		
		String majorKey = operation.getMajorKey();
		if (majorKey == null) {
			return Single.error(new DattyOperationException(ErrCode.BAD_ARGUMENTS, "empty majorKey", operation));
		}
		
		OperationExecutor<O, R> executor = UnitExecutors.findExecutor(operation.getCode());
		
		if (executor == null) {
			return Single.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, "unknown operation: " + operation.getCode().name(), operation));
		}
		
		return executor.execute(set.getRecordMap(), operation);

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
