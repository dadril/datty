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

import io.datty.api.DattyOperation;
import io.datty.api.SingleOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Batch Operation
 * 
 * @author dadril
 *
 */

public class BatchOperation implements DattyOperation {

	private final List<SingleOperation<?>> list = new ArrayList<>();

	/**
	 * Adds operation to the batch
	 * 
	 * @param operation
	 *            - not null single operation
	 * @return this
	 */

	public BatchOperation add(SingleOperation<?> operation) {
		list.add(operation);
		return this;
	}

	public List<SingleOperation<?>> getList() {
		return list;
	}

	public int size() {
		return list.size();
	}

	public SingleOperation<?> get(int i) {
		return list.get(i);
	}

}
