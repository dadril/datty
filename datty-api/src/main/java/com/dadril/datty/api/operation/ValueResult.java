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

import com.dadril.datty.api.SingleResult;
import com.dadril.datty.api.payload.SingleResultPayload;
import com.dadril.datty.api.payload.SingleResultType;
import com.dadril.datty.support.VersionedValue;

/**
 * ValueResult
 * 
 * @author dadril
 *
 */

public final class ValueResult implements SingleResult<Value> {

  private final Value value;
  
  public ValueResult(Value value) {
    this.value = value;
  }
  
	@Override
	public Value get() {
		return value;
	}

  @Override
  public void writeTo(SingleResultPayload payload) {
    payload.setType(SingleResultType.VALUE);
    payload.setVersion(value.getVersion());
    payload.setBinaryValue(value.getBackingValue());
  }	
  
  public enum Instantiator implements SingleInstantiator {
    
    INSTANCE;

    @Override
    public ValueResult parseFrom(SingleResultPayload payload) {
      
      byte[] binaryValue = payload.getBinaryValue();
      long version = payload.getVersion();
      
      return new ValueResult(VersionedValue.wrap(binaryValue, version));
    }
    
  }
  
}
