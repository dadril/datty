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
package io.datty.api.result;

import java.util.Map;
import java.util.Set;

/**
 * ExistsResult
 * 
 * @author dadril
 *
 */

public class ExistsResult extends AbstractResult {

	private final boolean existsRecord;
	
	private final Map<String, Boolean> existsValues;
	
	public ExistsResult(boolean existsRecord, Map<String, Boolean> existsValues) {
		this.existsRecord = existsRecord;
		this.existsValues = existsValues;
	}

	public boolean isExistsRecord() {
		return existsRecord;
	}

	public boolean isEmpty() {
		return existsValues.isEmpty();
	}
	
	public int size() {
		return existsValues.size();
	}
	
	public Set<String> minorKeys() {
		return existsValues.keySet();
	}
	
	public Boolean exists(String minorKey) {
		return existsValues.get(minorKey);
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.EXIST;
	}
	
}
