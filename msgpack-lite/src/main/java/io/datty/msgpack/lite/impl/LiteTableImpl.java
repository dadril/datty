package io.datty.msgpack.lite.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableLongValueImpl;
import org.msgpack.value.impl.ImmutableMapValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

import io.datty.msgpack.lite.LiteBoolean;
import io.datty.msgpack.lite.LiteNumber;
import io.datty.msgpack.lite.LiteString;
import io.datty.msgpack.lite.LiteTable;
import io.datty.msgpack.lite.LiteTableType;
import io.datty.msgpack.lite.LiteValue;
import io.datty.msgpack.lite.LiteValueExpression;
import io.datty.msgpack.lite.LiteValueFactory;
import io.datty.msgpack.lite.support.LiteException;
import io.datty.msgpack.lite.support.LiteNumberFormatException;
import io.datty.msgpack.lite.util.LiteStringifyUtil;
import io.datty.msgpack.lite.util.LiteValueUtil;
import io.datty.msgpack.lite.util.LiteStringifyUtil.NumberType;

/**
 * Implementation of the message table
 * 
 * @author Alex Shvid
 *
 */

public class LiteTableImpl extends AbstractLiteValueImpl<LiteTable> implements LiteTable {

	/**
	 * Keys could be stringify integers or any strings
	 */

	private final Map<String, LiteValue<?>> table = new HashMap<String, LiteValue<?>>();

	private LiteTableType type = LiteTableType.INT_KEY;

