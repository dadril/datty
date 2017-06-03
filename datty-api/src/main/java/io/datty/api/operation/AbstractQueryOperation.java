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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.datty.api.DattyConstants;
import io.datty.api.result.QueryResult;

/**
 * AbstractQueryOperation
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractQueryOperation<O extends QueryOperation> implements QueryOperation {

	protected String setName;

	protected String superKey;

	private boolean allMinorKeys;
	
	private Set<String> minorKeys;
	
	protected int timeoutMillis = DattyConstants.UNSET_TIMEOUT;
	
	protected QueryResult fallback;
	
	public AbstractQueryOperation(String setName) {
		this.setName = setName;
	}
	
	@Override
	public boolean hasTimeoutMillis() {
		return timeoutMillis != DattyConstants.UNSET_TIMEOUT;
	}
	
	@Override
	public String getSetName() {
		return setName;
	}

	@Override
	public String getSuperKey() {
		return superKey;
	}

	public O setSuperKey(String superKey) {
		this.superKey = superKey;
		return castThis();
	}
	
	public boolean isAllMinorKeys() {
		return allMinorKeys;
	}

	public O allMinorKeys(boolean all) {
		this.allMinorKeys = all;
		return castThis();
	}
	
	public O allMinorKeys() {
		this.allMinorKeys = true;
		return castThis();
	}
	
	public O addMinorKey(String minorKey) {
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
	
	public O addMinorKeys(Collection<String> minorKeys) {
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
	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public O withTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		return castThis();
	}
	
	@Override
	public QueryResult getFallback() {
		return fallback;
	}

	public O onFallback(QueryResult fallback) {
		this.fallback = fallback.setOperation(castThis());
		return castThis();
	}
	
	public O withFallback(QueryResult fallback) {
		return onFallback(fallback);
	}
	
	@SuppressWarnings("unchecked")
	protected O castThis() {
		return (O) this;
	}
	
	
}
