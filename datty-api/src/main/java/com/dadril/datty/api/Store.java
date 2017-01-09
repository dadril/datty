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

import java.util.Map;
import java.util.Properties;

/**
 * Store interface
 * 
 * @author dadril
 *
 */

public interface Store {

	/**
	 * Gets parent store manager instance
	 * 
	 * @return not null store manager
	 */

	StoreManager getStoreManager();

	/**
	 * Gets store name
	 * 
	 * @return not null store name
	 */

	String getStoreName();

	/**
	 * Gets store properties
	 * 
	 * @return not null properties of the store specific for store manager
	 */

	Properties getProperties();

	/**
	 * Update properties of the store
	 * 
	 * @param props
	 *            - not null properties specific for store manager
	 */

	void setProperties(Properties props);

	/**
	 * Gets statistics specific for store
	 * 
	 * @return not null map
	 */

	Map<String, String> getStatistics();

}
