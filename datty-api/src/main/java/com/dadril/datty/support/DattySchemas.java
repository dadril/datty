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

import com.dadril.datty.api.payload.DattyPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Datty schemas are using for serialization
 * 
 * @author dadril
 *
 */

public final class DattySchemas {

  private DattySchemas() {
    
  }
  
  public final static ObjectMapper JSON_MAPPER = new ObjectMapper();
  
  public final static IdStrategy JSON_STRATEGY = 
      new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | IdStrategy.ENUMS_BY_NAME, null, 0);

  
  public static final Schema<DattyPayload> PAYLOAD_SCHEMA = RuntimeSchema.getSchema(DattyPayload.class);

  public static final Schema<DattyPayload> PAYLOAD_JSON_SCHEMA = RuntimeSchema.getSchema(DattyPayload.class, JSON_STRATEGY);

  public static final int DEFAULT_LINKED_BUFFER_SIZE = 4096;
  
  private static int linkedBufferSize = DEFAULT_LINKED_BUFFER_SIZE;
  
  private static final ThreadLocal<LinkedBuffer> localLinkedBuffer = new ThreadLocal<LinkedBuffer>() {

    @Override
    protected LinkedBuffer initialValue() {
      return LinkedBuffer.allocate(linkedBufferSize);
    }

  };

  public static LinkedBuffer getLocalLinkedBuffer() {
    return localLinkedBuffer.get().clear();
  }

  public static void setLinkedBufferSize(int newSize) {
    if (newSize < 1 || newSize > 0xFFFFFF) {
      throw new IllegalArgumentException("invalid new size for linked buffer");
    }
    linkedBufferSize = newSize;
  }
  
}
