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
package io.datty.spring.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.Persistent;

import io.datty.api.DattyConstants;

/**
 * DattyEntity annotation
 * 
 * @author Alex Shvid
 *
 */

@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DattyEntity {
	
	/**
	 * Name of the set. 
	 * 
	 * @return set name or empty string
	 */

	String setName() default "";
	
	/**
	 * Time to live in seconds
	 * 
	 * @return ttl or 0
	 */
	
	int ttlSeconds() default DattyConstants.UNSET_TTL;
	
	/**
	 * SLA timeout milliseconds
	 * 
	 * @return timeout in milliseconds or 0
	 */
	
	int timeoutMillis() default DattyConstants.UNSET_TIMEOUT;
	
	/**
	 * Flag that is using to enable numeric keys in serialized message.
	 * 
	 * @return true if tags mode enabled.
	 */
	
	boolean numeric() default false;
		
}
