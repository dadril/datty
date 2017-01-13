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
package io.datty.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

	private final ByteBuffer backingBuffer;

	public ByteBufferInputStream(int bufferSize) {
		this(ByteBuffer.allocate(bufferSize));
		backingBuffer.flip();
	}

	public ByteBufferInputStream(ByteBuffer buffer) {
		this.backingBuffer = buffer;
	}

	public ByteBuffer getBackingBuffer() {
		return backingBuffer;
	}

	@Override
	public int read() throws IOException {
		if (!backingBuffer.hasRemaining())
			return -1;
		return backingBuffer.get() & 0xFF;
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws IOException {
		if (length == 0)
			return 0;
		int actual = Math.min(backingBuffer.remaining(), length);
		if (actual == 0)
			return -1;
		backingBuffer.get(bytes, offset, actual);
		return actual;
	}

	@Override
	public int available() throws IOException {
		return backingBuffer.remaining();
	}

}
