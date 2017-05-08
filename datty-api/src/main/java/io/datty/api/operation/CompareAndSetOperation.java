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

import io.datty.api.DattyRow;
import io.datty.api.result.CompareAndSetResult;
import io.netty.buffer.ByteBuf;

/**
 * CompareAndSetOperation
 * 
 * @author Alex Shvid
 *
 */

public class CompareAndSetOperation extends AbstractUpdateOperation<CompareAndSetOperation, CompareAndSetResult> {

	/**
	 * Old version of the record
	 */
	
	private Version version;
	
	private DattyRow row;

	public CompareAndSetOperation(String cacheName) {
		super(cacheName);
	}

	public CompareAndSetOperation(String cacheName, String majorKey) {
		super(cacheName, majorKey);
	}

	public boolean hasVersion() {
		return version != null;
	}
	
	public Version getVersion() {
		return version;
	}

	public CompareAndSetOperation setVersion(Version oldVersion) {
		this.version = oldVersion;
		return this;
	}
	
	public CompareAndSetOperation withVersion(Version oldVersion) {
		this.version = oldVersion;
		return this;
	}
	
	public DattyRow getRow() {
		return row;
	}

	public CompareAndSetOperation setRow(DattyRow row) {
		this.row = row;
		return this;
	}
	
	public boolean hasRow() {
		return row != null;
	}

	public CompareAndSetOperation addValue(String minorKey, ByteBuf value) {
		if (row == null) {
			row = new DattyRow();
		}
		row.putValue(minorKey, value);
		return this;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.COMPARE_AND_SET;
	}

	@Override
	public String toString() {
		return "CompareAndSetOperation [oldVersion=" + version + ", row=" + row + ", updatePolicy="
				+ updatePolicy + ", cacheName=" + cacheName + ", superKey=" + superKey + ", majorKey=" + majorKey
				+ ", timeoutMillis=" + timeoutMillis + "]";
	}
	
	
}
