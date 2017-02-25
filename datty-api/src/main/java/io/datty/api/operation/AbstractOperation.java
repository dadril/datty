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

import io.datty.api.DattyResult;
import io.datty.api.SingleOperation;
import io.datty.api.payload.SingleOperationPayload;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Abstract operation
 * 
 * @author dadril
 *
 */

public abstract class AbstractOperation<O extends SingleOperation<O>, R extends DattyResult>
		extends AbstractFuture<R> implements SingleOperation<O>,
		ListenableFuture<R> {


	protected String storeName;

	private String superKey;

	private String majorKey;

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

	protected boolean isExecuted() {
		return executed;
	}

	protected void ensureNotExecuted() {
		if (executed) {
			throw new IllegalStateException(
					"execution phase does not allow modificaitons in operation "
							+ this);
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
