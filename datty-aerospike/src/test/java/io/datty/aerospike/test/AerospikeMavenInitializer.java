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
package io.datty.aerospike.test;

import org.junit.runner.notification.RunListener;

/**
 * AerospikeMavenInitializer
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeMavenInitializer extends RunListener {

	static {
		System.out.println("AerospikeMavenInitializer");
		System.setProperty("datty.factory", "aerospike");
	}
	
}
