/*
 * Copyright (C) 2017 Datty.io Authors
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
package io.datty.msgpack.table.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import io.datty.msgpack.table.PackableValueExpression;
import io.datty.msgpack.table.util.PackableStringifyUtil;
import io.datty.msgpack.table.util.PackableStringifyUtil.NumberType;

/**
 * PackableValueExpressionImpl
 * 
 * @author Alex Shvid
 *
 */

public final class PackableValueExpressionImpl implements PackableValueExpression {

	private final List<String> path = new ArrayList<String>();

	public PackableValueExpressionImpl(String valueExpression) {
		
		if (valueExpression == null) {
			throw new IllegalArgumentException("empty valueExpression");
		}

		StringTokenizer tokenizer = new StringTokenizer(valueExpression, ".[]");
		
		while(tokenizer.hasMoreTokens()) {
			
			String token = tokenizer.nextToken().trim();
			
			if (!token.isEmpty()) {
				path.add(token);
			}
			
		}
		
	}

	public boolean isEmpty() {
		return path.isEmpty();
	}
	
	public int size() {
		return path.size();
	}
	
	public String get(int i) {
		return path.get(i);
	}
	
	public List<String> getPath() {
		return Collections.unmodifiableList(path);
	}

	public String asString() {
		StringBuilder str = new StringBuilder();
		for (String element : path) {
			NumberType numberType = PackableStringifyUtil.detectNumber(element);
			if (numberType == NumberType.LONG) {
				str.append("[").append(element).append("]");
			}
			else {
				if (str.length() > 0) {
					str.append(".");
				}
				str.append(element);
			}
		}
		return str.toString();
	}
	
	@Override
	public String toString() {
		return asString();
	}
	
}
