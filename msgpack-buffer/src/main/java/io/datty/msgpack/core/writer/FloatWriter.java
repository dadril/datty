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

import io.datty.msgpack.core.AbstractMessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * FloatWriter
 * 
 * @author Alex Shvid
 *
 */

public class FloatWriter extends AbstractMessageWriter implements ValueWriter<Float> {

	public static final FloatWriter INSTANCE = new FloatWriter();

	@Override
	public ByteBuf write(Float value, ByteBuf sink, boolean copy, boolean numeric) {
		if (value == null) {
			writeNull(sink);
		}
		else {
			writeFloat(value, sink);
		}
		return sink;
	}
	
}
