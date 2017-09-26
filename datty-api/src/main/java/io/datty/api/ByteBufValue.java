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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * ByteBufValue
 * 
 * Simple implementation of DattyValue with single ByteBuf value
 * 
 * @author Alex Shvid
 *
 */

public class ByteBufValue implements DattyValue {

	private final ByteBuf value;
	
	public ByteBufValue(ByteBuf value) {
		if (value == null) {
			throw new IllegalArgumentException("null value");
		}
		this.value = value;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public boolean hasByteBuf() {
		return true;
	}

	@Override
	public ByteBuf asByteBuf() {
		return value;
	}
	
	@Override
	public DattyValue retain() {
		ByteBuf o = value.retain();
		return o == value ? this : new ByteBufValue(o);
	}

	@Override
	public void release() {
		value.release();
	}

	@Override
	public ByteBuf write(ByteBuf sink) {
		return sink.writeBytes(value, value.readerIndex(), value.readableBytes());
	}

	@Override
	public byte[] toByteArray() {
		return ByteBufUtil.getBytes(value);
	}
	
	
	
}
