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
package io.datty.support;

import io.datty.api.DattyConstants;
import io.datty.api.operation.Value;

import java.util.Arrays;

/**
 * Simple POJO implementation for value with version or digest
 * 
 * @author dadril
 *
 */

public final class VersionedValue implements Value {

	private final byte[] value;
	private final byte[] digest;
	private final long version;

	private VersionedValue(byte[] value, long version) {
		this.value = value;
		this.digest = null;
		this.version = version;
	}

	private VersionedValue(byte[] value, byte[] digest) {
		this.value = value;
		this.digest = digest;
		this.version = DattyConstants.UNSET_VERSION;
	}

	public static VersionedValue wrap(byte[] value, long version) {
		return new VersionedValue(value, version);
	}

	public static VersionedValue wrap(byte[] value, byte[] digest) {
		return new VersionedValue(value, digest);
	}

	@Override
	public byte[] toByteArray() {
		if (value != null) {
			return Arrays.copyOf(value, value.length);
		}
		return null;
	}

	@Override
	public byte[] getBackingValue() {
		return value;
	}

	@Override
	public byte[] getDigest() {
		return digest;
	}

	public boolean hasDigest() {
		return digest != null;
	}

	@Override
	public long getVersion() {
		return version;
	}

	public boolean hasVersion() {
		return version != DattyConstants.UNSET_VERSION;
	}

}
