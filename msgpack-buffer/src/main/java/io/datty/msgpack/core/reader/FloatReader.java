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
package io.datty.msgpack.core.reader;

import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.core.AbstractMessageReader;
import io.datty.msgpack.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * FloatReader
 * 
 * @author Alex Shvid
 *
 */

public class FloatReader extends AbstractMessageReader implements ValueReader<Float> {

	public static final FloatReader INSTANCE = new FloatReader();
	
	@Override
	public Float read(ByteBuf buffer, boolean copy) {
		
		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (float) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseFloat(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0.0f;

		case Code.TRUE:
			return 1.0f;

		case Code.UINT8:
			return (float) readUnsignedByte(buffer);
		case Code.UINT16:
			return (float) readUnsignedShort(buffer);
		case Code.UINT32:
			return (float) readUnsignedInt(buffer);
		case Code.UINT64:
			return (float) readUnsignedLong(buffer);
		case Code.INT8:
			return (float) readByte(buffer);
		case Code.INT16:
			return (float) readShort(buffer);
		case Code.INT32:
			return (float) readInt(buffer);
		case Code.INT64:
			return (float) readLong(buffer);
		case Code.FLOAT32:
			return readFloat(buffer);
		case Code.FLOAT64:
			return (float) readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseFloat(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseFloat(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
	}
	
	private Float parseFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid float: " + str, e);
		}
	}
	
}
