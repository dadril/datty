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
 * Base interface for all queries
 * 
 * @author Alex Shvid
 *
 */

public interface DattyQuery {

	/**
	 * Gets cache name
	 * 
	 * @return not null cache name
	 */
	
	String getCacheName();
	
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
	 * Gets query code
	 * 
	 * @return not null query code
	 */
	
	QuCode getCode();

	/**
	 * Query code enum
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public enum QuCode {
		
		STATEMENT(1),
		PREPARED_STATEMENT(2);
		
		private final int opcode;
		
		private QuCode(int opcode) {
			this.opcode = opcode;
		}

		public int getCode() {
			return opcode;
		}
		
		public static QuCode findByCode(int code) {
			for (QuCode v : values()) {
				if (v.getCode() == code) {
					return v;
				}
			}
			return null;
		}
		
		public static int max() {
			int max = Integer.MIN_VALUE;
			for (QuCode v : values()) {
				if (max < v.getCode()) {
					max = v.getCode();
				}
			}
			return max;
		}
		
	}
	
}