	/**
	 * Simple comparator that uses to make sure that Map is sorted by keys
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public final class KeyComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {

			if (type == LiteTableType.INT_KEY) {

				try {
					int i1 = Integer.parseInt(o1);
					int i2 = Integer.parseInt(o2);

					return Integer.compare(i1, i2);
					
				} catch (NumberFormatException e) {
					return o1.compareTo(o2);
				}
			}

			return o1.compareTo(o2);
		}

	}

	@Override
	public LiteTableType getType() {
		return type;
	}

	@Override
	public LiteValue<?> get(String key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key);
	}
	
	@Override
	public LiteTable getTable(String key) {
		return LiteValueUtil.toTable(get(key));
	}

	@Override
	public Boolean getBoolean(String key) {
		LiteBoolean val = LiteValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	@Override
	public LiteNumber getNumber(String key) {
		return LiteValueUtil.toNumber(get(key));
	}
	
	@Override
	public Long getLong(String key) {
		LiteNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(String key) {
		LiteNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	@Override
	public LiteString getString(String key) {
		return LiteValueUtil.toString(get(key));
	}

	@Override
	public String getStringUtf8(String key) {
		LiteString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(String key, boolean copy) {
		LiteString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}

	@Override
	public LiteValue<?> get(Integer key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key.toString());
	}
	
	@Override
	public LiteTable getTable(Integer key) {
		return LiteValueUtil.toTable(get(key));
	}
	
	@Override
	public Boolean getBoolean(Integer key) {
		LiteBoolean val = LiteValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	@Override
	public LiteNumber getNumber(Integer key) {
		return LiteValueUtil.toNumber(get(key));
	}
	
	@Override
	public Long getLong(Integer key) {
		LiteNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(Integer key) {
		LiteNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	@Override
	public LiteString getString(Integer key) {
		return LiteValueUtil.toString(get(key));
	}
	
	@Override
	public String getStringUtf8(Integer key) {
		LiteString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(Integer key, boolean copy) {
		LiteString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}
	
	@Override
	public LiteValue<?> get(LiteValueExpression ve) {
	
		if (ve == null) {
			throw new IllegalArgumentException("null ve");
		}
		
		if (ve.isEmpty()) {
			return this;
		}
		
		int lastIndex = ve.size() - 1;
		LiteTable currentTable = this;
		for (int i = 0; i != lastIndex; ++i) {
			
			String key = ve.get(i);
			LiteValue<?> existingValue = currentTable.get(key);
			
			if (existingValue == null || !(existingValue instanceof LiteTable)) {
				return null;
			}
			else {
				currentTable = (LiteTable) existingValue;
			}
			
		}
		
		String key = ve.get(lastIndex);
		return currentTable.get(key);
		
	}
	
	@Override
	public LiteTable getTable(LiteValueExpression ve) {
		return LiteValueUtil.toTable(get(ve));
	}

	@Override
	public Boolean getBoolean(LiteValueExpression ve) {
		LiteBoolean val = LiteValueUtil.toBoolean(get(ve));
		return val != null ? val.asBoolean() : null;
	}
	
	@Override
	public LiteNumber getNumber(LiteValueExpression ve) {
		return LiteValueUtil.toNumber(get(ve));
	}
	
	@Override
	public Long getLong(LiteValueExpression ve) {
		LiteNumber number = getNumber(ve);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(LiteValueExpression ve) {
		LiteNumber number = getNumber(ve);
		return number != null ? number.asDouble() : null;
	}
	
	@Override
	public LiteString getString(LiteValueExpression ve) {
		return LiteValueUtil.toString(get(ve));
	}
	
	@Override
	public String getStringUtf8(LiteValueExpression ve) {
		LiteString str = getString(ve);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(LiteValueExpression ve, boolean copy) {
		LiteString str = getString(ve);
		return str != null ? str.getBytes(copy) : null;
	}
	
	@Override
	public LiteValue<?> put(String key, LiteValue<?> value) {

		if (type == LiteTableType.INT_KEY) {

			NumberType numberType = LiteStringifyUtil.detectNumber(key);
			if (numberType == NumberType.NAN) {
				type = LiteTableType.STRING_KEY;
			}

		}

		if (value != null) {
			return table.put(key, value);
		}
		else {
			return table.remove(key);
		}
	}
	
	@Override
	public LiteValue<?> put(String key, String stringfyValue) {
		return put(key, LiteValueFactory.newStringifyValue(stringfyValue));
	}
	
	@Override
	public LiteValue<?> putBoolean(String key, boolean value) {
		return put(key, new LiteBooleanImpl(value));
	}

	@Override
	public LiteValue<?> putLong(String key, long value) {
		return put(key, new LiteNumberImpl(value));
	}

	@Override
	public LiteValue<?> putDouble(String key, double value) {
		return put(key, new LiteNumberImpl(value));
	}

	@Override
	public LiteValue<?> putString(String key, String value) {
		return put(key, new LiteStringImpl(value));
	}

	@Override
	public LiteValue<?> putBytes(String key, byte[] value, boolean copy) {
		return put(key, new LiteStringImpl(value, copy));
	}

	@Override
	public LiteValue<?> put(Integer key, LiteValue<?> value) {
		
		if (value != null) {
			return table.put(key.toString(), value);
		}
		else {
			return table.remove(key.toString());
		}
		
	}
	
	@Override
	public LiteValue<?> put(Integer key, String stringfyValue) {
		return put(key, LiteValueFactory.newStringifyValue(stringfyValue));
	}

	@Override
	public LiteValue<?> putBoolean(Integer key, boolean value) {
		return put(key, new LiteBooleanImpl(value));
	}
	
	@Override
	public LiteValue<?> putLong(Integer key, long value) {
		return put(key, new LiteNumberImpl(value));
	}

	@Override
	public LiteValue<?> putDouble(Integer key, double value) {
		return put(key, new LiteNumberImpl(value));
	}
	
	@Override
	public LiteValue<?> putString(Integer key, String value) {
		return put(key, new LiteStringImpl(value));
	}

	@Override
	public LiteValue<?> putBytes(Integer key, byte[] value, boolean copy) {
		return put(key, new LiteStringImpl(value, copy));
	}
	
	@Override
	public LiteValue<?> put(LiteValueExpression ve, LiteValue<?> value) {

		if (ve == null) {
			throw new IllegalArgumentException("null ve");
		}
		
		if (ve.isEmpty()) {
			return value;
		}
		
		int lastIndex = ve.size() - 1;
		LiteTable currentTable = this;
		for (int i = 0; i != lastIndex; ++i) {
			
			String key = ve.get(i);
			LiteValue<?> existingValue = currentTable.get(key);
			
			if (existingValue == null || !(existingValue instanceof LiteTable)) {
			  LiteTable newTable = new LiteTableImpl();
			  currentTable.put(key, newTable);
			  currentTable = newTable;
			}
			else {
				currentTable = (LiteTable) existingValue;
			}
			
		}
		
		String key = ve.get(lastIndex);
		return currentTable.put(key, value);
	}
		
	@Override
	public LiteValue<?> put(LiteValueExpression ve, String stringfyValue) {
		return put(ve, LiteValueFactory.newStringifyValue(stringfyValue));
	}
	
	@Override
	public LiteValue<?> putBoolean(LiteValueExpression ve, boolean value) {
		return put(ve, new LiteBooleanImpl(value));
	}
	
	@Override
	public LiteValue<?> putLong(LiteValueExpression ve, long value) {
		return put(ve, new LiteNumberImpl(value));
	}

	@Override
	public LiteValue<?> putDouble(LiteValueExpression ve, double value) {
		return put(ve, new LiteNumberImpl(value));
	}
	
	@Override
	public LiteValue<?> putString(LiteValueExpression ve, String value) {
		return put(ve, new LiteStringImpl(value));
	}

	@Override
	public LiteValue<?> putBytes(LiteValueExpression ve, byte[] value, boolean copy) {
		return put(ve, new LiteStringImpl(value, copy));
	}
	
	@Override
	public LiteValue<?> remove(String key) {
		return put(key, (LiteValue<?>) null);
	}
	
	@Override
	public LiteValue<?> remove(Integer key) {
		return put(key, (LiteValue<?>) null);
	}
	
	@Override
	public LiteValue<?> remove(LiteValueExpression ve) {
		return put(ve, (LiteValue<?>) null);
	}
	
	@Override
	public Set<String> keySet() {
		return table.keySet();
	}
	
	@Override
	public List<Integer> intKeys() {
		
		List<Integer> list = new ArrayList<Integer>(table.size());
		
		for (String key : table.keySet()) {

			try {
				list.add(Integer.parseInt(key));
			}
			catch(NumberFormatException e) {
				// ignore
			}
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	@Override
	public Integer minIntKey() {
		
		Integer minKey = null;
		
		for (String key : table.keySet()) {

			try {
				int value = Integer.parseInt(key);
				if (minKey == null || value < minKey) {
					minKey = value;
				}
			}
			catch(NumberFormatException e) {
				// ignore
			}
		}
		
		return minKey;
	}
	
	@Override
	public Integer maxIntKey() {
		
		Integer maxKey = null;
		
		for (String key : table.keySet()) {

			try {
				int value = Integer.parseInt(key);
				if (maxKey == null || value > maxKey) {
					maxKey = value;
				}
			}
			catch(NumberFormatException e) {
				// ignore
			}
		}
		
		return maxKey;
	}

	@Override
	public int size() {
		return table.size();
	}

	@Override
	public void clear() {
		table.clear();
	}

	@Override
	public String asString() {
		StringBuilder str = new StringBuilder();
		str.append("{");
		boolean first = true;
		for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
			if (!first) {
				str.append(", ");
			}
			str.append(entry.getKey()).append("=").append(entry.getValue().asString());
			first = false;
		}
		str.append("}");
		return str.toString();
	}

	@Override
	public Value toValue() {

		switch(type) {
		
		case INT_KEY:
			return toIntValue();
			
		case STRING_KEY:
			return toStringValue();
			
		}
		
		throw new LiteException("unexpected type: " + type);
	}

	private Value toIntValue() {
		
    int size = size();
    
    int capacity = size << 1;
    Value[] array = new Value[capacity];
    
    int index = 0;
    for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
      
    	int integerKey;
    	try {
    		integerKey = Integer.parseInt(entry.getKey());
    	}
    	catch(NumberFormatException e) {
    		throw new LiteNumberFormatException(entry.getKey(), e);
    	}
    	
    	LiteValue<?> val = entry.getValue();
      
      array[index++] = new ImmutableLongValueImpl(integerKey);
      array[index++] = val.toValue();
      
    }
    
    return new ImmutableMapValueImpl(array);
		
	}

	private Value toStringValue() {
		
    int size = size();

    int capacity = size << 1;
    Value[] array = new Value[capacity];
    
    int index = 0;
    for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
      
    	LiteValue<?> val = entry.getValue();
      
      array[index++] = new ImmutableStringValueImpl(entry.getKey());
      array[index++] = val.toValue();
      
    }
    
    return new ImmutableMapValueImpl(array);
    
	}
	
  @Override
	public void writeTo(MessagePacker packer) throws IOException {
		switch(type) {
		
		case INT_KEY:
			writeIntMapTo(packer);
			break;
			
		case STRING_KEY:
			writeStringMapTo(packer);
			break;
			
		default:
			throw new IOException("unexpected type: " + type);
		}
		
	}	
  
  private void writeIntMapTo(MessagePacker packer) throws IOException {
  	
    int size = size();
    
    packer.packMapHeader(size);
    
    for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
    	
    	String key = entry.getKey();
    	int intKey;
    	try {
    		intKey = Integer.parseInt(key);
    	}
    	catch(NumberFormatException e) {
    		throw new IOException(key, e);
    	}
    	
    	LiteValue<?> value = entry.getValue();
    	
			packer.packInt(intKey);
    	value.writeTo(packer);
    }
    
  }
  
  private void writeStringMapTo(MessagePacker packer) throws IOException {
  	
    int size = size();
    
    packer.packMapHeader(size);
    
    for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
    	
    	String key = entry.getKey();
    	LiteValue<?> value = entry.getValue();
    	
			byte[] data = key.getBytes(StandardCharsets.UTF_8);
			packer.packRawStringHeader(data.length);
			packer.writePayload(data);
			
    	value.writeTo(packer);
    }
    
  }  


	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("LiteTable [type=" + type + ", size=" + table.size() + "] {\n");
		boolean first = true;
		for (Map.Entry<String, LiteValue<?>> entry : table.entrySet()) {
			if (!first) {
				str.append(",\n");
			}
			addSpaces(str, initialSpaces + tabSpaces);
			str.append(entry.getKey()).append("=");
			entry.getValue().print(str, initialSpaces + tabSpaces, tabSpaces);
			first = false;
		}
		str.append("\n");
		addSpaces(str, initialSpaces);
		str.append("}");
	}

	private void addSpaces(StringBuilder str, int spaces) {
		for (int i = 0; i != spaces; ++i) {
			str.append(' ');
		}
	}
	

}
