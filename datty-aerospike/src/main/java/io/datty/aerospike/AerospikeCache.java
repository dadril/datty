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

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;

import io.datty.aerospike.support.ExceptionTransformer;
import io.datty.api.Cache;
import io.datty.api.CacheManager;
import io.datty.api.DattyError;
import io.datty.api.DattyKey;
import io.datty.api.DattyOperation;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.operation.ExistsOperation;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.PutOperation;
import io.datty.support.exception.DattySingleException;
import io.datty.support.exception.DattyStreamException;

/**
 * AerospikeCache
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeCache implements Cache {

	private final static AtomicReferenceFieldUpdater<AerospikeCache, AerospikeCacheConfig> CONFIG_UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(AerospikeCache.class, AerospikeCacheConfig.class, "config");
	
	private final AerospikeCacheManager parent;
	private final String cacheName;
	private volatile AerospikeCacheConfig config;
	
	protected AerospikeCache(AerospikeCacheManager parent, String cacheName, Properties cacheProperties) {
		this.parent = parent;
		this.cacheName = cacheName;
		this.config = new AerospikeCacheConfig(parent.getConfig(), cacheProperties);
	}
	
	public AerospikeCacheManager getParent() {
		return parent;
	}
	
	public AerospikeCacheConfig getConfig() {
		return config;
	}
	
	@Override
	public CacheManager getCacheManager() {
		return parent;
	}

	@Override
	public String getCacheName() {
		return cacheName;
	}

	@Override
	public Properties getCacheProperties() {
		return config.getCacheProperties();
	}

	@Override
	public void setCacheProperties(Properties newProps) {
		CONFIG_UPDATER.set(this, new AerospikeCacheConfig(parent.getConfig(), newProps));
	}

	@Override
	public Map<String, String> getCacheStatistics() {
		return Collections.emptyMap();
	}

	@Override
	public GetOperation get(String majorKey) {
		return new GetOperation(cacheName, majorKey).withDatty(parent.getDatty());
	}

	@Override
	public ExistsOperation exists(String majorKey) {
		return new ExistsOperation(cacheName, majorKey).withDatty(parent.getDatty());
	}

	@Override
	public PutOperation put(String majorKey) {
		return new PutOperation(cacheName, majorKey).withDatty(parent.getDatty());
	}

	@Override
	public CompareAndSetOperation compareAndSet(String majorKey) {
		return new CompareAndSetOperation(cacheName, majorKey).withDatty(parent.getDatty());
	}

	@Override
	public ExecuteOperation execute(String majorKey) {
		return new ExecuteOperation(cacheName, majorKey).withDatty(parent.getDatty());
	}
	
	@Override
	public String toString() {
		return "AerospikeCache [name=" + cacheName + "]";
	}
	
	public ExceptionTransformer<DattySingleException> singleExceptionTransformer(final DattyOperation operation, final boolean filterConcurrent) {
		return new ExceptionTransformer<DattySingleException>() {

			@Override
			public DattySingleException transformException(AerospikeException e) {
				
				switch(e.getResultCode()) {
				
				case ResultCode.TIMEOUT:
					return new DattySingleException(DattyError.ErrCode.TIMEOUT, operation, e);
				
				case ResultCode.GENERATION_ERROR:
					return filterConcurrent ? null : new DattySingleException(DattyError.ErrCode.CONCURRENT_UPDATE, operation, e);
					
				default:
					return new DattySingleException(DattyError.ErrCode.UNKNOWN, "aerospike error: " + ResultCode.getResultString(e.getResultCode()), operation, e);
				}
			}
			
		};
	}
	
	public ExceptionTransformer<DattyStreamException> streamExceptionTransformer(final DattyKey key) {
		return new ExceptionTransformer<DattyStreamException>() {

			@Override
			public DattyStreamException transformException(AerospikeException e) {
				
				switch(e.getResultCode()) {
				
				case ResultCode.TIMEOUT:
					return new DattyStreamException(DattyError.ErrCode.TIMEOUT, key, e);
				
				case ResultCode.GENERATION_ERROR:
					return new DattyStreamException(DattyError.ErrCode.CONCURRENT_UPDATE, key, e);
					
				default:
					return new DattyStreamException(DattyError.ErrCode.UNKNOWN, "aerospike error: " + ResultCode.getResultString(e.getResultCode()), key, e);
				}
			}
			
		};
	}
	
}
