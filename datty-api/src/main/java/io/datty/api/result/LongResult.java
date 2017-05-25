package io.datty.api.result;

import io.datty.api.DattyResult;

/**
 * LongResult
 * 
 * @author Alex Shvid
 *
 */

public class LongResult implements DattyResult {

	private long value;
	
	public LongResult() {
	}
	
	public long getValue() {
		return value;
	}

	public LongResult setValue(long value) {
		this.value = value;
		return this;
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
