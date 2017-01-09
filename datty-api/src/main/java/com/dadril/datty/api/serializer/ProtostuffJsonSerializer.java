/*
 * Copyright (C) 2016 Data Drilling Corporation
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
package com.dadril.datty.api.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dadril.datty.api.payload.DattyPayload;
import com.dadril.datty.support.DattySchemas;

import io.protostuff.JsonIOUtil;

public enum ProtostuffJsonSerializer implements DattySerializer {

	INSTANCE;

	@Override
	public void serialize(DattyPayload payloadInput, OutputStream outputStream)
			throws IOException {
		JsonIOUtil.writeTo(outputStream, payloadInput,
				DattySchemas.PAYLOAD_JSON_SCHEMA, false);
	}

	@Override
	public void deserialize(InputStream inputStream, DattyPayload payloadOutput)
			throws IOException {
		JsonIOUtil.mergeFrom(inputStream, payloadOutput,
				DattySchemas.PAYLOAD_JSON_SCHEMA, false);
	}

}
