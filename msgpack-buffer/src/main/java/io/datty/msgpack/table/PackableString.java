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
 * Immutable String
 * 
 * That can be two types: UFT-8 and BYTES strings
 * 
 * @author Alex Shvid
 *
 */

public interface PackableString extends PackableValue<PackableString> {

	/**
	 * Gets type of the string
	 * 
	 * @return not null type, can by UTF8 or Bytes
	 */
	
	PackableStringType getType();

	/**
	 * Gets value as utf8 string
	 * 
	 * @return utf8 string
	 */
	
	String toUtf8();
	
	/**
	 * Gets value as byte array
	 * 
	 * @param copy - copy bytes or not
	 * @return not null bytes
	 */
	
	byte[] getBytes(boolean copy);
	
}
