package io.datty.msgpack.lite.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableBinaryValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

import io.datty.msgpack.lite.LiteString;
import io.datty.msgpack.lite.LiteStringType;
import io.datty.msgpack.lite.support.LiteException;

/**
 * LiteStringImpl
 * 
 * @author Alex Shvid
 *
 */

public final class LiteStringImpl extends AbstractLiteValueImpl<LiteString> implements LiteString {

	private final String stringValue;
	private final byte[] bytesValue;
	private final LiteStringType type;
	
	public LiteStringImpl(String value) {
		
		if (value == null) {
			throw new IllegalArgumentException("empty value");
		}		
		
		this.stringValue = value;
		this.bytesValue = null;
		this.type = LiteStringType.UTF8;
	}
	
	public LiteStringImpl(byte[] value, boolean copy) {
		
		if (value == null) {
			throw new IllegalArgumentException("empty value");
		}
		
		this.stringValue = null;
		this.bytesValue = copy ? Arrays.copyOf(value, value.length) : value;
		this.type = LiteStringType.BYTES;
	}
	
	@Override
	public LiteStringType getType() {
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
		
		throw new LiteException("unexpected type: " + type);
		
	}
	
	@Override
	public byte[] getBytes(boolean copy) {
		
		switch(type) {
		
		case UTF8:
			return stringValue.getBytes(StandardCharsets.UTF_8);
			
		case BYTES:
			return copy ? Arrays.copyOf(bytesValue, bytesValue.length) : bytesValue;
			
		}
		
		throw new LiteException("unexpected type: " + type);
	}
	
	@Override
	public Value toValue() {
		
		switch(type) {
		
		case UTF8:
	    return new ImmutableStringValueImpl(stringValue);  
		case BYTES:
			return new ImmutableBinaryValueImpl(bytesValue);  
		}
		
		throw new LiteException("unexpected type: " + type);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (type == LiteStringType.BYTES) {
			result = prime * result + Arrays.hashCode(bytesValue);
		}
		if (type == LiteStringType.UTF8) {
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
		LiteStringImpl other = (LiteStringImpl) obj;
		if (type != other.type)
			return false;
		if (type == LiteStringType.BYTES && !Arrays.equals(bytesValue, other.bytesValue))
			return false;
		if (type == LiteStringType.UTF8) {
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
		str.append("LiteString [type=").append(type);
		if (type == LiteStringType.UTF8) {
			str.append(", stringValue=").append(stringValue);
		}
		else if (type == LiteStringType.BYTES) {
			str.append(", bytesValue=").append(Arrays.toString(bytesValue));
		}
		str.append("]");
	}

}
