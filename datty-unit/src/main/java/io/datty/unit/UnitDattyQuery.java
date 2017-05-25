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

import io.datty.api.DattyResult;
import io.datty.api.DattyQuery;
import io.datty.api.operation.QueryOperation;
import rx.Observable;

/**
 * UnitDattyQuery
 * 
 * @author Alex Shvid
 *
 */

public class UnitDattyQuery implements DattyQuery {

	private final ConcurrentMap<String, UnitCache> cacheMap;
	
	public UnitDattyQuery(ConcurrentMap<String, UnitCache> cacheMap) {
		this.cacheMap = cacheMap;
	}
	
	@Override
	public Observable<DattyResult> executeQuery(QueryOperation operation) {
		return Observable.just(null);
	}

}
