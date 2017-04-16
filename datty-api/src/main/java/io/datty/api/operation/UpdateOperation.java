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

import io.datty.api.UpdatePolicy;
import io.datty.api.result.TypedResult;

/**
 * UpdateOperation
 * 
 * @author Alex Shvid
 *
 */

public interface UpdateOperation<O extends TypedOperation<O, R>, R extends TypedResult<O>> extends TypedOperation<O, R> {

	/**
	 * Gets update policy
	 * 
	 * @return not null update policy
	 */
	
	UpdatePolicy getUpdatePolicy();
	
	/**
	 * Returns true if defined time to live in seconds
	 * 
	 * @return flag 
	 */
	
	boolean hasTtlSeconds();
	
	/**
	 * Gets TTL time to live in seconds
	 * 
	 * @return ttl or 0
	 */
	
	int getTtlSeconds();
	
}
