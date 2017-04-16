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
package io.datty.aerospike.support;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AerospikeValueUtil
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeValueUtil {

	private AerospikeValueUtil() {
	}

	public static ByteBuf toByteBuf(Object value) {
		if (value instanceof byte[]) {
			return Unpooled.wrappedBuffer((byte[]) value);
		}
		else if (value instanceof String) {
			return asByteBuf((String) value);
		}
		else {
			return asByteBuf(value.toString());
		}
	}
	
	public static ByteBuf asByteBuf(String str) {
		return Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
	}
	
}
