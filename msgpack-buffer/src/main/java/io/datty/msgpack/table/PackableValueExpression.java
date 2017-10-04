/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table;

import java.util.List;

/**
 * PackableValueExpression
 * 
 * This class provides functionality similar in LUA to access object by expression language
 * 
 * Format of the expression language:
 * name.subname - using for access value in inner table
 * [1] - using to access value in int table
 * [1].subname - using to access inner table's value that are in int table
 * education[2].name - using to access 3 tables values
 * 
 * Indexes are always starting from 1 because of LUA
 * 
 * Important! Null friendly implementation
 * 
 * @author Alex Shvid
 *
 */

public interface PackableValueExpression {

	/**
	 * Check is the expression is empty
	 * 
	 * @return true if empty
	 */
	
	boolean isEmpty();
	
	/**
	 * Gets size of the keys in expression
	 * 
	 * @return size
	 */
	
	int size();
	
	/**
	 * Gets i-th element in expression
	 * 
	 * @param i - ith element
	 * @return key
	 */
	
	String get(int i);
	
	/**
	 * Gets the whole path
	 * 
	 * @return not null list
	 */
	
	List<String> getPath();
	
}
