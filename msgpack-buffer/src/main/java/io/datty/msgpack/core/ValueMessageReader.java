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
package io.datty.msgpack.core;

import org.msgpack.core.MessageFormat;

import io.datty.msgpack.MessageReader;
import io.datty.msgpack.core.reader.ValueReader;
import io.datty.msgpack.core.reader.ValueReaders;
import io.datty.msgpack.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ValueMessageReader
 * 
 * @author Alex Shvid
 *
 */

public class ValueMessageReader<K> extends AbstractMessageReader implements MessageReader<K> {

	public static final ValueMessageReader<?> INSTANCE = new ValueMessageReader<Object>();
	
	@Override
	public int readHeader(ByteBuf source) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	@Override
	public K readKey(ByteBuf source) {
		throw new UnsupportedOperationException("this method must be overriden");
	}

	/**
	 * Return types:
	 * 
	 * null
	 * Boolean
	 * Long
	 * Double
	 * String
	 * ByteBuf
	 * MessageReader
	 * 
	 */
	
	@Override
	public Object readValue(ByteBuf buffer, boolean copy) {
		
		if (!hasNext(buffer)) {
			return null;
		}
		
		MessageFormat f = getNextFormat(buffer);
		switch(f) {
		
	    case NIL:
	      return readNull(buffer);
	      
	    case POSFIXINT:
	    case NEGFIXINT: 
	    case UINT8:
	    case UINT16:
	    case UINT32:
	    case UINT64:
	    case INT8:
	    case INT16:
	    case INT32:
	    case INT64:
	    	return Long.valueOf(readVLong(buffer));
	    	
      case FLOAT32 : // float
      case FLOAT64 : // double
        return Double.valueOf(readVDouble(buffer));
      
      case FIXSTR: // fixstr
      case STR8 : // str 8
      case STR16 : // str 16
      case STR32 : // str 32
      	return readString(buffer);
        
      case BIN8:
      case BIN16:
      case BIN32:
      	return readBinary(buffer, copy);
      	
      case FIXARRAY:
      case ARRAY16:
      case ARRAY32:
      	return readArray(buffer, copy);      	
      	
      case FIXMAP:
      case MAP16:
      case MAP32:
      	return readMap(buffer, copy);

      default:
      	skipValue(buffer);
      	return null;
		}
		
	}
	
	@Override
	public ByteBuf skipValue(ByteBuf source, boolean copy) {
		
		if (!hasNext(source)) {
			return null;
		}

		int startIndex = source.readerIndex();
		skipValue(source);
		int endIndex = source.readerIndex();
		int length = endIndex - startIndex;
		
		if (copy) {
			ByteBuf dst = source.alloc().buffer(length);
			source.getBytes(startIndex, dst, length);
			return dst;
		}
		else {
			return source.slice(startIndex, length);
		}
		
	}

	private ByteBuf readBinary(ByteBuf buffer, boolean copy) {
		int length = readBinaryHeader(buffer);
		if (length > buffer.readableBytes()) {
      throw new MessageParseException("insufficient buffer length: " + buffer.readableBytes() + ", required length: " + length);
		}
		if (copy) {
			ByteBuf dst = buffer.alloc().buffer(length);
			buffer.readBytes(dst, length);
			return dst;
		}
		else {
			ByteBuf slice = buffer.slice(buffer.readerIndex(), length);
			buffer.skipBytes(length);
			return slice;
		}
	}
	
	private MessageReader<?> readArray(ByteBuf buffer, boolean copy) {
		int length = readArrayHeader(buffer);
		return new ArrayMessageReader(length);
	}
	
	private MessageReader<?> readMap(ByteBuf buffer, boolean copy) {
		int size = readMapHeader(buffer);
		if (hasNext(buffer)) {
			MessageFormat f = getNextFormat(buffer);
			if (isInteger(f)) {
				return new IntMapMessageReader(size);
			}
		}
		return new StringMapMessageReader(size);
	}
	
	private boolean isInteger(MessageFormat f) {
		switch(f) {
    case POSFIXINT:
    case NEGFIXINT: 
    case UINT8:
    case UINT16:
    case UINT32:
    case UINT64:
    case INT8:
    case INT16:
    case INT32:
    case INT64:
    	return true;
    default:
    	return false;
		}
	}

	/**
	 * This method automatically converts value to the expecting type
	 */
	
	@Override
	public <T> T readValue(Class<T> type, ByteBuf source, boolean copy) {
		
		ValueReader<T> reader = ValueReaders.find(type);
		
		if (reader == null) {
			skipValue(source);
			return null;
		}
		
		return reader.read(source, copy);
	}
	
	
}
