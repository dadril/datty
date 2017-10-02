package io.datty.msgpack.lite.impl;

import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.buffer.ArrayBufferOutput;

import io.datty.msgpack.lite.PackableValue;
import io.datty.msgpack.lite.support.PackableException;
import io.datty.msgpack.lite.util.PackableStringifyUtil;

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
