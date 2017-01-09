/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package com.dadril.datty.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream {

	private final ByteBuffer backingBuffer;

	public ByteBufferOutputStream(int bufferSize) {
		this(ByteBuffer.allocate(bufferSize));
	}

	public ByteBufferOutputStream(ByteBuffer byteBuffer) {
		this.backingBuffer = byteBuffer;
	}

	public ByteBuffer getBackingBuffer() {
		return backingBuffer;
	}

	@Override
	public void write(int b) throws IOException {
		if (!backingBuffer.hasRemaining()) {
			flush();
		}
		backingBuffer.put((byte) b);
	}

	@Override
	public void write(byte[] bytes, int offset, int length) throws IOException {
		if (backingBuffer.remaining() < length) {
			flush();
		}
		backingBuffer.put(bytes, offset, length);
	}

}
