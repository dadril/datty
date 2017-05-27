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
package io.datty.unit;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.datty.api.Cache;
import io.datty.api.CacheError;
import io.datty.api.CacheExistsAction;
import io.datty.api.CacheManager;
import io.datty.api.Datty;
import io.datty.api.DattyBatch;
import io.datty.api.DattyQuery;
import io.datty.api.DattySingle;
import io.datty.api.DattyStream;
import io.datty.spi.DattyBatchDriver;
import io.datty.spi.DattyDriver;
import io.datty.spi.DattyQueryDriver;
import io.datty.spi.DattySingleDriver;
import io.datty.spi.DattySingleProvider;
import io.datty.spi.DattyStreamDriver;
import io.datty.support.exception.DattyCacheException;

/**
 * Unit implementation for Datty API
 * 
 * @author Alex Shvid
 *
 */

public class UnitCacheManager implements CacheManager {

	private final String name;
	private final ConcurrentMap<String, UnitCache> cacheMap = new ConcurrentHashMap<String, UnitCache>();
	private Datty currentDatty;
	
	public UnitCacheManager() {
		this(new Properties());
	}

	public UnitCacheManager(Properties props) {
		this.name = props.getProperty(UnitPropertyKeys.NAME, UnitConstants.DEFAULT_NAME);
		
		DattySingle single = new DattySingleProvider(new DattySingleDriver(new UnitDattySingle(cacheMap)));
		DattyBatch batch = new DattyBatchDriver(single);
		DattyStream stream = new DattyStreamDriver(new UnitDattyStream(cacheMap));
		DattyQuery query = new DattyQueryDriver(new UnitDattyQuery(cacheMap));
		
		this.currentDatty = DattyDriver.newBuilder()
				.setSingle(single)
				.setBatch(batch)
				.setStream(stream)
				.setQuery(query)
				.build();
				
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Cache getCache(String cacheName) {
		return cacheMap.get(cacheName);
	}

	private UnitCache createAndPut(String cacheName, Properties cacheProperties) {
		UnitCache cache = new UnitCache(this, cacheName, cacheProperties);
		UnitCache c = cacheMap.putIfAbsent(cacheName, cache);
		if (c != null) {
			cache = c;
		}
		return cache;
	}
	
	@Override
	public Cache getCache(String cacheName, Properties cacheProperties, CacheExistsAction action) {
		UnitCache cache = cacheMap.get(cacheName);
		
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

	@Override
	public String toString() {
		return "UnitDatty [name=" + name + "]";
	}

}
