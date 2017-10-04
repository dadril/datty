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
 * ByteReader
 * 
 * @author Alex Shvid
 *
 */

public class ByteReader extends AbstractMessageReader implements ValueReader<Byte> {

	public static final ByteReader INSTANCE = new ByteReader();
	
	@Override
	public Byte read(ByteBuf buffer, boolean copy) {
		
		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (byte) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseByte(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0;

		case Code.TRUE:
			return 1;

		case Code.UINT8:
			short u8 = readUnsignedByte(buffer);
			if (u8 > (short) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(u8);
			}
			return (byte) u8;
		case Code.UINT16:
			int u16 = readUnsignedShort(buffer);
			if (u16 > (int) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(u16);
			}
			return (byte) u16;
		case Code.UINT32:
			long u32 = readUnsignedInt(buffer);
			if (u32 > (long) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(u32);
			}
			return (byte) u32;
		case Code.UINT64:
			long u64 = readUnsignedLong(buffer);
			if (u64 > (long) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(u64);
			}
			return (byte) u64;
		case Code.INT8:
			return readByte(buffer);
		case Code.INT16:
			short i16 = readShort(buffer);
      if (i16 < (short) Byte.MIN_VALUE || i16 > (short) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(i16);
      }
      return (byte) i16;
		case Code.INT32:
			int i32 = readInt(buffer);
      if (i32 < (int) Byte.MIN_VALUE || i32 > (int) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(i32);
      }
			return (byte) i32;
		case Code.INT64:
			long i64 = readLong(buffer);
      if (i64 < (long) Byte.MIN_VALUE || i64 > (long) Byte.MAX_VALUE) {
        throw new MessageNumberOverflowException(i64);
      }
			return (byte) i64;

		case Code.FLOAT32:
			return (byte) readFloat(buffer);
		case Code.FLOAT64:
			return (byte) readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseByte(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseByte(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
		
	}
	
	private Byte parseByte(String str) {
		try {
			return Byte.parseByte(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid byte: " + str, e);
		}
	}

	
}
