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
import io.datty.msgpack.message.support.MessageNumberOverflowException;
import io.datty.msgpack.message.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ShortReader
 * 
 * @author Alex Shvid
 *
 */

public class ShortReader extends AbstractMessageReader implements ValueReader<Short> {

	public static final ShortReader INSTANCE = new ShortReader();
	
	@Override
	public Short read(ByteBuf buffer, boolean copy) {

		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (short) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseShort(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0;

		case Code.TRUE:
			return 1;

		case Code.UINT8:
			return (short) readUnsignedByte(buffer);
		case Code.UINT16:
			int u16 = readUnsignedShort(buffer);
			if (u16 > (int) Short.MAX_VALUE) {
        throw new MessageNumberOverflowException(u16);
			}
			return (short) u16;
		case Code.UINT32:
			long u32 = readUnsignedInt(buffer);
			if (u32 > (long) Short.MAX_VALUE) {
        throw new MessageNumberOverflowException(u32);
			}
			return (short) u32;
		case Code.UINT64:
			long u64 = readUnsignedLong(buffer);
			if (u64 > (long) Short.MAX_VALUE) {
        throw new MessageNumberOverflowException(u64);
			}
			return (short) u64;
		case Code.INT8:
			return (short) readByte(buffer);
		case Code.INT16:
			return readShort(buffer);
		case Code.INT32:
			int i32 = readInt(buffer);
      if (i32 < (int) Short.MIN_VALUE || i32 > (int) Short.MAX_VALUE) {
        throw new MessageNumberOverflowException(i32);
      }
			return (short) i32;
		case Code.INT64:
			long i64 = readLong(buffer);
      if (i64 < (long) Short.MIN_VALUE || i64 > (long) Short.MAX_VALUE) {
        throw new MessageNumberOverflowException(i64);
      }
			return (short) i64;

		case Code.FLOAT32:
			return (short) readFloat(buffer);
		case Code.FLOAT64:
			return (short) readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseShort(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseShort(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
	}

	private Short parseShort(String str) {
		try {
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid short: " + str, e);
		}
	}
	
}
