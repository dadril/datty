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

import com.aerospike.client.async.MaxCommandAction;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.ConsistencyLevel;
import com.aerospike.client.policy.Priority;

/**
 * AerospikeEnums
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeEnums {

	private AerospikeEnums() {
	}
	
	public static Priority parsePriority(String name) {

		for (Priority value : Priority.values()) {

			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}

		}

		throw new IllegalArgumentException("invalid priority: " + name);
	}
	
	public static ConsistencyLevel parseConsistencyLevel(String name) {

		for (ConsistencyLevel value : ConsistencyLevel.values()) {

			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}

		}

		throw new IllegalArgumentException("invalid consistency level: " + name);

	}
	
	public static CommitLevel parseCommitLevel(String name) {

		for (CommitLevel value : CommitLevel.values()) {

			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}

		}

		throw new IllegalArgumentException("invalid commit level: " + name);

	}
	
	public static MaxCommandAction parseMaxCommandAction(String name) {

		for (MaxCommandAction value : MaxCommandAction.values()) {

			if (value.name().equalsIgnoreCase(name)) {
				return value;
			}

		}

		throw new IllegalArgumentException("invalid command action: " + name);
	}
	
}
