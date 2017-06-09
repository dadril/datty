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
package io.datty.api;

import io.datty.api.result.RecordResult;
import rx.Observable;

/**
 * DattyQuery
 * 
 * @author Alex Shvid
 *
 */

public interface DattyQuery {

	/**
	 * Retrieve or update data by using statement
	 * 
	 * @param statement - datty statement
	 * @return not null observable of results
	 */
	
	Observable<RecordResult> query(DattyStatement statement);
	
}
