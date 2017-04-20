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
package io.datty.spring.mapping;

import org.springframework.data.mapping.PersistentEntity;

/**
 * DattyPersistentEntity
 * 
 * @author Alex Shvid
 *
 */

public interface DattyPersistentEntity<T> extends PersistentEntity<T, DattyPersistentProperty> {

	/**
	 * Gets the cache name defined in entity
	 * 
	 * @return not null cache name
	 */
	
	String getCacheName();
	
	/**
	 * Using to detect if minor key present in empty definition
	 * 
	 * @return true if has minor key
	 */
	
	boolean hasMinorKey();
	
	/**
	 * Gets minor key
	 * 
	 * @return minor key or empty string
	 */
	
	String getMinorKey();
	
	/**
	 * Gets time to live in seconds if defined
	 * 
	 * @return ttl or 0
	 */
	
	int getTtlSeconds();
	
	/**
	 * Gets timeout in milliseconds if defined
	 * 
	 * @return timeout or 0
	 */
	
	int getTimeoutMillis();
	
}
