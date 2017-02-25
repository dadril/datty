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
package io.datty.api.serializer;

import io.datty.api.payload.DattyPayload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface DattySerializer {

	/**
	 * Serializes operation payload
	 * 
	 * @param payloadInput
	 *            - not null payload object
	 * @param output
	 *            - output stream
	 * @return not null byte array
	 */

	void serialize(DattyPayload payloadInput, OutputStream outputStream)
			throws IOException;

	/**
	 * Deserializes operation payload
	 * 
	 * @param inputStream
	 *            - not null input stream
	 * @param payloadOutput
	 *            - not null reusable payload
	 * @return not null operation object
	 */

	void deserialize(InputStream inputStream, DattyPayload payloadOutput)
			throws IOException;

}
