package io.datty.api.operation;

/**
 * DeleteOperation
 * 
 * @author Alex Shvid
 *
 */

public class DeleteOperation extends AbstractQueryOperation<DeleteOperation> {

	public DeleteOperation(String setName) {
		super(setName);
	}
	
	@Override
	public OpCode getCode() {
		return OpCode.DELETE;
	}

	@Override
	public String toString() {
		return "DeleteOperation [setName=" + setName + ", superKey=" + superKey + ", timeoutMillis=" + timeoutMillis
				+ ", fallback=" + fallback + ", getCode()=" + getCode() + "]";
	}
	
	
}
