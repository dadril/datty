package io.datty.msgpack.lite;

import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.buffer.ArrayBufferOutput;
import org.msgpack.value.Value;

import io.datty.msgpack.lite.PackableValue;
import io.datty.msgpack.lite.support.PackableException;
import io.datty.msgpack.lite.util.PackableStringifyUtil;

/**
 * AbstractPackableTest
 * 
 * @author Alex Shvid
 *
 */

public class AbstractPackableTest {

	public byte[] toByteArray(Value value) {

		ArrayBufferOutput out = new ArrayBufferOutput();
		try {
			MessagePacker packer = MessagePack.newDefaultPacker(out);
			value.writeTo(packer);
			packer.flush();
		} catch (IOException e) {
			throw new PackableException("IOException happened during serialization to byte array", e);
		}

		return out.toByteArray();

	}

	public String toHexString(PackableValue<?> value) {
		return PackableStringifyUtil.toHex(toByteArray(value.toValue()));
	}
	
}
