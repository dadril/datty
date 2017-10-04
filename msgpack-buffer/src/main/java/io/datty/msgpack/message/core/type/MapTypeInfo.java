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
package io.datty.msgpack.message.core.type;

import io.datty.msgpack.message.core.reader.ValueReader;
import io.datty.msgpack.message.core.writer.ValueWriter;
import io.datty.msgpack.message.support.MessageIOException;

/**
 * MapTypeInfo
 * 
 * @author Alex Shvid
 *
 */

public interface MapTypeInfo<K, V, T>  extends TypeInfo<T> {

	/**
	 * Gets key type
	 * 
	 * @return not null key type
	 */
	
	Class<K> getKeyType();
	
	/**
	 * Gets component type
	 * 
	 * @return not null value type
	 */
	
	Class<V> getComponentType();

	/**
	 * Gets key value reader
	 * 
	 * @return not null value reader
	 * @throws MessageIOException if not found
	 */
	
	ValueReader<K> getKeyValueReader();

	/**
	 * Gets key value writer
	 * 
	 * @return not null value reader
	 * @throws MessageIOException if not found
	 */
	
	ValueWriter<K> getKeyValueWriter();
	
	/**
	 * Gets component value reader
	 * 
	 * @return not null value reader
	 * @throws MessageIOException if not found
	 */
	
	ValueReader<V> getComponentValueReader();

	/**
	 * Gets component value writer
	 * 
	 * @return not null value reader
	 * @throws MessageIOException if not found
	 */
	
	ValueWriter<V> getComponentValueWriter();
	
}
