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
import io.datty.api.UpdatePolicy;
import io.datty.api.result.TypedResult;

/**
 * AbstractUpdateOperation
 * 
 * @author Alex Shvid
 *
 */

abstract class AbstractUpdateOperation<O extends TypedOperation<O, R>, R extends TypedResult<O>> 
	extends AbstractOperation<O, R> implements UpdateOperation<O, R> {

	protected int ttlSeconds = DattyConstants.UNSET_TTL;
	protected UpdatePolicy updatePolicy = UpdatePolicy.MERGE;
	
	@Override
	public boolean hasTtlSeconds() {
		return ttlSeconds != DattyConstants.UNSET_TTL;
	}

	@Override
	public int getTtlSeconds() {
		return ttlSeconds;
	}

	public O setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return castThis();
	}
	
	@Override
	public UpdatePolicy getUpdatePolicy() {
		return updatePolicy;
	}

	public O setUpdatePolicy(UpdatePolicy updatePolicy) {
		this.updatePolicy = updatePolicy;
		return castThis();
	}
	
}
