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
package io.datty.aerospike.support;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * RandomUtil
 * 
 * @author Alex Shvid
 *
 */

public final class RandomUtil {

	private static final SecureRandom RANDOM = new SecureRandom(BigInteger.valueOf(System.nanoTime()).toByteArray());
	
	private RandomUtil() {
	}

	public static SecureRandom getRandom() {
		return RANDOM;
	}
	
	public static <T> T selectRandom(T[] array) {
		int size = array.length;
		if (size == 0) {
			throw new IllegalArgumentException("empty array");
		}
		int idx = nextPositiveInt() % size;
		return array[idx];
	}
	
	public static int nextPositiveInt() {
		return Math.abs(RANDOM.nextInt());
	}
	
}
