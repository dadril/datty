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
package io.datty.msgpack.message.core.reader;

import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.message.core.AbstractMessageReader;
import io.datty.msgpack.message.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * LongReader
 * 
 * @author Alex Shvid
 *
 */

public class LongReader extends AbstractMessageReader implements ValueReader<Long> {

	public static final LongReader INSTANCE = new LongReader();

	@Override
	public Long read(ByteBuf buffer, boolean copy) {

		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (long) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseLong(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0L;

		case Code.TRUE:
			return 1L;

		case Code.UINT8:
			return (long) readUnsignedByte(buffer);
		case Code.UINT16:
			return (long) readUnsignedShort(buffer);
		case Code.UINT32:
			return readUnsignedInt(buffer);
		case Code.UINT64:
			return readUnsignedLong(buffer);
		case Code.INT8:
			return (long) readByte(buffer);
		case Code.INT16:
			return (long) readShort(buffer);
		case Code.INT32:
			return (long) readInt(buffer);
		case Code.INT64:
			return readLong(buffer);

		case Code.FLOAT32: 
			return (long) readFloat(buffer);
		case Code.FLOAT64: 
			return (long) readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseLong(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseLong(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
	}

	private Long parseLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid long: " + str, e);
		}
	}

}
