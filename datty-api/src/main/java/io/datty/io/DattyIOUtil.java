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
package io.datty.io;

import io.datty.api.DattyOperation;
import io.datty.api.DattyResult;
import io.datty.api.payload.DattyPayload;
import io.datty.api.serializer.DattySerializer;
import io.datty.support.DattyOperations;
import io.datty.support.DattyResults;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Datty IO util class
 * 
 * @author dadril
 *
 */

public final class DattyIOUtil {

	private DattyIOUtil() {

	}

	public static byte[] toByteArray(DattyOperation operation,
			DattySerializer serializer) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		write(operation, serializer, outputStream);

		return outputStream.toByteArray();
	}

	public static void write(DattyOperation operation,
			DattySerializer serializer, OutputStream out) throws IOException {

		DattyPayload payload = DattyOperations.toPayload(operation);

		serializer.serialize(payload, out);

	}

	public static DattyOperation parseOperation(byte[] binary,
			DattySerializer serializer) throws IOException {

		return parseOperation(new ByteArrayInputStream(binary), serializer);

	}

	public static DattyOperation parseOperation(InputStream in,
			DattySerializer serializer) throws IOException {

		DattyPayload payload = new DattyPayload();

		serializer.deserialize(in, payload);

		return DattyOperations.parseFrom(payload);

	}

	public static byte[] toByteArray(DattyResult result,
			DattySerializer serializer) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		write(result, serializer, outputStream);

		return outputStream.toByteArray();
	}

	public static void write(DattyResult result, DattySerializer serializer,
			OutputStream out) throws IOException {

		DattyPayload payload = DattyResults.toPayload(result);

		serializer.serialize(payload, out);

	}

	public static DattyResult parseResult(byte[] binary,
			DattySerializer serializer) throws IOException {

		return parseResult(new ByteArrayInputStream(binary), serializer);

	}

	public static DattyResult parseResult(InputStream in,
			DattySerializer serializer) throws IOException {

		DattyPayload payload = new DattyPayload();

		serializer.deserialize(in, payload);

		return DattyResults.parseFrom(payload);

	}
}
