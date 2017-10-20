package io.datty.api.operation;

/**
 * Size Operation
 * 
 * @author Alex Shvid
 *
 */

public final class Size extends AbstractSetOperation<Size> {

	public Size() {
	}
	
	public Size(String setName) {
		setSetName(setName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.SIZE;
	}

	@Override
	public String toString() {
		return "Size [setName=" + setName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
