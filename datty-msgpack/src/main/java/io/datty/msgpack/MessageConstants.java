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
package io.datty.msgpack;

/**
 * MessageConstants
 * 
 * @author Alex Shvid
 *
 */

public final class MessageConstants {

	private MessageConstants() {
	}

	/**
	 * Using starting index of array similar to LUA language
	 */
	
	public static volatile int ARRAY_START_INDEX = 1;
	
}
