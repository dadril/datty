package io.datty.msgpack.lite;

import java.util.List;
import java.util.Set;

/**
 * Mutable table interface
 * 
 * @author Alex Shvid
 *
 */

public interface PackableTable extends PackableValue<PackableTable> {

	/**
	 * Gets Msg table type
	 * 
	 * @return not null type of the table
	 */
	
	PackableTableType getType();
		
	/**
	 * Gets value by key
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message value or null
	 */
	
	PackableValue<?> get(String key);
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message table or null
	 */
	
	PackableTable getTable(String key);
	
	/**
	 * Gets boolean value by key
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return boolean or null
	 */
	
	Boolean getBoolean(String key);
	
	/**
	 * Gets number value by key
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return message number or null
	 */
	
	PackableNumber getNumber(String key);
	
	/**
	 * Gets long number value by key
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return long number or null
	 */
	
	Long getLong(String key);
	
	/**
	 * Gets double number value by key
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return double number or null
	 */
	
	Double getDouble(String key);
	
	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return Msg string or null
	 */
	
	PackableString getString(String key);
	
	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return string or null
	 */
	
	String getStringUtf8(String key);
	
	/**
	 * Gets bytes value by key
	 * 
	 * Converts any other simple value to bytes string
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	byte[] getBytes(String key, boolean copy);
	
	/**
	 * Gets value by key
	 * 
	 * @param key - integer key
	 * @return message value or null
	 */
	
	PackableValue<?> get(Integer key);
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param key - integer key
	 * @return message table or null
	 */
	
	PackableTable getTable(Integer key);
	
	/**
	 * Gets boolean value by key
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param key - integer key
	 * @return message boolean or null
	 */
	
	Boolean getBoolean(Integer key);
	
	/**
	 * Gets number value by key
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param key - integer key
	 * @return message number or null
	 */
	
	PackableNumber getNumber(Integer key);
	
	/**
	 * Gets long number value by key
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param key - integer key
	 * @return long number or null
	 */
	
	Long getLong(Integer key);
	
	/**
	 * Gets double number value by key
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param key - integer key
	 * @return double number or null
	 */
	
	Double getDouble(Integer key);
	
	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - integer key
	 * @return message string or null
	 */
	
	PackableString getString(Integer key);
	
	/**
	 * Gets string value by key
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param key - integer key
	 * @return string or null
	 */
	
	String getStringUtf8(Integer key);
	
	/**
	 * Gets bytes string value by key
	 * 
	 * Converts any other simple value to bytes string
	 * 
	 * @param key - integer key
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	byte[] getBytes(Integer key, boolean copy);
	
	/**
	 * Gets value
	 * 
	 * @param ve - value expression
	 * @return message value or null
	 */
	
	PackableValue<?> get(PackableValueExpression ve);
	
	/**
	 * Gets table by key if possible
	 * 
	 * Does not do type conversion, if Msg value is not a table it will return null
	 * 
	 * @param ve - value expression
	 * @return message table or null
	 */
	
	PackableTable getTable(PackableValueExpression ve);
	
	/**
	 * Gets boolean value
	 * 
	 * Converts any other simple value to boolean
	 * 
	 * @param ve - value expression
	 * @return message boolean or null
	 */
	
	Boolean getBoolean(PackableValueExpression ve);
	
	/**
	 * Gets number value
	 * 
	 * Converts any other simple value to number
	 * 
	 * @param ve - value expression
	 * @return message number or null
	 */
	
	PackableNumber getNumber(PackableValueExpression ve);
	
	/**
	 * Gets long number value
	 * 
	 * Converts any other simple value to long number
	 * 
	 * @param ve - value expression
	 * @return long or null
	 */
	
	Long getLong(PackableValueExpression ve);
	
	/**
	 * Gets double number value
	 * 
	 * Converts any other simple value to double number
	 * 
	 * @param ve - value expression
	 * @return double or null
	 */
	
	Double getDouble(PackableValueExpression ve);
	
	/**
	 * Gets string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @return message string or null
	 */
	
	PackableString getString(PackableValueExpression ve);
	
	/**
	 * Gets string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @return string or null
	 */
	
	String getStringUtf8(PackableValueExpression ve);
	
	/**
	 * Gets bytes string value
	 * 
	 * Converts any other simple value to string
	 * 
	 * @param ve - value expression
	 * @param copy - copy bytes or not
	 * @return bytes string or null
	 */
	
	byte[] getBytes(PackableValueExpression ve, boolean copy);
	
	/**
	 * Puts value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new value
	 * @return old value or null
	 */
	
	PackableValue<?> put(String key, PackableValue<?> value);
	
	/**
	 * Puts stringify value to the table with auto-detection type
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param stringifyValue - new value in string format
	 * @return old value or null
	 */
	
	PackableValue<?> put(String key, String stringifyValue);
	
