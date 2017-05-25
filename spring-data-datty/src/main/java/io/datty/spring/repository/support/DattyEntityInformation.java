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
package io.datty.spring.repository.support;

import org.springframework.data.repository.core.EntityInformation;

import io.datty.spring.core.DattyId;

/**
 * DattyEntityInformation
 * 
 * @author Alex Shvid
 *
 */

public interface DattyEntityInformation<T> extends EntityInformation<T, DattyId> {

	/**
	 * Gets cache name of the entity
	 * 
	 * @return not null cache name
	 */
	
	String getCacheName();
	
	/**
	 * Checks if entity has minorKey
	 * 
	 * @return true if entity has minorKey
	 */
	
	boolean hasMinorKey();
	
	/**
	 * Gets minor key
	 * 
	 * @return not null minor key
	 */
	
	String getMinorKey();
	
	/**
	 * Checks if entity has to be serialized with tag numbers
	 * 
	 * @return true if tags are enabled
	 */
	
	boolean useTags();
	
	/**
	 * Checks if entity has to be copy all fields during deserialization
	 * 
	 * @return true if copy mode enabled
	 */
	
	boolean copy();
	
	/**
	 * Gets time to live in seconds for update operations
	 * 
	 * @return ttl in seconds or 0
	 */
	
	int ttlSeconds();
	
	/**
	 * Gets timeout in milliseconds for all operations
	 * 
	 * @return timeout in milliseconds or 0
	 */
	
	int timeoutMillis();

}
