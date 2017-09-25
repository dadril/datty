package io.datty.msgpack.lite.impl;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableDoubleValueImpl;
import org.msgpack.value.impl.ImmutableLongValueImpl;

import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteNumberType;
import io.datty.msgpack.lite.support.LiteException;
import io.datty.msgpack.lite.support.LiteNumberFormatException;
import io.datty.msgpack.lite.util.LiteStringifyUtil;
import io.datty.msgpack.lite.util.LiteStringifyUtil.NumberType;

/**
 * 
 * Lite number immutable implementation
 * 
 * @author Alex Shvid
 *
 */

public final class LiteNumberImpl extends AbstractLiteValueImpl<LiteNumber> implements LiteNumber {

	private final long longValue;
	private final double doubleValue;
	private final LiteNumberType type;

	public LiteNumberImpl(long value) {
		this.type = LiteNumberType.LONG;
		this.longValue = value;
		this.doubleValue = 0.0;
	}
	
	public LiteNumberImpl(double value) {
		this.type = LiteNumberType.DOUBLE;
		this.longValue = 0L;
		this.doubleValue = value;
	}

	public LiteNumberImpl(String stringValue) {
		
		if (stringValue == null) {
			throw new IllegalArgumentException("empty value");
		}
		
		NumberType numberType = LiteStringifyUtil.detectNumber(stringValue);
		
		switch(numberType) {
		case LONG:
			try {
				this.type = LiteNumberType.LONG;
				this.longValue = Long.parseLong(stringValue);
				this.doubleValue = 0.0;
			}
			catch(NumberFormatException e) {
				throw new LiteNumberFormatException(stringValue, e);
			}				
			break;
		case DOUBLE:
			try {
				this.type = LiteNumberType.DOUBLE;
				this.longValue = 0L;
				this.doubleValue = Double.parseDouble(stringValue);
			}
			catch(NumberFormatException e) {
				throw new LiteNumberFormatException(stringValue, e);
			}			
			break;
		case NAN:
		default:
			throw new LiteNumberFormatException(stringValue);
		}
		
	}
	
	@Override
	public LiteNumberType getType() {
		return type;
	}

	@Override
	public LiteNumber add(LiteNumber otherNumber) {
		switch(otherNumber.getType()) {
		case LONG:
			return add(otherNumber.asLong());
		case DOUBLE:
			return add(otherNumber.asDouble());
		}
		throw new LiteException("unexpected type: " + otherNumber.getType());
	}
	
	@Override
	public LiteNumber add(long otherLongValue) {
		switch(type) {
		case LONG:
			return new LiteNumberImpl(longValue + otherLongValue);
		case DOUBLE:
			return new LiteNumberImpl(doubleValue + (double) otherLongValue);
		}
		throw new LiteException("unexpected type: " + type);
	}
	
	@Override
	public LiteNumber add(double otherDoubleValue) {
		switch(type) {
		case LONG:
			return new LiteNumberImpl((double) longValue + otherDoubleValue);
		case DOUBLE:
			return new LiteNumberImpl(doubleValue + otherDoubleValue);
		}
		throw new LiteException("unexpected type: " + type);
	}
	
	@Override
	public LiteNumber subtract(LiteNumber otherNumber) {
		switch(otherNumber.getType()) {
		case LONG:
			return subtract(otherNumber.asLong());
		case DOUBLE:
			return subtract(otherNumber.asDouble());
		}
		throw new LiteException("unexpected type: " + otherNumber.getType());
	}	

	@Override
	public LiteNumber subtract(long otherLongValue) {
		switch(type) {
		case LONG:
			return new LiteNumberImpl(longValue - otherLongValue);
		case DOUBLE:
			return new LiteNumberImpl(doubleValue - (double) otherLongValue);
		}
		throw new LiteException("unexpected type: " + type);
	}
	
	@Override
	public LiteNumber subtract(double otherDoubleValue) {
		switch(type) {
		case LONG:
			return new LiteNumberImpl((double) longValue - otherDoubleValue);
		case DOUBLE:
			return new LiteNumberImpl(doubleValue - otherDoubleValue);
		}
		throw new LiteException("unexpected type: " + type);
	}

	@Override
	public long asLong() {
		switch(type) {
		case LONG:
			return longValue;
		case DOUBLE:
			return (long) doubleValue;
		}
		throw new LiteException("unexpected type: " + type);
	}

	@Override
	public double asDouble() {
		switch(type) {
		case LONG:
			return longValue;
		case DOUBLE:
			return doubleValue;
		}
		throw new LiteException("unexpected type: " + type);
	}

	@Override
	public String asString() {
		switch(type) {
		case LONG:
			return Long.toString(longValue);
		case DOUBLE:
			return Double.toString(doubleValue);
		}
		throw new LiteException("unexpected type: " + type);
	}
	
  @Override
  public Value toValue() {
		switch(type) {
		case LONG:
			return new ImmutableLongValueImpl(longValue);  
		case DOUBLE:
	    return new ImmutableDoubleValueImpl(doubleValue);  
		}	
		throw new LiteException("unexpected type: " + type);
  }
  
  @Override
	public void writeTo(MessagePacker packer) throws IOException {
		switch(type) {
		case LONG:
			packer.packLong(longValue);
			break;
		case DOUBLE:
			packer.packDouble(doubleValue);
			break;
		default:
		  throw new IOException("unexpected type: " + type);		
		}	
	}
  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (type == LiteNumberType.DOUBLE) {
			long temp;
			temp = Double.doubleToLongBits(doubleValue);
			result = prime * result + (int) (temp ^ (temp >>> 32));
		}
		else if (type == LiteNumberType.LONG) {
			result = prime * result + (int) (longValue ^ (longValue >>> 32));
		}
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiteNumberImpl other = (LiteNumberImpl) obj;
		if (type != other.type)
			return false;
		if (type == LiteNumberType.DOUBLE && Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(other.doubleValue))
			return false;
		if (type == LiteNumberType.LONG && longValue != other.longValue)
			return false;
		return true;
	}

	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("LiteNumber [type=").append(type);
		if (type == LiteNumberType.LONG) {
			str.append(", longValue=").append(longValue);
		}
		else if (type == LiteNumberType.DOUBLE) {
			str.append(", doubleValue=").append(doubleValue);
		}
		str.append("]");
	}
	
}
