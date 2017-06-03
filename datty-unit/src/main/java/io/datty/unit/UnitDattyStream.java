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

import io.datty.api.DattyError;
import io.datty.api.DattyKey;
import io.datty.api.DattyStream;
import io.datty.api.UpdatePolicy;
import io.datty.support.exception.DattyStreamException;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * UnitDattyStream
 * 
 * @author Alex Shvid
 *
 */

public class UnitDattyStream implements DattyStream {

	private final ConcurrentMap<String, UnitSet> setMap;
	
	public UnitDattyStream(ConcurrentMap<String, UnitSet> setMap) {
		this.setMap = setMap;
	}

	@Override
	public Observable<ByteBuf> streamOut(DattyKey key) {
		
		UnitSet set = setMap.get(key.getSetName());
		if (set == null) {
			return Observable.error(new DattyStreamException(DattyError.ErrCode.SET_NOT_FOUND, key));
		}
		
		String majorKey = key.getMajorKey();
		String minorKey = key.getMinorKey();
		
		if (majorKey == null || minorKey == null) {
			return Observable.error(new DattyStreamException(DattyError.ErrCode.BAD_ARGUMENTS, key));
		}
		
		UnitRecord record = set.getRecordMap().get(majorKey);
		if (record == null) {
			return Observable.empty();
		}
				
		UnitValue value = record.getColumn(minorKey);
		if (value == null) {
			return Observable.just(null);
		}
		
		return Observable.just(value.asByteBuf());
		
	}

	@Override
	public Single<Long> streamIn(DattyKey key, Observable<ByteBuf> value) {
		
		UnitSet set = setMap.get(key.getSetName());
		if (set == null) {
			return Single.error(new DattyStreamException(DattyError.ErrCode.SET_NOT_FOUND, key));
		}
		
		String majorKey = key.getMajorKey();
		String minorKey = key.getMinorKey();
		
		if (majorKey == null || minorKey == null) {
			return Single.error(new DattyStreamException(DattyError.ErrCode.BAD_ARGUMENTS, key));
		}
				
		UnitRecord record = set.getRecordMap().get(majorKey);
		if (record == null) {
			
			record = new UnitRecord(minorKey, new UnitValue());
			UnitRecord c = set.getRecordMap().putIfAbsent(majorKey, record);
			if (c != null) {
				record = c;
			}
		}
		
		if (!record.hasColumn(minorKey)) {
			
			UnitRecord newRecord = new UnitRecord(record, minorKey, new UnitValue(), UpdatePolicy.MERGE);
			if (!set.getRecordMap().replace(majorKey, record, newRecord)) {
				return Single.error(new DattyStreamException(DattyError.ErrCode.CONCURRENT_UPDATE, key));
			}
			
			record = newRecord;
		}
		
		UnitValue streamingValue = record.getColumn(minorKey);
		if (streamingValue == null) {
			return Single.error(new DattyStreamException(DattyError.ErrCode.UNKNOWN, key));
		}
		
		final ByteBuf destBuffer = streamingValue.asByteBuf();
		
		Observable<ByteBuf> result = value.reduce(destBuffer, new Func2<ByteBuf, ByteBuf, ByteBuf>() {

			@Override
			public ByteBuf call(ByteBuf dest, ByteBuf chunk) {
				dest.writeBytes(chunk);
				return dest;
			}
			
		});
		
		return result.map(new Func1<ByteBuf, Long>() {

			@Override
			public Long call(ByteBuf buf) {
				return (long) buf.readableBytes();
			}
			
		}).toSingle();
	}

}
