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
package io.datty.msgpack.io;

import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.support.MessageParseException;
import io.datty.msgpack.support.MessageNumberOverflowException;
import io.netty.buffer.ByteBuf;

/**
 * MessageParser
 * 
 * @author Alex Shvid
 *
 */

public enum MessageParser {

  PARSER;

  public boolean isNextNull(ByteBuf buffer) {
    return getNextFormat(buffer) == MessageFormat.NIL;
  }

  public MessageFormat getNextFormat(ByteBuf buffer) {
    if (buffer.readableBytes() == 0) {
      throw new MessageParseException("insufficient buffer exception");
    }
    return MessageFormat.valueOf(buffer.getByte(buffer.readerIndex()));
  }

  public void skipBytes(int numberOfBytes, ByteBuf buffer) {
    buffer.skipBytes(numberOfBytes);
  }

  public byte readByte(ByteBuf buffer) {
    return buffer.readByte();
  }

  public short readShort(ByteBuf buffer) {
    return buffer.readShort();
  }

  public int readInt(ByteBuf buffer) {
    return buffer.readInt();
  }

  public long readLong(ByteBuf buffer) {
    return buffer.readLong();
  }

  private int readNextLength8(ByteBuf buffer) {
    byte u8 = readByte(buffer);
    return u8 & 0xff;
  }

  private int readNextLength16(ByteBuf buffer) {
    short u16 = readShort(buffer);
    return u16 & 0xffff;
  }

  private int readNextLength32(ByteBuf buffer) {
    return checkOverflow32(readInt(buffer));
  }

  private int checkOverflow32(int u32) {
    if (u32 < 0) {
      long l64 = (long) (u32 & 0x7fffffff) + 0x80000000L;
      throw new MessageNumberOverflowException(l64);
    }
    return u32;
  }

  private long checkOverflow64(long u64) {
    if (u64 < 0) {
      throw new MessageNumberOverflowException(u64);
    }
    return u64;
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
        case POSFIXINT :
        case NEGFIXINT :
        case BOOLEAN :
        case NIL :
          break;
        case FIXMAP : {
          int mapLen = b & 0x0f;
          count += mapLen * 2;
          break;
        }
        case FIXARRAY : {
          int arrayLen = b & 0x0f;
          count += arrayLen;
          break;
        }
        case FIXSTR : {
          int strLen = b & 0x1f;
          skipBytes(strLen, buffer);
          break;
        }
        case INT8 :
        case UINT8 :
          skipBytes(1, buffer);
          break;
        case INT16 :
        case UINT16 :
          skipBytes(2, buffer);
          break;
        case INT32 :
        case UINT32 :
        case FLOAT32 :
          skipBytes(4, buffer);
          break;
        case INT64 :
        case UINT64 :
        case FLOAT64 :
          skipBytes(8, buffer);
          break;
        case BIN8 :
        case STR8 :
          skipBytes(readNextLength8(buffer), buffer);
          break;
        case BIN16 :
        case STR16 :
          skipBytes(readNextLength16(buffer), buffer);
          break;
        case BIN32 :
        case STR32 :
          skipBytes(readNextLength32(buffer), buffer);
          break;
        case FIXEXT1 :
          skipBytes(2, buffer);
          break;
        case FIXEXT2 :
          skipBytes(3, buffer);
          break;
        case FIXEXT4 :
          skipBytes(5, buffer);
          break;
        case FIXEXT8 :
          skipBytes(9, buffer);
          break;
        case FIXEXT16 :
          skipBytes(17, buffer);
          break;
        case EXT8 :
          skipBytes(readNextLength8(buffer) + 1, buffer);
          break;
        case EXT16 :
          skipBytes(readNextLength16(buffer) + 1, buffer);
          break;
        case EXT32 :
          skipBytes(readNextLength32(buffer) + 1, buffer);
          break;
        case ARRAY16 :
          count += readNextLength16(buffer);
          break;
        case ARRAY32 :
          count += readNextLength32(buffer);
          break;
        case MAP16 :
          count += readNextLength16(buffer) * 2;
          break;
        case MAP32 :
          count += checkOverflow32(readNextLength32(buffer) * 2);
          break;
        case NEVER_USED :
          throw new MessageParseException("never used message format");
      }

