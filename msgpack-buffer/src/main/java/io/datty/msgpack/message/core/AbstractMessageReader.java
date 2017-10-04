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
package io.datty.msgpack.message.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.message.support.MessageNumberOverflowException;
import io.datty.msgpack.message.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * AbstractMessageReader
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractMessageReader {

	public boolean hasNext(ByteBuf buffer) {
		return buffer.readableBytes() > 0;
	}

	public MessageFormat getNextFormat(ByteBuf buffer) {
		return MessageFormat.valueOf(buffer.getByte(buffer.readerIndex()));
	}

	public byte getNextCode(ByteBuf buffer) {
		return buffer.getByte(buffer.readerIndex());
	}

	public void skipBytes(int numberOfBytes, ByteBuf buffer) {
		buffer.skipBytes(numberOfBytes);
	}

	public byte readByte(ByteBuf buffer) {
		return buffer.readByte();
	}

	public short readUnsignedByte(ByteBuf buffer) {
		return (short) (buffer.readByte() & 0xFF);
	}

	public short readShort(ByteBuf buffer) {
		return buffer.readShort();
	}

	public int readUnsignedShort(ByteBuf buffer) {
		return buffer.readShort() & 0xFFFF;
	}

	public int readInt(ByteBuf buffer) {
		return buffer.readInt();
	}

	public long readUnsignedInt(ByteBuf buffer) {
		int u32 = buffer.readInt();
		if (u32 < 0) {
			long l64 = (long) (u32 & 0x7fffffff) + 0x80000000L;
			return l64;
		}
		return u32;
	}

	public long readLong(ByteBuf buffer) {
		return buffer.readLong();
	}

	public long readUnsignedLong(ByteBuf buffer) {
		long u64 = buffer.readLong();
		if (u64 < 0) {
			throw new MessageNumberOverflowException(u64);
		}
		return u64;
	}

	public int readLength8(ByteBuf buffer) {
		return readUnsignedByte(buffer);
	}

	public int readLength16(ByteBuf buffer) {
		return readUnsignedShort(buffer);
	}

	public int readLength32(ByteBuf buffer) {
		long u32 = readUnsignedInt(buffer);
		if (u32 > (long) Integer.MAX_VALUE) {
			throw new MessageNumberOverflowException(u32);
		}
		return (int) u32;
	}

	public float readFloat(ByteBuf buffer) {
		return Float.intBitsToFloat(buffer.readInt());
	}

	public double readDouble(ByteBuf buffer) {
		return Double.longBitsToDouble(buffer.readLong());
	}

	public void skipValue(ByteBuf buffer) {
		skipValue(1, buffer);
	}

	public void skipValue(int count, ByteBuf buffer) {
		while (count > 0) {
			byte b = readByte(buffer);
			MessageFormat f = MessageFormat.valueOf(b);
			switch (f) {
			case POSFIXINT:
			case NEGFIXINT:
			case BOOLEAN:
			case NIL:
				break;
			case FIXMAP: {
				int mapLen = b & 0x0f;
				count += mapLen * 2;
				break;
			}
			case FIXARRAY: {
				int arrayLen = b & 0x0f;
				count += arrayLen;
				break;
			}
			case FIXSTR: {
				int strLen = b & 0x1f;
				skipBytes(strLen, buffer);
				break;
			}
			case INT8:
			case UINT8:
				skipBytes(1, buffer);
				break;
			case INT16:
			case UINT16:
				skipBytes(2, buffer);
				break;
			case INT32:
			case UINT32:
			case FLOAT32:
				skipBytes(4, buffer);
				break;
			case INT64:
			case UINT64:
			case FLOAT64:
				skipBytes(8, buffer);
				break;
			case BIN8:
			case STR8:
				skipBytes(readLength8(buffer), buffer);
				break;
			case BIN16:
			case STR16:
				skipBytes(readLength16(buffer), buffer);
				break;
			case BIN32:
			case STR32:
				skipBytes(readLength32(buffer), buffer);
				break;
			case FIXEXT1:
				skipBytes(2, buffer);
				break;
			case FIXEXT2:
				skipBytes(3, buffer);
				break;
			case FIXEXT4:
				skipBytes(5, buffer);
				break;
			case FIXEXT8:
				skipBytes(9, buffer);
				break;
			case FIXEXT16:
				skipBytes(17, buffer);
				break;
			case EXT8:
				skipBytes(readLength8(buffer) + 1, buffer);
				break;
			case EXT16:
				skipBytes(readLength16(buffer) + 1, buffer);
				break;
			case EXT32:
				skipBytes(readLength32(buffer) + 1, buffer);
				break;
			case ARRAY16:
				count += readLength16(buffer);
				break;
			case ARRAY32:
				count += readLength32(buffer);
				break;
			case MAP16:
				count += readLength16(buffer) * 2;
				break;
			case MAP32:
				count += readLength32(buffer) * 2;
				break;
			case NEVER_USED:
				throw new MessageParseException("never used message format");
			}

			count--;
		}
	}

	public <T> T readNull(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (b == Code.NIL) {
			return null;
		}
		throw new MessageParseException("unexpected null code " + b);
	}

	public boolean readBoolean(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (b == Code.NIL) {
			return false;
		} else if (b == Code.FALSE) {
			return false;
		} else if (b == Code.TRUE) {
			return true;
		}
		throw new MessageParseException("unexpected boolean code " + b);
	}

	public int readVInt(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (Code.isFixInt(b)) {
			return (int) b;
		}
		switch (b) {
		case Code.NIL:
			return 0;
		case Code.UINT8:
			return readUnsignedByte(buffer);
		case Code.UINT16:
			return readUnsignedShort(buffer);
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
			return readByte(buffer);
		case Code.INT16:
			return readShort(buffer);
		case Code.INT32:
			return readInt(buffer);
		case Code.INT64:
			long i64 = readLong(buffer);
			if (i64 < (long) Integer.MIN_VALUE || i64 > (long) Integer.MAX_VALUE) {
				throw new MessageNumberOverflowException(i64);
			}
			return (int) i64;
		}
		throw new MessageParseException("unexpected int code " + b);
	}

	public long readVLong(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (Code.isFixInt(b)) {
			return (long) b;
		}
		switch (b) {
		case Code.NIL:
			return 0L;
		case Code.UINT8:
			return readUnsignedByte(buffer);
		case Code.UINT16:
			return readUnsignedShort(buffer);
		case Code.UINT32:
			return readUnsignedInt(buffer);
		case Code.UINT64:
			return readUnsignedLong(buffer);
		case Code.INT8:
			return readByte(buffer);
		case Code.INT16:
			return readShort(buffer);
		case Code.INT32:
			return readInt(buffer);
		case Code.INT64:
			return readLong(buffer);
		}
		throw new MessageParseException("unexpected long code " + b);
	}

	public double readVDouble(ByteBuf buffer) {
		byte b = readByte(buffer);
		switch (b) {
		case Code.NIL:
			return 0.0;
		case Code.FLOAT32:
			return (double) readFloat(buffer);
		case Code.FLOAT64:
			return readDouble(buffer);
		}
		throw new MessageParseException("unexpected double code " + b);
	}

	public int readArrayHeader(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (Code.isFixedArray(b)) {
			return b & 0x0f;
		}
		switch (b) {
		case Code.ARRAY16:
			return readLength16(buffer);
		case Code.ARRAY32:
			return readLength32(buffer);
		}
		throw new MessageParseException("unexpected array code " + b);
	}

	public int readMapHeader(ByteBuf buffer) {
		byte b = readByte(buffer);
		if (Code.isFixedMap(b)) {
			return b & 0x0f;
		}
		switch (b) {
		case Code.MAP16:
			return readLength16(buffer);
		case Code.MAP32:
			return readLength32(buffer);
		}
		throw new MessageParseException("unexpected map code " + b);
	}

	private int readBinaryHeader(byte b, ByteBuf buffer) {
		if (Code.isFixedRaw(b)) { // FixRaw
			return b & 0x1f;
		}
		switch (b) {
		case Code.BIN8:
			return readLength8(buffer);
		case Code.BIN16:
			return readLength16(buffer);
		case Code.BIN32:
			return readLength32(buffer);
		default:
			throw new MessageParseException("unexpected binary header code " + b);
		}
	}

	public int readBinaryHeader(ByteBuf buffer) {
		return readBinaryHeader(readByte(buffer), buffer);
	}

	public ByteBuf readBytes(ByteBuf buffer, boolean copy) {
		return readBytes(readByte(buffer), buffer, copy);
	}

	public ByteBuf readBytes(byte b, ByteBuf buffer, boolean copy) {
		int length = readBinaryHeader(b, buffer);
		if (length > buffer.readableBytes()) {
			throw new MessageParseException(
					"insufficient buffer length: " + buffer.readableBytes() + ", required length: " + length);
		}
		if (copy) {
			ByteBuf dst = buffer.alloc().buffer(length);
			buffer.readBytes(dst, length);
			return dst;
		} else {
			ByteBuf slice = buffer.slice(buffer.readerIndex(), length);
			buffer.readerIndex(buffer.readerIndex() + length);
			return slice;
		}
	}

	private int readStringHeader(byte b, ByteBuf buffer) {
		if (Code.isFixedRaw(b)) { // FixRaw
			return b & 0x1f;
		}
		switch (b) {
		case Code.STR8: // str 8
			return readLength8(buffer);
		case Code.STR16: // str 16
			return readLength16(buffer);
		case Code.STR32: // str 32
			return readLength32(buffer);
		default:
			throw new MessageParseException("unexpected string header code " + b);
		}
	}

	public int readStringHeader(ByteBuf buffer) {
		return readStringHeader(readByte(buffer), buffer);
	}

	public String readString(ByteBuf buffer) {
		return readString(buffer, StandardCharsets.UTF_8);
	}

	public String readString(byte b, ByteBuf buffer) {
		return readString(b, buffer, StandardCharsets.UTF_8);
	}

	public String readString(ByteBuf buffer, Charset charset) {
		return readString(buffer, readStringHeader(buffer), charset);
	}

	public String readString(byte b, ByteBuf buffer, Charset charset) {
		return readString(buffer, readStringHeader(b, buffer), charset);
	}

	public String readAsciiString(ByteBuf source) {
		return readString(source, readBinaryHeader(source), StandardCharsets.US_ASCII);
	}

	public String readAsciiString(byte b, ByteBuf source) {
		return readString(source, readBinaryHeader(b, source), StandardCharsets.US_ASCII);
	}

	public String readString(ByteBuf buffer, int length, Charset charset) {
		if (length > buffer.readableBytes()) {
			throw new MessageParseException(
					"insufficient buffer length: " + buffer.readableBytes() + ", required length: " + length);
		}
		if (buffer.hasArray()) {
			int start = buffer.readerIndex();
			int baseOffset = buffer.arrayOffset() + start;
			String str = new String(buffer.array(), baseOffset, length, charset);
			buffer.readerIndex(start + length);
			return str;
		} else {
			byte[] bytes = new byte[length];
			buffer.readBytes(bytes);
			return new String(bytes, charset);
		}
	}

}
