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
package io.datty.spring.converter;

import io.datty.spring.mapping.Entity;
import io.netty.buffer.ByteBuf;

/**
 * AllTypesEntity
 * 
 * @author Alex Shvid
 *
 */

@Entity(cacheName = "TEST_CACHE", minorKey = "def")
public class AllTypesEntity {

	private boolean booleanVal;
	private Boolean booleanWal;
	private byte byteVal;
	private Byte byteWal;
	private short shortVal;
	private Short shortWal;
	private int intVal;
	private Integer intWal;
	private long longVal;
	private Long longWal;
	private float floatVal;
	private Float floatWal;
	private double doubleVal;
	private Double doubleWal;
	private String stringVal;
	private ByteBuf bbVal;

	public boolean isBooleanVal() {
		return booleanVal;
	}

	public void setBooleanVal(boolean booleanVal) {
		this.booleanVal = booleanVal;
	}

	public Boolean getBooleanWal() {
		return booleanWal;
	}

	public void setBooleanWal(Boolean booleanWal) {
		this.booleanWal = booleanWal;
	}

	public byte getByteVal() {
		return byteVal;
	}

	public void setByteVal(byte byteVal) {
		this.byteVal = byteVal;
	}

	public Byte getByteWal() {
		return byteWal;
	}

	public void setByteWal(Byte byteWal) {
		this.byteWal = byteWal;
	}

	public short getShortVal() {
		return shortVal;
	}

	public void setShortVal(short shortVal) {
		this.shortVal = shortVal;
	}

	public Short getShortWal() {
		return shortWal;
	}

	public void setShortWal(Short shortWal) {
		this.shortWal = shortWal;
	}

	public int getIntVal() {
		return intVal;
	}

	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}

	public Integer getIntWal() {
		return intWal;
	}

	public void setIntWal(Integer intWal) {
		this.intWal = intWal;
	}

	public long getLongVal() {
		return longVal;
	}

	public void setLongVal(long longVal) {
		this.longVal = longVal;
	}

	public Long getLongWal() {
		return longWal;
	}

	public void setLongWal(Long longWal) {
		this.longWal = longWal;
	}

	public float getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(float floatVal) {
		this.floatVal = floatVal;
	}

	public Float getFloatWal() {
		return floatWal;
	}

	public void setFloatWal(Float floatWal) {
		this.floatWal = floatWal;
	}

	public double getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public Double getDoubleWal() {
		return doubleWal;
	}

	public void setDoubleWal(Double doubleWal) {
		this.doubleWal = doubleWal;
	}

	public String getStringVal() {
		return stringVal;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	public ByteBuf getBbVal() {
		return bbVal;
	}

	public void setBbVal(ByteBuf bbVal) {
		this.bbVal = bbVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbVal == null) ? 0 : bbVal.hashCode());
		result = prime * result + (booleanVal ? 1231 : 1237);
		result = prime * result + ((booleanWal == null) ? 0 : booleanWal.hashCode());
		result = prime * result + byteVal;
		result = prime * result + ((byteWal == null) ? 0 : byteWal.hashCode());
		long temp;
		temp = Double.doubleToLongBits(doubleVal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((doubleWal == null) ? 0 : doubleWal.hashCode());
		result = prime * result + Float.floatToIntBits(floatVal);
		result = prime * result + ((floatWal == null) ? 0 : floatWal.hashCode());
		result = prime * result + intVal;
		result = prime * result + ((intWal == null) ? 0 : intWal.hashCode());
		result = prime * result + (int) (longVal ^ (longVal >>> 32));
		result = prime * result + ((longWal == null) ? 0 : longWal.hashCode());
		result = prime * result + shortVal;
		result = prime * result + ((shortWal == null) ? 0 : shortWal.hashCode());
		result = prime * result + ((stringVal == null) ? 0 : stringVal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllTypesEntity other = (AllTypesEntity) obj;
		if (bbVal == null) {
			if (other.bbVal != null)
				return false;
		} else if (!bbVal.equals(other.bbVal))
			return false;
		if (booleanVal != other.booleanVal)
			return false;
		if (booleanWal == null) {
			if (other.booleanWal != null)
				return false;
		} else if (!booleanWal.equals(other.booleanWal))
			return false;
		if (byteVal != other.byteVal)
			return false;
		if (byteWal == null) {
			if (other.byteWal != null)
				return false;
		} else if (!byteWal.equals(other.byteWal))
			return false;
		if (Double.doubleToLongBits(doubleVal) != Double.doubleToLongBits(other.doubleVal))
			return false;
		if (doubleWal == null) {
			if (other.doubleWal != null)
				return false;
		} else if (!doubleWal.equals(other.doubleWal))
			return false;
		if (Float.floatToIntBits(floatVal) != Float.floatToIntBits(other.floatVal))
			return false;
		if (floatWal == null) {
			if (other.floatWal != null)
				return false;
		} else if (!floatWal.equals(other.floatWal))
			return false;
		if (intVal != other.intVal)
			return false;
		if (intWal == null) {
			if (other.intWal != null)
				return false;
		} else if (!intWal.equals(other.intWal))
			return false;
		if (longVal != other.longVal)
			return false;
		if (longWal == null) {
			if (other.longWal != null)
				return false;
		} else if (!longWal.equals(other.longWal))
			return false;
		if (shortVal != other.shortVal)
			return false;
		if (shortWal == null) {
			if (other.shortWal != null)
				return false;
		} else if (!shortWal.equals(other.shortWal))
			return false;
		if (stringVal == null) {
			if (other.stringVal != null)
				return false;
		} else if (!stringVal.equals(other.stringVal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AllTypesEntity [booleanVal=" + booleanVal + ", booleanWal=" + booleanWal + ", byteVal=" + byteVal
				+ ", byteWal=" + byteWal + ", shortVal=" + shortVal + ", shortWal=" + shortWal + ", intVal=" + intVal
				+ ", intWal=" + intWal + ", longVal=" + longVal + ", longWal=" + longWal + ", floatVal=" + floatVal
				+ ", floatWal=" + floatWal + ", doubleVal=" + doubleVal + ", doubleWal=" + doubleWal + ", stringVal="
				+ stringVal + ", bbVal=" + bbVal + "]";
	}

	
	
}
