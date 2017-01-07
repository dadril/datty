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
package com.dadril.datty.api.operation;

import com.dadril.datty.api.payload.SingleOperationPayload;
import com.dadril.datty.api.payload.SingleOperationType;

/**
 * Get operation
 * 
 * @author dadril
 *
 */

public class GetOperation extends AbstractOperation<GetOperation, ValueResult> {

  public GetOperation(String storeName) {
    super(storeName);
  }

  public GetOperation(String storeName, String majorKey) {
    super(storeName);
    setMajorKey(majorKey);
  }

  @Override
  public void writeTo(SingleOperationPayload op) {
    op.setType(SingleOperationType.GET);
    writeAbstractFields(op);
  }

  public enum Instantiator implements SingleInstantiator {
    
    INSTANCE;

    @Override
    public GetOperation parseFrom(SingleOperationPayload payload) {

      GetOperation op = new GetOperation(payload.getStoreName());

      op.setSuperKey(payload.getSuperKey());
      op.setMajorKey(payload.getMajorKey());
      op.setMinorKey(payload.getMinorKey());
      
      return op;
    }
    
  }
  
}
