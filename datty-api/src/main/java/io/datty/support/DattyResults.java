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

import io.datty.api.DattyResult;
import io.datty.api.SingleResult;
import io.datty.api.operation.BooleanResult;
import io.datty.api.operation.ErrorResult;
import io.datty.api.operation.ValueResult;
import io.datty.api.operation.VoidResult;
import io.datty.api.payload.DattyPayload;
import io.datty.api.payload.DattyPayloadType;
import io.datty.api.payload.SingleResultPayload;
import io.datty.api.payload.SingleResultType;
import io.datty.api.serializer.DattySerializer;
import io.datty.support.exception.DattyInstantiatorNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversion util class from results to payload
 * 
 * @author dadril
 *
 */

public final class DattyResults {

	private DattyResults() {
	}

	private final static Map<SingleResultType, SingleResult.SingleInstantiator> knownOperations = new HashMap<>();

	static {
		knownOperations.put(SingleResultType.VOID,
				VoidResult.Instantiator.INSTANCE);
		knownOperations.put(SingleResultType.BOOLEAN,
				BooleanResult.Instantiator.INSTANCE);
		knownOperations.put(SingleResultType.VALUE,
				ValueResult.Instantiator.INSTANCE);
		knownOperations.put(SingleResultType.ERROR,
				ErrorResult.Instantiator.INSTANCE);
	}

	public static byte[] toByteArray(DattyResult result,
			DattySerializer serializer) throws IOException {

		DattyPayload payloadInput = toPayload(result);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		serializer.serialize(payloadInput, outputStream);

		return outputStream.toByteArray();
	}

	public static DattyPayload toPayload(DattyResult result) {

		if (result instanceof SingleResult) {
			DattyPayload payload = new DattyPayload();
			payload.setType(DattyPayloadType.SR);
			SingleResult<?> single = (SingleResult<?>) result;
			single.writeTo(payload.addResult());
			return payload;
		} else {
			throw new IllegalArgumentException("unknown result type "
					+ result.getClass());
		}
	}

	public static DattyResult parseFrom(DattyPayload payload) {

		DattyPayloadType type = payload.getType();
		if (type == null) {
			throw new IllegalArgumentException("empty payload type in "
					+ payload);
		}

		switch (type) {

		case SR:
			return parseSingleResult(payload);

		default:
			throw new IllegalArgumentException("unsupported payload type "
					+ type);

		}

	}

	private static SingleResult<?> parseSingleResult(DattyPayload payload) {

		List<SingleResultPayload> res = payload.getResultList();

		if (res == null || res.size() != 1) {
			throw new IllegalArgumentException("invalid result list size");
		}

		return parseFrom(res.get(0));

	}

	public static SingleResult<?> parseFrom(SingleResultPayload payload) {

		SingleResultType type = payload.getType();
		if (type == null) {
			throw new IllegalArgumentException("empty payload type in "
					+ payload);
		}

		SingleResult.SingleInstantiator instantiator = knownOperations
				.get(type);

		if (instantiator == null) {
			throw new DattyInstantiatorNotFoundException(type.name());
		}

		return instantiator.parseFrom(payload);
	}

}
