package io.datty.msgpack.lite;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import io.datty.msgpack.lite.impl.PackableBooleanImpl;
import io.datty.msgpack.lite.impl.PackableEventImpl;
import io.datty.msgpack.lite.impl.PackableNumberImpl;
import io.datty.msgpack.lite.impl.PackableStringImpl;
import io.datty.msgpack.lite.impl.PackableTableImpl;
import io.datty.msgpack.lite.support.PackableException;
import io.datty.msgpack.lite.support.PackableNumberFormatException;
import io.datty.msgpack.lite.support.PackableParseException;
import io.datty.msgpack.lite.util.PackableStringifyUtil;
import io.datty.msgpack.lite.util.PackableStringifyUtil.NumberType;

/**
 * PackableValueFactory
 * 
 * @author Alex Shvid
 *
 */

public final class PackableValueFactory {

	private PackableValueFactory() {
	}
	
	/**
	 * Creates a new empty message event
	 * 
	 * @return not null instance
	 */
	
	public static final PackableEvent newEvent() {
		return new PackableEventImpl();
	}
	
	/**
	 * Creates a message from serialized MsgPack blob
	 * 
	 * @param blob - input buffer
	 * 
	 * @return not null instance
	 */
	
	public static final PackableEvent parseEvent(byte[] blob) {
		return new PackableEventImpl(blob);
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
	
	public static final PackableEvent parseEvent(byte[] buffer, int offset, int length) {
		return new PackableEventImpl(buffer, offset, length);
	}
	
	/**
	 * Creates a message from serialized MsgPack blob
	 * 
	 * @param buffer - input buffer
	 * 
	 * @return not null instance
	 */
	
	public static final PackableEvent parseEvent(ByteBuffer buffer) {
		return new PackableEventImpl(buffer);
	}
	
	/**
	 * Parse value from buffer
	 * 
	 * @param buffer - not null byte array
	 * @return message value
	 * @throws IOException
	 */

	@SuppressWarnings("unchecked")
	public static <T extends PackableValue<?>> T newTypedValue(byte[] buffer) {
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

	public static PackableValue<?> newValue(byte[] buffer) {
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

	public static PackableValue<?> newValue(byte[] buffer, int offset, int length) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(buffer, offset, length);
		try {
			return newValue(unpacker);
		} catch (IOException e) {
			throw new PackableException("unexpected IOException", e);
		}
	}

	/**
	 * Parse value from ByteBuffer
	 * 
	 * @param buffer - not null byte buffer
	 * @return message value
	 * @throws IOException
	 */

	public static PackableValue<?> newValue(ByteBuffer buffer) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer");
		}
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(buffer);
		try {
			return newValue(unpacker);
		} catch (IOException e) {
			throw new PackableException("unexpected IOException", e);
		}
	}

	/**
	 * Parse value from message unpacker
	 * 
	 * @param unpacker - message unpacker
	 * @return Msg value or null
	 * @throws IOException
	 */

	public static PackableValue<?> newValue(MessageUnpacker unpacker) throws IOException {

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

  private static PackableValue<?> newArray(MessageUnpacker unpacker) throws IOException {

  	PackableTableImpl table = new PackableTableImpl();
  	
    int arraySize = unpacker.unpackArrayHeader();
    if (arraySize == 0) {
      return table;
    }

    for (int i = 0; i != arraySize; ++i) {
    	
      PackableValue<?> value = newValue(unpacker);

      if (value != null) {
      	table.put(i, value);
      }
      
    }

    return table;
  }

  private static PackableValue<?> newMap(MessageUnpacker unpacker) throws IOException {

  	PackableTableImpl table = new PackableTableImpl();
  	
    int mapSize = unpacker.unpackMapHeader();
    if (mapSize == 0) {
      return table;
    }

    for (int i = 0; i != mapSize; ++i) {
    	
    	PackableValue<?> key = newValue(unpacker);
      PackableValue<?> value = newValue(unpacker);

      if (key != null && value != null) {
      	
      	if (key instanceof PackableNumber && table.getType() == PackableTableType.INT_KEY) {
      		PackableNumber number = (PackableNumber) key;
      		table.put((int) number.asLong(), value);
      	}
      	else {
      		table.put(key.asString(), value);
      	}
      }
      
    }

    return table;
  }

	private static PackableValue<?> newSimpleValue(MessageFormat format, MessageUnpacker unpacker) throws IOException {

		switch (format) {

		case BOOLEAN:
			return new PackableBooleanImpl(unpacker.unpackBoolean());

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
			return new PackableNumberImpl(unpacker.unpackLong());

		case FLOAT32:
		case FLOAT64:
			return new PackableNumberImpl(unpacker.unpackDouble());

		case STR8:
		case STR16:
		case STR32:
		case FIXSTR:
			return new PackableStringImpl(unpacker.unpackString());

		case BIN8:
		case BIN16:
		case BIN32:
			return new PackableStringImpl(unpacker.readPayload(unpacker.unpackBinaryHeader()), false);

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

	public static PackableValue<?> newStringifyValue(String stringifyValue) {

		if (stringifyValue == null) {
			return null;
		}

		if (stringifyValue.equalsIgnoreCase("true")) {
			return new PackableBooleanImpl(true);
		}

		if (stringifyValue.equalsIgnoreCase("false")) {
			return new PackableBooleanImpl(false);
		}

		NumberType type = PackableStringifyUtil.detectNumber(stringifyValue);

		switch (type) {

		case LONG:
			try {
				return new PackableNumberImpl(Long.parseLong(stringifyValue));
			} catch (NumberFormatException e) {
				throw new PackableNumberFormatException(stringifyValue, e);
			}

		case DOUBLE:
			try {
				return new PackableNumberImpl(Double.parseDouble(stringifyValue));
			} catch (NumberFormatException e) {
				throw new PackableNumberFormatException(stringifyValue, e);
			}

		case NAN:
			return new PackableStringImpl(stringifyValue);

		default:
			throw new PackableParseException("invalid type: " + type + ", for stringfy value: " + stringifyValue);
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
