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
package io.datty.support;

import io.datty.api.DattyValue;
import io.netty.buffer.ByteBuf;

/**
 * NullDattyValue
 * 
 * @author Alex Shvid
 *
 */

public enum NullDattyValue implements DattyValue {

	NULL;

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public boolean hasByteBuf() {
		return false;
	}

	@Override
	public ByteBuf asByteBuf() {
		return null;
	}

	@Override
	public DattyValue retain() {
		return this;
	}

	@Override
	public void release() {
	}

	@Override
	public ByteBuf write(ByteBuf sink) {
		return sink;
	}

	@Override
	public byte[] toByteArray() {
		return null;
	}
	
}
