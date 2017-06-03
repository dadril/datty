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
package io.datty.aerospike.info;

/**
 * AerospikeInfoRequest
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeInfoRequest {

	private final String namespace;
	private final String setName;
	
	public AerospikeInfoRequest(String namespace, String setName) {
		this.namespace = namespace;
		this.setName = setName;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getSetName() {
		return setName;
	}
	
	public String toCommand() {
		StringBuilder str = new StringBuilder();
		str.append("sets/").append(namespace).append("/").append(setName);
		return str.toString();
	}

	@Override
	public String toString() {
		return "AerospikeInfoRequest [namespace=" + namespace + ", setName=" + setName + "]";
	}
	
}
