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

import io.datty.api.DattyCode;
import io.datty.api.UpdatePolicy;
import io.datty.msgpack.MessageReader;
import io.netty.buffer.ByteBuf;

/**
 * AbstractUpdateOperationIO
 * 
 * @author Alex Shvid
 *
 */

@SuppressWarnings("rawtypes")
abstract class AbstractUpdateOperationIO<O extends AbstractUpdateOperation> extends AbstractOperationIO<O> {

	@Override
	public boolean readField(O operation, int fieldCode, MessageReader<Integer> reader, ByteBuf source) {
		
		boolean read = super.readField(operation, fieldCode, reader, source);
		
		if (read) {
			return true;
		}
		
		switch(fieldCode) {
		
			case DattyCode.FIELD_TTL_SEC:
				operation.setTtlSeconds(((Long) reader.readValue(source, true)).intValue());
				return true;
	
			case DattyCode.FIELD_UPDATE_POLICY:
				int code = ((Long) reader.readValue(source, true)).intValue();
				operation.setUpdatePolicy(UpdatePolicy.findByCode(code));
				return true;

		}
		
		return false;
	}

	@Override
	protected void writeFields(O operation, FieldWriter fieldWriter) {
		
		super.writeFields(operation, fieldWriter);
		
		if (operation.hasTtlSeconds()) {
			fieldWriter.writeField(DattyCode.FIELD_TTL_SEC, operation.getTtlSeconds());
		}
		
		fieldWriter.writeField(DattyCode.FIELD_UPDATE_POLICY, operation.getUpdatePolicy().getCode());
		
	}
	
}
