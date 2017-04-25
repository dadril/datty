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

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.reader.StringReader;
import io.netty.buffer.ByteBuf;

/**
 * StringMapMessageReader
 * 
 * @author Alex Shvid
 *
 */

public class StringMapMessageReader extends ValueMessageReader<String> implements MessageReader<String> {

	private final int size;
	
	public StringMapMessageReader(int size) {
		this.size = size;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public String readKey(ByteBuf source) {
		return StringReader.INSTANCE.read(source, true);
	}

}
