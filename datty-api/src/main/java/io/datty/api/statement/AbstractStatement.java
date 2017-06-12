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
package io.datty.api.statement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.datty.api.DattyConstants;
import io.datty.api.DattyQuery;
import io.datty.api.DattyStatement;
import io.datty.api.result.RecordResult;
import rx.Observable;

/**
 * Abstract statement
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractStatement<S extends DattyStatement> implements DattyStatement {

	protected DattyQuery datty;
	
	protected String setName;

	protected String superKey;

	private boolean allMinorKeys;
	
	private Set<String> minorKeys;
	
	protected int timeoutMillis = DattyConstants.UNSET_TIMEOUT;
	
	protected RecordResult fallback;
	
	protected Object upstreamContext;
	
	public AbstractStatement(String setName) {
		this.setName = setName;
	}
	
	@Override
	public String getSetName() {
		return setName;
	}

	@Override
	public boolean hasSuperKey() {
		return superKey != null;
	}

	@Override
	public String getSuperKey() {
		return superKey;
	}

	public S setSuperKey(String superKey) {
		this.superKey = superKey;
		return castThis();
	}
	
	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	public S allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return castThis();
	}
	
	public S allMinorKeys() {
		this.allMinorKeys = true;
		return castThis();
	}
	
	public S addMinorKey(String minorKey) {
		if (this.minorKeys == null) {
			this.minorKeys = Collections.singleton(minorKey);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}			
			this.minorKeys.add(minorKey);
		}
		return castThis();
	}
	
	public S addMinorKeys(Collection<String> minorKeys) {
		if (this.minorKeys == null) {
			this.minorKeys = new HashSet<String>(minorKeys);
		}
		else {
			if (this.minorKeys.size() == 1) {
				this.minorKeys = new HashSet<String>(this.minorKeys);
			}
			this.minorKeys.addAll(minorKeys);
		}
		return castThis();
	}
	
	public Set<String> getMinorKeys() {
		return minorKeys != null ? minorKeys : Collections.<String>emptySet();
	}

	@Override
	public boolean hasTimeoutMillis() {
		return timeoutMillis != DattyConstants.UNSET_TIMEOUT;
	}
	
	@Override
	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public S withTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		return castThis();
	}
	
	public S withDatty(DattyQuery datty) {
		this.datty = datty;
		return castThis();
	}
	
	@SuppressWarnings("unchecked")
	protected S castThis() {
		return (S) this;
	}
	
	public RecordResult getFallback() {
		return fallback;
	}

	public S onFallback(RecordResult fallback) {
		this.fallback = fallback;
		return castThis();
	}
	
	public S withFallback(RecordResult fallback) {
		return onFallback(fallback);
	}

	public boolean hasUpstreamContext() {
		return upstreamContext != null;
	}
	
	public Object getUpstreamContext() {
		return upstreamContext;
	}

	public S setUpstreamContext(Object upstreamContext) {
		this.upstreamContext = upstreamContext;
		return castThis();
	}

	@Override
	public Observable<RecordResult> query() {
		
		if (datty == null) {
			throw new IllegalStateException("datty is empty");
		}
		
		return datty.query(castThis());
	}

	
}
