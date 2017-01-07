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

import java.util.Properties;

/**
 * StoreManager interface
 * 
 * @author dadril
 *
 */

public interface StoreManager extends Datty {

  /**
   * Gets the name of the store manager
   * 
   * @return not null name
   */
  
  String getName();
  
  /**
   * Finds existing store
   * 
   * @param storeName - store name
   * @return store instance or null
   */
  
  Store findStore(String storeName);
  
  /**
   * Gets existing store or creates a new one
   * 
   * @param storeName - store name
   * @return not null store
   */
  
  Store getStore(String storeName, Properties props, StoreExistsAction action);
	
}
