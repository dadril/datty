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
package io.datty.api.operation;

import io.datty.api.Datty;
import io.datty.api.DattyConstants;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.result.AbstractResult;
import io.datty.support.exception.DattyUncompletedException;
import rx.Single;

/**
 * Abstract operation
 * 
 * @author dadril
 *
 */

public abstract class AbstractOperation<O extends DattyOperation, R extends DattyResult> implements DattyOperation {

	protected Datty datty;
	
	protected String cacheName;

	protected String superKey;

	protected String majorKey;
	
	protected int timeoutMillis = DattyConstants.UNSET_TIMEOUT;

	protected DattyResult result;
	
	public AbstractOperation(String cacheName) {
		this.cacheName = cacheName;
	}
	
	public AbstractOperation(String cacheName, String majorKey) {
		this.cacheName = cacheName;
		this.majorKey = majorKey;
	}

	@Override
	public String getCacheName() {
		return cacheName;
	}

	@Override
	public String getSuperKey() {
		return superKey;
	}

	public O setSuperKey(String superKey) {
		this.superKey = superKey;
		return castThis();
	}
	
	@Override
	public String getMajorKey() {
		return majorKey;
	}

	public O setMajorKey(String majorKey) {
		this.majorKey = majorKey;
		return castThis();
	}

	@Override
	public boolean hasTimeoutMillis() {
		return timeoutMillis != DattyConstants.UNSET_TIMEOUT;
	}
	
	@Override
	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public O withTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		return castThis();
	}
	
	public O withDatty(Datty datty) {
		this.datty = datty;
		return castThis();
	}
	
	@SuppressWarnings("unchecked")
	private O castThis() {
		return (O) this;
	}

	@Override
	public boolean isCompleted() {
		return result != null;
	}

	@Override
	public DattyResult getResult() {
		
		if (result == null) {
			throw new DattyUncompletedException("operation is not completed: " + this);
		}
		
		return result;
	}

	@Override
	public void reset() {
		this.result = null;
	}

	@Override
	public DattyResult complete(DattyResult result) {
		
		if (result == null) {
			throw new IllegalArgumentException("empty result");
		}
		
		this.result = result;
		
		if (result instanceof AbstractResult) {
			AbstractResult abstractResult = (AbstractResult) result;
			abstractResult.setOperation(this);
		}
		
		return this.result;
	}

	@Override
	public Single<DattyResult> execute() {
		
		if (datty == null) {
			throw new IllegalStateException("datty is empty");
		}
		
		return datty.execute(this);
	}

	
	
}
