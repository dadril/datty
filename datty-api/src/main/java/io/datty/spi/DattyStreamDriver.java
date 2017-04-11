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
package io.datty.spi;

import io.datty.api.DattyError;
import io.datty.api.DattyKey;
import io.datty.api.DattyStream;
import io.datty.support.exception.DattyStreamException;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;

/**
 * Server side for correct exceptions handling
 * 
 * @author Alex Shvid
 *
 */

public class DattyStreamDriver implements DattyStream {

	private final DattyStream stream;
	
	public DattyStreamDriver(DattyStream stream) {
		this.stream = stream;
	}

	@Override
	public Observable<ByteBuf> streamOut(final DattyKey key) {
		
		Observable<ByteBuf> results = stream.streamOut(key);
		
		results = results.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable t) {

				if (!(t instanceof DattyStreamException)) {
					throw new DattyStreamException(DattyError.ErrCode.UNKNOWN, key, t);
				}
				
			}
			
		});
		
		return results;
	}

	@Override
	public Single<Long> streamIn(final DattyKey key, Observable<ByteBuf> value) {
		
		Single<Long> result = stream.streamIn(key, value);
		
		result = result.doOnError(new Action1<Throwable>() {

			@Override
			public void call(Throwable t) {

				if (!(t instanceof DattyStreamException)) {
					throw new DattyStreamException(DattyError.ErrCode.UNKNOWN, key, t);
				}
				
			}
			
		});
		
		return result;
	}
	
	
	
}
