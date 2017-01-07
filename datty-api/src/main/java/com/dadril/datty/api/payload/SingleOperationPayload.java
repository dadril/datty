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
package com.dadril.datty.api.payload;

import java.io.IOException;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import com.dadril.datty.api.DattyConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.protostuff.Tag;

@JsonInclude(Include.NON_DEFAULT)
public class SingleOperationPayload {
  
  @Tag(1)
  private SingleOperationType type;
  
  @Tag(2)
  private String storeName;
  
  @Tag(3)
  private String superKey;
  
  @Tag(4)
  private String majorKey;
  
  @Tag(5)
  private String minorKey;

  @Tag(6)
  private long oldVersion;

  @Tag(7)
  private byte[] oldValue;
  
  @Tag(8)
  private byte[] newValue;
  
  @Tag(9)
  private int ttlSeconds;
  
  public void reset() {
    this.type = null;
    this.storeName = null;
    this.superKey = null;
    this.majorKey = null;
    this.minorKey = null;
    this.oldVersion = DattyConstants.UNSET_VERSION;
    this.oldValue = null;
    this.newValue = null;
    this.ttlSeconds = DattyConstants.UNSET_TTL;
  }
  
  public SingleOperationType getType() {
    return type;
  }

  public void setType(SingleOperationType type) {
    this.type = type;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public String getSuperKey() {
    return superKey;
  }

  public void setSuperKey(String superKey) {
    this.superKey = superKey;
  }

  public String getMajorKey() {
    return majorKey;
  }

  public void setMajorKey(String majorKey) {
    this.majorKey = majorKey;
  }

  public String getMinorKey() {
    return minorKey;
  }

  public void setMinorKey(String minorKey) {
    this.minorKey = minorKey;
  }
  
  public long getOldVersion() {
    return oldVersion;
  }

  public void setOldVersion(long oldVersion) {
    this.oldVersion = oldVersion;
  }

  public byte[] getOldValue() {
    return oldValue;
  }

  public void setOldValue(byte[] oldValue) {
    this.oldValue = oldValue;
  }

  public byte[] getNewValue() {
    return newValue;
  }

  public void setNewValue(byte[] newValue) {
    this.newValue = newValue;
  }

  public int getTtlSeconds() {
    return ttlSeconds;
  }

  public void setTtlSeconds(int ttlSeconds) {
    this.ttlSeconds = ttlSeconds;
  }
  
  /**
   * MessagePack
   */
  
  public void pack(MessagePacker packer) throws IOException {
    
    int size = 0;
    
    if (type != null) {
      size++;
    }
    
    if (storeName != null) {
      size++;
    }
    
    if (superKey != null) {
      size++;
    }
    
    if (majorKey != null) {
      size++;
    }

    if (minorKey != null) {
      size++;
    }
    
    if (oldVersion != DattyConstants.UNSET_VERSION) {
      size++;
    }

    if (oldValue != null) {
      size++;
    }
  
    if (newValue != null) {
      size++;
    }
    
    if (ttlSeconds != DattyConstants.UNSET_TTL) {
      size++;
    }
    
    packer.packMapHeader(size);
    
    if (type != null) {
      packer.packInt(1);
      packer.packString(type.name());
    }
    
    if (storeName != null) {
      packer.packInt(2);
      packer.packString(storeName);
    }
    
    if (superKey != null) {
      packer.packInt(3);
      packer.packString(superKey);
    }
    
    if (majorKey != null) {
      packer.packInt(4);
      packer.packString(majorKey);
    }

    if (minorKey != null) {
      packer.packInt(5);
      packer.packString(minorKey);
    }
    
    if (oldVersion != DattyConstants.UNSET_VERSION) {
      packer.packInt(6);
      packer.packLong(oldVersion);
    }

    if (oldValue != null) {
      packer.packInt(7);
      packer.packBinaryHeader(oldValue.length);
      packer.addPayload(oldValue);
    }
  
    if (newValue != null) {
      packer.packInt(8);
      packer.packBinaryHeader(newValue.length);
      packer.addPayload(newValue);
    }
    
    if (ttlSeconds != DattyConstants.UNSET_TTL) {
      packer.packInt(9);
      packer.packLong(ttlSeconds);
    }
    
  }
  
  public void unpack(MessageUnpacker unpacker) throws IOException {
    
    int size = unpacker.unpackMapHeader();
    
    for (int k = 0; k != size; ++k) {
    
      int tag = unpacker.unpackInt();
      
      switch(tag) {
        
        case 1:
          this.type = SingleOperationType.valueOf(unpacker.unpackString());
          break;

        case 2:
          this.storeName = unpacker.unpackString();
          break;
          
        case 3:
          this.superKey = unpacker.unpackString();
          break;   
          
        case 4:
          this.majorKey = unpacker.unpackString();
          break;            

        case 5:
          this.minorKey = unpacker.unpackString();
          break;
          
        case 6:
          this.oldVersion = unpacker.unpackLong();
          break;     
          
        case 7:
          this.oldValue = unpacker.readPayload(unpacker.unpackBinaryHeader());
          break;    
          
        case 8:
          this.newValue = unpacker.readPayload(unpacker.unpackBinaryHeader());
          break;  
          
        case 9:
          this.ttlSeconds = unpacker.unpackInt();
          break;   
          
        default:
          unpacker.skipValue();
          break;          
      }
    
    } 
    
  }
  
}
