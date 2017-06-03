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
package io.datty.aerospike;

import com.aerospike.client.Key;
import com.aerospike.client.Record;

/**
 * AerospikeRecord
 * 
 * Extension of the aerospike Record class to include also a Key class
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeRecord {

	private final Key key;
	private final Record record;
	
	public AerospikeRecord(Key key, Record record) {
		this.key = key;
		this.record = record;
	}

	public Key getKey() {
		return key;
	}

	public Record getRecord() {
		return record;
	}

	@Override
	public String toString() {
		return "AerospikeRecord [key=" + key + ", record=" + record + "]";
	}
	
}
