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
package io.datty.api;

/**
 * AbstractIOTest
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractDattyIOTest {

	protected boolean numeric = false;
	
	protected String setName = "SetName";
	
	protected String superKey = "SuperKey";
	
	protected String majorKey = "MajorKey";
	
	protected int timeoutMillis = 100;
	
}
