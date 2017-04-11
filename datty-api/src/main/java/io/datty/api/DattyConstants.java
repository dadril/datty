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

/**
 * Datty Constants
 * 
 * @author Alex Shvid
 *
 */

public final class DattyConstants {

	private DattyConstants() {
	}

	/**
	 * Default/unset parameter for TimeToLive in seconds
	 */

	public static final int UNSET_TTL = 0;

	/**
	 * Default/unset parameter for SLA timeout in milliseconds
	 */

	public static final int UNSET_TIMEOUT = 0;

	/**
	 * Default/unset parameter for version of the value
	 */

	public static final long UNSET_VERSION = 0L;

	/**
	 * Default/unset error code
	 */

	public static final int UNSET_ERROR = 0;

	/**
	 * Default max concurrent tries parameter
	 */

	public static final int MAX_CONCURRENT_TRIES = 5;
	
}
