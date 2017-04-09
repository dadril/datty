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

import java.io.PrintWriter;
import java.io.StringWriter;

import io.datty.api.DattyError;

/**
 * All errors must be based on this interface
 * 
 * ErrorResult
 * 
 * @author dadril
 *
 */

public class ErrorResult extends AbstractResult implements DattyError {

	private int errorCode;
	private String errorMessage;
	private String errorStacktrace;

	public ErrorResult() {
		this.errorCode = DattyError.ErrCode.UNKNOWN.getCode();
	}

	public ErrorResult(DattyError.ErrCode errcode) {
		this.errorCode = errcode.getCode();
	}
	
	public ErrorResult(DattyError.ErrCode errcode, String message) {
		this.errorCode = errcode.getCode();
		this.errorMessage = message;
	}

	public ErrorResult(DattyError.ErrCode errcode, Throwable e) {
		this(errcode.getCode(), e);
	}
	
	public ErrorResult(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public ErrorResult(int errorCode, String message) {
		this.errorCode = errorCode;
		this.errorMessage = message;
	}
	
	public ErrorResult(int errorCode, Throwable e) {
		this.errorCode = errorCode;
		this.errorMessage = e.getMessage();
		this.errorStacktrace = getStackTrace(e);
	}

	public static String getStackTrace(Throwable t) {
		StringWriter w = new StringWriter();
		if (t != null) {
			t.printStackTrace(new PrintWriter(w));
		}
		return w.toString();
	}
	
	public static ErrorResult of(DattyError.ErrCode errcode) {
		return new ErrorResult(errcode); 
	}
	
	public static ErrorResult of(DattyError.ErrCode errcode, String message) {
		return new ErrorResult(errcode, message); 
	}

	public static ErrorResult of(int errcode) {
		return new ErrorResult(errcode); 
	}
	
	public static ErrorResult of(int errcode, String message) {
		return new ErrorResult(errcode, message); 
	}
	
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

	@Override
	public String toString() {
		return "ErrorResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorStacktrace="
				+ errorStacktrace + "]";
	}
	
}
