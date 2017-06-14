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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.Persistent;

import io.datty.api.DattyConstants;

/**
 * Entity annotation
 * 
 * @author Alex Shvid
 *
 */

@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Entity {

	/**
	 * Name of the set. 
	 * 
	 * @return set name or empty string
	 */

	String setName() default "";
	
	/**
	 * Minor Key if defined.
	 * 
	 * If defined then entity will be placed in the particular minorKey,
	 * otherwise it will be stored in all minorKeys
	 * 
	 * @return minorKey or empty string
	 */
	
	String minorKey() default "";
	
	/**
	 * Flag that is using to enable Tags of the properties.
	 * In this case entity will be serialized in to integer map whereas all properties will be defined as tag numbers,
	 * the same like in protobuf.
	 * 
	 * Otherwise it will be used propertyName as a key
	 * 
	 * @return true if tags mode enabled.
	 */
	
	boolean numeric() default false;
	
	/**
	 * Copy bytes from the buffers
	 * 
	 * @return true if copy all bytes for reads and writes
	 */
	
	boolean copy() default false;

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
	
}
