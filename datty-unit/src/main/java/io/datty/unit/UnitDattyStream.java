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
package io.datty.unit;

import java.util.concurrent.ConcurrentMap;

import io.datty.api.DattyKey;
import io.datty.api.DattyStream;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;

/**
 * UnitDattyStream
 * 
 * @author Alex Shvid
 *
 */

public class UnitDattyStream implements DattyStream {

	private final ConcurrentMap<String, UnitCache> cacheMap;
	
	public UnitDattyStream(ConcurrentMap<String, UnitCache> cacheMap) {
		this.cacheMap = cacheMap;
	}

	@Override
	public Observable<ByteBuf> streamOut(DattyKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<Long> streamIn(DattyKey key, Observable<ByteBuf> value) {
		// TODO Auto-generated method stub
		return null;
	}

}
