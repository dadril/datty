package io.datty.msgpack.lite;

import java.util.List;

/**
 * LiteValueExpression
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

public interface LiteValueExpression {

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
