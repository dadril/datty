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
package io.datty.msgpack;

import io.datty.msgpack.core.ValueMessageReader;
import io.netty.buffer.ByteBuf;

/**
 * MessageFactory
 * 
 * @author Alex Shvid
 *
 */

public final class MessageFactory {

	private MessageFactory() {
	}

	public static Object readValue(ByteBuf source, boolean copy) {
		return ValueMessageReader.INSTANCE.readValue(source, copy);
	}
	
	public static ByteBuf skipValue(ByteBuf source, boolean copy) {
		return ValueMessageReader.INSTANCE.skipValue(source, copy);
	}
	
	public static <T> T readValue(Class<T> type, ByteBuf source, boolean copy) {
		return ValueMessageReader.INSTANCE.readValue(type, source, copy);
	}
	
}
