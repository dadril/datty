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
package io.datty.api.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.datty.api.UpdatePolicy;
import io.datty.api.result.BooleanResult;
import io.netty.buffer.ByteBuf;

/**
 * CompareAndSetOperation
 * 
 * Compare And Set Operation
 * 
 * @author dadril
 *
 */

public class CompareAndSetOperation extends
	AbstractUpdateOperation<CompareAndSetOperation, BooleanResult> implements UpdateOperation {

	/**
	 * Old version of the record
	 */
	
	private Version oldVersion;
	
	/**
	 * Key is the minorKey, value is payload
	 */
	private Map<String, ByteBuf> newValues = null;
	
	private UpdatePolicy updatePolicy = UpdatePolicy.MERGE;

	public CompareAndSetOperation(String cacheName) {
		super(cacheName);
	}

	public CompareAndSetOperation(String cacheName, String majorKey) {
		super(cacheName, majorKey);
	}

	public Version getOldVersion() {
		return oldVersion;
	}

	public CompareAndSetOperation setOldVersion(Version oldVersion) {
		this.oldVersion = oldVersion;
		return this;
	}

	public CompareAndSetOperation addValue(String minorKey, ByteBuf valueOrNull) {
		if (newValues == null) {
			this.newValues = Collections.singletonMap(minorKey, valueOrNull);
		}
		else if (newValues.size() == 1) {
			this.newValues = new HashMap<>(newValues);
			this.newValues.put(minorKey, valueOrNull);
		}
		else {
			this.newValues.put(minorKey, valueOrNull);
		}
		return this;
	}
	
	public UpdatePolicy getUpdatePolicy() {
		return updatePolicy;
	}

	public CompareAndSetOperation setUpdatePolicy(UpdatePolicy updatePolicy) {
		this.updatePolicy = updatePolicy;
		return this;
	}

	@Override
	public OpCode getCode() {
		return OpCode.COMPARE_AND_SET;
	}

	@Override
	public String toString() {
		return "CompareAndSetOperation [oldVersion=" + oldVersion + ", newValues=" + newValues + ", updatePolicy="
				+ updatePolicy + ", cacheName=" + cacheName + ", superKey=" + superKey + ", majorKey=" + majorKey
				+ ", timeoutMillis=" + timeoutMillis + "]";
	}
	
	
}
