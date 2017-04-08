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
package io.datty.api.result;

import io.datty.api.DattyError;
import io.datty.support.DattyErrorCodes;

/**
 * All errors must be based on this interface
 * 
 * ErrorResult
 * 
 * @author dadril
 *
 */

public class ErrorResult extends AbstractResult implements DattyError {

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
	public ResCode getCode() {
		return ResCode.ERROR;
	}

}
