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

import io.datty.api.DattyConstants;
import io.datty.api.operation.UpdateOperation;

/**
 * AerospikeCacheConfig
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeCacheConfig {

	private final AerospikeConfig parent;
	private final Properties cacheProperties;
	
	private final QueryPolicy queryPolicy;
	private final WritePolicy writePolicy;
	
	public AerospikeCacheConfig(AerospikeConfig parent, Properties cacheProperties) {
		this.parent = parent;
		this.cacheProperties = cacheProperties;
		this.queryPolicy = AerospikeConfig.fillQueryPolicy(parent.copyQueryPolicy(), cacheProperties);
		this.writePolicy = AerospikeConfig.fillWritePolicy(parent.copyWritePolicy(), cacheProperties);
	}

	public Properties getCacheProperties() {
		return cacheProperties;
	}

	public QueryPolicy getQueryPolicy() {
		return queryPolicy;
	}
	
	public QueryPolicy getQueryPolicy(int timeoutMillis) {
		if (timeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			QueryPolicy newQueryPolicy = AerospikeConfig.copyQueryPolicy(queryPolicy);
			newQueryPolicy.timeout = timeoutMillis;
			return newQueryPolicy;
		}
		return queryPolicy;
	}

	public WritePolicy getWritePolicy() {
		return writePolicy;
	}
	
	public WritePolicy getWritePolicy(int timeoutMillis, boolean copy) {
		if (copy || timeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			WritePolicy newWritePolicy = new WritePolicy(writePolicy);
			newWritePolicy.timeout = timeoutMillis;
			return newWritePolicy;
		}
		return writePolicy;
	}
	
	public WritePolicy getWritePolicy(UpdateOperation<?, ?> operation) {
		
		WritePolicy newWritePolicy = new WritePolicy(writePolicy);
		
		switch(operation.getUpdatePolicy()) {
		
			case MERGE:
				writePolicy.recordExistsAction = RecordExistsAction.UPDATE;
				break;
				
			case REPLACE:
				writePolicy.recordExistsAction = RecordExistsAction.REPLACE;
				break;
	  }
		
		int ttlSeconds = operation.getTtlSeconds();
		if (ttlSeconds != DattyConstants.UNSET_TTL) {
			newWritePolicy.expiration = ttlSeconds;
		}
		
		int timeoutMillis = operation.getTimeoutMillis();
		if (timeoutMillis != DattyConstants.UNSET_TIMEOUT) {
			newWritePolicy.timeout = timeoutMillis;
		}
		
		return newWritePolicy;
	}

	public AerospikeConfig getParent() {
		return parent;
	}

}
