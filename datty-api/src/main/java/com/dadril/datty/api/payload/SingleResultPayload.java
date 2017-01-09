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
package com.dadril.datty.api.payload;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import com.dadril.datty.api.DattyConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.protostuff.Tag;

@JsonInclude(Include.NON_DEFAULT)
public class SingleResultPayload {

	@Tag(1)
	private SingleResultType type;

	@Tag(2)
	private boolean booleanValue;

	@Tag(3)
	private byte[] binaryValue;

	@Tag(4)
	private long version;

	@Tag(5)
	private ErrorPayload error;

	public void reset() {
		this.type = null;
		this.booleanValue = false;
		this.binaryValue = null;
		this.version = DattyConstants.UNSET_VERSION;
		this.error = null;
	}

	public SingleResultType getType() {
		return type;
	}

	public void setType(SingleResultType type) {
		this.type = type;
	}

	public ErrorPayload getError() {
		return error;
	}

	public void setError(ErrorPayload error) {
		this.error = error;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public byte[] getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(byte[] binaryValue) {
		this.binaryValue = binaryValue;
	}

	/**
	 * MessagePack
	 */

	public void pack(MessagePacker packer) throws IOException {

		int size = 0;

		if (type != null) {
			size++;
		}

		if (booleanValue) {
			size++;
		}

		if (binaryValue != null) {
			size++;
		}

		if (version != DattyConstants.UNSET_VERSION) {
			size++;
		}

		if (error != null) {
			size++;
		}

		packer.packMapHeader(size);

		if (type != null) {
			packer.packInt(1);
			packer.packString(type.name());
		}

		if (booleanValue) {
			packer.packInt(2);
			packer.packBoolean(booleanValue);
		}

		if (binaryValue != null) {
			packer.packInt(3);
			packer.packBinaryHeader(binaryValue.length);
			packer.addPayload(binaryValue);
		}

		if (version != DattyConstants.UNSET_VERSION) {
			packer.packInt(4);
			packer.packLong(version);
		}

		if (error != null) {
			packer.packInt(5);
			error.pack(packer);
		}

	}

	public void unpack(MessageUnpacker unpacker) throws IOException {

		int size = unpacker.unpackMapHeader();

		for (int k = 0; k != size; ++k) {

			int tag = unpacker.unpackInt();

			switch (tag) {

			case 1:
				this.type = SingleResultType.valueOf(unpacker.unpackString());
				break;

			case 2:
				this.booleanValue = unpacker.unpackBoolean();
				break;

			case 3:
				this.binaryValue = unpacker.readPayload(unpacker
						.unpackBinaryHeader());
				break;

			case 4:
				this.version = unpacker.unpackLong();
				break;

			case 5:
				this.error = new ErrorPayload();
				this.error.unpack(unpacker);
				break;

			default:
				unpacker.skipValue();
				break;

			}

		}

	}

}
