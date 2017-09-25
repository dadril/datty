package io.datty.msgpack.lite;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import io.datty.msgpack.lite.impl.LiteBooleanImpl;
import io.datty.msgpack.lite.impl.LiteEventImpl;
import io.datty.msgpack.lite.impl.LiteNumberImpl;
import io.datty.msgpack.lite.impl.LiteStringImpl;
import io.datty.msgpack.lite.impl.LiteTableImpl;
import io.datty.msgpack.lite.support.LiteException;
import io.datty.msgpack.lite.support.LiteNumberFormatException;
import io.datty.msgpack.lite.support.LiteParseException;
import io.datty.msgpack.lite.util.LiteStringifyUtil;
import io.datty.msgpack.lite.util.LiteStringifyUtil.NumberType;

/**
 * LiteValueFactory
 * 
 * @author Alex Shvid
 *
 */

public final class LiteValueFactory {

	private LiteValueFactory() {
	}
	
	/**
	 * Creates a new empty message event
	 * 
	 * @return not null instance
	 */
	
	public static final LiteEvent newEvent() {
		return new LiteEventImpl();
	}
	
	/**
	 * Creates a message from serialized MsgPack blob
	 * 
	 * @param blob - input buffer
	 * 
	 * @return not null instance
	 */
	
	public static final LiteEvent parseEvent(byte[] blob) {
		return new LiteEventImpl(blob);
	}
	
	/**
	 * 
	 * Creates a message from serialized MsgPack blob
	 * 
	 * @param buffer - input buffer
	 * @param offset - position in the buffer
	 * @param length - length of the byte array
	 * @return not null instance
	 */
	
	public static final LiteEvent parseEvent(byte[] buffer, int offset, int length) {
		return new LiteEventImpl(buffer, offset, length);
	}
	
	/**
	 * Creates a message from serialized MsgPack blob
	 * 
	 * @param buffer - input buffer
	 * 
	 * @return not null instance
	 */
	
	public static final LiteEvent parseEvent(ByteBuffer buffer) {
		return new LiteEventImpl(buffer);
	}
	
	/**
	 * Parse value from buffer
	 * 
	 * @param buffer - not null byte array
	 * @return message value
	 * @throws IOException
	 */

