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

import java.nio.charset.StandardCharsets;

import org.msgpack.core.MessagePack.Code;

import io.netty.buffer.ByteBuf;

/**
 * MessageWriter
 * 
 * @author Alex Shvid
 *
 */

public enum MessageWriter {

  WRITER;

  public ByteBuf writeByte(byte value, ByteBuf buffer) {
    return buffer.writeByte(value);
  }

  public ByteBuf writeByteAndByte(byte b, byte v, ByteBuf buffer) {
    return buffer.writeByte(b).writeByte(v);
  }

  public ByteBuf writeByteAndShort(byte b, short v, ByteBuf buffer) {
    return buffer.writeByte(b).writeShort(v);
  }

  public ByteBuf writeByteAndInt(byte b, int v, ByteBuf buffer) {
    return buffer.writeByte(b).writeInt(v);
  }

  public ByteBuf writeByteAndLong(byte b, long v, ByteBuf buffer) {
    return buffer.writeByte(b).writeLong(v);
  }

  public ByteBuf writeVInt(final int v, ByteBuf buffer) {
    if (v < -(1 << 5)) {
      if (v < -(1 << 15)) {
        return writeByteAndInt(Code.INT32, v, buffer);
      } else if (v < -(1 << 7)) {
        return writeByteAndShort(Code.INT16, (short) v, buffer);
      } else {
        return writeByteAndByte(Code.INT8, (byte) v, buffer);
      }
    } else if (v < (1 << 7)) {
      return writeByte((byte) v, buffer);
    } else {
      if (v < (1 << 8)) {
        return writeByteAndByte(Code.UINT8, (byte) v, buffer);
      } else if (v < (1 << 16)) {
        return writeByteAndShort(Code.UINT16, (short) v, buffer);
      } else {
        // unsigned 32
        return writeByteAndInt(Code.UINT32, v, buffer);
      }
    }
  }

  public ByteBuf writeVLong(long v, ByteBuf buffer) {
    if (v < -(1L << 5)) {
      if (v < -(1L << 15)) {
        if (v < -(1L << 31)) {
          return writeByteAndLong(Code.INT64, v, buffer);
        } else {
          return writeByteAndInt(Code.INT32, (int) v, buffer);
        }
      } else {
        if (v < -(1 << 7)) {
          return writeByteAndShort(Code.INT16, (short) v, buffer);
        } else {
          return writeByteAndByte(Code.INT8, (byte) v, buffer);
        }
      }
    } else if (v < (1 << 7)) {
      // fixnum
      return writeByte((byte) v, buffer);
    } else {
      if (v < (1L << 16)) {
        if (v < (1 << 8)) {
          return writeByteAndByte(Code.UINT8, (byte) v, buffer);
        } else {
          return writeByteAndShort(Code.UINT16, (short) v, buffer);
        }
      } else {
        if (v < (1L << 32)) {
          return writeByteAndInt(Code.UINT32, (int) v, buffer);
        } else {
          return writeByteAndLong(Code.UINT64, v, buffer);
        }
      }
    }
  }

  public ByteBuf writeFloat(float v, ByteBuf buffer) {
    return writeByteAndInt(Code.FLOAT32, Float.floatToRawIntBits(v), buffer);
  }

  public ByteBuf writeDouble(double v, ByteBuf buffer) {
    return writeByteAndLong(Code.FLOAT64, Double.doubleToRawLongBits(v), buffer);
  }

  public ByteBuf writeBoolean(boolean b, ByteBuf buffer) {
    return writeByte(b ? Code.TRUE : Code.FALSE, buffer);
  }

  public ByteBuf writeNull(ByteBuf buffer) {
    return writeByte(Code.NIL, buffer);
  }

  public ByteBuf writeArrayHeader(int arraySize, ByteBuf buffer) {

    if (arraySize < 0) {
      throw new IllegalArgumentException("array size must be >= 0");
    }

    if (arraySize < (1 << 4)) {
      return writeByte((byte) (Code.FIXARRAY_PREFIX | arraySize), buffer);
    } else if (arraySize < (1 << 16)) {
      return writeByteAndShort(Code.ARRAY16, (short) arraySize, buffer);
    } else {
      return writeByteAndInt(Code.ARRAY32, arraySize, buffer);
    }

  }

  public ByteBuf writeMapHeader(int mapSize, ByteBuf buffer) {

    if (mapSize < 0) {
      throw new IllegalArgumentException("map size must be >= 0");
    }

    if (mapSize < (1 << 4)) {
      return writeByte((byte) (Code.FIXMAP_PREFIX | mapSize), buffer);
    } else if (mapSize < (1 << 16)) {
      return writeByteAndShort(Code.MAP16, (short) mapSize, buffer);
    } else {
      return writeByteAndInt(Code.MAP32, mapSize, buffer);
    }
  }

  public ByteBuf writeBinaryHeader(int len, ByteBuf buffer) {
    if (len < (1 << 8)) {
      return writeByteAndByte(Code.BIN8, (byte) len, buffer);
    } else if (len < (1 << 16)) {
      return writeByteAndShort(Code.BIN16, (short) len, buffer);
    } else {
      return writeByteAndInt(Code.BIN32, len, buffer);
    }
  }

  public ByteBuf writeRawStringHeader(int len, ByteBuf buffer) {
    if (len < (1 << 5)) {
      return writeByte((byte) (Code.FIXSTR_PREFIX | len), buffer);
    } else if (len < (1 << 8)) {
      return writeByteAndByte(Code.STR8, (byte) len, buffer);
    } else if (len < (1 << 16)) {
      return writeByteAndShort(Code.STR16, (short) len, buffer);
    } else {
      return writeByteAndInt(Code.STR32, len, buffer);
    }
  }

  public ByteBuf writeBytes(ByteBuf input, ByteBuf buffer) {
    return buffer.writeBytes(input);
  }

  public ByteBuf writeBytes(byte[] input, ByteBuf buffer) {
    return buffer.writeBytes(input);
  }
  
  public ByteBuf writeString(String str, ByteBuf buffer) {
    byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    return writeRawStringHeader(bytes.length, buffer).writeBytes(bytes);
  }

}
