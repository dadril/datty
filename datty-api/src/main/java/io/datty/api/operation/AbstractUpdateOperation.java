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
package io.datty.api.operation;

import io.datty.api.DattyConstants;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;

/**
 * Abstract update operation
 * 
 * @author dadril
 *
 */

public class AbstractUpdateOperation<O extends DattyOperation, R extends DattyResult> 
	extends AbstractOperation<O, R> implements UpdateOperation {

	private int ttlSeconds = DattyConstants.UNSET_TTL;
	
	public AbstractUpdateOperation(String cacheName) {
		super(cacheName);
	}
	
	public AbstractUpdateOperation(String cacheName, String majorKey) {
		super(cacheName, majorKey);
	}
	
	@Override
	public int getTtlSeconds() {
		return ttlSeconds;
	}

	@SuppressWarnings("unchecked")
	public O setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return (O) this;
	}
	
}
