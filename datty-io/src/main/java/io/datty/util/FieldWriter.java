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
package io.datty.util;

import java.util.Collection;

import io.datty.api.DattyField;
import io.datty.api.DattyOperation.OpCode;
import io.datty.api.DattyResult.ResCode;
import io.datty.api.DattyRow;
import io.datty.api.DattyRowIO;
import io.datty.api.version.Version;
import io.datty.api.version.VersionIO;
import io.datty.msgpack.message.MessageWriter;
import io.datty.msgpack.message.core.AbstractMessageWriter;
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
	private final boolean numeric;
	private int maxHeaderSize;
	private int headerIndex;
	private int size;
	
	public FieldWriter(MessageWriter writer, ByteBuf sink, boolean numeric) {
		this(writer, sink, numeric, AbstractMessageWriter.MAX_BYTE_HEADER_SIZE);
	}
	
	public FieldWriter(MessageWriter writer, ByteBuf sink, boolean numeric, int maxHeaderSize) {
		this.writer = writer;
		this.sink = sink;
		this.numeric = numeric;
		this.maxHeaderSize = maxHeaderSize;
		this.headerIndex = writer.skipHeader(maxHeaderSize, sink);
	}
	
	private void writeKey(DattyField field) {
		if (numeric) {
			writer.writeKey(field.getFieldCode(), sink);
		}
		else {
			writer.writeKey(field.getFieldName(), sink);
		}
	}
	
	public void writeField(DattyField field, OpCode value) {
		writeKey(field);
		writer.writeValue(value.getCode(), sink);
		size++;
	}

	public void writeField(DattyField field, ResCode value) {
		writeKey(field);
		writer.writeValue(value.getCode(), sink);
		size++;
	}
	
	public void writeField(DattyField field, boolean value) {
		writeKey(field);
		writer.writeValue(value, sink);
		size++;
	}

	public void writeField(DattyField field, long value) {
		writeKey(field);
		writer.writeValue(value, sink);
		size++;
	}
	
	public void writeField(DattyField field, String value) {
		writeKey(field);
		writer.writeValue(value, sink);
		size++;
	}
	
	public void writeField(DattyField field, ByteBuf value) {
		writeKey(field);
		writer.writeValue(value, sink, false);
		size++;
	}
	
	public void writeField(DattyField field, Collection<String> values) {
		writeKey(field);
		DattyCollectionIO.writeStringArray(values, sink);
		size++;
	}
	
	public void writeField(DattyField field, Version version) {
		writeKey(field);
		VersionIO.writeVersion(version, sink, numeric);
		size++;
	}
	
	public void writeField(DattyField field, DattyRow row) {
		writeKey(field);
		sink = DattyRowIO.writeRow(row, sink);
		size++;
	}
	
	public ByteBuf writeEnd() {
		writer.writeHeader(size, maxHeaderSize, headerIndex, sink);
		return sink;
	}
	
}
