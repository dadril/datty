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

import io.datty.api.CacheError;

/**
 * DattyCacheException
 * 
 * @author Alex Shvid
 *
 */

public class DattyCacheException extends DattyException {

	private static final long serialVersionUID = 1918157804627815761L;

	private final CacheError error;
	private final String cacheName;
	
	public DattyCacheException(CacheError error, String cacheName) {
		super(error.name() + ": " + cacheName);
		this.error = error;
		this.cacheName = cacheName;
	}

	public DattyCacheException(CacheError error, String cacheName, Throwable t) {
		super(error.name() + ": " + cacheName, t);
		this.error = error;
		this.cacheName = cacheName;
	}

	public CacheError getError() {
		return error;
	}

	public String getCacheName() {
		return cacheName;
	}

	
}
