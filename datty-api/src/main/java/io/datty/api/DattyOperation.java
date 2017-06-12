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
 * Base interface for all operations
 * 
 * All operations are going to be executed in single thread in event loop
 * 
 * @author Alex Shvid
 *
 */

public interface DattyOperation {

	/**
	 * Gets set name
	 * 
	 * @return not null set name
	 */
	
	String getSetName();
	
	/**
	 * Checks if super key is defined
	 * 
	 * @return true if exists
	 */
	
	boolean hasSuperKey();
	
	/**
	 * Gets super key (cross database routing key) if exists
	 * 
	 * Best usage is the country code
	 * 
	 * @return super key or null
	 */
	
	String getSuperKey();
	
	/**
	 * Returns true if defined timeout millis
	 * 
	 * @return flag
	 */
	
	boolean hasTimeoutMillis();
	
	/**
	 * Gets SLA timeout in milliseconds for operation
	 * 
	 * @return timeout milliseconds or 0
	 */
	
	int getTimeoutMillis();
	
	/**
	 * Gets operation code
	 * 
	 * @return not null operation code
	 */
	
	OpCode getCode();

	/**
	 * Operation code enum
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public enum OpCode {
		
		HEAD(1),
		GET(2),
		PUT(3),
		REMOVE(4),
		COMPARE_AND_SET(5),
		EXECUTE(6),
		
		SCAN(7),
		SIZE(8),
		CLEAR(9),
		
		SELECT(10),
		UPDATE(11),
		DELETE(12);
		
		private final int opcode;
		
		private OpCode(int opcode) {
			this.opcode = opcode;
		}

		public int getCode() {
			return opcode;
		}
		
		public static OpCode findByCode(int code) {
			for (OpCode v : values()) {
				if (v.getCode() == code) {
					return v;
				}
			}
			return null;
		}
		
		public static int max() {
			int max = Integer.MIN_VALUE;
			for (OpCode v : values()) {
				if (max < v.getCode()) {
					max = v.getCode();
				}
			}
			return max;
		}
		
	}
	
}
