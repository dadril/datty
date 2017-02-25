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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.datty.api.payload.DattyPayload;
import io.datty.support.DattySchemas;
import io.protostuff.ProtobufIOUtil;

public enum ProtostuffSerializer implements DattySerializer {

	INSTANCE;

	@Override
	public void serialize(DattyPayload payloadInput, OutputStream outputStream)
			throws IOException {
		ProtobufIOUtil.writeTo(outputStream, payloadInput,
				DattySchemas.PAYLOAD_SCHEMA,
				DattySchemas.getLocalLinkedBuffer());
	}

	@Override
	public void deserialize(InputStream inputStream, DattyPayload payloadOutput)
			throws IOException {
		ProtobufIOUtil.mergeFrom(inputStream, payloadOutput,
				DattySchemas.PAYLOAD_SCHEMA);
	}

}
