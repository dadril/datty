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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name annotation to define details about property identification and serialization
 * 
 * @author Alex Shvid
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface DattyProperty {

	/**
	 * The name of the property that overrides field name.
	 * 
	 * @return
	 */
	String name() default "";
	
	/**
	 * The unique code of the property
	 * 
	 * @return
	 */
	int code() default 0;
	
	/**
	 * Other names of the property
	 * 
	 * @return array of names
	 */
	
	String[] otherNames() default {};
	
	/**
	 * Copy bytes from the buffers
	 * 
	 * @return true if copy all bytes for reads and writes
	 */
	
	boolean copy() default true;
	
}
