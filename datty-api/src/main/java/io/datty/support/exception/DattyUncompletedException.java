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
package io.datty.support.exception;

/**
 * DattyUncompletedException
 * 
 * @author dadril
 *
 */

public class DattyUncompletedException extends DattyException {

	private static final long serialVersionUID = 5923609109806506268L;

	public DattyUncompletedException(String msg) {
		super(msg);
	}

	public DattyUncompletedException(String msg, Throwable t) {
		super(msg, t);
	}

	public DattyUncompletedException(Throwable t) {
		super(t);
	}

	
}
