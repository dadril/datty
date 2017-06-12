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

import io.datty.api.DattyCode;
import io.datty.api.DattyOperation;
import io.datty.api.DattyOperationIO;
import io.datty.api.operation.ClearOperationIO;
import io.datty.api.operation.CompareAndSetOperationIO;
import io.datty.api.operation.ExecuteOperationIO;
import io.datty.api.operation.GetOperationIO;
import io.datty.api.operation.HeadOperationIO;
import io.datty.api.operation.PutOperationIO;
import io.datty.api.operation.RemoveOperationIO;
import io.datty.api.operation.ScanOperationIO;
import io.datty.api.operation.SizeOperationIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.support.exception.DattyException;
import io.netty.buffer.ByteBuf;

/**
 * DattyIO
 * 
 * @author Alex Shvid
 *
 */

public final class DattyIO {

	private final static int MAX_OPCODE = DattyOperation.OpCode.max();
	
	private final static DattyOperationIO<?>[] codeOperations = new DattyOperationIO<?>[MAX_OPCODE+1];
	
	private DattyIO() {
	}
	
	static {
		
		codeOperations[DattyOperation.OpCode.CLEAR.getCode()] = new ClearOperationIO();
		codeOperations[DattyOperation.OpCode.SIZE.getCode()] = new SizeOperationIO();
		codeOperations[DattyOperation.OpCode.SCAN.getCode()] = new ScanOperationIO();
		
		codeOperations[DattyOperation.OpCode.HEAD.getCode()] = new HeadOperationIO();
		codeOperations[DattyOperation.OpCode.GET.getCode()] = new GetOperationIO();
		codeOperations[DattyOperation.OpCode.REMOVE.getCode()] = new RemoveOperationIO();
		codeOperations[DattyOperation.OpCode.PUT.getCode()] = new PutOperationIO();
		codeOperations[DattyOperation.OpCode.COMPARE_AND_SET.getCode()] = new CompareAndSetOperationIO();
		codeOperations[DattyOperation.OpCode.EXECUTE.getCode()] = new ExecuteOperationIO();
		
	}
	
	@SuppressWarnings("unchecked")
	public static DattyOperation readOperation(MessageReader<Integer> reader, ByteBuf source) {
				
		int size = reader.size();
		if (size == 0) {
			return null;
		}
		size--;
		
		Integer fieldNum = reader.readKey(source);
		if (fieldNum == null) {
			throw new DattyException("expected field number");
		}
		
		if (DattyCode.FIELD_OPCODE != fieldNum.intValue()) {
			throw new DattyException("first field in message must be OPCODE");
		}
		
		Object value = reader.readValue(source, true);
		
		if (value == null || !(value instanceof Long)) {
			throw new DattyException("expected OPCODE value to be Long and not empty: " + value);
		}
		
		int opcode = ((Long) value).intValue();
		
		if (opcode < 0 || opcode > MAX_OPCODE) {
			throw new DattyException("invalid OPCODE: " + opcode);
		}
		
		@SuppressWarnings("rawtypes")
		DattyOperationIO io = codeOperations[opcode];
		
		if (io == null) {
			throw new DattyException("io operation is not found for opcode: " + opcode);
		}
		
		DattyOperation operation = io.newOperation();
		
		for (int i = 0; i != size; ++i) {
			
			fieldNum = reader.readKey(source);
			if (fieldNum == null) {
				throw new DattyException("expected field number");
			}
			
			boolean read = io.readField(operation, fieldNum.intValue(), reader, source);
			if (!read) {
				reader.skipValue(source, false);
			}
			
		}
		
		return operation;
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends DattyOperation> ByteBuf write(O operation, MessageWriter writer, ByteBuf sink) {
		
		int opcode = operation.getCode().getCode();
		
		@SuppressWarnings("rawtypes")
		DattyOperationIO io = codeOperations[opcode];
		
		if (io == null) {
			throw new DattyException("io operation is not found for opcode: " + opcode);
		}
		
		return io.write(operation, writer, sink);
		
	}
	
}
