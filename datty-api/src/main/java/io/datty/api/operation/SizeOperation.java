package io.datty.api.operation;

/**
 * SizeOperation
 * 
 * @author Alex Shvid
 *
 */

public class SizeOperation extends AbstractQueryOperation<SizeOperation> {

	public SizeOperation(String setName) {
		super(setName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.SIZE;
	}

	@Override
	public String toString() {
		return "SizeOperation [setName=" + setName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
