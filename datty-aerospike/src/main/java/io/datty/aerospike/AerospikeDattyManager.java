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
import com.aerospike.client.Info;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.cluster.Node;

import io.datty.aerospike.support.RandomUtil;
import io.datty.api.Datty;
import io.datty.api.DattyBatch;
import io.datty.api.DattyManager;
import io.datty.api.DattyQuery;
import io.datty.api.DattySet;
import io.datty.api.DattySetError;
import io.datty.api.DattySingle;
import io.datty.api.DattyStream;
import io.datty.api.SetExistsAction;
import io.datty.spi.DattyBatchDriver;
import io.datty.spi.DattyDriver;
import io.datty.spi.DattyQueryDriver;
import io.datty.spi.DattySingleDriver;
import io.datty.spi.DattySingleProvider;
import io.datty.spi.DattyStreamDriver;
import io.datty.support.exception.DattyFactoryException;
import io.datty.support.exception.DattySetException;

/**
 * AerospikeDattyManager
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeDattyManager implements DattyManager {

	private final String managerName;
	private final AerospikeConfig config;
	private final AerospikeRxClient client;
	private boolean unitEmulation;
	private final ConcurrentMap<String, AerospikeSet> setMap = new ConcurrentHashMap<String, AerospikeSet>();
	private Datty currentDatty;
	private AerospikeVersion version;
	
	public AerospikeDattyManager(Properties props) {
		
		this.managerName = props.getProperty(AerospikePropertyKeys.NAME, AerospikeConstants.DEFAULT_NAME);
		this.config = new AerospikeConfig(props);
		this.client = new AerospikeRxClient(instantiateClient(this.config)); 
		
		DattySingle single = new DattySingleProvider(new DattySingleDriver(new AerospikeDattySingle(this)));
		DattyBatch batch = new DattyBatchDriver(single);
		DattyStream stream = new DattyStreamDriver(new AerospikeDattyStream(this));
		DattyQuery query = new DattyQueryDriver(new AerospikeDattyQuery(this));
		
		this.currentDatty = DattyDriver.newBuilder()
				.setSingle(single)
				.setBatch(batch)
				.setStream(stream)
				.setQuery(query)
				.build();
		
		this.version = requestVersion();
		System.out.println("version =  " + version);
	}
	
	public AerospikeConfig getConfig() {
		return config;
	}

	public AerospikeRxClient getClient() {
		return client;
	}

	public AerospikeVersion getVersion() {
		return version;
	}

	public boolean isUnitEmulation() {
		return unitEmulation;
	}

	public void setUnitEmulation(boolean unitEmulation) {
		this.unitEmulation = unitEmulation;
	}

	@Override
	public String getManagerName() {
		return managerName;
	}

	@Override
	public DattySet getSet(String setName) {
		return setMap.get(setName);
	}
	
	protected AerospikeSet getAerospikeSet(String setName) {
		return setMap.get(setName);
	}
	
	private AerospikeSet createAndPut(String name, Properties properties) {
		AerospikeSet set = new AerospikeSet(this, name, properties);
		AerospikeSet c = setMap.putIfAbsent(name, set);
		if (c != null) {
			set = c;
		}
		return set;
	}

	@Override
	public DattySet getSet(String name, Properties properties, SetExistsAction action) {
		AerospikeSet set = setMap.get(name);
		
		switch(action) {
		
		case CREATE_ONLY:
			if (set != null) {
				throw new DattySetException(DattySetError.SET_EXISTS, name);
			}
			break;
		
		case CREATE_IF_NOT_EXISTS:
			if (set == null) {
				set = createAndPut(name, properties);
			}
 			break;
 			
		case UPDATE:
			if (set != null) {
				set.setProperties(properties);
			}
			else {
				set = createAndPut(name, properties);
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

	private AsyncClient instantiateClient(AerospikeConfig config) {
		try {
			return new AsyncClient(config.getClientPolicy(), config.getHosts());
		}
		catch(AerospikeException e) {
			throw new DattyFactoryException("fail to instantiate aerospike instance: ", e);
		}
	}
	
	private AerospikeVersion requestVersion() {
		Node[] nodes = client.getClient().getNodes();
		Node node = RandomUtil.selectRandom(nodes);
		String response = Info.request(config.getClientPolicy().infoPolicyDefault, node, "build");
		return AerospikeVersion.parse(response);
	}
	
	@Override
	public String toString() {
		return "AerospikeDattyManager [name=" + managerName + ", version=" + version + "]";
	}
	
}
