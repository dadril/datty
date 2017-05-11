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
import io.datty.api.result.PutResult;
import io.netty.buffer.ByteBuf;

/**
 * Put operation
 * 
 * @author Alex Shvid
 *
 */

public class PutOperation extends AbstractUpdateOperation<PutOperation, PutResult> {

	private DattyRow row;
	
	public PutOperation(String storeName) {
		super(storeName);
	}

	public PutOperation(String storeName, String majorKey) {
		super(storeName);
		setMajorKey(majorKey);
	}

	public DattyRow getRow() {
		return row;
	}
	
	public PutOperation setRow(DattyRow row) {
		this.row = row;
		return this;
	}
	
	public PutOperation addValue(String minorKey, ByteBuf value) {
		if (row == null) {
			row = new DattyRow();
		}
		row.putValue(minorKey, value, true);
		return this;
	}
	
	public boolean hasRow() {
		return row != null;
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.PUT;
	}

	@Override
	public String toString() {
		return "PutOperation [row=" + row + ", updatePolicy=" + updatePolicy + ", cacheName=" + cacheName
				+ ", superKey=" + superKey + ", majorKey=" + majorKey + ", timeoutMillis=" + timeoutMillis + "]";
	}

}
