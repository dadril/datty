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
package io.datty.msgpack.core;

import io.datty.msgpack.MessageConstants;
import io.datty.msgpack.MessageReader;
import io.netty.buffer.ByteBuf;

/**
 * ArrayMessageReader
 * 
 * @author Alex Shvid
 *
 */

public class ArrayMessageReader extends ValueMessageReader<Integer> implements MessageReader<Integer> {

	private final int length;
	private final int start;
	private int index = 0;
	
	public ArrayMessageReader(int length) {
		this(length, MessageConstants.ARRAY_START_INDEX);
	}
	
	public ArrayMessageReader(int length, int start) {
		this.length = length;
		this.start = start;
	}
	
	@Override
	public int size() {
		return length;
	}

	@Override
	public Integer readKey(ByteBuf source) {
		if (index < length) {
			int i = index++;
			return i + start;
		}
		return null;
	}

}
