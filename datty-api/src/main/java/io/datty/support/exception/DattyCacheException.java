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
package io.datty.support.exception;

/**
 * DattyCacheException
 * 
 * @author Alex Shvid
 *
 */

public class DattyCacheException extends DattyException {

	private static final long serialVersionUID = 1918157804627815761L;

	public enum CacheErrors {
		
		CACHE_EXISTS,
		CACHE_MISTMATCH;
		
	}
	
	private final CacheErrors errors;
	private final String cacheName;
	
	public DattyCacheException(CacheErrors errors, String cacheName) {
		super(errors.name() + ": " + cacheName);
		this.errors = errors;
		this.cacheName = cacheName;
	}

	public DattyCacheException(CacheErrors errors, String cacheName, Throwable t) {
		super(errors.name() + ": " + cacheName, t);
		this.errors = errors;
		this.cacheName = cacheName;
	}

	public CacheErrors getErrors() {
		return errors;
	}

	public String getCacheName() {
		return cacheName;
	}

	
}
