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

import java.util.Set;

import org.msgpack.value.Value;

/**
 * PackableMessage
 * 
 * Event contains both header and body.
 * 
 * @author Alex Shvid
 *
 */

public interface PackableMessage {

	/**
	 * All headers are under this key
	 */
	
	static final String HEADER_KEY = "header";

	/**
	 * All payloads are under this key
	 */
	
	static final String BODY_KEY = "body";
		
	/**
	 * Checks if message is empty
	 * 
	 * @return true if both header and body are empty
	 */
	
	boolean isEmpty();
	
	/**
	 * Adds header to the message
	 * 
	 * @param key - name of the header
	 * @param value - value of the header
	 * @return this
	 */
	
	PackableMessage addHeader(String key, String value);
	
	/**
	 * Gets header
	 * 
	 * @param key - header key
	 * @return null or header value
	 */
	
  String getHeader(String key);
	
  /**
   * Gets all header keys from the message
   * 
   * @return not null set
   */
  
  Set<String> getHeaderKeys();
  
	/**
	 * Adds payload to the message
	 * 
	 * @param key - name of the payload
	 * @param payload - message value
	 * @return this
	 */
	
	PackableMessage addPayload(String key, PackableValue<?> payload);
	
	/**
	 * Adds payload to the message
	 * 
	 * @param key - name of the payload
	 * @param payload - byte array
	 * @param copy - copy if needed
	 * @return this
	 */
	
	PackableMessage addPayload(String key, byte[] payload, boolean copy);

	/**
	 * Adds payload as UTF-8 string
	 * 
	 * @param key - name of the payload
	 * @param payload - byte array
	 * @return this
	 */
	
	PackableMessage addPayloadUtf8(String key, String payload);
	
	/**
	 * Gets typed payload
	 * 
	 * @param key - payload key
	 * @return null or payload value
	 */
	
	<T extends PackableValue<?>> T getTypedPayload(String key);
	
	/**
	 * Gets payload
	 * 
	 * @param key - payload key
	 * @return null or payload value
	 */
	
	PackableValue<?> getPayload(String key);
	
	/**
	 * Gets payload
	 * 
	 * @param key - payload key
	 * @param copy - copy bytes if needed
	 * @return null or payload value
	 */
	
	byte[] getPayload(String key, boolean copy);

	/**
	 * Gets payload as UTF-8 string
	 * 
	 * @param key - payload key
	 * @return null or payload value
	 */
	
	String getPayloadUtf8(String key);
	
	/**
	 * Gets payload keys from the message
	 *  
	 * @return not null set 
	 */
	
	Set<String> getPayloadKeys();
	
	/**
	 * Converts message to json format
	 * 
	 * @return not null json
	 */
	
	String toJson();
	
	/**
	 * Serialize message to MsgPack
	 * 
	 * @return not null byte array
	 */
	
	byte[] toByteArray();
	
	/**
	 * Converts STEP message to MsgPack value
	 * 
	 * @return not null value object
	 */
	
	Value toValue();
	
}
