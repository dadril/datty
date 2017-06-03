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
package io.datty.aerospike;

import java.util.Properties;

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import io.datty.api.DattyOperation;
import io.datty.api.operation.UpdateOperation;

/**
 * AerospikeSetConfig
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeSetConfig {

	private final AerospikeConfig parent;
	private final Properties properties;
	
	private final QueryPolicy queryPolicy;
	private final WritePolicy writePolicy;
	
	public AerospikeSetConfig(AerospikeConfig parent, Properties properties) {
		this.parent = parent;
		this.properties = properties;
		this.queryPolicy = AerospikeConfig.fillQueryPolicy(parent.copyQueryPolicy(), properties);
		this.writePolicy = AerospikeConfig.fillWritePolicy(parent.copyWritePolicy(), properties);
	}

	public Properties getProperties() {
		return properties;
	}

	public QueryPolicy getQueryPolicy(boolean copy) {
		return copy ? AerospikeConfig.copyQueryPolicy(queryPolicy) : queryPolicy;
	}
	
	public QueryPolicy getQueryPolicy(DattyOperation operation, boolean copy) {
		if (operation.hasTimeoutMillis()) {
			QueryPolicy newQueryPolicy = AerospikeConfig.copyQueryPolicy(queryPolicy);
			newQueryPolicy.timeout = operation.getTimeoutMillis();
			return newQueryPolicy;
		}
		return getQueryPolicy(copy);
	}

	public WritePolicy getWritePolicy(boolean copy) {
		return copy ? new WritePolicy() : writePolicy;
	}
	
	public WritePolicy getWritePolicy(UpdateOperation<?, ?> operation, boolean copy) {
		
		WritePolicy newWritePolicy = new WritePolicy(writePolicy);
		
		switch(operation.getUpdatePolicy()) {
		
			case MERGE:
				writePolicy.recordExistsAction = RecordExistsAction.UPDATE;
				break;
				
			case REPLACE:
				writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
				break;
	  }
		
		if (operation.hasTtlSeconds()) {
			newWritePolicy.expiration = operation.getTtlSeconds();
		}
		
		if (operation.hasTimeoutMillis()) {
			newWritePolicy.timeout = operation.getTimeoutMillis();
		}
		
		return newWritePolicy;
	}

	public AerospikeConfig getParent() {
		return parent;
	}

}
