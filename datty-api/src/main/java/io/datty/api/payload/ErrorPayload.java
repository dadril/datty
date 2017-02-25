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
package io.datty.api.payload;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import io.datty.api.DattyConstants;
import io.protostuff.Tag;

public class ErrorPayload {

	@Tag(1)
	private int errorCode;

	@Tag(2)
	private String errorMessage;

	@Tag(3)
	private String errorStacktrace;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorStacktrace() {
		return errorStacktrace;
	}

	public void setErrorStacktrace(String errorStacktrace) {
		this.errorStacktrace = errorStacktrace;
	}

	/**
	 * MessagePack
	 */

	public void pack(MessagePacker packer) throws IOException {

		if (errorCode != DattyConstants.UNSET_ERROR) {
			packer.packInt(1);
			packer.packInt(errorCode);
		}

		if (errorMessage != null) {
			packer.packInt(2);
			packer.packString(errorMessage);
		}

		if (errorStacktrace != null) {
			packer.packInt(3);
			packer.packString(errorStacktrace);
		}

		packer.packInt(0);

	}

	public void unpack(MessageUnpacker unpacker) throws IOException {

		while (true) {

			int tag = unpacker.unpackInt();

			if (tag == 0) {
				return;
			}

			switch (tag) {

			case 1:
				errorCode = unpacker.unpackInt();
				break;

			case 2:
				errorMessage = unpacker.unpackString();
				break;

			case 3:
				errorStacktrace = unpacker.unpackString();
				break;

			}

		}

	}

}
