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
package io.datty.spring.support;

import io.datty.api.DattyRow;
import io.datty.spring.convert.DattyMappingConverter;
import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyMappingContext;

/**
 * DattyConverterUtil
 * 
 * @author Alex Shvid
 *
 */

public final class DattyConverterUtil {

	/**
	 * Lazy instantiation in case of not use of this util class
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	static final class Lazy {
		
		static final DattyMappingConverter converter = createConverter();
		
	}
	
	private DattyConverterUtil() { 
	}
	
	public static DattyId toDattyId(Object id) {
		
		return Lazy.converter.toDattyId(id);
		
	}
	
	public static <R> R read(Class<R> entityClass, DattyRow row) {
		
		return Lazy.converter.read(entityClass, row);
		
	}
	
	public static void write(Object entity, DattyRow row) {
		
		Lazy.converter.write(entity, row);
		
	}
	
	public static DattyMappingConverter createConverter() {
		
		DattyMappingContext mappingContext = new DattyMappingContext();
		
		DattyMappingConverter converter = new DattyMappingConverter(mappingContext);
		
		return converter;
	}
	
}
