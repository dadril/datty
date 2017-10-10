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
package io.datty.msgpack.core.type;

import java.util.List;
import java.util.Map;

import io.datty.msgpack.support.MessageParseException;

/**
 * DefaultTypeInfoProvider
 * 
 * @author Alex Shvid
 *
 */

public enum DefaultTypeInfoProvider implements TypeInfoProvider {

	INSTANCE;

	@Override
	public <T> TypeInfo<T> getTypeInfo(Class<T> type) {
		
		if (type.isArray()) {
			
			Class<?> componentType = type.getComponentType();
			
			SimpleTypeInfo<?> componentTypeInfo = TypeRegistry.findSimple(componentType);
			
			if (componentTypeInfo == null) {
				throw new MessageParseException("componentType not found for array: " + type);
			}
			
			return new ArrayTypeInfoAdapter<Object, T>(type, (SimpleTypeInfo<Object>) componentTypeInfo);
			
		}
		
		if (List.class.isAssignableFrom(type)) {
			return new ListObjectTypeInfoAdapter<T>(type);
		}
		
		if (Map.class.isAssignableFrom(type)) {
			return new MapObjectTypeInfoAdapter<T>(type);
		}
		
		TypeInfo<T> typeInfo = TypeRegistry.findSimple(type);
		
		if (typeInfo == null) {
			throw new MessageParseException("type info not found for class: " + type);
		}
		
		return typeInfo;
		
	}
	
	
	
}
