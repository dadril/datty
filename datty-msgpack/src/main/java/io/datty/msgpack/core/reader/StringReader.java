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
import io.netty.buffer.ByteBuf;

/**
 * StringReader
 * 
 * @author Alex Shvid
 *
 */

public class StringReader extends AbstractMessageReader implements ValueReader<String> {

	public static final StringReader INSTANCE = new StringReader();

	@Override
	public String read(ByteBuf source, boolean copy) {

		if (!hasNext(source)) {
			return null;
		}

		byte b = getNextCode(source);

		if (Code.isFixInt(b)) {
			return Integer.toString((int) readByte(source));
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return readString(source);
		}

		switch (b) {

		case Code.NIL:
			return readNull(source);

		case Code.FALSE:
		case Code.TRUE:
			return Boolean.toString(readBoolean(source));

		case Code.UINT8:
		case Code.UINT16:
		case Code.UINT32:
		case Code.UINT64:
		case Code.INT8:
		case Code.INT16:
		case Code.INT32:
		case Code.INT64:
			return Long.toString(readVLong(source));

		case Code.FLOAT32: // float
		case Code.FLOAT64: // double
			return Double.toString(readVDouble(source));

		case Code.STR8: // str 8
		case Code.STR16: // str 16
		case Code.STR32: // str 32
			return readString(source);

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return readAsciiString(source);

		default:
			skipValue(source);
			return null;
		}
		
	}

}
