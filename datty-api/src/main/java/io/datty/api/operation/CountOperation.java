package io.datty.api.operation;

/**
 * CountOperation
 * 
 * @author Alex Shvid
 *
 */

public class CountOperation extends AbstractQueryOperation<CountOperation> {

	public CountOperation(String cacheName) {
		super(cacheName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.COUNT;
	}

	@Override
	public String toString() {
		return "CountOperation [cacheName=" + cacheName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
