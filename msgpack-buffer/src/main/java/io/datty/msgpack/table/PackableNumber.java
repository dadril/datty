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

/**
 * Immutable number interface
 * 
 * @author Alex Shvid
 *
 */

public interface PackableNumber extends PackableValue<PackableNumber> {

	/**
	 * Gets type of the Msg number
	 * 
	 * @return not null type
	 */
	
	PackableNumberType getType();
	
	/**
	 * Gets long, even if value is double, it will be converted to long
	 * 
	 * @return long value
	 */
	
	long asLong();
	
	/**
	 * Gets double, even if value is long, it will be converted to double
	 * 
	 * @return double value
	 */
	
	double asDouble();
	
	/**
	 * Add number
	 * 
	 * @param number - number to add
	 * @return new immutable Msg number
	 */
	
	PackableNumber add(PackableNumber otherNumber);
	PackableNumber add(long otherLongValue);
	PackableNumber add(double otherDoubleValue);

	/**
	 * Subtract number
	 * 
	 * @param number - number to add
	 * @return new immutable Msg number
	 */
	
	PackableNumber subtract(PackableNumber otherNumber);
	PackableNumber subtract(long otherLongValue);
	PackableNumber subtract(double otherDoubleValue);

}
