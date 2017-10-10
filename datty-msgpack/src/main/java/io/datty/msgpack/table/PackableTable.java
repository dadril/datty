/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table;

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

import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.writer.StringWriter;
import io.datty.msgpack.table.support.PackableException;
import io.datty.msgpack.table.support.PackableNumberFormatException;
import io.datty.msgpack.table.util.PackableStringifyUtil;
import io.datty.msgpack.table.util.PackableStringifyUtil.NumberType;
import io.datty.msgpack.table.util.PackableValueUtil;
import io.netty.buffer.ByteBuf;

/**
 * Mutable table
 * 
 * @author Alex Shvid
 *
 */

public final class PackableTable extends PackableValue<PackableTable> {

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

	/**
	 * Gets Msg table type
	 * 
	 * @return not null type of the table
	 */
	
	public PackableTableType getType() {
		return type;
	}

	/**
	 * Gets value by key
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message value or null
	 */
	
	public PackableValue<?> get(String key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key);
	}
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message table or null
	 */
	
	public PackableTable getTable(String key) {
		return PackableValueUtil.toTable(get(key));
	}

	/**
	 * Gets boolean value by key
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return boolean or null
	 */
	
	public Boolean getBoolean(String key) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	/**
	 * Gets number value by key
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message number or null
	 */
	
	public PackableNumber getNumber(String key) {
		return PackableValueUtil.toNumber(get(key));
	}
	
	/**
	 * Gets long number value by key
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return long number or null
	 */
	
	public Long getLong(String key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	/**
	 * Gets double number value by key
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return double number or null
	 */
	
	public Double getDouble(String key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return Msg string or null
	 */
	
	public PackableString getString(String key) {
		return PackableValueUtil.toString(get(key));
	}

	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return string or null
	 */
	
	public String getStringUtf8(String key) {
		PackableString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	/**
	 * Gets bytes value by key
	 * 
	 * Converts any other simple value to bytes string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	public byte[] getBytes(String key, boolean copy) {
		PackableString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}

	/**
	 * Gets value by key
	 * 
	 * @param key - integer key
	 * @return message value or null
	 */
	
	public PackableValue<?> get(Integer key) {
		
		if (key == null) {
			throw new IllegalArgumentException("empty key");
		}
		
		return table.get(key.toString());
	}
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param key - integer key
	 * @return message table or null
	 */
	
	public PackableTable getTable(Integer key) {
		return PackableValueUtil.toTable(get(key));
	}
	
	/**
	 * Gets boolean value by key
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param key - integer key
	 * @return message boolean or null
	 */
	
	public Boolean getBoolean(Integer key) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(key));
		return val != null ? val.asBoolean() : null;
	}

	/**
	 * Gets number value by key
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param key - integer key
	 * @return message number or null
	 */
	
	public PackableNumber getNumber(Integer key) {
		return PackableValueUtil.toNumber(get(key));
	}
	
	/**
	 * Gets long number value by key
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param key - integer key
	 * @return long number or null
	 */
	
	public Long getLong(Integer key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asLong() : null;
	}
	
	/**
	 * Gets double number value by key
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param key - integer key
	 * @return double number or null
	 */
	
	public Double getDouble(Integer key) {
		PackableNumber number = getNumber(key);
		return number != null ? number.asDouble() : null;
	}

	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - integer key
	 * @return message string or null
	 */
	
	public PackableString getString(Integer key) {
		return PackableValueUtil.toString(get(key));
	}
	
	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - integer key
	 * @return string or null
	 */
	
	public String getStringUtf8(Integer key) {
		PackableString str = getString(key);
		return str != null ? str.toUtf8() : null;
	}

	/**
	 * Gets bytes string value by key
	 * 
	 * Converts any other simple value to bytes string
	 * 
	 * @param key - integer key
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	public byte[] getBytes(Integer key, boolean copy) {
		PackableString str = getString(key);
		return str != null ? str.getBytes(copy) : null;
	}
	
	/**
	 * Gets value
	 * 
	 * @param ve - value expression
	 * @return message value or null
	 */
	
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
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param ve - value expression
	 * @return message table or null
	 */
	
	public PackableTable getTable(PackableValueExpression ve) {
		return PackableValueUtil.toTable(get(ve));
	}

	/**
	 * Gets boolean value
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param ve - value expression
	 * @return message boolean or null
	 */
	
	public Boolean getBoolean(PackableValueExpression ve) {
		PackableBoolean val = PackableValueUtil.toBoolean(get(ve));
		return val != null ? val.asBoolean() : null;
	}
	
	/**
	 * Gets number value
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param ve - value expression
	 * @return message number or null
	 */
	
	public PackableNumber getNumber(PackableValueExpression ve) {
		return PackableValueUtil.toNumber(get(ve));
	}
	
	/**
	 * Gets long number value
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param ve - value expression
	 * @return long or null
	 */
	
	public Long getLong(PackableValueExpression ve) {
		PackableNumber number = getNumber(ve);
		return number != null ? number.asLong() : null;
	}
	
	/**
	 * Gets double number value
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param ve - value expression
	 * @return double or null
	 */
	
	public Double getDouble(PackableValueExpression ve) {
		PackableNumber number = getNumber(ve);
		return number != null ? number.asDouble() : null;
	}
	
	/**
	 * Gets string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @return message string or null
	 */
	
	public PackableString getString(PackableValueExpression ve) {
		return PackableValueUtil.toString(get(ve));
	}
	
	/**
	 * Gets string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @return string or null
	 */
	
	public String getStringUtf8(PackableValueExpression ve) {
		PackableString str = getString(ve);
		return str != null ? str.toUtf8() : null;
	}

	/**
	 * Gets bytes string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	public byte[] getBytes(PackableValueExpression ve, boolean copy) {
		PackableString str = getString(ve);
		return str != null ? str.getBytes(copy) : null;
	}
	
	/**
	 * Puts value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new value
	 * @return old value or null
	 */
	
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
	
	/**
	 * Puts stringify value to the table with auto-detection type
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param stringifyValue - new value in string format
	 * @return old value or null
	 */
	
	public PackableValue<?> put(String key, String stringfyValue) {
		return put(key, PackableValueFactory.newStringifyValue(stringfyValue));
	}
	
	/**
	 * Puts boolean value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	public PackableValue<?> putBoolean(String key, boolean value) {
		return put(key, new PackableBoolean(value));
	}

	/**
	 * Puts long value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new long value
	 * @return old value or null
	 */
	
	public PackableValue<?> putLong(String key, long value) {
		return put(key, new PackableNumber(value));
	}

	/**
	 * Puts double value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new double value
	 * @return old value or null
	 */
	
	public PackableValue<?> putDouble(String key, double value) {
		return put(key, new PackableNumber(value));
	}

	/**
	 * Puts string value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new string value
	 * @return old value or null
	 */
	
	public PackableValue<?> putString(String key, String value) {
		return put(key, new PackableString(value));
	}

	/**
	 * Puts bytes value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new bytes value
	 * @param copy - copy bytes if needed
	 * @return old value or null
	 */
	
	public PackableValue<?> putBytes(String key, byte[] value, boolean copy) {
		return put(key, new PackableString(value, copy));
	}

	/**
	 * Puts value to the table
	 * 
	 * @param key - int keys
	 * @param value - new value
	 * @return old value or null
	 */
	
	public PackableValue<?> put(Integer key, PackableValue<?> value) {
		
		if (value != null) {
			return table.put(key.toString(), value);
		}
		else {
			return table.remove(key.toString());
		}
		
	}
	
	/**
	 * Puts stringify value to the table with auto-detection type
	 * 
	 * @param key - int keys
	 * @param stringifyValue - new value in string formats
	 * @return old value or null
	 */
	
	public PackableValue<?> put(Integer key, String stringfyValue) {
		return put(key, PackableValueFactory.newStringifyValue(stringfyValue));
	}

	/**
	 * Puts boolean value to the table
	 * 
	 * @param key - int keys
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	public PackableValue<?> putBoolean(Integer key, boolean value) {
		return put(key, new PackableBoolean(value));
	}
	
	/**
	 * Puts long value to the table
	 * 
	 * @param key - int keys
	 * @param value - new long value
	 * @return old value or null
	 */
	
	public PackableValue<?> putLong(Integer key, long value) {
		return put(key, new PackableNumber(value));
	}

	/**
	 * Puts double value to the table
	 * 
	 * @param key - int keys
	 * @param value - new double value
	 * @return old value or null
	 */
	
	public PackableValue<?> putDouble(Integer key, double value) {
		return put(key, new PackableNumber(value));
	}
	
	/**
	 * Puts string value to the table
	 * 
	 * @param key - int keys
	 * @param value - new string value
	 * @return old value or null
	 */
	
	public PackableValue<?> putString(Integer key, String value) {
		return put(key, new PackableString(value));
	}

	/**
	 * Puts bytes value to the table
	 * 
	 * @param key - int keys
	 * @param value - new bytes value
	 * @param copy - copy if needed
	 * @return old value or null
	 */
	
	public PackableValue<?> putBytes(Integer key, byte[] value, boolean copy) {
		return put(key, new PackableString(value, copy));
	}
	
	/**
	 * Puts value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new value
	 * @return old value or null
	 */
	
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
			  PackableTable newTable = new PackableTable();
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
		
	/**
	 * Puts stringfy value to the table with auto-detection type
	 * 
	 * @param ve - value expression
	 * @param stringfyValue - new value in string format
	 * @return old value or null
	 */
	
	public PackableValue<?> put(PackableValueExpression ve, String stringfyValue) {
		return put(ve, PackableValueFactory.newStringifyValue(stringfyValue));
	}
	
	/**
	 * Puts boolean value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	public PackableValue<?> putBoolean(PackableValueExpression ve, boolean value) {
		return put(ve, new PackableBoolean(value));
	}
	
	/**
	 * Puts long value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new long value
	 * @return old value or null
	 */
	
	public PackableValue<?> putLong(PackableValueExpression ve, long value) {
		return put(ve, new PackableNumber(value));
	}

	/**
	 * Puts double value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new double value
	 * @return old value or null
	 */
	
	public PackableValue<?> putDouble(PackableValueExpression ve, double value) {
		return put(ve, new PackableNumber(value));
	}
	
	/**
	 * Puts string value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new string value
	 * @return old value or null
	 */
	
	public PackableValue<?> putString(PackableValueExpression ve, String value) {
		return put(ve, new PackableString(value));
	}

	/**
	 * Puts bytes value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new bytes value
	 * @param copy - copy if needed
	 * @return old value or null
	 */
	
	public PackableValue<?> putBytes(PackableValueExpression ve, byte[] value, boolean copy) {
		return put(ve, new PackableString(value, copy));
	}
	
	/**
	 * Removes value from the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return old value or null
	 */
	
	public PackableValue<?> remove(String key) {
		return put(key, (PackableValue<?>) null);
	}
	
	/**
	 * Removes value from the table
	 * 
	 * @param key - int keys
	 * @return old value or null
	 */
	
	public PackableValue<?> remove(Integer key) {
		return put(key, (PackableValue<?>) null);
	}
	
	/**
	 * Removes value from the table
	 * 
	 * @param ve - value expression
	 * @return old value or null
	 */
	
	public PackableValue<?> remove(PackableValueExpression ve) {
		return put(ve, (PackableValue<?>) null);
	}
	
	/**
	 * Gets all keys in the table
	 * 
	 * @return set of keys
	 */
	
	public Set<String> keySet() {
		return table.keySet();
	}
	
	/**
	 * Gets all keys sorted by ascending order
	 * 
	 * @return not null sorted integer keys
	 */
	
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

	/**
	 * Gets minimum integer key in table
	 * 
	 * @return min int key or null of not found
	 */
	
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
	
	/**
	 * Gets maximum integer key in table
	 * 
	 * @return max int key or null of not found
	 */
	
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

	/**
	 * Gets size of the table
	 * 
	 * @return size of the table
	 */
	
	public int size() {
		return table.size();
	}

	/**
	 * Clear table
	 */
	
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
	public ByteBuf pack(ByteBuf buffer) throws IOException {
		
		switch(type) {
		
		case INT_KEY:
			return packIntMap(buffer);
			
		case STRING_KEY:
			return packStringMap(buffer);
			
		default:
			throw new IOException("unexpected type: " + type);
		}
	}

	private ByteBuf packIntMap(ByteBuf buffer) throws IOException {
		
    int size = size();
    
		MapMessageWriter writer = MapMessageWriter.INSTANCE;
		
		writer.writeHeader(size, buffer);
    
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
    	
    	buffer = writer.writeVInt(intKey, buffer);
    	buffer = value.pack(buffer);
    }
    
    return buffer;
	}
	
	private ByteBuf packStringMap(ByteBuf buffer) throws IOException {
		
    int size = size();
    
		MapMessageWriter writer = MapMessageWriter.INSTANCE;
    
		writer.writeHeader(size, buffer);
    
    for (Map.Entry<String, PackableValue<?>> entry : table.entrySet()) {
    	
    	String key = entry.getKey();
    	PackableValue<?> value = entry.getValue();
    	
    	buffer = StringWriter.INSTANCE.writeString(key, buffer);
    	buffer = value.pack(buffer);
    }
    
    return buffer;
	}
	
	@Override
	public void print(StringBuilder str, int initialSpaces, int tabSpaces) {
		str.append("PackableTable [type=" + type + ", size=" + table.size() + "] {\n");
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

