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
package io.datty.msgpack.table;

import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.buffer.ArrayBufferOutput;
import org.msgpack.value.Value;

import io.datty.msgpack.table.support.PackableException;
import io.datty.msgpack.table.util.PackableStringifyUtil;
import io.netty.buffer.ByteBuf;

/**
 * Base interface for all packable values
 * 
 * @author Alex Shvid
 *
 * @param <T> - child type
 */

public abstract class PackableValue<T> {

	/**
	 * Gets value as a string
	 * 
	 * @return string representation of the value
	 */
	
	public abstract String asString();
	
	/**
	 * Gets msgpack value
	 * 
	 * @return not null msgpack value
	 */
	
	public abstract Value toValue();

	/**
	 * Gets msgpack json value
	 * 
	 * @return not null json value
	 */
	
	public String toJson() {
		return toValue().toJson();
	}
	
	/**
	 * Writes payload to packer
	 * 
	 * This method is faster then toValue().writeTo(packer) 
	 * because does not create intermediate Value object
	 * 
	 * @param packer - output packer
	 * throws IOException
	 */
	
	public abstract void writeTo(MessagePacker packer) throws IOException;
	
	/**
	 * Writes payload to buffer
	 * 
	 * @param not null buffer
	 * @throws IOException
	 */
	
	public abstract ByteBuf pack(ByteBuf buffer)  throws IOException;
	
	/**
	 * Gets hex string value representation
	 * 
	 * @return not null hex string
	 */
	
	public String toHexString() {
		return PackableStringifyUtil.toHex(toByteArray());
	}
	
	/**
	 * Gets byte array representation of the value
	 * 
	 * @return serialized value
	 */
	
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
	
	/**
	 * Prints value to string
	 * 
	 * @param out - output string builder
	 * @param initialSpaces - initial number of spaces
	 * @param tabSpaces - tab number of spaces
	 */
	
	public abstract void print(StringBuilder out, int initialSpaces, int tabSpaces);
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		print(str, 0, 2);
		return str.toString();
	}
	
}
