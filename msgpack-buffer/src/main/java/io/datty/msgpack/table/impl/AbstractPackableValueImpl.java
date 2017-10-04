/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table.impl;

import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.buffer.ArrayBufferOutput;

import io.datty.msgpack.table.PackableValue;
import io.datty.msgpack.table.support.PackableException;
import io.datty.msgpack.table.util.PackableStringifyUtil;

/**
 * AbstractPackableValueImpl
 * 
 * @author Alex Shvid
 *
 * @param <T> - Msg type
 */

public abstract class AbstractPackableValueImpl<T> implements PackableValue<T> {

	@Override
	public byte[] toByteArray() {

		ArrayBufferOutput out = new ArrayBufferOutput();
		try {
			MessagePacker packer = MessagePack.newDefaultPacker(out);
			writeTo(packer);
			packer.flush();
		} catch (IOException e) {
			throw new PackableException("IOException happened during serialization to byte array", e);
		}

		return out.toByteArray();
	}

	@Override
	public String toHexString() {
		return PackableStringifyUtil.toHex(toByteArray());
	}

	@Override
	public String toJson() {
		return toValue().toJson();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		print(str, 0, 2);
		return str.toString();
	}

}
