package io.datty.api.result;

/**
 * LongResult
 * 
 * @author dadril
 *
 */

public class LongResult extends AbstractResult {

	private final long value;

	public LongResult(long value) {
		this.value = value;
	}

	public long get() {
		return value;
	}

	@Override
	public ResCode getCode() {
		return ResCode.LONG;
	}

	@Override
	public String toString() {
		return "LongResult [value=" + value + "]";
	}
	
}
