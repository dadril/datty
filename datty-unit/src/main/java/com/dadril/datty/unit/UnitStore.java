/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package com.dadril.datty.unit;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.dadril.datty.api.Store;
import com.dadril.datty.api.StoreManager;

/**
 * Unit implementation of the Store interface
 * 
 * @author dadril
 *
 */

public class UnitStore implements Store {

	private final static AtomicReferenceFieldUpdater<UnitStore, Properties> PROPS_UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(UnitStore.class, Properties.class, "props");

	private final StoreManager parent;
	private final String name;
	private volatile Properties props;
	
	private final ConcurrentMap<String, UnitRecord> recordMap = new ConcurrentHashMap<String, UnitRecord>();

	public UnitStore(StoreManager parent, String name, Properties props) {
		this.parent = parent;
		this.name = name;
		this.props = props;
	}

	@Override
	public StoreManager getStoreManager() {
		return parent;
	}

	@Override
	public String getStoreName() {
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
	public String toString() {
		return "UnitStore [name=" + name + "]";
	}
	
}
