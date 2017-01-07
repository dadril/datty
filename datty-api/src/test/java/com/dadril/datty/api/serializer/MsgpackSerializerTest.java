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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.dadril.datty.api.operation.GetOperation;
import com.dadril.datty.support.DattyOperations;

public class MsgpackSerializerTest extends AbstractSerializerTest {

  @Test
  public void testOutput() throws IOException {
    
    GetOperation getOp = new GetOperation("TEST", "key1");
    
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    MsgpackSerializer.INSTANCE.serialize(DattyOperations.toPayload(getOp), bout);
    
    byte[] blob = bout.toByteArray();

    System.out.println(Arrays.toString(blob));
    
  }
  
  @Test
  public void testSingle() throws IOException {
    
    testSingle(MsgpackSerializer.INSTANCE);
    
  }

  
  @Test
  public void testBatch() throws IOException {
    
    testBatch(MsgpackSerializer.INSTANCE);
    
  }
  
  @Test
  public void testPerformace() throws IOException {
    
    long ser = testSerializePerformace(MsgpackSerializer.INSTANCE, DEFAULT_ITERS);
    long deser = testDeserializePerformace(MsgpackSerializer.INSTANCE, DEFAULT_ITERS);
    System.out.println("MsgpackSerializer performance " + ser + "/" + deser);
    
  }
  
}
