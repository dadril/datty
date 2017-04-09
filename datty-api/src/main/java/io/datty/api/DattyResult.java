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

import io.datty.api.DattyOperation.OpCode;

/**
 * Base interface for all results
 * 
 * @author dadril
 *
 */

public interface DattyResult {
	
	/**
	 * Gets operation associated with result
	 * 
	 * @return not null datty operation
	 */
	
	DattyOperation getOperation();

	/**
	 * Gets result code
	 * 
	 * @return not null result code
	 */
	
	ResCode getCode();

	/**
	 * Result code enum
	 * 
	 * @author dadril
	 *
	 */
	
	public enum ResCode {
		
		VOID(1),
		ERROR(2),
		BOOL(3),
		BOOLMAP(4),
		LONG(5),
		VALUE(6),
		RECORD(7);
		
		private final int rescode;
		
		private ResCode(int rescode) {
			this.rescode = rescode;
		}

		public int getCode() {
			return rescode;
		}
		
		public static ResCode findByCode(int code) {
			for (ResCode v : values()) {
				if (v.getCode() == code) {
					return v;
				}
			}
			return null;
		}
		
		public static int max() {
			int max = Integer.MIN_VALUE;
			for (ResCode v : values()) {
				if (max < v.getCode()) {
					max = v.getCode();
				}
			}
			return max;
		}
		
	}
}
