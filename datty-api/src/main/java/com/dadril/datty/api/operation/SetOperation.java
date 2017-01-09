/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package com.dadril.datty.api.operation;

import com.dadril.datty.api.DattyConstants;
import com.dadril.datty.api.payload.SingleOperationPayload;
import com.dadril.datty.api.payload.SingleOperationType;

/**
 * Set operation
 * 
 * @author dadril
 *
 */

public class SetOperation extends AbstractOperation<SetOperation, VoidResult> {

	private final byte[] newValueOrNull;
	private int ttlSeconds = DattyConstants.UNSET_TTL;

	public SetOperation(String storeName, byte[] newValueOrNull) {
		super(storeName);
		this.newValueOrNull = newValueOrNull;
	}

	public SetOperation(String storeName, String majorKey, byte[] newValueOrNull) {
		super(storeName);
		setMajorKey(majorKey);
		this.newValueOrNull = newValueOrNull;
	}

	public byte[] getNewValueOrNull() {
		return newValueOrNull;
	}

	public int getTtlSeconds() {
		return ttlSeconds;
	}

	public SetOperation setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
		return this;
	}

	@Override
	public void writeTo(SingleOperationPayload op) {
		op.setType(SingleOperationType.SET);
		op.setNewValue(newValueOrNull);
		op.setTtlSeconds(ttlSeconds);
		writeAbstractFields(op);
	}

	public enum Instantiator implements SingleInstantiator {

		INSTANCE;

		@Override
		public SetOperation parseFrom(SingleOperationPayload payload) {

			SetOperation op = new SetOperation(payload.getStoreName(),
					payload.getNewValue());

			op.setSuperKey(payload.getSuperKey());
			op.setMajorKey(payload.getMajorKey());
			op.setMinorKey(payload.getMinorKey());
			op.setTtlSeconds(payload.getTtlSeconds());

			return op;
		}

	}

}
