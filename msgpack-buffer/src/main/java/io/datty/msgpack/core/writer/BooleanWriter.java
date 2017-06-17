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
 * BooleanWriter
 * 
 * @author Alex Shvid
 *
 */

public class BooleanWriter extends AbstractMessageWriter implements ValueWriter<Boolean> {

	public static final BooleanWriter INSTANCE = new BooleanWriter();

	@Override
	public ByteBuf write(Boolean value, ByteBuf sink, boolean copy, boolean numeric) {
		if (value == null) {
			writeNull(sink);
		}
		else {
			writeBoolean(value, sink);
		}
		return sink;
	}
	
}
