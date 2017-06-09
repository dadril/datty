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

import java.util.Set;

/**
 * StatementOperation
 * 
 * @author Alex Shvid
 *
 */

public interface StatementOperation extends SetOperation {

	/**
	 * Is all keys are requested
	 * 
	 * @return true if all keys
	 */
	
	boolean isAllMinorKeys();
	
	/**
	 * Gets set of minor keys
	 * 
	 * @return not null set of minor keys to queries
	 */
	
	Set<String> getMinorKeys();
	
}
