/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package com.dadril.datty.api.operation;

import com.dadril.datty.api.DattyResult;
import com.dadril.datty.api.SingleOperation;
import com.dadril.datty.api.payload.SingleOperationPayload;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;

import io.protostuff.Tag;

/**
 * Abstract operation
 * 
 * @author dadril
 *
 */

public abstract class AbstractOperation<O extends SingleOperation<O>, R extends DattyResult> extends AbstractFuture<R> implements SingleOperation<O>, ListenableFuture<R> {

  @Tag(1)
	protected String storeName;
  
  @Tag(2)
	private String superKey;
  
  @Tag(3)
	private String majorKey;
  
  @Tag(4)
	private String minorKey;
  
	transient private boolean executed;
	
	public AbstractOperation(String storeName) {
		this.storeName = storeName;
	}
	
	public String getStoreName() {
		return storeName;
	}

	public O setSuperKey(String superKey) {
		ensureNotExecuted();
		this.superKey = superKey;
		return castThis();
	}
	
	public String getSuperKey() {
		return superKey;
	}
	
	public O setMajorKey(String majorKey) {
		ensureNotExecuted();
		this.majorKey = majorKey;
		return castThis();
	}
	
	public String getMajorKey() {
		return majorKey;
	}

	public O setMinorKey(String minorKey) {
		ensureNotExecuted();
		this.minorKey = minorKey;
		return castThis();
	}

	public String getMinorKey() {
		return minorKey;
	}

	@Override
	public O addToBatch(BatchOperation batch) {
		Preconditions.checkNotNull(batch, "null batch");
		batch.add(castThis());
		return castThis();
	}
	
	protected boolean isExecuted() {
		return executed;
	}
	
	protected void ensureNotExecuted() {
		if (executed) {
			throw new IllegalStateException("execution phase does not allow modificaitons in operation " + this);
		}
	}
	
	@SuppressWarnings("unchecked")
	private O castThis() {
		return (O) this;
	}
	
	protected void writeAbstractFields(SingleOperationPayload op) {
	  op.setStoreName(storeName);
	  op.setSuperKey(superKey);
	  op.setMajorKey(majorKey);
	  op.setMinorKey(minorKey);
	}
	
}
