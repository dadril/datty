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

import io.datty.api.DattyError.ErrCode;
import io.datty.api.DattyQuery;
import io.datty.api.DattyStatement;
import io.datty.api.result.RecordResult;
import io.datty.support.exception.DattyOperationException;
import rx.Observable;

/**
 * AerospikeDattyQuery
 * 
 * @author Alex Shvid
 *
 */


public class AerospikeDattyQuery implements DattyQuery {

	private final AerospikeDattyManager manager;
	
	public AerospikeDattyQuery(AerospikeDattyManager manager) {
		this.manager = manager;
	}

	@Override
	public Observable<RecordResult> query(DattyStatement statement) {
		return Observable.error(new DattyOperationException(ErrCode.UNKNOWN_OPERATION, statement.getCode().name(), statement));
	}

}
