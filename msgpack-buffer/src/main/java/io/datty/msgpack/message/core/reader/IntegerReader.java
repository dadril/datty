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
 * IntegerReader
 * 
 * @author Alex Shvid
 *
 */

public class IntegerReader extends AbstractMessageReader implements ValueReader<Integer> {

	public static final IntegerReader INSTANCE = new IntegerReader();
	
	@Override
	public Integer read(ByteBuf buffer, boolean copy) {

		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (int) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseInt(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0;

		case Code.TRUE:
			return 1;

		case Code.UINT8:
			return (int) readUnsignedByte(buffer);
		case Code.UINT16:
			return (int) readUnsignedShort(buffer);
		case Code.UINT32:
			long u32 = readUnsignedInt(buffer);
			if (u32 > (long) Integer.MAX_VALUE) {
        throw new MessageNumberOverflowException(u32);
			}
			return (int) u32;
		case Code.UINT64:
			long u64 = readUnsignedLong(buffer);
			if (u64 > (long) Integer.MAX_VALUE) {
        throw new MessageNumberOverflowException(u64);
			}
			return (int) u64;
		case Code.INT8:
			return (int) readByte(buffer);
		case Code.INT16:
			return (int) readShort(buffer);
		case Code.INT32:
			return readInt(buffer);
		case Code.INT64:
			long i64 = readLong(buffer);
      if (i64 < (long) Integer.MIN_VALUE || i64 > (long) Integer.MAX_VALUE) {
        throw new MessageNumberOverflowException(i64);
      }
			return (int) i64;

		case Code.FLOAT32:
			return (int) readFloat(buffer);
		case Code.FLOAT64:
			return (int) readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseInt(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseInt(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
	}

	private Integer parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid int: " + str, e);
		}
	}


}
