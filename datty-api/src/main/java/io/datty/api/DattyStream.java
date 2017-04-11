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
package io.datty.api;

import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

/**
 * DattyStream
 * 
 * @author Alex Shvid
 *
 */

public interface DattyStream {

	/**
	 * Gets large value by key
	 * 
	 * @param key - datty key
	 * @return stream ValueResult with value bytes or return ErrorResult 
	 */
	
	Observable<ByteBuf> streamOut(DattyKey key);
	
	/**
	 * Puts large value by key
	 * 
	 * @param key - datty key
	 * @param value - large value
	 * @return number or bytes written in LongResult or ErrorResult
	 */
	
	Single<Long> streamIn(DattyKey key, Observable<ByteBuf> value);
		
}
