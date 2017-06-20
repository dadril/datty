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

import java.util.List;

import io.datty.spring.mapping.Entity;
import io.netty.buffer.ByteBuf;

/**
 * AllListsEntity
 * 
 * @author Alex Shvid
 *
 */

@Entity(setName = "TEST_CACHE")
public class AllListsEntity {

	private List<Boolean> booleanVal;
	private List<Byte> byteVal;
	private List<Short> shortVal;
	private List<Integer> intVal;
	private List<Long> longVal;
	private List<Float> floatVal;
	private List<Double> doubleVal;
	private List<String> stringVal;
	private List<ByteBuf> bbVal;

	public List<Boolean> getBooleanVal() {
		return booleanVal;
	}

	public void setBooleanVal(List<Boolean> booleanVal) {
		this.booleanVal = booleanVal;
	}

	public List<Byte> getByteVal() {
		return byteVal;
	}

	public void setByteVal(List<Byte> byteVal) {
		this.byteVal = byteVal;
	}

	public List<Short> getShortVal() {
		return shortVal;
	}

	public void setShortVal(List<Short> shortVal) {
		this.shortVal = shortVal;
	}

	public List<Integer> getIntVal() {
		return intVal;
	}

	public void setIntVal(List<Integer> intVal) {
		this.intVal = intVal;
	}

	public List<Long> getLongVal() {
		return longVal;
	}

	public void setLongVal(List<Long> longVal) {
		this.longVal = longVal;
	}

	public List<Float> getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(List<Float> floatVal) {
		this.floatVal = floatVal;
	}

	public List<Double> getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(List<Double> doubleVal) {
		this.doubleVal = doubleVal;
	}

	public List<String> getStringVal() {
		return stringVal;
	}

	public void setStringVal(List<String> stringVal) {
		this.stringVal = stringVal;
	}

	public List<ByteBuf> getBbVal() {
		return bbVal;
	}

	public void setBbVal(List<ByteBuf> bbVal) {
		this.bbVal = bbVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbVal == null) ? 0 : bbVal.hashCode());
		result = prime * result + ((booleanVal == null) ? 0 : booleanVal.hashCode());
		result = prime * result + ((byteVal == null) ? 0 : byteVal.hashCode());
		result = prime * result + ((doubleVal == null) ? 0 : doubleVal.hashCode());
		result = prime * result + ((floatVal == null) ? 0 : floatVal.hashCode());
		result = prime * result + ((intVal == null) ? 0 : intVal.hashCode());
		result = prime * result + ((longVal == null) ? 0 : longVal.hashCode());
		result = prime * result + ((shortVal == null) ? 0 : shortVal.hashCode());
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
		AllListsEntity other = (AllListsEntity) obj;
		if (bbVal == null) {
			if (other.bbVal != null)
				return false;
		} else if (!bbVal.equals(other.bbVal))
			return false;
		if (booleanVal == null) {
			if (other.booleanVal != null)
				return false;
		} else if (!booleanVal.equals(other.booleanVal))
			return false;
		if (byteVal == null) {
			if (other.byteVal != null)
				return false;
		} else if (!byteVal.equals(other.byteVal))
			return false;
		if (doubleVal == null) {
			if (other.doubleVal != null)
				return false;
		} else if (!doubleVal.equals(other.doubleVal))
			return false;
		if (floatVal == null) {
			if (other.floatVal != null)
				return false;
		} else if (!floatVal.equals(other.floatVal))
			return false;
		if (intVal == null) {
			if (other.intVal != null)
				return false;
		} else if (!intVal.equals(other.intVal))
			return false;
		if (longVal == null) {
			if (other.longVal != null)
				return false;
		} else if (!longVal.equals(other.longVal))
			return false;
		if (shortVal == null) {
			if (other.shortVal != null)
				return false;
		} else if (!shortVal.equals(other.shortVal))
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
		return "AllListsEntity [booleanVal=" + booleanVal + ", byteVal=" + byteVal + ", shortVal=" + shortVal + ", intVal="
				+ intVal + ", longVal=" + longVal + ", floatVal=" + floatVal + ", doubleVal=" + doubleVal + ", stringVal="
				+ stringVal + ", bbVal=" + bbVal + "]";
	}

	
	
}
