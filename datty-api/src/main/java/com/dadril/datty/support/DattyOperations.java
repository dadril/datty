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
package com.dadril.datty.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dadril.datty.api.DattyOperation;
import com.dadril.datty.api.SingleOperation;
import com.dadril.datty.api.operation.BatchOperation;
import com.dadril.datty.api.operation.CasOperation;
import com.dadril.datty.api.operation.ExistOperation;
import com.dadril.datty.api.operation.GetOperation;
import com.dadril.datty.api.operation.SetOperation;
import com.dadril.datty.api.payload.DattyPayload;
import com.dadril.datty.api.payload.DattyPayloadType;
import com.dadril.datty.api.payload.SingleOperationPayload;
import com.dadril.datty.api.payload.SingleOperationType;
import com.dadril.datty.api.serializer.DattySerializer;
import com.dadril.datty.support.exception.InstantiatorNotFoundException;

/**
 * Conversion util class from operation to payload
 * 
 * @author dadril
 *
 */

public final class DattyOperations {

  private final static Map<SingleOperationType, SingleOperation.SingleInstantiator> knownOperations = new HashMap<>();
  
  static {
    knownOperations.put(SingleOperationType.EXIST, ExistOperation.Instantiator.INSTANCE);
    knownOperations.put(SingleOperationType.GET, GetOperation.Instantiator.INSTANCE);
    knownOperations.put(SingleOperationType.SET, SetOperation.Instantiator.INSTANCE);
    knownOperations.put(SingleOperationType.CAS, CasOperation.Instantiator.INSTANCE);
  }
  
  private DattyOperations() {
  }
  
  public static byte[] toByteArray(DattyOperation operation, DattySerializer serializer) throws IOException {
    
    DattyPayload payloadInput = toPayload(operation);
    
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    serializer.serialize(payloadInput, outputStream);
    
    return outputStream.toByteArray();
  }
  
  public static DattyOperation parseOperation(byte[] binary, DattySerializer serializer) throws IOException {

    ByteArrayInputStream bois = new ByteArrayInputStream(binary);
    
    DattyPayload payload = new DattyPayload();
    
    serializer.deserialize(bois, payload);
    
    return parseFrom(payload);
    
  }
  
  public static DattyPayload toPayload(DattyOperation operation) {
    
    if (operation instanceof SingleOperation) {
      DattyPayload payload = new DattyPayload();
      payload.setType(DattyPayloadType.SO);
      SingleOperation<?> single = (SingleOperation<?>) operation;
      single.writeTo(payload.addOperation());
      return payload;
    }
    else if (operation instanceof BatchOperation) {
      DattyPayload payload = new DattyPayload();
      payload.setType(DattyPayloadType.BO);
      BatchOperation batch = (BatchOperation) operation;
      for (SingleOperation<?> single : batch.getList()) {
        single.writeTo(payload.addOperation());
      }
      return payload;
    }
    else {
      throw new IllegalArgumentException("unknown operaiton type " + operation.getClass());
    }
  }
  
  public static DattyOperation parseFrom(DattyPayload payload) {
    
    DattyPayloadType type = payload.getType();
    if (type == null) {
      throw new IllegalArgumentException("empty payload type in " + payload);
    }
    
    switch(type) {
      
      case SO:
        return parseSingleOperation(payload);
        
      case BO:
        return parseBatchOperation(payload);
        
      default:
        throw new IllegalArgumentException("unsupported payload type " + type);
      
    }
    
  }
  
  private static BatchOperation parseBatchOperation(DattyPayload payload) {
    
    BatchOperation batch = new BatchOperation();

    List<SingleOperationPayload> ops = payload.getOperationList();
    if (ops != null) {
      for (SingleOperationPayload single : ops) {
        
        batch.add(parseFrom(single));
      }
    }
    
    return batch;
    
  }
  
  private static SingleOperation<?> parseSingleOperation(DattyPayload payload) {
    
    List<SingleOperationPayload> ops = payload.getOperationList();
    
    if (ops == null || ops.size() != 1) {
      throw new IllegalArgumentException("invalid operation list size");
    }
    
    return parseFrom(ops.get(0));
    
  }
  
  public static SingleOperation<?> parseFrom(SingleOperationPayload payload) {
    
    SingleOperationType type = payload.getType();
    if (type == null) {
      throw new IllegalArgumentException("empty payload type in " + payload);
    }
    
    SingleOperation.SingleInstantiator instantiator = knownOperations.get(type);
    
    if (instantiator == null) {
      throw new InstantiatorNotFoundException(type.name());
    }
    
    return instantiator.parseFrom(payload);
  }
  
}