	/**
	 * Puts boolean value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	PackableValue<?> putBoolean(String key, boolean value);
	
	/**
	 * Puts long value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new long value
	 * @return old value or null
	 */
	
	PackableValue<?> putLong(String key, long value);
	
	/**
	 * Puts double value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new double value
	 * @return old value or null
	 */
	
	PackableValue<?> putDouble(String key, double value);
	
	/**
	 * Puts string value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new string value
	 * @return old value or null
	 */
	
	PackableValue<?> putString(String key, String value);
	
	/**
	 * Puts bytes value to the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @param value - new bytes value
	 * @param copy - copy bytes if needed
	 * @return old value or null
	 */
	
	PackableValue<?> putBytes(String key, byte[] value, boolean copy);
	
	/**
	 * Puts value to the table
	 * 
	 * @param key - int keys
	 * @param value - new value
	 * @return old value or null
	 */
	
	PackableValue<?> put(Integer key, PackableValue<?> value);
	
	/**
	 * Puts stringify value to the table with auto-detection type
	 * 
	 * @param key - int keys
	 * @param stringifyValue - new value in string formats
	 * @return old value or null
	 */
	
	PackableValue<?> put(Integer key, String stringifyValue);
	
	/**
	 * Puts boolean value to the table
	 * 
	 * @param key - int keys
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	PackableValue<?> putBoolean(Integer key, boolean value);
	
	/**
	 * Puts long value to the table
	 * 
	 * @param key - int keys
	 * @param value - new long value
	 * @return old value or null
	 */
	
	PackableValue<?> putLong(Integer key, long value);
	
	/**
	 * Puts double value to the table
	 * 
	 * @param key - int keys
	 * @param value - new double value
	 * @return old value or null
	 */
	
	PackableValue<?> putDouble(Integer key, double value);
	
	/**
	 * Puts string value to the table
	 * 
	 * @param key - int keys
	 * @param value - new string value
	 * @return old value or null
	 */
	
	PackableValue<?> putString(Integer key, String value);
	
	/**
	 * Puts bytes value to the table
	 * 
	 * @param key - int keys
	 * @param value - new bytes value
	 * @param copy - copy if needed
	 * @return old value or null
	 */
	
	PackableValue<?> putBytes(Integer key, byte[] value, boolean copy);
	
	/**
	 * Puts value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new value
	 * @return old value or null
	 */
	
	PackableValue<?> put(PackableValueExpression ve, PackableValue<?> value);
	
	/**
	 * Puts stringfy value to the table with auto-detection type
	 * 
	 * @param ve - value expression
	 * @param stringfyValue - new value in string format
	 * @return old value or null
	 */
	
	PackableValue<?> put(PackableValueExpression ve, String stringfyValue);	
	
	/**
	 * Puts boolean value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new boolean value
	 * @return old value or null
	 */
	
	PackableValue<?> putBoolean(PackableValueExpression ve, boolean value);
	
	/**
	 * Puts long value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new long value
	 * @return old value or null
	 */
	
	PackableValue<?> putLong(PackableValueExpression ve, long value);
	
	/**
	 * Puts double value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new double value
	 * @return old value or null
	 */
	
	PackableValue<?> putDouble(PackableValueExpression ve, double value);
	
	/**
	 * Puts string value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new string value
	 * @return old value or null
	 */
	
	PackableValue<?> putString(PackableValueExpression ve, String value);
	
	/**
	 * Puts bytes value to the table
	 * 
	 * @param ve - value expression
	 * @param value - new bytes value
	 * @param copy - copy if needed
	 * @return old value or null
	 */
	
	PackableValue<?> putBytes(PackableValueExpression ve, byte[] value, boolean copy);
	
	/**
	 * Removes value from the table
	 * 
	 * @param key - could be String or stringfy Integer
	 * @return old value or null
	 */
	
	PackableValue<?> remove(String key);
	
	/**
	 * Removes value from the table
	 * 
	 * @param key - int keys
	 * @return old value or null
	 */
	
	PackableValue<?> remove(Integer key);
	
	/**
	 * Removes value from the table
	 * 
	 * @param ve - value expression
	 * @return old value or null
	 */
	
	PackableValue<?> remove(PackableValueExpression ve);
	
	/**
	 * Gets all keys in the table
	 * 
	 * @return set of keys
	 */
	
	Set<String> keySet();
	
	/**
	 * Gets all keys sorted by ascending order
	 * 
	 * @return not null sorted integer keys
	 */
	
	List<Integer> intKeys();

	/**
	 * Gets minimum integer key in table
	 * 
	 * @return min int key or null of not found
	 */
	
	Integer minIntKey();
	
	/**
	 * Gets maximum integer key in table
	 * 
	 * @return max int key or null of not found
	 */
	
	Integer maxIntKey();
	
	/**
	 * Gets size of the table
	 * 
	 * @return size of the table
	 */
	
	int size();
	
	/**
	 * Clear table
	 */
	
	void clear();
	
}

