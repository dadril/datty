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
package io.datty.spring.convert;

import org.springframework.data.convert.EntityConverter;
import org.springframework.data.convert.EntityReader;

import io.datty.api.DattyRecord;
import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;

/**
 * DattyConverter
 * 
 * @author Alex Shvid
 *
 */

public interface DattyConverter
		extends EntityConverter<DattyPersistentEntity<?>, DattyPersistentProperty, Object, DattyRecord>, DattyWriter<Object>,
		EntityReader<Object, DattyRecord> {

	/**
	 * Converts Java id object to DattyId data structure
	 * 
	 * @param id - not null Java object id
	 * @return not null datty id
	 */
	
	DattyId toDattyId(Object id);
	
	/**
	 * Writes object with parameter numeric 
	 * 
	 * @param source - source object
	 * @param sink - output buffer
	 * @param numeric - numeric keys
	 */
	
	void write(Object source, DattyRecord sink, boolean numeric); 

}
