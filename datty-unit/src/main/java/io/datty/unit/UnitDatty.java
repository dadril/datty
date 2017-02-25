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

import io.datty.api.Datty;
import io.datty.api.DattyKey;
import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.Region;
import io.datty.api.RegionExistsAction;
import io.netty.buffer.ByteBuf;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.Single;

/**
 * Unit implementation for Datty API
 * 
 * @author dadril
 *
 */

public class UnitDatty implements Datty {

	private final String name;
	private final ConcurrentMap<String, UnitStore> storeMap = new ConcurrentHashMap<String, UnitStore>();

	public UnitDatty() {
		this(new Properties());
	}

	public UnitDatty(Properties props) {
		this.name = props.getProperty(UnitPropertyKeys.NAME);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Region findStore(String regionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Region getStore(String regionName, Properties regionProperties,
			RegionExistsAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<DattyResult> execute(DattyOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<DattyResult> executeBatch(
			Observable<DattyOperation> operations) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public String toString() {
		return "UnitDatty [name=" + name + "]";
	}

}
