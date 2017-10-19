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

import io.datty.api.DattyRecord;
import io.datty.api.DattyValue;
import io.datty.api.result.PutResult;

/**
 * Put operation
 * 
 * @author Alex Shvid
 *
 */

public class PutOperation extends AbstractUpdateOperation<PutOperation, PutResult> {

	private DattyRecord record;
	
	public PutOperation() {
	}
	
	public PutOperation(String setName) {
		setSetName(setName);
	}

	public PutOperation(String setName, String majorKey) {
		setSetName(setName).setMajorKey(majorKey);
	}
	
	public DattyRecord getRecord() {
		return record;
	}
	
	public PutOperation setRecord(DattyRecord rec) {
		this.record = rec;
		return this;
	}
	
	public PutOperation addValue(String minorKey, DattyValue value) {
		if (record == null) {
			record = new DattyRecord();
		}
		record.put(minorKey, value);
		return this;
	}
	
	public boolean hasRecord() {
		return record != null;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.PUT;
	}

	@Override
	public String toString() {
		return "PutOperation [record=" + record + ", updatePolicy=" + updatePolicy + ", setName=" + setName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
