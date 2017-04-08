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

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import io.datty.api.Cache;
import io.datty.api.CacheManager;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.operation.ExistsOperation;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.SetOperation;

/**
 * Unit implementation of the Cache interface
 * 
 * @author dadril
 *
 */

public class UnitCache implements Cache {

	private final static AtomicReferenceFieldUpdater<UnitCache, Properties> PROPS_UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(UnitCache.class, Properties.class, "props");

	private final UnitDatty parent;
	private final String cacheName;
	private volatile Properties props;
	
	private final ConcurrentMap<String, UnitRecord> recordMap = new ConcurrentHashMap<String, UnitRecord>();

	protected UnitCache(UnitDatty parent, String cacheName, Properties props) {
		this.parent = parent;
		this.cacheName = cacheName;
		this.props = props;
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
		return props;
	}

	@Override
	public void setCacheProperties(Properties newProps) {
		PROPS_UPDATER.set(this, newProps);
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
	public SetOperation set(String majorKey) {
		return new SetOperation(cacheName, majorKey).withDatty(parent.getDatty());
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
		return "UnitCache [name=" + cacheName + "]";
	}
	
}