      count--;
    }
  }

  public void readNull(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (b == Code.NIL) {
      return;
    }
    throw new MessageParseException("unexpected null code " + b);
  }

  public boolean readBoolean(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (b == Code.NIL) {
      return false;
    }
    else if (b == Code.FALSE) {
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
      case Code.UINT8 : // unsigned int 8
        byte u8 = readByte(buffer);
        return u8 & 0xff;
      case Code.UINT16 : // unsigned int 16
        short u16 = readShort(buffer);
        return u16 & 0xffff;
      case Code.UINT32 : // unsigned int 32
        int u32 = checkOverflow32(readInt(buffer));
        return u32;
      case Code.UINT64 : // unsigned int 64
        long u64 = checkOverflow64(readLong(buffer));
        if (u64 > (long) Integer.MAX_VALUE) {
          throw new MessageNumberOverflowException(u64);
        }
        return (int) u64;
      case Code.INT8 : // signed int 8
        byte i8 = readByte(buffer);
        return i8;
      case Code.INT16 : // signed int 16
        short i16 = readShort(buffer);
        return i16;
      case Code.INT32 : // signed int 32
        int i32 = readInt(buffer);
        return i32;
      case Code.INT64 : // signed int 64
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
      case Code.UINT8 : // unsigned int 8
        byte u8 = readByte(buffer);
        return (long) (u8 & 0xff);
      case Code.UINT16 : // unsigned int 16
        short u16 = readShort(buffer);
        return (long) (u16 & 0xffff);
      case Code.UINT32 : // unsigned int 32
        int u32 = readInt(buffer);
        if (u32 < 0) {
          return (long) (u32 & 0x7fffffff) + 0x80000000L;
        } else {
          return (long) u32;
        }
      case Code.UINT64 : // unsigned int 64
        long u64 = checkOverflow64(readLong(buffer));
        return u64;
      case Code.INT8 : // signed int 8
        byte i8 = readByte(buffer);
        return (long) i8;
      case Code.INT16 : // signed int 16
        short i16 = readShort(buffer);
        return (long) i16;
      case Code.INT32 : // signed int 32
        int i32 = readInt(buffer);
        return (long) i32;
      case Code.INT64 : // signed int 64
        long i64 = readLong(buffer);
        return i64;
    }
    throw new MessageParseException("unexpected long code " + b);
  }

  public double readVDouble(ByteBuf buffer) {
    byte b = readByte(buffer);
    switch (b) {
      case Code.NIL:
        return 0.0;
      case Code.FLOAT32 : // float
        float fv = readFloat(buffer);
        return (double) fv;
      case Code.FLOAT64 : // double
        double dv = readDouble(buffer);
        return dv;
    }
    throw new MessageParseException("unexpected double code " + b);
  }

  public int readArrayHeader(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (Code.isFixedArray(b)) { // fixarray
      return b & 0x0f;
    }
    switch (b) {
      case Code.ARRAY16 : { // array 16
        int len = readNextLength16(buffer);
        return len;
      }
      case Code.ARRAY32 : { // array 32
        int len = readNextLength32(buffer);
        return len;
      }
    }
    throw new MessageParseException("unexpected array code " + b);
  }

  public int readMapHeader(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (Code.isFixedMap(b)) { // fixmap
      return b & 0x0f;
    }
    switch (b) {
      case Code.MAP16 : { // map 16
        int len = readNextLength16(buffer);
        return len;
      }
      case Code.MAP32 : { // map 32
        int len = readNextLength32(buffer);
        return len;
      }
    }
    throw new MessageParseException("unexpected map code " + b);
  }

  private int readBinaryHeader(byte b, ByteBuf buffer) {
    switch (b) {
      case Code.BIN8 : // bin 8
        return readNextLength8(buffer);
      case Code.BIN16 : // bin 16
        return readNextLength16(buffer);
      case Code.BIN32 : // bin 32
        return readNextLength32(buffer);
      default :
        throw new MessageParseException("unexpected binary header code " + b);
    }
  }

  public int readBinaryHeader(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (Code.isFixedRaw(b)) { // FixRaw
      return b & 0x1f;
    }
    return readBinaryHeader(b, buffer);
  }

  private int readStringHeader(byte b, ByteBuf buffer) {
    switch (b) {
      case Code.STR8 : // str 8
        return readNextLength8(buffer);
      case Code.STR16 : // str 16
        return readNextLength16(buffer);
      case Code.STR32 : // str 32
        return readNextLength32(buffer);
      default :
        throw new MessageParseException("unexpected string header code " + b);
    }
  }

  public int readStringHeader(ByteBuf buffer) {
    byte b = readByte(buffer);
    if (Code.isFixedRaw(b)) { // FixRaw
      return b & 0x1f;
    }
    return readStringHeader(b, buffer);
  }

}
