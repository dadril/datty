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

import java.util.Map;
import java.util.Properties;

import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.operation.ExistsOperation;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.SetOperation;

/**
 * Cache interface
 * 
 * @author dadril
 *
 */

public interface Cache {

	/**
	 * Gets parent cache manager instance
	 * 
	 * @return not null cache manager
	 */

	CacheManager getCacheManager();

	/**
	 * Gets cache name
	 * 
	 * @return not null cache name
	 */

	String getCacheName();

	/**
	 * Gets cache properties
	 * 
	 * @return not null properties of the cache
	 */

	Properties getCacheProperties();

	/**
	 * Update cache properties 
	 * 
	 * @param props - not null cache properties
	 */

	void setCacheProperties(Properties props);

	/**
	 * Gets cache statistics for monitoring
	 * 
	 * @return not null map
	 */

	Map<String, String> getCacheStatistics();
	
	/**
	 * Creates get operation
	 * 
	 * @param majorKey - major key
	 * @return not null get operation
	 */
	
	GetOperation get(String majorKey);

	/**
	 * Creates exists operation
	 * 
	 * @param majorKey - major key
	 * @return not null exists operation
	 */
	
	ExistsOperation exists(String majorKey);
	
	/**
	 * Creates set operation
	 * @param majorKey - major key
	 * @return not null set operation
	 */
	
	SetOperation set(String majorKey);
	
	/**
	 * Creates compare and set operation
	 * @param majorKey - major key
	 * @return not null compare and set operation
	 */
	
	CompareAndSetOperation compareAndSet(String majorKey);
	
	/**
	 * Creates execute operation
	 * @param majorKey - major key
	 * @return not null execute operation
	 */
	
	ExecuteOperation execute(String majorKey);
	
}
