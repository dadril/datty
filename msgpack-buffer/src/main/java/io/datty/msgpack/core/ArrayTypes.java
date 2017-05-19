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
package io.datty.msgpack.core;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;

/**
 * ArrayTypes
 * 
 * @author Alex Shvid
 *
 */

public final class ArrayTypes {

	private final static Map<Class<?>, Class<?>> arrayToElement = new HashMap<>();
	
	static {
		
		arrayToElement.put(boolean[].class, boolean.class);
		arrayToElement.put(Boolean[].class, Boolean.class);
		arrayToElement.put(byte[].class, byte.class);
		arrayToElement.put(Byte[].class, Byte.class);
		arrayToElement.put(short[].class, short.class);
		arrayToElement.put(Short[].class, Short.class);
		arrayToElement.put(int[].class, int.class);
		arrayToElement.put(Integer[].class, Integer.class);
		arrayToElement.put(long[].class, long.class);
		arrayToElement.put(Long[].class, Long.class);
		arrayToElement.put(float[].class, float.class);
		arrayToElement.put(Float[].class, Float.class);
		arrayToElement.put(double[].class, double.class);
		arrayToElement.put(Double[].class, Double.class);
		arrayToElement.put(String[].class, String.class);
		arrayToElement.put(ByteBuf[].class, ByteBuf.class);
		
	}
	
	
	private ArrayTypes() {
	}
	
	public static Class<?> findElementType(Class<?> arrayType) {
/*		Class<?> cacheClass = arrayToElement.get(arrayType);
		return cacheClass != null ? cacheClass : arrayType.getComponentType();*/
		return arrayType.getComponentType();
	}
	
	
}
