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
package io.datty.api.result;

import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;

/**
 * Abstract Result
 * 
 * @author dadril
 *
 */

public abstract class AbstractResult implements DattyResult {

	private DattyOperation operation;
	
	@Override
	public DattyOperation getOperation() {
		return operation;
	}

	protected void setOperation(DattyOperation operation) {
		this.operation = operation;
	}
	
}