	@SuppressWarnings("unchecked")
	public static <T extends LiteValue<?>> T newTypedValue(byte[] buffer) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		return (T) newValue(buffer, 0, buffer.length);
	}

	/**
	 * Parse value from buffer
	 * 
	 * @param buffer - not null byte array
	 * @return message value
	 * @throws IOException
	 */

	public static LiteValue<?> newValue(byte[] buffer) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		return newValue(buffer, 0, buffer.length);
	}

	/**
	 * Parse value from buffer
	 * 
	 * @param buffer - not null byte array
	 * @param offset - offset in the array
	 * @param length - length of the payload
	 * @return message value
	 * @throws IOException
	 */

	public static LiteValue<?> newValue(byte[] buffer, int offset, int length) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(buffer, offset, length);
		try {
			return newValue(unpacker);
		} catch (IOException e) {
			throw new LiteException("unexpected IOException", e);
		}
	}

	/**
	 * Parse value from ByteBuffer
	 * 
	 * @param buffer - not null byte buffer
	 * @return message value
	 * @throws IOException
	 */

	public static LiteValue<?> newValue(ByteBuffer buffer) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(buffer);
		try {
			return newValue(unpacker);
		} catch (IOException e) {
			throw new LiteException("unexpected IOException", e);
		}
	}

	/**
	 * Parse value from message unpacker
	 * 
	 * @param unpacker - message unpacker
	 * @return Msg value or null
	 * @throws IOException
	 */

	public static LiteValue<?> newValue(MessageUnpacker unpacker) throws IOException {

		if (unpacker == null) {
			throw new IllegalArgumentException("null unpacker");
		}
		
		if(!unpacker.hasNext()) { 
			return null;
		}
		
		MessageFormat format = unpacker.getNextFormat();

		if (isNull(format)) {
			unpacker.unpackNil();
			return null;
		}

		else if (isArray(format)) {
			return newArray(unpacker);
		}

		else if (isMap(format)) {
			return newMap(unpacker);
		}

		else {
			return newSimpleValue(format, unpacker);
		}

	}

  private static LiteValue<?> newArray(MessageUnpacker unpacker) throws IOException {

  	LiteTableImpl table = new LiteTableImpl();
  	
    int arraySize = unpacker.unpackArrayHeader();
    if (arraySize == 0) {
      return table;
    }

    for (int i = 0; i != arraySize; ++i) {
    	
      LiteValue<?> value = newValue(unpacker);

      if (value != null) {
      	table.put(i, value);
      }
      
    }

    return table;
  }

  private static LiteValue<?> newMap(MessageUnpacker unpacker) throws IOException {

  	LiteTableImpl table = new LiteTableImpl();
  	
    int mapSize = unpacker.unpackMapHeader();
    if (mapSize == 0) {
      return table;
    }

    for (int i = 0; i != mapSize; ++i) {
    	
    	LiteValue<?> key = newValue(unpacker);
      LiteValue<?> value = newValue(unpacker);

      if (key != null && value != null) {
      	
      	if (key instanceof LiteNumber && table.getType() == LiteTableType.INT_KEY) {
      		LiteNumber number = (LiteNumber) key;
      		table.put((int) number.asLong(), value);
      	}
      	else {
      		table.put(key.asString(), value);
      	}
      }
      
    }

    return table;
  }

	private static LiteValue<?> newSimpleValue(MessageFormat format, MessageUnpacker unpacker) throws IOException {

		switch (format) {

		case BOOLEAN:
			return new LiteBooleanImpl(unpacker.unpackBoolean());

		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case UINT8:
		case UINT16:
		case UINT32:
		case UINT64:
		case POSFIXINT:
		case NEGFIXINT:
			return new LiteNumberImpl(unpacker.unpackLong());

		case FLOAT32:
		case FLOAT64:
			return new LiteNumberImpl(unpacker.unpackDouble());

		case STR8:
		case STR16:
		case STR32:
		case FIXSTR:
			return new LiteStringImpl(unpacker.unpackString());

		case BIN8:
		case BIN16:
		case BIN32:
			return new LiteStringImpl(unpacker.readPayload(unpacker.unpackBinaryHeader()), false);

		default:
			unpacker.skipValue();
			return null;
		}

	}
	
	/**
	 * Parse stringify value primitive value
	 * 
	 * @param stringifyValue - value in string formats
	 * @return Msg value or null
	 */

	public static LiteValue<?> newStringifyValue(String stringifyValue) {

		if (stringifyValue == null) {
			return null;
		}

		if (stringifyValue.equalsIgnoreCase("true")) {
			return new LiteBooleanImpl(true);
		}

		if (stringifyValue.equalsIgnoreCase("false")) {
			return new LiteBooleanImpl(false);
		}

		NumberType type = LiteStringifyUtil.detectNumber(stringifyValue);

		switch (type) {

		case LONG:
			try {
				return new LiteNumberImpl(Long.parseLong(stringifyValue));
			} catch (NumberFormatException e) {
				throw new LiteNumberFormatException(stringifyValue, e);
			}

		case DOUBLE:
			try {
				return new LiteNumberImpl(Double.parseDouble(stringifyValue));
			} catch (NumberFormatException e) {
				throw new LiteNumberFormatException(stringifyValue, e);
			}

		case NAN:
			return new LiteStringImpl(stringifyValue);

		default:
			throw new LiteParseException("invalid type: " + type + ", for stringfy value: " + stringifyValue);
		}

	}

	public static boolean isArray(MessageFormat format) {

		switch (format) {

		case FIXARRAY:
		case ARRAY16:
		case ARRAY32:
			return true;

		default:
			return false;

		}

	}

	public static boolean isMap(MessageFormat format) {

		switch (format) {

		case FIXMAP:
		case MAP16:
		case MAP32:
			return true;

		default:
			return false;

		}

	}

	public static boolean isNull(MessageFormat format) {
		return MessageFormat.NIL == format;
	}

}
