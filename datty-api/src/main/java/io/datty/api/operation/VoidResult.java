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
package io.datty.api.operation;

import io.datty.api.SingleResult;
import io.datty.api.payload.SingleResultPayload;
import io.datty.api.payload.SingleResultType;

/**
 * VoidResult
 * 
 * @author dadril
 *
 */

public enum VoidResult implements SingleResult<Void> {

	VOID;

	@Override
	public Void get() {
		return null;
	}

	@Override
	public void writeTo(SingleResultPayload payload) {
		payload.setType(SingleResultType.VOID);
	}

	public enum Instantiator implements SingleInstantiator {

		INSTANCE;

		@Override
		public VoidResult parseFrom(SingleResultPayload payload) {
			return VoidResult.VOID;
		}

	}

}
