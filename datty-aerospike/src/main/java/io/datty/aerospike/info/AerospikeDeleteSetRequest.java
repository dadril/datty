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
 * AerospikeDeleteSetRequest
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeDeleteSetRequest {
	
	private final String namespace;
	private final String setName;
	
	public AerospikeDeleteSetRequest(String namespace, String setName) {
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
		str.append("set-config:context=namespace;id=").append(namespace).append(";set=").append(setName).append(";set-delete=true;");
		return str.toString();
	}

	@Override
	public String toString() {
		return "AerospikeDeleteSetRequest [namespace=" + namespace + ", setName=" + setName + "]";
	}
	
}
