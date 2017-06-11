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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.google.common.cache.CacheBuilder;

import io.datty.api.DattySet;
import io.datty.api.DattyManager;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.operation.ExistsOperation;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.PutOperation;
import io.datty.support.exception.DattyException;

/**
 * Unit implementation of the DattySet interface
 * 
 * @author Alex Shvid
 *
 */

public class UnitSet implements DattySet {

	private final static AtomicReferenceFieldUpdater<UnitSet, Properties> PROPS_UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(UnitSet.class, Properties.class, "props");

	private final UnitDattyManager parent;
	private final String name;
	private volatile Properties props;
	
	private final com.google.common.cache.Cache<String, UnitRecord> backingCache;
	private final ConcurrentMap<String, UnitRecord> recordMap;

	protected UnitSet(UnitDattyManager parent, String setName, Properties props) {
		this.parent = parent;
		this.name = setName;
		this.props = props;
		
		String ttlSeconds = props.getProperty(UnitPropertyKeys.MAX_ENTRIES);
		String maxEntries = props.getProperty(UnitPropertyKeys.MAX_ENTRIES);
		if (maxEntries != null) {
			CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
			try {
				builder.maximumSize(Integer.parseInt(maxEntries));
			}
			catch(NumberFormatException e) {
				throw new DattyException("invalid property maxEntries in set: " + setName + ", value=" + maxEntries);
			}
			if (ttlSeconds != null) {
				try {
					builder.expireAfterWrite(Integer.parseInt(ttlSeconds), TimeUnit.SECONDS);
				}
				catch(NumberFormatException e) {
					throw new DattyException("invalid property maxEntries in cache: " + setName + ", value=" + maxEntries);
				}
			}
			
			this.backingCache = builder.build();
			this.recordMap = backingCache.asMap();
		}
		else {
			this.backingCache = null;
			this.recordMap = new ConcurrentHashMap<String, UnitRecord>();
		}
		
	}

	@Override
	public DattyManager getDattyManager() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Properties getProperties() {
		return props;
	}

	@Override
	public void setProperties(Properties newProps) {
		PROPS_UPDATER.set(this, newProps);
	}

	@Override
	public Map<String, String> getStatistics() {
		return Collections.emptyMap();
	}

	@Override
	public GetOperation get(String majorKey) {
		return new GetOperation().setSetName(name).setMajorKey(majorKey).setDatty(parent.getDatty());
	}

	@Override
	public ExistsOperation exists(String majorKey) {
		return new ExistsOperation().setSetName(name).setMajorKey(majorKey).setDatty(parent.getDatty());
	}

	@Override
	public PutOperation put(String majorKey) {
		return new PutOperation().setSetName(name).setMajorKey(majorKey).setDatty(parent.getDatty());
	}

	@Override
	public CompareAndSetOperation compareAndSet(String majorKey) {
		return new CompareAndSetOperation().setSetName(name).setMajorKey(majorKey).setDatty(parent.getDatty());
	}

	@Override
	public ExecuteOperation execute(String majorKey) {
		return new ExecuteOperation().setSetName(name).setMajorKey(majorKey).setDatty(parent.getDatty());
	}
	
	public ConcurrentMap<String, UnitRecord> getRecordMap() {
		return recordMap;
	}

	@Override
	public String toString() {
		return "UnitSet [name=" + name + "]";
	}
	
}
