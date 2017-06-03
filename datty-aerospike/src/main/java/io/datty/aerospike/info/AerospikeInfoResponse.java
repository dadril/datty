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
package io.datty.aerospike.info;

import java.util.StringTokenizer;

/**
 * AerospikeInfoResponse
 * 
 * @author Alex Shvid
 *
 */

public class AerospikeInfoResponse {

	private static final String KEY_VALUE_SEPARATOR = "=";
	private static final String TOKEN_SEPARATOR = ":";
	private static final String COMMAND_SUFFIX = ";";

	private static final String N_OBJECTS = "n_objects";
	private static final String OBJECTS = "objects";
	private static final String SET_STOP_WRITE_COUNT = "set-stop-write-count";
	private static final String SET_EVICT_HWM_COUNT = "set-evict-hwm-count";
	private static final String SET_ENABLE_XDR = "set-enable-xdr";
	private static final String SET_DELETE = "set-delete";

	private boolean exists;
	private long nObjects;
	private long setStopWriteCount;
	private long setEvictHwmCount;
	private String setEnableXdr;
	private boolean setDelete;

	public AerospikeInfoResponse(String response) {

		if (response == null) {
			throw new IllegalArgumentException("response is null");
		}

		exists = !response.isEmpty();
		nObjects = 0;
		setStopWriteCount = 0;
		setEvictHwmCount = 0;
		setEnableXdr = "use-default";
		setDelete = false;

		if (response.endsWith(COMMAND_SUFFIX)) {
			response = response.substring(0, response.length() - 1);
		}

		StringTokenizer tokenizer = new StringTokenizer(response, TOKEN_SEPARATOR);

		while (tokenizer.hasMoreTokens()) {

			String token = tokenizer.nextToken();

			int idx = token.indexOf(KEY_VALUE_SEPARATOR);
			if (idx == -1) {
				// no value, skip
			} else {

				String key = token.substring(0, idx);
				String value = "";
				if (idx + 1 < token.length()) {
					value = token.substring(idx + 1, token.length());
				}

				if (N_OBJECTS.equals(key)) {
					nObjects = parseLong(value, 0);
				} else if (OBJECTS.equals(key)) {
					nObjects = parseLong(value, 0);
				} else if (SET_STOP_WRITE_COUNT.equals(key)) {
					setStopWriteCount = parseLong(value, 0);
				} else if (SET_EVICT_HWM_COUNT.equals(key)) {
					setEvictHwmCount = parseLong(value, 0);
				} else if (SET_ENABLE_XDR.equals(key)) {
					setEnableXdr = value;
				} else if (SET_DELETE.equals(key)) {
					setDelete = Boolean.parseBoolean(value);
				}

			}

		}

	}

	public boolean isExists() {
		return exists;
	}

	public long getNObjects() {
		return nObjects;
	}

	public long getSetStopWriteCount() {
		return setStopWriteCount;
	}

	public long getSetEvictHwmCount() {
		return setEvictHwmCount;
	}

	public String getSetEnableXdr() {
		return setEnableXdr;
	}

	public boolean isSetDelete() {
		return setDelete;
	}

	private static long parseLong(String str, long fallbackValue) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return fallbackValue;
		}
	}

}
