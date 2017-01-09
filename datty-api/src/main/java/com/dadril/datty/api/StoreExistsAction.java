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
package com.dadril.datty.api;

/**
 * Type of the action
 * 
 * @author dadril
 *
 */

public enum StoreExistsAction {

	/**
	 * Creates a new store only with given properties
	 * 
	 * throws StoreExistsException if store already exists
	 */

	CREATE_ONLY,

	/**
	 * Creates a new store if not exists, otherwise returns existing store
	 * without changing it's properties
	 */

	CREATE_IF_NOT_EXISTS,

	/**
	 * Creates a new store if not exists, otherwise updates properties for
	 * existing one
	 * 
	 * throws StoreMismatchException if it is impossible to update properties
	 * runtime and they are different
	 * 
	 */

	UPDATE;

}
