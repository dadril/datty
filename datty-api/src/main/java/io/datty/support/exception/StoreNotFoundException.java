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
package io.datty.support.exception;

public class StoreNotFoundException extends DattyException {

	private static final long serialVersionUID = 4824538129120664380L;

	public StoreNotFoundException(String storeName) {
		super(storeName);
	}

	public StoreNotFoundException(String storeName, Throwable t) {
		super(storeName, t);
	}

}
