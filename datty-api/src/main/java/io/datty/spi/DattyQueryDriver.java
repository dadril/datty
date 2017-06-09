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
import io.datty.api.DattyQuery;
import io.datty.api.DattyStatement;
import io.datty.api.result.RecordResult;
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
	public Observable<RecordResult> query(final DattyStatement statement) {
		
		Observable<RecordResult> result = delegate.query(statement);
		
		if (statement.hasTimeoutMillis()) {
			
			result = result.timeout(statement.getTimeoutMillis(), TimeUnit.MILLISECONDS, 
					Observable.<RecordResult>error(new DattyOperationException(DattyError.ErrCode.TIMEOUT, statement)));
			
		}
		
		result = result.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable t) {

				if (!(t instanceof DattyOperationException)) {
					throw new DattyOperationException(DattyError.ErrCode.UNKNOWN, statement, t);
				}
				
			}
			
		});
		
		return result;
	}
	
}
