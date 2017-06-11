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

import io.datty.api.DattyConstants;
import io.datty.api.DattySingle;
import io.datty.api.result.AbstractResult;
import io.datty.api.result.TypedResult;
import rx.Single;

/**
 * Abstract operation
 * 
 * @author Alex Shvid
 *
 */

abstract class AbstractOperation<O extends TypedOperation<O, R>, R extends TypedResult<O>> implements TypedOperation<O, R> {

	protected DattySingle datty;
	
	protected String setName;

	protected String superKey;

	protected String majorKey;
	
	protected int timeoutMillis = DattyConstants.UNSET_TIMEOUT;
	
	protected R fallback;
	
	protected Object upstreamContext;
	
	@Override
	public String getSetName() {
		return setName;
	}
	
	public O setSetName(String setName) {
		this.setName = setName;
		return castThis();
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

	public O setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		return castThis();
	}
	
	public O setDatty(DattySingle datty) {
		this.datty = datty;
		return castThis();
	}
	
	@SuppressWarnings("unchecked")
	protected O castThis() {
		return (O) this;
	}
	
	@Override
	public R getFallback() {
		return fallback;
	}

	@Override
	public boolean hasFallback() {
		return fallback != null;
	}
	
	@Override
	public O setFallback(R fallback) {
		if (fallback instanceof AbstractResult) {
			@SuppressWarnings("unchecked")
			AbstractResult<O, R> abstractResult = (AbstractResult<O, R>) fallback;
			abstractResult.setOperation(castThis());
		}
		this.fallback = fallback;
		return castThis();
	}
	
	public boolean hasUpstreamContext() {
		return upstreamContext != null;
	}
	
	public Object getUpstreamContext() {
		return upstreamContext;
	}

	public O setUpstreamContext(Object upstreamContext) {
		this.upstreamContext = upstreamContext;
		return castThis();
	}

	@Override
	public Single<R> execute() {
		
		if (datty == null) {
			throw new IllegalStateException("datty is empty");
		}
		
		return datty.execute(castThis());
	}

	
}
