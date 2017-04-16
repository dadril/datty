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

/**
 * AerospikePropertyKeys
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikePropertyKeys {

	private AerospikePropertyKeys() {
	}

	/**
	 * CacheManager constants
	 */
	
	public static final String NAME = "name";
	public static final String NAMESPACE = "namespace";
	
	// this property has ExecutorService
	public static final String SHARED_THREAD_POOL = "sharedThreadPool";
	
	/**
	 * Hosts
	 */
	
	public static final String ENDPOINT_PREFIX = "endpoint";
	public static final String HOST_SUFFIX = ".host";
	public static final String PORT_SUFFIX = ".port";
	
	/**
	 * Client properties
	 */
	
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	public static final String TIMEOUT = "timeout";
	public static final String MAX_THREADS = "maxThreads";
	public static final String MAX_SOCKET_IDLE = "maxSocketIdle";
	public static final String TEND_INTERVAL = "tendInterval";
	public static final String FAIL_IF_NOT_CONNECTED = "failIfNotConnected";

	public static final String ASYNC_MAX_COMMAND_ACTION = "asyncMaxCommandAction";
	public static final String ASYNC_MAX_COMMANDS = "asyncMaxCommands";
	public static final String ASYNC_SELECTOR_TIMEOUT = "asyncSelectorTimeout";
	public static final String ASYNC_SELECTOR_THREADS = "asyncSelectorThreads";
	
	
  public static final String PRIORITY = "priority";
  public static final String CONSISTENCY_LEVEL = "consistencyLevel";
  public static final String MAX_RETRIES = "maxRetries";
  public static final String SLEEP_BETWEEN_RETRIES = "sleepBetweenRetries";
  public static final String SEND_KEY = "sendKey";
  public static final String COMMIT_LEVEL = "commitLevel";
  public static final String EXPIRATION = "expiration";
  public static final String MAX_CONCURRENT_NODES = "maxConcurrentNodes";
  public static final String RECORD_QUEUE_SIZE = "recordQueueSize";
  public static final String MAX_CONCURRENT_THREADS = "maxConcurrentThreads";
  
  
  public static final String READ_PREFIX = "read.";
  public static final String WRITE_PREFIX = "write.";
  public static final String QUERY_PREFIX = "query.";
  public static final String BATCH_PREFIX = "batch.";
  public static final String INFO_PREFIX = "info.";
  
}
