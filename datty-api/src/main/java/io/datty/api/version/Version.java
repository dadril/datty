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
package io.datty.api.version;

/**
 * Abstract interface that provides version of record
 * 
 * @author Alex Shvid
 *
 */

public interface Version {

	/**
	 * Gets version type
	 * 
	 * @return not null version type
	 */
	
	VersionType getType();
	
	/**
	 * Gets version value as a long
	 * 
	 * @return not null version
	 */
	
	long asLong();
	
	/**
	 * Gets version value as a string
	 * 
	 * @return not null version
	 */
	
	String asString();
	
	
}
