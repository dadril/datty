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
import io.netty.buffer.ByteBuf;

/**
 * BooleanReader
 * 
 * @author Alex Shvid
 *
 */

public class BooleanReader extends AbstractMessageReader implements ValueReader<Boolean> {

	public static final BooleanReader INSTANCE = new BooleanReader();

	@Override
	public Boolean read(ByteBuf source, boolean copy) {

		if (!hasNext(source)) {
			return null;
		}

		byte b = getNextCode(source);

		if (Code.isFixInt(b)) {
			return toBoolean(readByte(source));
		}

		if (Code.isFixedRaw(b)) { // FixRaw
			return Boolean.parseBoolean(readString(source));
		}

		switch (b) {

		case Code.NIL:
			return readNull(source);

		case Code.FALSE:
		case Code.TRUE:
			return readBoolean(source);

		case Code.UINT8:
		case Code.UINT16:
		case Code.UINT32:
		case Code.UINT64:
		case Code.INT8:
		case Code.INT16:
		case Code.INT32:
		case Code.INT64:
			return toBoolean((int) readVLong(source));

		case Code.FLOAT32:
		case Code.FLOAT64:
			return toBoolean((int) readVDouble(source));

		case Code.STR8:
		case Code.STR16:
		case Code.STR32:
			return Boolean.parseBoolean(readString(source));

		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return Boolean.parseBoolean(readAsciiString(source));

		default:
			skipValue(source);
			return null;
		}

	}

	private Boolean toBoolean(int val) {
		return val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}

}
