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
package io.datty.api.payload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.protostuff.Tag;

@JsonInclude(Include.NON_DEFAULT)
public class DattyPayload {

	@Tag(1)
	private DattyPayloadType type;

	@Tag(2)
	private List<SingleOperationPayload> operationList;

	@Tag(3)
	private List<SingleResultPayload> resultList;

	public void reset() {
		this.type = null;
		this.operationList = null;
		this.resultList = null;
	}

	public SingleOperationPayload addOperation() {
		if (operationList == null) {
			operationList = new ArrayList<>();
		}
		SingleOperationPayload op = new SingleOperationPayload();
		operationList.add(op);
		return op;
	}

	public SingleResultPayload addResult() {
		if (resultList == null) {
			resultList = new ArrayList<>();
		}
		SingleResultPayload r = new SingleResultPayload();
		resultList.add(r);
		return r;
	}

	public DattyPayloadType getType() {
		return type;
	}

	public void setType(DattyPayloadType type) {
		this.type = type;
	}

	public List<SingleOperationPayload> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<SingleOperationPayload> operationList) {
		this.operationList = operationList;
	}

	public List<SingleResultPayload> getResultList() {
		return resultList;
	}

	public void setResultList(List<SingleResultPayload> resultList) {
		this.resultList = resultList;
	}

	public void copyFrom(DattyPayload other) {
		this.type = other.type;
		this.operationList = other.operationList;
		this.resultList = other.resultList;
	}

	/**
	 * MessagePack
	 */

	public void pack(MessagePacker packer) throws IOException {

		int size = 0;
		if (type != null) {
			size++;
		}
		if (operationList != null) {
			size++;
		}
		if (resultList != null) {
			size++;
		}

		packer.packMapHeader(size);

		if (type != null) {
			packer.packInt(1);
			packer.packString(type.name());
		}

		if (operationList != null) {
			packer.packInt(2);
			packer.packArrayHeader(operationList.size());
			for (SingleOperationPayload op : operationList) {
				op.pack(packer);
			}
		}

		if (resultList != null) {
			packer.packInt(3);
			packer.packArrayHeader(resultList.size());
			for (SingleResultPayload r : resultList) {
				r.pack(packer);
			}
		}

	}

	public void unpack(MessageUnpacker unpacker) throws IOException {

		int size = unpacker.unpackMapHeader();

		for (int k = 0; k != size; ++k) {

			int tag = unpacker.unpackInt();
			int length = 0;

			switch (tag) {

			case 1:
				this.type = DattyPayloadType.valueOf(unpacker.unpackString());
				break;

			case 2:
				length = unpacker.unpackArrayHeader();
				this.operationList = new ArrayList<>(length);
				for (int i = 0; i != length; ++i) {
					SingleOperationPayload op = new SingleOperationPayload();
					op.unpack(unpacker);
					this.operationList.add(op);
				}
				break;

			case 3:
				length = unpacker.unpackArrayHeader();
				this.resultList = new ArrayList<>(length);
				for (int i = 0; i != length; ++i) {
					SingleResultPayload r = new SingleResultPayload();
					r.unpack(unpacker);
					this.resultList.add(r);
				}
				break;

			default:
				unpacker.skipValue();
				break;

			}

		}

	}

}
