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

import io.datty.api.operation.PutOperation;

/**
 * PutResult
 * 
 * @author Alex Shvid
 *
 */

public class PutResult extends AbstractResult<PutOperation, PutResult> {

	private boolean updated;
	
	private long writtenBytes;
	
	public PutResult() {
	}
	
	public PutResult(long writtenBytes) {
		this.writtenBytes = writtenBytes;
	}
	
	public boolean isUpdated() {
		return updated;
	}

	public PutResult setUpdated(boolean flag) {
		this.updated = flag;
		return this;
	}
	
	public boolean hasWrittenBytes() {
		return writtenBytes != 0L;
	}
	
	public long getWrittenBytes() {
		return writtenBytes;
	}

	public PutResult setWrittenBytes(long writtenBytes) {
		this.writtenBytes = writtenBytes;
		return this;
	}

	@Override
	public ResCode getCode() {
		return ResCode.PUT;
	}

	@Override
	public String toString() {
		return "PutResult [updated=" + updated + ", writtenBytes=" + writtenBytes + "]";
	}

}
