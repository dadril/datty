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
package com.dadril.datty.support.exception;

public class StoreExistsException extends DattyException {

  private static final long serialVersionUID = -6866410369647925540L;

  public StoreExistsException(String storeName) {
		super(storeName);
	}

	public StoreExistsException(String storeName, Throwable t) {
		super(storeName, t);
	}

}
