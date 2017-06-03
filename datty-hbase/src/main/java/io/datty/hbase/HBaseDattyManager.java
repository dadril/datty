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
package io.datty.hbase;

import java.util.Properties;

import io.datty.api.DattySet;
import io.datty.api.SetExistsAction;
import io.datty.api.DattyManager;
import io.datty.api.Datty;

/**
 * HBaseDattyManager
 * 
 * @author Alex Shvid
 *
 */

public class HBaseDattyManager implements DattyManager {

	public HBaseDattyManager(Properties props) {
		
	}

	@Override
	public String getManagerName() {
		return "hbase";
	}

	@Override
	public DattySet getSet(String cacheName) {
		return null;
	}

	@Override
	public DattySet getSet(String cacheName, Properties cacheProperties, SetExistsAction action) {
		return null;
	}

	@Override
	public Datty getDatty() {
		return null;
	}

	@Override
	public void setDatty(Datty newDatty) {
	}
	
}
