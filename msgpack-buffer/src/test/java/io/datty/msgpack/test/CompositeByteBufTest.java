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
package io.datty.msgpack.test;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;


/**
 * CompositeByteBufTest
 * 
 * @author Alex Shvid
 *
 */

public class CompositeByteBufTest {

	@Test
	public void testTwoComponents() {
		
		ByteBuf first = Unpooled.wrappedBuffer("a".getBytes());
		ByteBuf second = Unpooled.wrappedBuffer("b".getBytes());
		
		CompositeByteBuf result = first.alloc().compositeBuffer();
		result.addComponent(true, first);
		result.addComponent(true, second);
		
		byte[] actual = ByteBufUtil.getBytes(result);
		
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals('a', actual[0]);
		Assert.assertEquals('b', actual[1]);
		
	}

	
	@Test
	public void testCompositeWrite() {
		
		ByteBuf first = Unpooled.buffer();
		first.writeByte('a');
		CompositeByteBuf result = first.alloc().compositeBuffer();
		result.addComponent(true, first);
		result.writeByte('b');

		byte[] actual = ByteBufUtil.getBytes(result);
		
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals('a', actual[0]);
		Assert.assertEquals('b', actual[1]);
		
	}
	
}
