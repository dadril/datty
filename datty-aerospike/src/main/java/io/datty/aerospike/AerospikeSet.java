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
import io.datty.api.DattyError;
import io.datty.api.DattyKey;
import io.datty.api.DattyManager;
import io.datty.api.DattyOperation;
import io.datty.api.DattySet;
import io.datty.api.operation.CompareAndSetOperation;
import io.datty.api.operation.ExecuteOperation;
import io.datty.api.operation.FetchOperation;
import io.datty.api.operation.PutOperation;
import io.datty.support.exception.DattyOperationException;
import io.datty.support.exception.DattyStreamException;

/**
 * AerospikeSet
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeSet implements DattySet {

	private final static AtomicReferenceFieldUpdater<AerospikeSet, AerospikeSetConfig> CONFIG_UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(AerospikeSet.class, AerospikeSetConfig.class, "config");
	
	private final AerospikeDattyManager parent;
	private final String name;
	private volatile AerospikeSetConfig config;
	
	protected AerospikeSet(AerospikeDattyManager parent, String name, Properties properties) {
		this.parent = parent;
		this.name = name;
		this.config = new AerospikeSetConfig(parent.getConfig(), properties);
	}
	
	public AerospikeDattyManager getParent() {
		return parent;
	}
	
	public AerospikeSetConfig getConfig() {
		return config;
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
		return config.getProperties();
	}

	@Override
	public void setProperties(Properties newProps) {
		CONFIG_UPDATER.set(this, new AerospikeSetConfig(parent.getConfig(), newProps));
	}

	@Override
	public Map<String, String> getStatistics() {
		return Collections.emptyMap();
	}

	@Override
	public FetchOperation fetch(String majorKey) {
		return new FetchOperation(name, majorKey).setDatty(parent.getDatty());
	}

	@Override
	public PutOperation put(String majorKey) {
		return new PutOperation(name, majorKey).setDatty(parent.getDatty());
	}

	@Override
	public CompareAndSetOperation compareAndSet(String majorKey) {
		return new CompareAndSetOperation(name, majorKey).setDatty(parent.getDatty());
	}

	@Override
	public ExecuteOperation execute(String majorKey) {
		return new ExecuteOperation(name, majorKey).setDatty(parent.getDatty());
	}
	
	@Override
	public String toString() {
		return "AerospikeSet [name=" + name + "]";
	}
	
	public ExceptionTransformer<DattyOperationException> singleExceptionTransformer(final DattyOperation operation, final boolean filterConcurrent) {
		return new ExceptionTransformer<DattyOperationException>() {

			@Override
			public DattyOperationException transformException(AerospikeException e) {
				
				switch(e.getResultCode()) {
				
				case ResultCode.TIMEOUT:
					return new DattyOperationException(DattyError.ErrCode.TIMEOUT, operation, e);
				
				case ResultCode.GENERATION_ERROR:
					return filterConcurrent ? null : new DattyOperationException(DattyError.ErrCode.CONCURRENT_UPDATE, operation, e);
					
				default:
					return new DattyOperationException(DattyError.ErrCode.UNKNOWN, "aerospike error: " + ResultCode.getResultString(e.getResultCode()), operation, e);
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
