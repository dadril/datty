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
package io.datty.support;

import io.datty.api.DattyError;
import io.datty.api.operation.TypedOperation;
import io.datty.api.result.TypedResult;
import io.datty.support.exception.DattySingleException;

/**
 * DattyErrors
 * 
 * @author Alex Shvid
 *
 */

public final class DattyErrors {

	private DattyErrors() {
	}
	
	public static <O extends TypedOperation<O, R>, R extends TypedResult<O>> R throwOnError(R result) {
		
		if (result instanceof DattyError) {
			
			DattyError error = (DattyError) result;
			
			throw new DattySingleException(error, result.getOperation());
			
		}
		
		return result;
		
	}
	
}
