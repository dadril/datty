package io.datty.msgpack.table.impl;

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

import io.datty.msgpack.table.PackableBoolean;
import io.datty.msgpack.table.PackableNumber;
import io.datty.msgpack.table.PackableString;
import io.datty.msgpack.table.PackableTable;
import io.datty.msgpack.table.PackableTableType;
import io.datty.msgpack.table.PackableValue;
import io.datty.msgpack.table.PackableValueExpression;
import io.datty.msgpack.table.PackableValueFactory;
import io.datty.msgpack.table.support.PackableException;
import io.datty.msgpack.table.support.PackableNumberFormatException;
import io.datty.msgpack.table.util.PackableStringifyUtil;
import io.datty.msgpack.table.util.PackableValueUtil;
import io.datty.msgpack.table.util.PackableStringifyUtil.NumberType;

/**
 * Implementation of the packable table
 * 
 * @author Alex Shvid
 *
 */

public class PackableTableImpl extends AbstractPackableValueImpl<PackableTable> implements PackableTable {

	/**
	 * Keys could be stringify integers or any strings
	 */

	private final Map<String, PackableValue<?>> table = new HashMap<String, PackableValue<?>>();

	private PackableTableType type = PackableTableType.INT_KEY;

	/**
	 * Simple comparator that uses to make sure that Map is sorted by keys
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public final class KeyComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {

			if (type == PackableTableType.INT_KEY) {

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
	public PackableTableType getType() {
		return type;
	}

	@Override
	public PackableValue<?> get(String key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key);
	}
	
	@Override
	public PackableTable getTable(String key) {
		return PackableValueUtil.toTable(get(key));
	}

	@Override
	public Boolean getBoolean(String key) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	@Override
	public PackableNumber getNumber(String key) {
		return PackableValueUtil.toNumber(get(key));
	}
	
	@Override
	public Long getLong(String key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(String key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	@Override
	public PackableString getString(String key) {
		return PackableValueUtil.toString(get(key));
	}

	@Override
	public String getStringUtf8(String key) {
		PackableString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(String key, boolean copy) {
		PackableString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}

	@Override
	public PackableValue<?> get(Integer key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key.toString());
	}
	
	@Override
	public PackableTable getTable(Integer key) {
		return PackableValueUtil.toTable(get(key));
	}
	
	@Override
	public Boolean getBoolean(Integer key) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	@Override
	public PackableNumber getNumber(Integer key) {
		return PackableValueUtil.toNumber(get(key));
	}
	
	@Override
	public Long getLong(Integer key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(Integer key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	@Override
	public PackableString getString(Integer key) {
		return PackableValueUtil.toString(get(key));
	}
	
	@Override
	public String getStringUtf8(Integer key) {
		PackableString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(Integer key, boolean copy) {
		PackableString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}
	
	@Override
	public PackableValue<?> get(PackableValueExpression ve) {
	
		if (ve == null) {
			throw new IllegalArgumentException("null ve");
		}
		
		if (ve.isEmpty()) {
			return this;
		}
		
		int lastIndex = ve.size() - 1;
		PackableTable currentTable = this;
		for (int i = 0; i != lastIndex; ++i) {
			
			String key = ve.get(i);
			PackableValue<?> existingValue = currentTable.get(key);
			
			if (existingValue == null || !(existingValue instanceof PackableTable)) {
				return null;
			}
			else {
				currentTable = (PackableTable) existingValue;
			}
			
		}
		
		String key = ve.get(lastIndex);
		return currentTable.get(key);
		
	}
	
	@Override
	public PackableTable getTable(PackableValueExpression ve) {
		return PackableValueUtil.toTable(get(ve));
	}

	@Override
	public Boolean getBoolean(PackableValueExpression ve) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(ve));
		return val != null ? val.asBoolean() : null;
	}
	
	@Override
	public PackableNumber getNumber(PackableValueExpression ve) {
		return PackableValueUtil.toNumber(get(ve));
	}
	
	@Override
	public Long getLong(PackableValueExpression ve) {
		PackableNumber number = getNumber(ve);
		return number != null ? number.asLong() : null;
	}
	
	@Override
	public Double getDouble(PackableValueExpression ve) {
		PackableNumber number = getNumber(ve);
		return number != null ? number.asDouble() : null;
	}
	
	@Override
	public PackableString getString(PackableValueExpression ve) {
		return PackableValueUtil.toString(get(ve));
	}
	
	@Override
	public String getStringUtf8(PackableValueExpression ve) {
		PackableString str = getString(ve);
		return str != null ? str.toUtf8() : null;
	}

	@Override
	public byte[] getBytes(PackableValueExpression ve, boolean copy) {
		PackableString str = getString(ve);
		return str != null ? str.getBytes(copy) : null;
	}
	
	@Override
	public PackableValue<?> put(String key, PackableValue<?> value) {

		if (type == PackableTableType.INT_KEY) {

			NumberType numberType = PackableStringifyUtil.detectNumber(key);
			if (numberType == NumberType.NAN) {
				type = PackableTableType.STRING_KEY;
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
	public PackableValue<?> put(String key, String stringfyValue) {
		return put(key, PackableValueFactory.newStringifyValue(stringfyValue));
	}
	
	@Override
	public PackableValue<?> putBoolean(String key, boolean value) {
		return put(key, new PackableBooleanImpl(value));
	}

	@Override
	public PackableValue<?> putLong(String key, long value) {
		return put(key, new PackableNumberImpl(value));
	}

	@Override
	public PackableValue<?> putDouble(String key, double value) {
		return put(key, new PackableNumberImpl(value));
	}

	@Override
	public PackableValue<?> putString(String key, String value) {
		return put(key, new PackableStringImpl(value));
	}

	@Override
	public PackableValue<?> putBytes(String key, byte[] value, boolean copy) {
		return put(key, new PackableStringImpl(value, copy));
	}

	@Override
	public PackableValue<?> put(Integer key, PackableValue<?> value) {
		
		if (value != null) {
			return table.put(key.toString(), value);
		}
		else {
			return table.remove(key.toString());
		}
		
	}
	
	@Override
	public PackableValue<?> put(Integer key, String stringfyValue) {
		return put(key, PackableValueFactory.newStringifyValue(stringfyValue));
	}

	@Override
	public PackableValue<?> putBoolean(Integer key, boolean value) {
		return put(key, new PackableBooleanImpl(value));
	}
	
	@Override
	public PackableValue<?> putLong(Integer key, long value) {
		return put(key, new PackableNumberImpl(value));
	}

	@Override
	public PackableValue<?> putDouble(Integer key, double value) {
		return put(key, new PackableNumberImpl(value));
	}
	
	@Override
	public PackableValue<?> putString(Integer key, String value) {
		return put(key, new PackableStringImpl(value));
	}

	@Override
	public PackableValue<?> putBytes(Integer key, byte[] value, boolean copy) {
		return put(key, new PackableStringImpl(value, copy));
	}
	
	@Override
	public PackableValue<?> put(PackableValueExpression ve, PackableValue<?> value) {

		if (ve == null) {
			throw new IllegalArgumentException("null ve");
		}
		
		if (ve.isEmpty()) {
			return value;
		}
		
		int lastIndex = ve.size() - 1;
		PackableTable currentTable = this;
		for (int i = 0; i != lastIndex; ++i) {
			
			String key = ve.get(i);
			PackableValue<?> existingValue = currentTable.get(key);
			
			if (existingValue == null || !(existingValue instanceof PackableTable)) {
			  PackableTable newTable = new PackableTableImpl();
			  currentTable.put(key, newTable);
			  currentTable = newTable;
			}
			else {
				currentTable = (PackableTable) existingValue;
			}
			
		}
		
		String key = ve.get(lastIndex);
		return currentTable.put(key, value);
	}
		
	@Override
	public PackableValue<?> put(PackableValueExpression ve, String stringfyValue) {
		return put(ve, PackableValueFactory.newStringifyValue(stringfyValue));
	}
	
	@Override
	public PackableValue<?> putBoolean(PackableValueExpression ve, boolean value) {
		return put(ve, new PackableBooleanImpl(value));
	}
	
	@Override
	public PackableValue<?> putLong(PackableValueExpression ve, long value) {
		return put(ve, new PackableNumberImpl(value));
	}

	@Override
	public PackableValue<?> putDouble(PackableValueExpression ve, double value) {
		return put(ve, new PackableNumberImpl(value));
	}
	
	@Override
	public PackableValue<?> putString(PackableValueExpression ve, String value) {
		return put(ve, new PackableStringImpl(value));
	}

	@Override
	public PackableValue<?> putBytes(PackableValueExpression ve, byte[] value, boolean copy) {
		return put(ve, new PackableStringImpl(value, copy));
	}
	
	@Override
	public PackableValue<?> remove(String key) {
		return put(key, (PackableValue<?>) null);
	}
	
	@Override
	public PackableValue<?> remove(Integer key) {
		return put(key, (PackableValue<?>) null);
	}
	
	@Override
	public PackableValue<?> remove(PackableValueExpression ve) {
		return put(ve, (PackableValue<?>) null);
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
		for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
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
		
		throw new PackableException("unexpected type: " + type);
	}

	private Value toIntValue() {
		
    int size = size();
    
    int capacity = size << 1;
    Value[] array = new Value[capacity];
    
    int index = 0;
    for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
      
    	int integerKey;
    	try {
    		integerKey = Integer.parseInt(entry.getKey());
    	}
    	catch(NumberFormatException e) {
    		throw new PackableNumberFormatException(entry.getKey(), e);
    	}
    	
    	PackableValue<?> val = entry.getValue();
      
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
    for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
      
    	PackableValue<?> val = entry.getValue();
      
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
    
    for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
    	
    	String key = entry.getKey();
    	int intKey;
    	try {
    		intKey = Integer.parseInt(key);
    	}
    	catch(NumberFormatException e) {
    		throw new IOException(key, e);
    	}
    	
    	PackableValue<?> value = entry.getValue();
    	
			packer.packInt(intKey);
    	value.writeTo(packer);
    }
    
  }
  
  private void writeStringMapTo(MessagePacker packer) throws IOException {
  	
    int size = size();
    
    packer.packMapHeader(size);
    
    for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
    	
    	String key = entry.getKey();
    	PackableValue<?> value = entry.getValue();
    	
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
		for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
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
