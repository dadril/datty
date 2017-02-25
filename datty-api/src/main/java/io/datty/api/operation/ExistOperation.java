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

import io.datty.api.payload.SingleOperationPayload;
import io.datty.api.payload.SingleOperationType;

/**
 * Exist operation
 * 
 * @author dadril
 *
 */

public class ExistOperation extends
		AbstractOperation<ExistOperation, BooleanResult> {

	public ExistOperation(String storeName) {
		super(storeName);
	}

	public ExistOperation(String storeName, String majorKey) {
		super(storeName);
		setMajorKey(majorKey);
	}

	@Override
	public void writeTo(SingleOperationPayload op) {
		op.setType(SingleOperationType.EXIST);
		writeAbstractFields(op);
	}

	public enum Instantiator implements SingleInstantiator {

		INSTANCE;

		@Override
		public ExistOperation parseFrom(SingleOperationPayload payload) {

			ExistOperation op = new ExistOperation(payload.getStoreName());

			op.setSuperKey(payload.getSuperKey());
			op.setMajorKey(payload.getMajorKey());
			op.setMinorKey(payload.getMinorKey());

			return op;
		}

	}
}
