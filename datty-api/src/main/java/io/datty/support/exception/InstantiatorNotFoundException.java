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

public class InstantiatorNotFoundException extends DattyException {

	private static final long serialVersionUID = -6014419327711605806L;

	public InstantiatorNotFoundException(String typeName) {
		super("instantiator not found for " + typeName);
	}

	public InstantiatorNotFoundException(String typeName, Throwable t) {
		super("instantiator not found for " + typeName, t);
	}

}
