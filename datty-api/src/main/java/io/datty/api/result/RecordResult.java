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

import java.util.Collections;
import java.util.Set;

import io.datty.api.DattyResult;
import io.datty.api.DattyRecord;
import io.datty.api.DattyValue;
import io.datty.api.version.Version;

/**
 * RecordResult
 * 
 * @author Alex Shvid
 *
 */

public class RecordResult implements DattyResult {

	private String majorKey;
	
	/**
	 * Record version if exists
	 */
	
	private Version version;
	
	private DattyRecord record;
	
	/**
	 * Count records in result
	 * Valid for CountOperation and DeleteOperation
	 */
	
	private long count;
	
	public RecordResult() {
	}
	
	public boolean hasMajorKey() {
		return majorKey != null;
	}
	
	public String getMajorKey() {
		return majorKey;
	}

	public RecordResult setMajorKey(String majorKey) {
		this.majorKey = majorKey;
		return this;
	}

	public DattyRecord getRecord() {
		return record;
	}
	
	public RecordResult setRecord(DattyRecord rec) {
		this.record = rec;
		return this;
	}
	
	public boolean hasRecord() {
		return record != null;
	}
	
	public RecordResult addValue(String minorKey, DattyValue value) {
		if (record == null) {
			record = new DattyRecord();
		}
		record.put(minorKey, value);
		return this;
	}
	
	public boolean hasVersion() {
		return version != null;
	}
	
	public Version getVersion() {
		return version;
	}
	
	public RecordResult setVersion(Version version) {
		this.version = version;
		return this;
	}
	
	public long count() {
		return count;
	}

	public boolean hasCount() {
		return count != 0;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}

	public boolean exists() {
		return this.version != null;
	}

	public boolean isEmpty() {
		
		if (!hasRecord()) {
			return true;
		}
		
		return record.isEmpty();
	}
	
	public Set<String> minorKeys() {
		
		if (!hasRecord()) {
			return Collections.emptySet();
		}
		
		return record.minorKeys();
	}
	
	public DattyValue get(String minorKey) {
		
		if (!hasRecord()) {
			return null;
		}
		
		return record.get(minorKey);
	}
	
	public int size() {
		
		if (!hasRecord()) {
			return 0;
		}
		
		return record.size();
	}
	
	@Override
	public ResCode getCode() {
		return ResCode.RECORD;
	}

	@Override
	public String toString() {
		return "SetResult [majorKey=" + majorKey + ", version=" + version + ", row=" + record
				+ ", count=" + count + "]";
	}

}
