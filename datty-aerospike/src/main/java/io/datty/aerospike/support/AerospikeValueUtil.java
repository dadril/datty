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
import java.util.Arrays;

import com.aerospike.client.Value;
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
		
		if (bufferOrNull != null) {
			// send arguments as Bytes
			return new BytesValue(arrayOf(bufferOrNull));
		}
		
		return new NullValue();
	}
	
	public static byte[] arrayOf(ByteBuf buffer) {
		return toByteArray(buffer,  buffer.readerIndex(), buffer.readableBytes(), false);
	}
	
	public static byte[] arrayOf(ByteBuf buffer, int start, int length) {
		return toByteArray(buffer, start, length, false);
	}
	
	public static byte[] toByteArray(ByteBuf buffer) {
		return toByteArray(buffer, buffer.readerIndex(), buffer.readableBytes(), true);
	}
	
	public static byte[] toByteArray(ByteBuf buffer, int start, int length, boolean copy) {

		if (isOutOfBounds(start, length, buffer.capacity())) {
			throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" + length
					+ ") <= " + "buf.capacity(" + buffer.capacity() + ')');
		}

		if (buffer.hasArray()) {
			if (copy || start != 0 || length != buffer.capacity()) {
				int baseOffset = buffer.arrayOffset() + start;
				return Arrays.copyOfRange(buffer.array(), baseOffset, baseOffset + length);
			} else {
				return buffer.array();
			}
		}

		byte[] v = new byte[length];
		int readerIndex = buffer.readerIndex();
		buffer.getBytes(start, v);
		buffer.readerIndex(readerIndex);
		return v;

	}
	
	public static boolean isOutOfBounds(int index, int length, int capacity) {
		return (index | length | (index + length) | (capacity - (index + length))) < 0;
	}
	
}
