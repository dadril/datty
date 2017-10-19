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
import io.datty.api.operation.HeadOperation;
import io.datty.api.operation.FetchOperation;
import io.datty.api.operation.PutOperation;

/**
 * Set interface
 * 
 * @author Alex Shvid
 *
 */

public interface DattySet {

	/**
	 * Gets parent datty manager instance
	 * 
	 * @return not null cache manager
	 */

	DattyManager getDattyManager();

	/**
	 * Gets set name
	 * 
	 * @return not null set name
	 */

	String getName();

	/**
	 * Gets set properties
	 * 
	 * @return not null properties of the set
	 */

	Properties getProperties();

	/**
	 * Update set properties 
	 * 
	 * @param props - not null set properties
	 */

	void setProperties(Properties props);

	/**
	 * Gets set statistics for monitoring
	 * 
	 * @return not null map
	 */

	Map<String, String> getStatistics();
	
	/**
	 * Creates head operation
	 * 
	 * @param majorKey - major key
	 * @return not null exists operation
	 */
	
	HeadOperation head(String majorKey);
	
	/**
	 * Creates get operation
	 * 
	 * @param majorKey - major key
	 * @return not null get operation
	 */
	
	FetchOperation fetch(String majorKey);
	
	/**
	 * Creates set operation
	 * @param majorKey - major key
	 * @return not null set operation
	 */
	
	PutOperation put(String majorKey);
	
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
