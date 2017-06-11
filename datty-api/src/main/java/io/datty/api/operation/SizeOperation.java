package io.datty.api.operation;

/**
 * SizeOperation
 * 
 * @author Alex Shvid
 *
 */

public class SizeOperation extends AbstractSetOperation<SizeOperation> {

	public SizeOperation() {
	}
	
	public SizeOperation(String setName) {
		setSetName(setName);
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
