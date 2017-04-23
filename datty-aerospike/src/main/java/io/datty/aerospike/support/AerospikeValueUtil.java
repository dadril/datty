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

import com.aerospike.client.Value;
import com.aerospike.client.Value.ByteSegmentValue;
import com.aerospike.client.Value.BytesValue;
import com.aerospike.client.Value.NullValue;

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

	public static ByteBuf toByteBuf(Object aerospikeValue) {
		if (aerospikeValue instanceof byte[]) {
			return Unpooled.wrappedBuffer((byte[]) aerospikeValue);
		}
		else if (aerospikeValue instanceof String) {
			return asByteBuf((String) aerospikeValue);
		}
		else {
			return asByteBuf(aerospikeValue.toString());
		}
	}
	
	public static ByteBuf asByteBuf(String str) {
		return Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
	}
	
	public static Value toValue(ByteBuf bufferOrNull) {
		
		if (bufferOrNull == null) {
			return new NullValue();
		}
		else if (bufferOrNull.hasArray()) {
			int start = bufferOrNull.readerIndex();
			int length = bufferOrNull.readableBytes();
			if (start != 0 || length != bufferOrNull.capacity()) {
				int baseOffset = bufferOrNull.arrayOffset() + start;
				return new ByteSegmentValue(bufferOrNull.array(), baseOffset, baseOffset + length);
			} else {
				return new BytesValue(bufferOrNull.array());
			}
		}
		else {
			byte[] bytes = new byte[bufferOrNull.readableBytes()];
			bufferOrNull.getBytes(bufferOrNull.readerIndex(), bytes);
			return new BytesValue(bytes);
		}
		
	}
	
}
