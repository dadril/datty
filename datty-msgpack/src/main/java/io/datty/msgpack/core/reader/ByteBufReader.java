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

import java.nio.charset.StandardCharsets;

import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.core.AbstractMessageReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBufReader
 * 
 * @author Alex Shvid
 *
 */

public class ByteBufReader extends AbstractMessageReader implements ValueReader<ByteBuf> {

	public static final ByteBufReader INSTANCE = new ByteBufReader();
	
	@Override
	public ByteBuf read(ByteBuf source, boolean copy) {

		if (!hasNext(source)) {
			return null;
		}

		byte b = getNextCode(source);

		if (Code.isFixedRaw(b)) { // FixRaw
			return readBytes(source, copy);
		}
		
		if (Code.isFixInt(b)) {
			return wrap(Integer.toString((int) readByte(source)));
		}

		switch (b) {

		case Code.NIL:
			return readNull(source);

		case Code.FALSE:
		case Code.TRUE:
			return wrap(Boolean.toString(readBoolean(source)));

		case Code.UINT8:
		case Code.UINT16:
		case Code.UINT32:
		case Code.UINT64:
		case Code.INT8:
		case Code.INT16:
		case Code.INT32:
		case Code.INT64:
			return wrap(Long.toString(readVLong(source)));

		case Code.FLOAT32: // float
		case Code.FLOAT64: // double
			return wrap(Double.toString(readVDouble(source)));

		case Code.STR8: 
		case Code.STR16: 
		case Code.STR32: 
		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return readBytes(source, copy);

		default:
			skipValue(source);
			return null;
		}

	}
	
	public ByteBuf wrap(String str) {
		return Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
	}
	
}
