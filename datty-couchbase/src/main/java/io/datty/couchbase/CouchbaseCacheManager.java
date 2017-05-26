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
package io.datty.couchbase;

import java.util.Properties;

import io.datty.api.Cache;
import io.datty.api.CacheExistsAction;
import io.datty.api.CacheManager;
import io.datty.api.Datty;

/**
 * CouchbaseCacheManager
 * 
 * @author Alex Shvid
 *
 */

public class CouchbaseCacheManager implements CacheManager {

	public CouchbaseCacheManager(Properties props) {
		
	}

	@Override
	public String getName() {
		return "couchbase";
	}

	@Override
	public Cache getCache(String cacheName) {
		return null;
	}

	@Override
	public Cache getCache(String cacheName, Properties cacheProperties, CacheExistsAction action) {
		return null;
	}

	@Override
	public Datty getDatty() {
		return null;
	}

	@Override
	public void setDatty(Datty newDatty) {
	}
	
}
