package io.datty.api.serializer;

import io.datty.api.operation.GetOperation;
import io.datty.api.operation.ValueResult;
import io.datty.api.payload.DattyPayload;
import io.datty.io.DattyIOUtil;
import io.datty.support.DattyOperations;
import io.datty.support.VersionedValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;

public abstract class AbstractSerializerTest {

	public static final int DEFAULT_ITERS = 1000000;

	private static final byte[] VALUE = "VALUE".getBytes();

	protected void compareGet(GetOperation getOp, GetOperation actual) {

		Assert.assertEquals(getOp.getStoreName(), actual.getStoreName());
		Assert.assertEquals(getOp.getMajorKey(), actual.getMajorKey());
		Assert.assertNull(getOp.getSuperKey());
		Assert.assertNull(getOp.getMinorKey());

	}

	protected void compareResult(ValueResult expected, ValueResult actual) {

		Assert.assertTrue(Arrays.equals(expected.get().getBackingValue(),
				actual.get().getBackingValue()));
		Assert.assertEquals(expected.get().getVersion(), actual.get()
				.getVersion());

	}

	protected void testSingle(DattySerializer serializer) throws IOException {

		/**
		 * Operation
		 */

		GetOperation getOp = new GetOperation("TEST", "key1");

		byte[] blob = DattyIOUtil.toByteArray(getOp, serializer);

		GetOperation actual = (GetOperation) DattyIOUtil.parseOperation(blob,
				serializer);

		compareGet(getOp, actual);

		/**
		 * Result
		 */

		ValueResult result = new ValueResult(VersionedValue.wrap(VALUE, 123L));

		blob = DattyIOUtil.toByteArray(result, serializer);

		ValueResult actualRes = (ValueResult) DattyIOUtil.parseResult(blob,
				serializer);

		compareResult(result, actualRes);
	}

	protected long testSerializePerformace(DattySerializer serializer,
			int iterations) throws IOException {

		GetOperation getOp = new GetOperation("TEST", "key1");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		serializer.serialize(DattyOperations.toPayload(getOp), bout);

		long t0 = System.currentTimeMillis();

		for (int i = 0; i != iterations; ++i) {

			bout.reset();
			serializer.serialize(DattyOperations.toPayload(getOp), bout);
		}

		return System.currentTimeMillis() - t0;
	}

	protected long testDeserializePerformace(DattySerializer serializer,
			int iterations) throws IOException {

		GetOperation getOp = new GetOperation("TEST", "key1");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		serializer.serialize(DattyOperations.toPayload(getOp), bout);
		byte[] blob = bout.toByteArray();
		ByteArrayInputStream bin = new ByteArrayInputStream(blob);
		DattyPayload payload = new DattyPayload();

		long t0 = System.currentTimeMillis();

		for (int i = 0; i != iterations; ++i) {

			bin.reset();
			payload.reset();
			serializer.deserialize(bin, payload);
		}

		return System.currentTimeMillis() - t0;
	}
}
