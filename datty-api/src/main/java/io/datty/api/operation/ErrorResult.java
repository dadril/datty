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

import io.datty.api.SingleResult;
import io.datty.api.payload.ErrorPayload;
import io.datty.api.payload.SingleResultPayload;
import io.datty.api.payload.SingleResultType;
import io.datty.support.DattyErrorCodes;

/**
 * All errors must be based on this interface
 * 
 * ErrorResult
 * 
 * @author dadril
 *
 */

public class ErrorResult implements SingleResult<Error>, Error {

	private int errorCode = DattyErrorCodes.UNKNOWN_ERROR;
	private String errorMessage;
	private String errorStacktrace;

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	public ErrorResult setErrorCode(int errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	public ErrorResult setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	@Override
	public String getErrorStacktrace() {
		return errorStacktrace;
	}

	public ErrorResult setErrorStacktrace(String errorStacktrace) {
		this.errorStacktrace = errorStacktrace;
		return this;
	}

	@Override
	public Error get() {
		return this;
	}

	@Override
	public void writeTo(SingleResultPayload payload) {
		payload.setType(SingleResultType.ERROR);
		ErrorPayload error = new ErrorPayload();
		error.setErrorCode(errorCode);
		error.setErrorMessage(errorMessage);
		error.setErrorStacktrace(errorStacktrace);
		payload.setError(error);
	}

	public enum Instantiator implements SingleInstantiator {

		INSTANCE;

		@Override
		public ErrorResult parseFrom(SingleResultPayload payload) {

			ErrorPayload err = payload.getError();

			ErrorResult res = new ErrorResult();

			if (err != null) {
				res.setErrorCode(err.getErrorCode());
				res.setErrorMessage(err.getErrorMessage());
				res.setErrorStacktrace(err.getErrorStacktrace());
			}

			return res;
		}

	}
}
