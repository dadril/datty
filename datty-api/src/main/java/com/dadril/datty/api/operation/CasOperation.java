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

import com.dadril.datty.api.DattyConstants;
import com.dadril.datty.api.payload.SingleOperationPayload;
import com.dadril.datty.api.payload.SingleOperationType;
import com.dadril.datty.support.VersionedValue;

/**
 * CasOperation
 * 
 * Compare And Set Operation
 * 
 * @author dadril
 *
 */

public class CasOperation extends AbstractOperation<CasOperation, BooleanResult> {

  private final Value oldValue;
  private final byte[] newValueOrNull;
  private int ttlSeconds = DattyConstants.UNSET_TTL;
  
	public CasOperation(String storeName, Value oldValue, byte[] newValueOrNull) {
		super(storeName);
		this.oldValue = oldValue;
    this.newValueOrNull = newValueOrNull;
	}
	
	public CasOperation(String storeName, String majorKey, Value oldValue, byte[] newValueOrNull) {
	  super(storeName);
	  setMajorKey(majorKey);
	  this.oldValue = oldValue;
	  this.newValueOrNull = newValueOrNull;
	}

  public Value getOldValue() {
    return oldValue;
  }

  public byte[] getNewValue() {
    return newValueOrNull;
  }
  
  public int getTtlSeconds() {
    return ttlSeconds;
  }

  public CasOperation setTtlSeconds(int ttlSeconds) {
    this.ttlSeconds = ttlSeconds;
    return this;
  }
  
  @Override
  public void writeTo(SingleOperationPayload op) {
    
    op.setType(SingleOperationType.CAS);
    op.setNewValue(newValueOrNull);
    op.setTtlSeconds(ttlSeconds);
    
    long version = oldValue.getVersion();
    if (version != DattyConstants.UNSET_VERSION) {
      op.setOldVersion(version);
    }
    else {
      op.setOldValue(oldValue.getBackingValue());
    }
    
    writeAbstractFields(op);
  }

  public enum Instantiator implements SingleInstantiator {
    
    INSTANCE;

    @Override
    public CasOperation parseFrom(SingleOperationPayload payload) {

      CasOperation op = new CasOperation(payload.getStoreName(), 
          VersionedValue.wrap(payload.getOldValue(), payload.getOldVersion()),
          payload.getNewValue());

      op.setSuperKey(payload.getSuperKey());
      op.setMajorKey(payload.getMajorKey());
      op.setMinorKey(payload.getMinorKey());
      op.setTtlSeconds(payload.getTtlSeconds());
      
      return op;
    }
    
  }
	
}
