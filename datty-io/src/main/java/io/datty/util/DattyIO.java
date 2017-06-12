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
import io.datty.api.DattyResult;
import io.datty.api.DattyResultIO;
import io.datty.api.operation.ClearOperationIO;
import io.datty.api.operation.CompareAndSetOperationIO;
import io.datty.api.operation.ExecuteOperationIO;
import io.datty.api.operation.GetOperationIO;
import io.datty.api.operation.HeadOperationIO;
import io.datty.api.operation.PutOperationIO;
import io.datty.api.operation.RemoveOperationIO;
import io.datty.api.operation.ScanOperationIO;
import io.datty.api.operation.SizeOperationIO;
import io.datty.api.result.CompareAndSetResultIO;
import io.datty.api.result.ExecuteResultIO;
import io.datty.api.result.GetResultIO;
import io.datty.api.result.HeadResultIO;
import io.datty.api.result.PutResultIO;
import io.datty.api.result.RecordResultIO;
import io.datty.api.result.RemoveResultIO;
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.IntMapMessageReader;
import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.ValueMessageReader;
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
	private final static int MAX_RESCODE = DattyResult.ResCode.max();
	
	private final static DattyOperationIO<?>[] codeOperations = new DattyOperationIO<?>[MAX_OPCODE+1];
	
	private final static DattyResultIO<?>[] codeResults = new DattyResultIO<?>[MAX_OPCODE+1];
	
	private DattyIO() {
	}
	
	static {
		
		codeOperations[DattyOperation.OpCode.HEAD.getCode()] = new HeadOperationIO();
		codeOperations[DattyOperation.OpCode.GET.getCode()] = new GetOperationIO();
		codeOperations[DattyOperation.OpCode.PUT.getCode()] = new PutOperationIO();
		codeOperations[DattyOperation.OpCode.REMOVE.getCode()] = new RemoveOperationIO();
		codeOperations[DattyOperation.OpCode.COMPARE_AND_SET.getCode()] = new CompareAndSetOperationIO();
		codeOperations[DattyOperation.OpCode.EXECUTE.getCode()] = new ExecuteOperationIO();
		
		codeOperations[DattyOperation.OpCode.CLEAR.getCode()] = new ClearOperationIO();
		codeOperations[DattyOperation.OpCode.SIZE.getCode()] = new SizeOperationIO();
		codeOperations[DattyOperation.OpCode.SCAN.getCode()] = new ScanOperationIO();
		
		codeResults[DattyResult.ResCode.HEAD.getCode()] = HeadResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.GET.getCode()] = GetResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.PUT.getCode()] = PutResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.REMOVE.getCode()] = RemoveResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.COMPARE_AND_SET.getCode()] = CompareAndSetResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.EXECUTE.getCode()] = ExecuteResultIO.INSTANCE;
		codeResults[DattyResult.ResCode.RECORD.getCode()] = RecordResultIO.INSTANCE;
		
	}

	/**
	 * Read operation from the input buffer
	 * 
	 * @param source - input buffer
	 * @return not null datty operation
	 */
	
	@SuppressWarnings("unchecked")
	public static DattyOperation readOperation(ByteBuf source) {
		
		if (source == null || source.readableBytes() == 0) {
			throw new DattyException("empty buffer");
		}
		
		if (!ValueMessageReader.INSTANCE.isMap(source)) {
			throw new DattyException("expected map in the buffer");
		}
		
		MessageReader<?> reader = ValueMessageReader.INSTANCE.readMap(source, true);
		
		if (!(reader instanceof IntMapMessageReader)) {
			throw new DattyException("expected IntMapMessageReader in the buffer");
		}
		
		return readOperation( (MessageReader<Integer>) reader, source);
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
			throw new DattyException("expected OPCODE value to be Long and not null: " + value);
		}
		
		int opcode = ((Long) value).intValue();
		
		if (opcode < 0 || opcode > MAX_OPCODE) {
			throw new DattyException("out of range OPCODE: " + opcode);
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
	
	/**
	 * Writes operation to the sink
	 * 
	 * @param operation - datty operation
	 * @param sink - output buffer
	 * @return old or new sink
	 */
	
	public static <O extends DattyOperation> ByteBuf writeOperation(O operation, ByteBuf sink) {
		return writeOperation(operation, MapMessageWriter.INSTANCE, sink);
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends DattyOperation> ByteBuf writeOperation(O operation, MessageWriter writer, ByteBuf sink) {
		
		int opcode = operation.getCode().getCode();
		
		@SuppressWarnings("rawtypes")
		DattyOperationIO io = codeOperations[opcode];
		
		if (io == null) {
			throw new DattyException("io operation is not found for opcode: " + opcode);
		}
		
		return io.write(operation, writer, sink);
		
	}
	
	/**
	 * Read result from the input buffer
	 * 
	 * @param source - input buffer
	 * @return not null datty result
	 */
	
	@SuppressWarnings("unchecked")
	public static DattyResult readResult(ByteBuf source) {
		
		if (source == null || source.readableBytes() == 0) {
			throw new DattyException("empty buffer");
		}
		
		if (!ValueMessageReader.INSTANCE.isMap(source)) {
			throw new DattyException("expected map in the buffer");
		}
		
		MessageReader<?> reader = ValueMessageReader.INSTANCE.readMap(source, true);
		
		if (!(reader instanceof IntMapMessageReader)) {
			throw new DattyException("expected IntMapMessageReader in the buffer");
		}
		
		return readResult( (MessageReader<Integer>) reader, source);
	}
	
	@SuppressWarnings("unchecked")
	public static DattyResult readResult(MessageReader<Integer> reader, ByteBuf source) {
				
		int size = reader.size();
		if (size == 0) {
			return null;
		}
		size--;
		
		Integer fieldNum = reader.readKey(source);
		if (fieldNum == null) {
			throw new DattyException("expected field number");
		}
		
		if (DattyCode.FIELD_RESCODE != fieldNum.intValue()) {
			throw new DattyException("first field in message must be RESCODE");
		}
		
		Object value = reader.readValue(source, true);
		
		if (value == null || !(value instanceof Long)) {
			throw new DattyException("expected RESCODE value to be Long and not null: " + value);
		}
		
		int rescode = ((Long) value).intValue();
		
		if (rescode < 0 || rescode > MAX_RESCODE) {
			throw new DattyException("out of range OPCODE: " + rescode);
		}
		
		@SuppressWarnings("rawtypes")
		DattyResultIO io = codeResults[rescode];
		
		if (io == null) {
			throw new DattyException("io operation is not found for rescode: " + rescode);
		}
		
		DattyResult result = io.newResult();
		
		for (int i = 0; i != size; ++i) {
			
			fieldNum = reader.readKey(source);
			if (fieldNum == null) {
				throw new DattyException("expected field number");
			}
			
			boolean read = io.readField(result, fieldNum.intValue(), reader, source);
			if (!read) {
				reader.skipValue(source, false);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Writes result to the sink
	 * 
	 * @param result - datty result
	 * @param sink - output buffer
	 * @return old or new sink
	 */
	
	public static <R extends DattyResult> ByteBuf writeResult(R result, ByteBuf sink) {
		return writeResult(result, MapMessageWriter.INSTANCE, sink);
	}
	
	@SuppressWarnings("unchecked")
	public static <R extends DattyResult> ByteBuf writeResult(R result, MessageWriter writer, ByteBuf sink) {
		
		int rescode = result.getCode().getCode();
		
		@SuppressWarnings("rawtypes")
		DattyResultIO io = codeResults[rescode];
		
		if (io == null) {
			throw new DattyException("io result is not found for rescode: " + rescode);
		}
		
		return io.write(result, writer, sink);
		
	}
	
}
