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
package io.datty.unit.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;

import io.datty.api.DattySet;
import io.datty.api.SetExistsAction;
import io.datty.api.DattyFactory;
import io.datty.api.DattyManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * AbstractDattyUnitTest
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractDattyUnitTest {

	static final String FACTORY_NAME = System.getProperty("datty.factory");
	
	public static final String SET_NAME = "TestCache";
	
	static protected DattyFactory dattyFactory;
	
	static protected DattyManager dattyManager;
	
	static protected DattySet dattySet;
	
	static protected  final String minorKey = "def";
	
	static protected  final ByteBuf value = Unpooled.wrappedBuffer("value".getBytes());

	static protected  final ByteBuf newValue = Unpooled.wrappedBuffer("newValue".getBytes());

	static protected ByteBuf value() {
		return value.resetReaderIndex();
	}

	static protected ByteBuf newValue() {
		return newValue.resetReaderIndex();
	}
	
	@BeforeClass
	public static void init() {
		
		if (FACTORY_NAME != null) {
			dattyFactory = new DattyFactory.Locator().getByName(FACTORY_NAME);
		}
		else {
			dattyFactory = new DattyFactory.Locator().getSingle();
		}
		
		System.out.println("Init DattyFactory = " + dattyFactory.getFactoryName());
		
		dattyManager = dattyFactory.newDefaultInstance();
		
		dattySet = dattyManager.getSet(SET_NAME, new Properties(), SetExistsAction.CREATE_IF_NOT_EXISTS);
		
	}
	
	public static <T> List<T> toList(Iterable<T> iterable) {
		
		List<T> list = new ArrayList<>();
		
		for (T item : iterable) {
			list.add(item);
		}
		
		return list;
	}
	
}
