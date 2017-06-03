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

import io.datty.api.DattySet;
import io.datty.api.DattySetError;
import io.datty.api.SetExistsAction;
import io.datty.api.DattyManager;
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
import io.datty.support.exception.DattySetException;

/**
 * Unit implementation for DattyManager
 * 
 * @author Alex Shvid
 *
 */

public class UnitDattyManager implements DattyManager {

	private final String name;
	private final ConcurrentMap<String, UnitSet> setMap = new ConcurrentHashMap<String, UnitSet>();
	private Datty currentDatty;
	
	public UnitDattyManager() {
		this(new Properties());
	}

	public UnitDattyManager(Properties props) {
		this.name = props.getProperty(UnitPropertyKeys.NAME, UnitConstants.DEFAULT_NAME);
		
		DattySingle single = new DattySingleProvider(new DattySingleDriver(new UnitDattySingle(setMap)));
		DattyBatch batch = new DattyBatchDriver(single);
		DattyStream stream = new DattyStreamDriver(new UnitDattyStream(setMap));
		DattyQuery query = new DattyQueryDriver(new UnitDattyQuery(setMap));
		
		this.currentDatty = DattyDriver.newBuilder()
				.setSingle(single)
				.setBatch(batch)
				.setStream(stream)
				.setQuery(query)
				.build();
				
	}

	@Override
	public String getManagerName() {
		return name;
	}

	@Override
	public DattySet getSet(String setName) {
		return setMap.get(setName);
	}

	private UnitSet createAndPut(String setName, Properties cacheProperties) {
		UnitSet set = new UnitSet(this, setName, cacheProperties);
		UnitSet c = setMap.putIfAbsent(setName, set);
		if (c != null) {
			set = c;
		}
		return set;
	}
	
	@Override
	public DattySet getSet(String setName, Properties properties, SetExistsAction action) {
		UnitSet set = setMap.get(setName);
		
		switch(action) {
		
		case CREATE_ONLY:
			if (set != null) {
				throw new DattySetException(DattySetError.SET_EXISTS, setName);
			}
			break;
		
		case CREATE_IF_NOT_EXISTS:
			if (set == null) {
				set = createAndPut(setName, properties);
			}
 			break;
 			
		case UPDATE:
			if (set != null) {
				set.setProperties(properties);
			}
			else {
				set = createAndPut(setName, properties);
			}
 			break;
 			
		}

		return set;
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
		return "UnitDattyManager [name=" + name + "]";
	}

}
