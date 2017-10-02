package io.datty.msgpack.lite.impl;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableDoubleValueImpl;
import org.msgpack.value.impl.ImmutableLongValueImpl;

import io.datty.msgpack.lite.PackableNumber;
import io.datty.msgpack.lite.PackableNumberType;
import io.datty.msgpack.lite.support.PackableException;
import io.datty.msgpack.lite.support.PackableNumberFormatException;
import io.datty.msgpack.lite.util.PackableStringifyUtil;
import io.datty.msgpack.lite.util.PackableStringifyUtil.NumberType;

/**
 * 
 * Packable number immutable implementation
 * 
 * @author Alex Shvid
 *
 */

public final class PackableNumberImpl extends AbstractPackableValueImpl<PackableNumber> implements PackableNumber {

	private final long longValue;
	private final double doubleValue;
	private final PackableNumberType type;

	public PackableNumberImpl(long value) {
		this.type = PackableNumberType.LONG;
		this.longValue = value;
		this.doubleValue = 0.0;
	}
	
	public PackableNumberImpl(double value) {
		this.type = PackableNumberType.DOUBLE;
		this.longValue = 0L;
		this.doubleValue = value;
	}

	public PackableNumberImpl(String stringValue) {
		
		if (stringValue == null) {
			throw new IllegalArgumentException("empty value");
		}
		
		NumberType numberType = PackableStringifyUtil.detectNumber(stringValue);
		
		switch(numberType) {
		case LONG:
			try {
				this.type = PackableNumberType.LONG;
				this.longValue = Long.parseLong(stringValue);
				this.doubleValue = 0.0;
			}
			catch(NumberFormatException e) {
				throw new PackableNumberFormatException(stringValue, e);
			}				
			break;
		case DOUBLE:
			try {
				this.type = PackableNumberType.DOUBLE;
				this.longValue = 0L;
				this.doubleValue = Double.parseDouble(stringValue);
			}
			catch(NumberFormatException e) {
				throw new PackableNumberFormatException(stringValue, e);
			}			
			break;
		case NAN:
		default:
			throw new PackableNumberFormatException(stringValue);
		}
		
	}
	
	@Override
	public PackableNumberType getType() {
		return type;
	}

	@Override
	public PackableNumber add(PackableNumber otherNumber) {
		switch(otherNumber.getType()) {
		case LONG:
			return add(otherNumber.asLong());
		case DOUBLE:
			return add(otherNumber.asDouble());
		}
		throw new PackableException("unexpected type: " + otherNumber.getType());
	}
	
	@Override
	public PackableNumber add(long otherLongValue) {
		switch(type) {
		case LONG:
			return new PackableNumberImpl(longValue + otherLongValue);
		case DOUBLE:
			return new PackableNumberImpl(doubleValue + (double) otherLongValue);
		}
		throw new PackableException("unexpected type: " + type);
	}
	
	@Override
	public PackableNumber add(double otherDoubleValue) {
		switch(type) {
		case LONG:
			return new PackableNumberImpl((double) longValue + otherDoubleValue);
		case DOUBLE:
			return new PackableNumberImpl(doubleValue + otherDoubleValue);
		}
		throw new PackableException("unexpected type: " + type);
	}
	
	@Override
	public PackableNumber subtract(PackableNumber otherNumber) {
		switch(otherNumber.getType()) {
		case LONG:
			return subtract(otherNumber.asLong());
		case DOUBLE:
			return subtract(otherNumber.asDouble());
		}
		throw new PackableException("unexpected type: " + otherNumber.getType());
	}	

	@Override
	public PackableNumber subtract(long otherLongValue) {
		switch(type) {
		case LONG:
			return new PackableNumberImpl(longValue - otherLongValue);
		case DOUBLE:
			return new PackableNumberImpl(doubleValue - (double) otherLongValue);
		}
		throw new PackableException("unexpected type: " + type);
	}
	
	@Override
	public PackableNumber subtract(double otherDoubleValue) {
		switch(type) {
		case LONG:
			return new PackableNumberImpl((double) longValue - otherDoubleValue);
		case DOUBLE:
			return new PackableNumberImpl(doubleValue - otherDoubleValue);
		}
		throw new PackableException("unexpected type: " + type);
	}

	@Override
	public long asLong() {
		switch(type) {
		case LONG:
			return longValue;
		case DOUBLE:
			return (long) doubleValue;
		}
		throw new PackableException("unexpected type: " + type);
	}

	@Override
	public double asDouble() {
		switch(type) {
		case LONG:
			return longValue;
		case DOUBLE:
			return doubleValue;
		}
		throw new PackableException("unexpected type: " + type);
	}

	@Override
	public String asString() {
		switch(type) {
		case LONG:
			return Long.toString(longValue);
		case DOUBLE:
			return Double.toString(doubleValue);
		}
		throw new PackableException("unexpected type: " + type);
	}
	
  @Override
  public Value toValue() {
		switch(type) {
		case LONG:
			return new ImmutableLongValueImpl(longValue);  
		case DOUBLE:
	    return new ImmutableDoubleValueImpl(doubleValue);  
		}	
		throw new PackableException("unexpected type: " + type);
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
		if (type == PackableNumberType.DOUBLE) {
			long temp;
			temp = Double.doubleToLongBits(doubleValue);
			result = prime * result + (int) (temp ^ (temp >>> 32));
		}
		else if (type == PackableNumberType.LONG) {
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
		PackableNumberImpl other = (PackableNumberImpl) obj;
		if (type != other.type)
			return false;
		if (type == PackableNumberType.DOUBLE && Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(other.doubleValue))
			return false;
		if (type == PackableNumberType.LONG && longValue != other.longValue)
			return false;
		return true;
	}

	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("LiteNumber [type=").append(type);
		if (type == PackableNumberType.LONG) {
			str.append(", longValue=").append(longValue);
		}
		else if (type == PackableNumberType.DOUBLE) {
			str.append(", doubleValue=").append(doubleValue);
		}
		str.append("]");
	}
	
}
