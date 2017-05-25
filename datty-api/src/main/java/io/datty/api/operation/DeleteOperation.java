package io.datty.api.operation;

/**
 * DeleteOperation
 * 
 * @author Alex Shvid
 *
 */

public class DeleteOperation extends AbstractQueryOperation<DeleteOperation> {

	public DeleteOperation(String cacheName) {
		super(cacheName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.DELETE;
	}

	@Override
	public String toString() {
		return "DeleteOperation [cacheName=" + cacheName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
