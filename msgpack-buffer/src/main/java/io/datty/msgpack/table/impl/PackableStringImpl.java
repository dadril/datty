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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableBinaryValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

import io.datty.msgpack.core.writer.ByteBufWriter;
import io.datty.msgpack.core.writer.StringWriter;
import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableStringType;
import io.datty.msgpack.table.support.PackableException;
import io.netty.buffer.ByteBuf;

/**
 * PackableStringImpl
 * 
 * @author Alex Shvid
 *
 */

public final class PackableStringImpl extends AbstractPackableValueImpl<PackableString> implements PackableString {

	private final String stringValue;
	private final byte[] bytesValue;
	private final PackableStringType type;
	
	public PackableStringImpl(String value) {
		
		if (value == null) {
			throw new IllegalArgumentException("empty value");
		}		
		
		this.stringValue = value;
		this.bytesValue = null;
		this.type = PackableStringType.UTF8;
	}
	
	public PackableStringImpl(byte[] value, boolean copy) {
		
		if (value == null) {
			throw new IllegalArgumentException("empty value");
		}
		
		this.stringValue = null;
		this.bytesValue = copy ? Arrays.copyOf(value, value.length) : value;
		this.type = PackableStringType.BYTES;
	}
	
	@Override
	public PackableStringType getType() {
		return type;
	}
	
	@Override
	public String asString() {
		return toUtf8();
	}
	
	@Override
	public String toUtf8() {
		
		switch(type) {
		
		case UTF8:
			return stringValue;
			
		case BYTES:
			return new String(bytesValue, StandardCharsets.UTF_8);
			
		}
		
		throw new PackableException("unexpected type: " + type);
		
	}
	
	@Override
	public byte[] getBytes(boolean copy) {
		
		switch(type) {
		
		case UTF8:
			return stringValue.getBytes(StandardCharsets.UTF_8);
			
		case BYTES:
			return copy ? Arrays.copyOf(bytesValue, bytesValue.length) : bytesValue;
			
		}
		
		throw new PackableException("unexpected type: " + type);
	}
	
	@Override
	public Value toValue() {
		
		switch(type) {
		
		case UTF8:
	    return new ImmutableStringValueImpl(stringValue);  
		case BYTES:
			return new ImmutableBinaryValueImpl(bytesValue);  
		}
		
		throw new PackableException("unexpected type: " + type);
	}

  @Override
	public void writeTo(MessagePacker packer) throws IOException {
		switch(type) {
		
		case UTF8:
			byte[] data = stringValue.getBytes(StandardCharsets.UTF_8);
			packer.packRawStringHeader(data.length);
			packer.writePayload(data);
			break;
			
		case BYTES:
			packer.packBinaryHeader(bytesValue.length);
			packer.writePayload(bytesValue);
			break;
			
		default:
		  throw new IOException("unexpected type: " + type);		
		}	
	}
  

	@Override
	public ByteBuf pack(ByteBuf buffer) throws IOException {
		
		switch(type) {
		
		case UTF8:
			return StringWriter.INSTANCE.writeString(stringValue, buffer);
			
		case BYTES:
			ByteBufWriter.INSTANCE.writeBinaryHeader(bytesValue.length, buffer);
			return ByteBufWriter.INSTANCE.writeBytes(bytesValue, buffer);
			
		default:
		  throw new IOException("unexpected type: " + type);		
		}			
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (type == PackableStringType.BYTES) {
			result = prime * result + Arrays.hashCode(bytesValue);
		}
		if (type == PackableStringType.UTF8) {
			result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
		}
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackableStringImpl other = (PackableStringImpl) obj;
		if (type != other.type)
			return false;
		if (type == PackableStringType.BYTES && !Arrays.equals(bytesValue, other.bytesValue))
			return false;
		if (type == PackableStringType.UTF8) {
			if (stringValue == null) {
				if (other.stringValue != null)
					return false;
			} else if (!stringValue.equals(other.stringValue))
				return false;
		}
		return true;
	}

	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("PackableString [type=").append(type);
		if (type == PackableStringType.UTF8) {
			str.append(", stringValue=").append(stringValue);
		}
		else if (type == PackableStringType.BYTES) {
			str.append(", bytesValue=").append(Arrays.toString(bytesValue));
		}
		str.append("]");
	}

}
