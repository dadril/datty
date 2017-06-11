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
package io.datty.api.operation;

import java.util.Collection;

import io.datty.api.DattyOperation.OpCode;
import io.datty.api.DattyResult.ResCode;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.AbstractMessageWriter;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.netty.buffer.ByteBuf;

/**
 * FieldWriter
 * 
 * @author Alex Shvid
 *
 */

public final class FieldWriter {

	private final MessageWriter writer; 
	private ByteBuf sink;
	private int maxHeaderSize;
	private int headerIndex;
	private int size;
	
	public FieldWriter(MessageWriter writer, ByteBuf sink) {
		this(writer, sink, AbstractMessageWriter.MAX_BYTE_HEADER_SIZE);
	}
	
	public FieldWriter(MessageWriter writer, ByteBuf sink, int maxHeaderSize) {
		this.writer = writer;
		this.sink = sink;
		this.maxHeaderSize = maxHeaderSize;
		this.headerIndex = writer.skipHeader(maxHeaderSize, sink);
	}
	
	public void writeField(int fieldCode, OpCode value) {
		writer.writeValue(fieldCode, sink);
		writer.writeValue(value.getCode(), sink);
		size++;
	}

	public void writeField(int fieldCode, ResCode value) {
		writer.writeValue(fieldCode, sink);
		writer.writeValue(value.getCode(), sink);
		size++;
	}
	
	public void writeField(int fieldCode, boolean value) {
		writer.writeValue(fieldCode, sink);
		writer.writeValue(value, sink);
		size++;
	}

	public void writeField(int fieldCode, long value) {
		writer.writeValue(fieldCode, sink);
		writer.writeValue(value, sink);
		size++;
	}
	
	public void writeField(int fieldCode, String value) {
		writer.writeValue(fieldCode, sink);
		writer.writeValue(value, sink);
		size++;
	}
	
	public void writeField(int fieldCode, Collection<String> values) {
		
		writer.writeValue(fieldCode, sink);

		ArrayMessageWriter arrayWriter = ArrayMessageWriter.INSTANCE;
		
		arrayWriter.writeHeader(values.size(), sink); 
		for (String str : values) {
			arrayWriter.writeValue(str, sink);
		}
		
		size++;
	}
	
	public ByteBuf writeEnd() {
		writer.writeHeader(size, maxHeaderSize, headerIndex, sink);
		return sink;
	}
	
}
