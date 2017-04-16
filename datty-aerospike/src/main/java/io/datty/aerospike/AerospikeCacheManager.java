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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.async.AsyncClient;

import io.datty.api.Cache;
import io.datty.api.CacheError;
import io.datty.api.CacheExistsAction;
import io.datty.api.CacheManager;
import io.datty.api.Datty;
import io.datty.api.DattySingle;
import io.datty.api.DattyStream;
import io.datty.spi.DattyDriver;
import io.datty.spi.DattySingleDriver;
import io.datty.spi.DattySingleProvider;
import io.datty.spi.DattyStreamDriver;
import io.datty.support.exception.DattyCacheException;
import io.datty.support.exception.DattyFactoryException;

/**
 * AerospikeCacheManager
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeCacheManager implements CacheManager {

	private final String name;
	private final AerospikeConfig config;
	private final AerospikeRxClient client;
	private boolean unitEmulation;
	private final ConcurrentMap<String, AerospikeCache> cacheMap = new ConcurrentHashMap<String, AerospikeCache>();
	private Datty currentDatty;
	
	public AerospikeCacheManager(Properties props) {
		
		this.name = props.getProperty(AerospikePropertyKeys.NAME, AerospikeConstants.DEFAULT_NAME);
		this.config = new AerospikeConfig(props);
		this.client = new AerospikeRxClient(instantiateClient(this.config)); 
		
		DattySingle dattySingle = new DattySingleProvider(new DattySingleDriver(new AerospikeDattySingle(this)));
		DattyStream dattyStream = new DattyStreamDriver(new AerospikeDattyStream(this));
		
		this.currentDatty = new DattyDriver(dattySingle, dattyStream);
	}
	
	public AerospikeConfig getConfig() {
		return config;
	}

	public AerospikeRxClient getClient() {
		return client;
	}

	public boolean isUnitEmulation() {
		return unitEmulation;
	}

	public void setUnitEmulation(boolean unitEmulation) {
		this.unitEmulation = unitEmulation;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Cache getCache(String cacheName) {
		return cacheMap.get(cacheName);
	}
	
	protected AerospikeCache getAerospikeCache(String cacheName) {
		return cacheMap.get(cacheName);
	}
	
	private AerospikeCache createAndPut(String cacheName, Properties cacheProperties) {
		AerospikeCache cache = new AerospikeCache(this, cacheName, cacheProperties);
		AerospikeCache c = cacheMap.putIfAbsent(cacheName, cache);
		if (c != null) {
			cache = c;
		}
		return cache;
	}

	@Override
	public Cache getCache(String cacheName, Properties cacheProperties, CacheExistsAction action) {
		AerospikeCache cache = cacheMap.get(cacheName);
		
		switch(action) {
		
		case CREATE_ONLY:
			if (cache != null) {
				throw new DattyCacheException(CacheError.CACHE_EXISTS, cacheName);
			}
			break;
		
		case CREATE_IF_NOT_EXISTS:
			if (cache == null) {
				cache = createAndPut(cacheName, cacheProperties);
			}
 			break;
 			
		case UPDATE:
			if (cache != null) {
				cache.setCacheProperties(cacheProperties);
			}
			else {
				cache = createAndPut(cacheName, cacheProperties);
			}
 			break;
 			
		}

		return cache;
	}
	
	@Override
	public Datty getDatty() {
		return this.currentDatty;
	}

	@Override
	public void setDatty(Datty newDatty) {

		if (newDatty == null) {
			throw new IllegalArgumentException("empty new datty");
		}
		
		this.currentDatty = newDatty;
	}

	private AsyncClient instantiateClient(AerospikeConfig config) {
		try {
			return new AsyncClient(config.getClientPolicy(), config.getHosts());
		}
		catch(AerospikeException e) {
			throw new DattyFactoryException("fail to instantiate aerospike instance: ", e);
		}
	}
	
	@Override
	public String toString() {
		return "AerospikeCacheManager [name=" + name + "]";
	}
	
}
