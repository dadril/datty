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
package io.datty.aerospike;

import java.util.Arrays;
import java.util.Map;

import com.aerospike.client.Bin;

import io.datty.aerospike.support.AerospikeValueUtil;
import io.datty.api.DattyValue;
import io.netty.buffer.ByteBuf;

/**
 * AerospikeBins
 * 
 * @author Alex Shvid
 *
 */

public final class AerospikeBins {

	private final Bin[] bins;
	private final long writtableBytes;
	
	public AerospikeBins(String minorKey, ByteBuf value) {
		this.writtableBytes = value.readableBytes();
		Bin bin = new Bin(minorKey, AerospikeValueUtil.toValue(value));
		this.bins = new Bin[] { bin };
	}
	
	public AerospikeBins(Map<String, DattyValue> values) {
		
		Bin[] localBins = new Bin[values.size()];
		long localBytes = 0l;
		
		int i = 0;
		for (Map.Entry<String, DattyValue> entry : values.entrySet()) {
			
			String binName = entry.getKey();
			DattyValue value = entry.getValue();
			
			if (!value.isEmpty()) {
				byte[] blob = value.toByteArray();
				if (blob != null) {
					localBytes += blob.length;
					localBins[i++] = new Bin(binName, blob);
				}
			}
			
		}
		
		if (i != values.size()) {
			localBins = Arrays.copyOf(localBins, i);
		}
		
		this.bins = localBins;
		this.writtableBytes = localBytes;
		
	}

	public Bin[] getBins() {
		return bins;
	}

	public long getWrittableBytes() {
		return writtableBytes;
	}

	public boolean isEmpty() {
		return bins.length == 0;
	}
	
	@Override
	public String toString() {
		return "AerospikeBins [bins=" + Arrays.toString(bins) + ", writtableBytes=" + writtableBytes + "]";
	}
	
	
	
}
