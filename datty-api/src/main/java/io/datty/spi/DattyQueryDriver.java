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

import io.datty.api.DattyError;
import io.datty.api.DattyResult;
import io.datty.api.DattyQuery;
import io.datty.api.operation.QueryOperation;
import io.datty.api.result.QueryResult;
import io.datty.support.exception.DattyOperationException;
import rx.Observable;
import rx.functions.Action1;

/**
 * Server side for correct exceptions handling and SLA
 * 
 * @author Alex Shvid
 *
 */

public class DattyQueryDriver implements DattyQuery {
	
	private final DattyQuery delegate;
	
	public DattyQueryDriver(DattyQuery delegate) {
		this.delegate = delegate;
	}
	

	@Override
	public Observable<DattyResult> executeQuery(final QueryOperation operation) {
		
		Observable<DattyResult> result = delegate.executeQuery(operation);
		
		if (operation.hasTimeoutMillis()) {
			
			result = result.timeout(operation.getTimeoutMillis(), TimeUnit.MILLISECONDS, 
					Observable.<QueryResult>error(new DattyOperationException(DattyError.ErrCode.TIMEOUT, operation)));
			
		}
		
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
	
}
