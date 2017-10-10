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
 * DoubleReader
 * 
 * @author Alex Shvid
 *
 */

public class DoubleReader extends AbstractMessageReader implements ValueReader<Double> {

	public static final DoubleReader INSTANCE = new DoubleReader();
	
	@Override
	public Double read(ByteBuf buffer, boolean copy) {
		
		if (!hasNext(buffer)) {
			return null;
		}

		byte b = readByte(buffer);

		if (Code.isFixInt(b)) {
			return (double) b;
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return parseDouble(readString(b, buffer));
		}

		switch (b) {

		case Code.NIL:
			return null;

		case Code.FALSE:
			return 0.0;

		case Code.TRUE:
			return 1.0;

		case Code.UINT8:
			return (double) readUnsignedByte(buffer);
		case Code.UINT16:
			return (double) readUnsignedShort(buffer);
		case Code.UINT32:
			return (double) readUnsignedInt(buffer);
		case Code.UINT64:
			return (double) readUnsignedLong(buffer);
		case Code.INT8:
			return (double) readByte(buffer);
		case Code.INT16:
			return (double) readShort(buffer);
		case Code.INT32:
			return (double) readInt(buffer);
		case Code.INT64:
			return (double) readLong(buffer);
		case Code.FLOAT32:
			return (double) readFloat(buffer);
		case Code.FLOAT64:
			return readDouble(buffer);

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
			return parseDouble(readString(b, buffer));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return parseDouble(readAsciiString(b, buffer));

		default:
			buffer.readerIndex(buffer.readerIndex() - 1);
			skipValue(buffer);
			return null;
		}
		
	}
	
	private Double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			throw new MessageParseException("invalid double: " + str, e);
		}
	}
	
}
