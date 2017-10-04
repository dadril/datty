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
package io.datty.spring.mapping;

import org.springframework.data.mapping.PersistentProperty;

import io.datty.msgpack.core.type.TypeInfo;
import io.datty.msgpack.core.type.TypeInfoProvider;

/**
 * DattyPersistentProperty
 * 
 * @author Alex Shvid
 *
 */

public interface DattyPersistentProperty extends PersistentProperty<DattyPersistentProperty> {

	/**
	 * Gets primary property name
	 * 
	 * @return not null name
	 */
	
	String getPrimaryName();
	
	/**
	 * Gets other property names
	 * 
	 * @return not null array of names
	 */
	
	String[] getOtherNames();
	
	/**
	 * Gets property tag number
	 * 
	 * @return not null tag number
	 */
	
	int getCode();
	
	/**
	 * Get copy flag
	 * 
	 * @return true if copy all bytes for reads and writes
	 */
	
	boolean copy();
	
	/**
	 * Returns the true if the field is embedded type.
	 * 
	 * @return
	 */
	boolean isEmbeddedType();
	
	/**
	 * Returns type info of the property
	 * 
	 * @param provider - is using for embedded entity discovery
	 * @return not null type info
	 */
	
	TypeInfo<?> getTypeInfo(TypeInfoProvider provider);
	
}
