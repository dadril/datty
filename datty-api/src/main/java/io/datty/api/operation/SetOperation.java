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
import io.datty.api.result.VoidResult;
import io.netty.buffer.ByteBuf;

/**
 * Set operation
 * 
 * @author dadril
 *
 */

public class SetOperation extends AbstractUpdateOperation<SetOperation, VoidResult> implements UpdateOperation {

	/**
	 * Key is the minorKey, value is payload
	 */
	private Map<String, ByteBuf> newValues = null;
	
	private UpdatePolicy updatePolicy = UpdatePolicy.MERGE;
	
	public SetOperation(String storeName) {
		super(storeName);
	}

	public SetOperation(String storeName, String majorKey) {
		super(storeName);
		setMajorKey(majorKey);
	}

	public SetOperation addValue(String minorKey, ByteBuf valueOrNull) {
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

	public SetOperation setUpdatePolicy(UpdatePolicy updatePolicy) {
		this.updatePolicy = updatePolicy;
		return this;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.SET;
	}

	@Override
	public String toString() {
		return "SetOperation [newValues=" + newValues + ", updatePolicy=" + updatePolicy + ", cacheName=" + cacheName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
