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

import rx.Single;

/**
 * Base interface for all operations
 * 
 * All operations are going to be executed in single thread in event loop
 * 
 * @author dadril
 *
 */

public interface DattyOperation {

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
	 * Gets major key (partition key)
	 * 
	 * @return major key
	 */
	
	String getMajorKey();
	
	/**
	 * Gets SLA timeout in milliseconds for operation
	 * 
	 * @return timeout milliseconds or 0
	 */
	
	int getTimeoutMillis();
	
	/**
	 * Gets status of the operation
	 * 
	 * @return true if completed
	 */
	
	boolean isCompleted();
	
	/**
	 * Gets operation result
	 * 
	 * @return not null result or throw DattyUncompletedException
	 */
	
	DattyResult getResult();
	
	/**
	 * Resets operation, removes result
	 */
	
	void reset();
	
	/**
	 * Completes operation
	 * 
	 * @param result - not null result
	 */
	
	void complete(DattyResult result);
	
	/**
	 * Executes operation if is created from Cache
	 * 
	 * @param datty - datty instance
	 */

	Single<DattyResult> execute();
	
	/**
	 * Gets operation code
	 * 
	 * @return not null operation code
	 */
	
	OpCode getCode();

	/**
	 * Operation code enum
	 * 
	 * @author dadril
	 *
	 */
	
	public enum OpCode {
		
		EXISTS(1),
		GET(2),
		SET(3),
		COMPARE_AND_SET(4),
		EXECUTE(5);
		
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
		
	}
	
}
