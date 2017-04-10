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
package io.datty.unit.test;

import java.util.Properties;

import org.junit.BeforeClass;

import io.datty.api.Cache;
import io.datty.api.CacheExistsAction;
import io.datty.api.CacheFactory;
import io.datty.api.CacheManager;
import io.datty.unit.UnitPropertyKeys;

public abstract class AbstractDattyUnitTest {

	static final String FACTORY_NAME = System.getProperty("datty.factory");
	
	public static final String CACHE_NAME = "TestCache";
	
	static protected CacheFactory cacheFactory;
	
	static protected CacheManager cacheManager;
	
	static protected Cache cache;

	@BeforeClass
	public static void init() {
		
		if (FACTORY_NAME != null) {
			cacheFactory = new CacheFactory.Locator().getByName(FACTORY_NAME);
		}
		else {
			cacheFactory = new CacheFactory.Locator().getSingle();
		}
		
		System.out.println("Init CacheFactory = " + cacheFactory.getFactoryName());
		
		Properties props = new Properties();
		props.put(UnitPropertyKeys.NAME, "test");
		
		cacheManager = cacheFactory.newInstance(props);
		
		cache = cacheManager.getCache(CACHE_NAME, new Properties(), CacheExistsAction.CREATE_IF_NOT_EXISTS);
		
	}
	
}
