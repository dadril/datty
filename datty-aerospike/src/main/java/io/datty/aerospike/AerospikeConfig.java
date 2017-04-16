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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import com.aerospike.client.Host;
import com.aerospike.client.async.AsyncClientPolicy;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import io.datty.support.exception.DattyFactoryException;

/**
 * AerospikeConfig
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeConfig {

	private final Properties props;
	private final String namespace;
	private final AsyncClientPolicy clientPolicy;
	private final List<Host> hosts;
	
	public AerospikeConfig(Properties props) {
		this.props = props;
		this.namespace = props.getProperty(AerospikePropertyKeys.NAMESPACE, AerospikeConstants.DEFAULT_NAMESPACE);
		this.clientPolicy = createClientPolicy(props);
		this.hosts = createHosts(props);
		
		if (this.hosts.isEmpty()) {
			throw new DattyFactoryException("hosts are empty, please define property server1 or system property aerospike.server1 for unit tests");
		}
	}

	public String getNamespace() {
		return namespace;
	}

	public Properties getProperties() {
		return props;
	}

	public AsyncClientPolicy getClientPolicy() {
		return clientPolicy;
	}

	public Host[] getHosts() {
		return hosts.toArray(new Host[hosts.size()]);
	}
	
	public QueryPolicy copyQueryPolicy() {
		return copyQueryPolicy(clientPolicy.queryPolicyDefault);
	}
	
	public static QueryPolicy copyQueryPolicy(QueryPolicy src) {
		QueryPolicy dest = new QueryPolicy();
		copyPolicy(dest, src);
		dest.maxConcurrentNodes = src.maxConcurrentNodes;
		dest.recordQueueSize = src.recordQueueSize;
		return dest;
	}
	
	public WritePolicy copyWritePolicy() {
		return new WritePolicy(clientPolicy.writePolicyDefault);
	}
	
	public BatchPolicy copyBatchPolicy() {
		return new BatchPolicy(clientPolicy.batchPolicyDefault);
	}
	
	private static void copyPolicy(Policy dest, Policy src) {
		dest.priority = src.priority;
		dest.consistencyLevel = src.consistencyLevel;
		dest.replica = src.replica;
		dest.timeout = src.timeout;
		dest.maxRetries = src.maxRetries;
		dest.sleepBetweenRetries = src.sleepBetweenRetries;
		dest.sendKey = src.sendKey;
	}

	private static AsyncClientPolicy createClientPolicy(Properties props) {
		AsyncClientPolicy policy = new AsyncClientPolicy();
		
		ExecutorService sharedThreadPool = (ExecutorService) props.get(AerospikePropertyKeys.SHARED_THREAD_POOL);
		if (sharedThreadPool != null) {
			policy.threadPool = sharedThreadPool;
			policy.sharedThreadPool = true;
		}
		
		String val = props.getProperty(AerospikePropertyKeys.USER);
		if (val != null) {
			policy.user = val;
		}
		
		val = props.getProperty(AerospikePropertyKeys.PASSWORD);
		if (val != null) {
			policy.password = val;
		}
		
		val = props.getProperty(AerospikePropertyKeys.TIMEOUT);
		if (val != null) {
			policy.timeout = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.MAX_THREADS);
		if (val != null) {
			policy.maxThreads = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.MAX_SOCKET_IDLE);
		if (val != null) {
			policy.maxSocketIdle = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.TEND_INTERVAL);
		if (val != null) {
			policy.tendInterval = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.FAIL_IF_NOT_CONNECTED);
		if (val != null) {
			policy.failIfNotConnected = Boolean.parseBoolean(val);
		}
		
		policy.readPolicyDefault  = fillReadPolicy(new Policy(), props);
		policy.writePolicyDefault = fillWritePolicy(new WritePolicy(), props);
		policy.queryPolicyDefault = fillQueryPolicy(new QueryPolicy(), props);
		policy.batchPolicyDefault = fillBatchPolicy(new BatchPolicy(), props);
		policy.infoPolicyDefault  = fillInfoPolicy(new InfoPolicy(), props);
		
		policy.asyncReadPolicyDefault = policy.readPolicyDefault;
		policy.asyncWritePolicyDefault = policy.writePolicyDefault;
		policy.asyncQueryPolicyDefault = policy.queryPolicyDefault;
		policy.asyncBatchPolicyDefault = policy.batchPolicyDefault;
		policy.asyncTaskThreadPool = sharedThreadPool;
		
		val = props.getProperty(AerospikePropertyKeys.ASYNC_MAX_COMMAND_ACTION);
		if (val != null) {
			policy.asyncMaxCommandAction = AerospikeEnums.parseMaxCommandAction(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.ASYNC_MAX_COMMANDS);
		if (val != null) {
			policy.asyncMaxCommands = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.ASYNC_SELECTOR_TIMEOUT);
		if (val != null) {
			policy.asyncSelectorTimeout = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.ASYNC_SELECTOR_THREADS);
		if (val != null) {
			policy.asyncSelectorThreads = Integer.parseInt(val);
		}
		
		return policy;
	}
	
	public static Policy fillReadPolicy(Policy policy, Properties props) {
		fillPolicy(policy, AerospikePropertyKeys.READ_PREFIX, props);
		return policy;
	}
	
	public static WritePolicy fillWritePolicy(WritePolicy policy, Properties props) {
		
		policy.recordExistsAction = RecordExistsAction.UPDATE;
		policy.generationPolicy = GenerationPolicy.NONE;
		
		fillPolicy(policy, AerospikePropertyKeys.WRITE_PREFIX, props);
		
		String val = props.getProperty(AerospikePropertyKeys.WRITE_PREFIX + AerospikePropertyKeys.COMMIT_LEVEL);
		if (val != null) {
			policy.commitLevel = AerospikeEnums.parseCommitLevel(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.WRITE_PREFIX + AerospikePropertyKeys.EXPIRATION);
		if (val != null) {
			policy.expiration = Integer.parseInt(val);
		}
		
		return policy;
		
	}

	public static QueryPolicy fillQueryPolicy(QueryPolicy policy, Properties props) {
		
		fillPolicy(policy, AerospikePropertyKeys.QUERY_PREFIX, props);
		
		String val = props.getProperty(AerospikePropertyKeys.READ_PREFIX + AerospikePropertyKeys.MAX_CONCURRENT_NODES);
		if (val != null) {
			policy.maxConcurrentNodes = Integer.parseInt(val);
		}
		
		val = props.getProperty(AerospikePropertyKeys.READ_PREFIX + AerospikePropertyKeys.RECORD_QUEUE_SIZE);
		if (val != null) {
			policy.recordQueueSize = Integer.parseInt(val);
		}
		
		return policy;
	}
	
	public static BatchPolicy fillBatchPolicy(BatchPolicy policy, Properties props) {
		
		fillPolicy(policy, AerospikePropertyKeys.BATCH_PREFIX, props);
		
		String val = props.getProperty(AerospikePropertyKeys.BATCH_PREFIX + AerospikePropertyKeys.MAX_CONCURRENT_THREADS);
		if (val != null) {
			policy.maxConcurrentThreads = Integer.parseInt(val);
		}
		
		return policy;
	}
	
	public static InfoPolicy fillInfoPolicy(InfoPolicy policy, Properties props) {
		
		String val = props.getProperty(AerospikePropertyKeys.INFO_PREFIX + AerospikePropertyKeys.TIMEOUT);
		if (val != null) {
			policy.timeout = Integer.parseInt(val);
		}
		
		return policy;
	}
	
	private static void fillPolicy(Policy policy, String prefix, Properties props) {
			
		String val = props.getProperty(prefix + AerospikePropertyKeys.PRIORITY);
		if (val != null) {
			policy.priority = AerospikeEnums.parsePriority(val);
		}
		
		val = props.getProperty(prefix + AerospikePropertyKeys.CONSISTENCY_LEVEL);
		if (val != null) {
			policy.consistencyLevel = AerospikeEnums.parseConsistencyLevel(val);
		}
		
		val = props.getProperty(prefix + AerospikePropertyKeys.TIMEOUT);
		if (val != null) {
			policy.timeout = Integer.parseInt(val);
		}
		
		val = props.getProperty(prefix + AerospikePropertyKeys.MAX_RETRIES);
		if (val != null) {
			policy.maxRetries = Integer.parseInt(val);
		}

		val = props.getProperty(prefix + AerospikePropertyKeys.SLEEP_BETWEEN_RETRIES);
		if (val != null) {
			policy.sleepBetweenRetries = Integer.parseInt(val);
		}
		
		val = props.getProperty(prefix + AerospikePropertyKeys.SEND_KEY);
		if (val != null) {
			policy.sendKey = Boolean.parseBoolean(val);
		}
		
	}
	
	private List<Host> createHosts(Properties props) {
		
		List<Host> list = new ArrayList<Host>();
		
		for (int i = 1; i != AerospikeConstants.MAX_HOSTS; ++i) {
			
			String server = props.getProperty(AerospikePropertyKeys.SERVER_PREFIX + i);
			
			if (server == null) {
				break;
			}

			String host = server;
			int portNumber = AerospikeConstants.DEFAULT_PORT;
			
			int separatorIndex = server.indexOf(':');
			if (separatorIndex != -1) {
				host = server.substring(0, separatorIndex);
				portNumber = Integer.parseInt(server.substring(separatorIndex+1));
			}
			
			list.add(new Host(host, portNumber));
		}
		
		return list;
	}

}
