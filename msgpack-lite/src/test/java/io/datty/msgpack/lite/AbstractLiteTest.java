package io.datty.msgpack.lite;

import java.io.IOException;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.buffer.ArrayBufferOutput;
import org.msgpack.value.Value;

import io.datty.msgpack.lite.LiteValue;
import io.datty.msgpack.lite.support.LiteException;
import io.datty.msgpack.lite.util.LiteStringifyUtil;

/**
 * AbstractLiteTest
 * 
 * @author Alex Shvid
 *
 */

public class AbstractLiteTest {

	public byte[] toByteArray(Value value) {

		ArrayBufferOutput out = new ArrayBufferOutput();
		try {
			MessagePacker packer = MessagePack.newDefaultPacker(out);
			value.writeTo(packer);
			packer.flush();
		} catch (IOException e) {
			throw new LiteException("IOException happened during serialization to byte array", e);
		}

		return out.toByteArray();

	}

	public String toHexString(LiteValue<?> value) {
		return LiteStringifyUtil.toHex(toByteArray(value.toValue()));
	}
	
}
