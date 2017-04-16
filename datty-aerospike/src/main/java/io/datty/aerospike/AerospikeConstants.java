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
package io.datty.aerospike;

/**
 * AerospikeConstants
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeConstants {

	private AerospikeConstants() {
	}
	
	/**
	 * Default name for the CacheManager
	 */
	
	public static final String DEFAULT_NAME = "aerospike";
	
	public static final String DEFAULT_NAMESPACE = "test";
	
	public static final int MAX_HOSTS = 100;
	
	public static final int DEFAULT_PORT = 3000;
	
}
