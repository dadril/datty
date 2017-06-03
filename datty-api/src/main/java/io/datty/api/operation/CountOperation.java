package io.datty.api.operation;

/**
 * CountOperation
 * 
 * @author Alex Shvid
 *
 */

public class CountOperation extends AbstractQueryOperation<CountOperation> {

	public CountOperation(String setName) {
		super(setName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.COUNT;
	}

	@Override
	public String toString() {
		return "CountOperation [setName=" + setName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
