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
package io.datty.msgpack.core.writer;

import io.netty.buffer.ByteBuf;

/**
 * ValueWriter
 * 
 * @author Alex Shvid
 *
 */

public interface ValueWriter<V> {

	/**
	 * Writes value to the sink
	 * 
	 * @param value - java object value
	 * @param sink - output buffer
	 * @param copy - always copy if true
	 * @return old sink or new sink
	 */
	
	ByteBuf write(V value, ByteBuf sink, boolean copy);
	
}
