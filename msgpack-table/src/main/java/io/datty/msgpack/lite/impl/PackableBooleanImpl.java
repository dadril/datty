package io.datty.msgpack.lite.impl;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableBooleanValueImpl;

import io.datty.msgpack.lite.PackableBoolean;

/**
 * PackableBooleanImpl immutable implementation
 * 
 * @author Alex Shvid
 *
 */

public final class PackableBooleanImpl extends AbstractPackableValueImpl<PackableBoolean> implements PackableBoolean {

	private final boolean booleanValue;
	
	public PackableBooleanImpl(boolean value) {
		this.booleanValue = value;
	}
	
	public PackableBooleanImpl(String value) {
		this.booleanValue = Boolean.parseBoolean(value);
	}
	
	@Override
	public String asString() {
		return Boolean.toString(booleanValue);
	}

	@Override
	public boolean asBoolean() {
		return booleanValue;
	}

	@Override
  public Value toValue() {
    return booleanValue ? ImmutableBooleanValueImpl.TRUE : ImmutableBooleanValueImpl.FALSE;
  }
	
  @Override
	public void writeTo(MessagePacker packer) throws IOException {
  	packer.packBoolean(booleanValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (booleanValue ? 1231 : 1237);
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
		PackableBooleanImpl other = (PackableBooleanImpl) obj;
		if (booleanValue != other.booleanValue)
			return false;
		return true;
	}

	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("LiteBoolean [booleanValue=").append(booleanValue).append("]");
	}

}
