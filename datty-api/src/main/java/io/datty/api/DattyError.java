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
 * Error interface for result operation
 * 
 * @author Alex Shvid
 *
 */

public interface DattyError {

	/**
	 * Gets error code
	 * 
	 * @return error code number
	 */

	ErrCode getErrorCode();

	/**
	 * Gets error message if has
	 * 
	 * @return error code message or null
	 */

	String getErrorMessage();

	/**
	 * Gets exception stacktrace if has
	 * 
	 * @return exception stacktrace or null
	 */

	String getErrorStackTrace();

	/**
	 * Enum of known error codes
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public enum ErrCode {
		
		UNKNOWN(1),
		CACHE_NOT_FOUND(2),
		TIMEOUT(3),
		CONCURRENT_UPDATE(4),
		BAD_ARGUMENTS(5),
		UNKNOWN_OPERATION(6);
		
		private final int errcode;
		
		private ErrCode(int errcode) {
			this.errcode = errcode;
		}

		public int getCode() {
			return errcode;
		}
		
		public static ErrCode findByCode(int code) {
			for (ErrCode v : values()) {
				if (v.getCode() == code) {
					return v;
				}
			}
			return null;
		}
		
		public static int max() {
			int max = Integer.MIN_VALUE;
			for (ErrCode v : values()) {
				if (max < v.getCode()) {
					max = v.getCode();
				}
			}
			return max;
		}
		
	}
	
}
