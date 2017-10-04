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

import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack.Code;

import io.datty.msgpack.message.MessageReader;
import io.datty.msgpack.message.core.reader.ArrayReader;
import io.datty.msgpack.message.core.reader.ListReader;
import io.datty.msgpack.message.core.reader.MapReader;
import io.datty.msgpack.message.core.reader.ValueReader;
import io.datty.msgpack.message.core.type.ArrayTypeInfo;
import io.datty.msgpack.message.core.type.ListTypeInfo;
import io.datty.msgpack.message.core.type.MapTypeInfo;
import io.datty.msgpack.message.core.type.SimpleTypeInfo;
import io.datty.msgpack.message.core.type.TypeInfo;
import io.datty.msgpack.message.support.MessageParseException;
import io.netty.buffer.ByteBuf;

/**
 * ValueMessageReader
 * 
 * @author Alex Shvid
 *
 */

public class ValueMessageReader extends AbstractMessageReader implements MessageReader {

	public static final ValueMessageReader INSTANCE = new ValueMessageReader();
	
	public static final ValueReader<Object> OBJECT_KEY_READER = new ValueReader<Object>() {

		@Override
		public Object read(ByteBuf source, boolean copy) {
			return INSTANCE.readValue(source, true);
		}
		
	};
	
	public static final ValueReader<Object> OBJECT_VALUE_READER = new ValueReader<Object>() {

		@Override
		public Object read(ByteBuf source, boolean copy) {
			return INSTANCE.readValue(source, copy);
		}
		
	};
	
	@Override
	public int size() {
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
	 * 
	 */
	
	@Override
	public Object readKey(ByteBuf buffer) {
		
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
	    	return Integer.valueOf(readVInt(buffer));
	    	
      case FLOAT32 :
      case FLOAT64 :
        return Integer.valueOf((int) readVDouble(buffer));
      
      case FIXSTR: 
      case STR8 :
      case STR16 : 
      case STR32 : 
      	return readString(buffer);

      default:
  			throw new MessageParseException("reader not found for format: " + f.name());
		}
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
	      
	    case BOOLEAN:
	    	return Boolean.valueOf(readBoolean(buffer));
	      
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
	    	
      case FLOAT32 : 
      case FLOAT64 : 
        return Double.valueOf(readVDouble(buffer));
      
      case FIXSTR: 
      case STR8 : 
      case STR16 :
      case STR32 : 
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
  			throw new MessageParseException("reader not found for format: " + f.name());
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

	public ByteBuf readBinary(ByteBuf source, boolean copy) {
		int length = readBinaryHeader(source);
		if (length > source.readableBytes()) {
      throw new MessageParseException("insufficient buffer length: " + source.readableBytes() + ", required length: " + length);
		}
		if (copy) {
			ByteBuf dst = source.alloc().buffer(length);
			source.readBytes(dst, length);
			return dst;
		}
		else {
			ByteBuf slice = source.slice(source.readerIndex(), length);
			source.skipBytes(length);
			return slice;
		}
	}
	
	public MessageReader readArray(ByteBuf source, boolean copy) {
		int length = readArrayHeader(source);
		return new ArrayMessageReader(length);
	}
	
	public MessageReader readMap(ByteBuf source, boolean copy) {
		int size = readMapHeader(source);
		return new MapMessageReader(size);
	}
	
	public boolean isInteger(MessageFormat f) {
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

	public boolean isMap(ByteBuf source) {
		MessageFormat f = getNextFormat(source);
		return isMap(f);
	}
	
	public boolean isMap(MessageFormat f) {
		switch(f) {
		
    case FIXMAP:
    case MAP16:
    case MAP32:
    	return true;

    default:
    	return false;
		}
	}
	
	public boolean isNull(ByteBuf source) {
		
		if (!hasNext(source)) {
			return true;
		}
		
		byte b = getNextCode(source);
		
		return b == Code.NIL;
	}
	
	public boolean isBinary(ByteBuf source) {
		
		if (!hasNext(source)) {
			return false;
		}

		byte b = getNextCode(source);
		
		if (Code.isFixedRaw(b)) { // FixRaw
			return true;
		}
		
		switch (b) {
		
		case Code.BIN8:
		case Code.BIN16:
		case Code.BIN32:
			return true;		
		
		}

		return false;
	}
		
	/**
	 * This method automatically converts value to the expecting type
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T readValue(TypeInfo<T> type, ByteBuf source, boolean copy) {

		if (type instanceof SimpleTypeInfo) {
			
			SimpleTypeInfo<T> simpleType = (SimpleTypeInfo<T>) type;
			
			return simpleType.getValueReader().read(source, copy);
		}
		
	  else if (type instanceof ArrayTypeInfo) {
			
			ArrayTypeInfo<Object, T> arrayType = (ArrayTypeInfo<Object, T>) type;
			
			return (T) ArrayReader.INSTANCE.read(arrayType.getComponentType(), arrayType.getComponentValueReader(), source, copy);
		}
		
		else if (type instanceof ListTypeInfo) {
			
			ListTypeInfo<Object, T> listType = (ListTypeInfo<Object, T>) type;
			
			return (T) ListReader.INSTANCE.read(listType.getComponentValueReader(), source, copy);
		}
		
		else if (type instanceof MapTypeInfo) {
			
			MapTypeInfo<Object, Object, T> mapType = (MapTypeInfo<Object, Object, T>) type;
			
			return (T) MapReader.INSTANCE.read(mapType.getKeyValueReader(), mapType.getComponentValueReader(), source, copy);
		}
		
		else {
			throw new MessageParseException("unknown type info: " + type);
		}
		
	}
	
	
	
}
