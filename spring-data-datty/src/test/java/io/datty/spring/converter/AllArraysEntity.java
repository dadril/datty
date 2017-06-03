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

import java.util.Arrays;

import io.datty.spring.mapping.Entity;
import io.netty.buffer.ByteBuf;

/**
 * AllArraysEntity
 * 
 * @author Alex Shvid
 *
 */

@Entity(setName = "TEST_CACHE", minorKey = "def")
public class AllArraysEntity {

	private boolean[] booleanVal;
	private Boolean[] booleanWal;
	private byte[] byteVal;
	private Byte[] byteWal;
	private short[] shortVal;
	private Short[] shortWal;
	private int[] intVal;
	private Integer[] intWal;
	private long[] longVal;
	private Long[] longWal;
	private float[] floatVal;
	private Float[] floatWal;
	private double[] doubleVal;
	private Double[] doubleWal;
	private String[] stringVal;
	private ByteBuf[] bbVal;

	public boolean[] getBooleanVal() {
		return booleanVal;
	}

	public void setBooleanVal(boolean[] booleanVal) {
		this.booleanVal = booleanVal;
	}

	public Boolean[] getBooleanWal() {
		return booleanWal;
	}

	public void setBooleanWal(Boolean[] booleanWal) {
		this.booleanWal = booleanWal;
	}

	public byte[] getByteVal() {
		return byteVal;
	}

	public void setByteVal(byte[] byteVal) {
		this.byteVal = byteVal;
	}

	public Byte[] getByteWal() {
		return byteWal;
	}

	public void setByteWal(Byte[] byteWal) {
		this.byteWal = byteWal;
	}

	public short[] getShortVal() {
		return shortVal;
	}

	public void setShortVal(short[] shortVal) {
		this.shortVal = shortVal;
	}

	public Short[] getShortWal() {
		return shortWal;
	}

	public void setShortWal(Short[] shortWal) {
		this.shortWal = shortWal;
	}

	public int[] getIntVal() {
		return intVal;
	}

	public void setIntVal(int[] intVal) {
		this.intVal = intVal;
	}

	public Integer[] getIntWal() {
		return intWal;
	}

	public void setIntWal(Integer[] intWal) {
		this.intWal = intWal;
	}

	public long[] getLongVal() {
		return longVal;
	}

	public void setLongVal(long[] longVal) {
		this.longVal = longVal;
	}

	public Long[] getLongWal() {
		return longWal;
	}

	public void setLongWal(Long[] longWal) {
		this.longWal = longWal;
	}

	public float[] getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(float[] floatVal) {
		this.floatVal = floatVal;
	}

	public Float[] getFloatWal() {
		return floatWal;
	}

	public void setFloatWal(Float[] floatWal) {
		this.floatWal = floatWal;
	}

	public double[] getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(double[] doubleVal) {
		this.doubleVal = doubleVal;
	}

	public Double[] getDoubleWal() {
		return doubleWal;
	}

	public void setDoubleWal(Double[] doubleWal) {
		this.doubleWal = doubleWal;
	}

	public String[] getStringVal() {
		return stringVal;
	}

	public void setStringVal(String[] stringVal) {
		this.stringVal = stringVal;
	}

	public ByteBuf[] getBbVal() {
		return bbVal;
	}

	public void setBbVal(ByteBuf[] bbVal) {
		this.bbVal = bbVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bbVal);
		result = prime * result + Arrays.hashCode(booleanVal);
		result = prime * result + Arrays.hashCode(booleanWal);
		result = prime * result + Arrays.hashCode(byteVal);
		result = prime * result + Arrays.hashCode(byteWal);
		result = prime * result + Arrays.hashCode(doubleVal);
		result = prime * result + Arrays.hashCode(doubleWal);
		result = prime * result + Arrays.hashCode(floatVal);
		result = prime * result + Arrays.hashCode(floatWal);
		result = prime * result + Arrays.hashCode(intVal);
		result = prime * result + Arrays.hashCode(intWal);
		result = prime * result + Arrays.hashCode(longVal);
		result = prime * result + Arrays.hashCode(longWal);
		result = prime * result + Arrays.hashCode(shortVal);
		result = prime * result + Arrays.hashCode(shortWal);
		result = prime * result + Arrays.hashCode(stringVal);
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
		AllArraysEntity other = (AllArraysEntity) obj;
		if (!Arrays.equals(bbVal, other.bbVal))
			return false;
		if (!Arrays.equals(booleanVal, other.booleanVal))
			return false;
		if (!Arrays.equals(booleanWal, other.booleanWal))
			return false;
		if (!Arrays.equals(byteVal, other.byteVal))
			return false;
		if (!Arrays.equals(byteWal, other.byteWal))
			return false;
		if (!Arrays.equals(doubleVal, other.doubleVal))
			return false;
		if (!Arrays.equals(doubleWal, other.doubleWal))
			return false;
		if (!Arrays.equals(floatVal, other.floatVal))
			return false;
		if (!Arrays.equals(floatWal, other.floatWal))
			return false;
		if (!Arrays.equals(intVal, other.intVal))
			return false;
		if (!Arrays.equals(intWal, other.intWal))
			return false;
		if (!Arrays.equals(longVal, other.longVal))
			return false;
		if (!Arrays.equals(longWal, other.longWal))
			return false;
		if (!Arrays.equals(shortVal, other.shortVal))
			return false;
		if (!Arrays.equals(shortWal, other.shortWal))
			return false;
		if (!Arrays.equals(stringVal, other.stringVal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AllArraysEntity [booleanVal=" + Arrays.toString(booleanVal) + ", booleanWal=" + Arrays.toString(booleanWal)
				+ ", byteVal=" + Arrays.toString(byteVal) + ", byteWal=" + Arrays.toString(byteWal) + ", shortVal="
				+ Arrays.toString(shortVal) + ", shortWal=" + Arrays.toString(shortWal) + ", intVal=" + Arrays.toString(intVal)
				+ ", intWal=" + Arrays.toString(intWal) + ", longVal=" + Arrays.toString(longVal) + ", longWal="
				+ Arrays.toString(longWal) + ", floatVal=" + Arrays.toString(floatVal) + ", floatWal="
				+ Arrays.toString(floatWal) + ", doubleVal=" + Arrays.toString(doubleVal) + ", doubleWal="
				+ Arrays.toString(doubleWal) + ", stringVal=" + Arrays.toString(stringVal) + ", bbVal=" + Arrays.toString(bbVal)
				+ "]";
	}
	
	

}
