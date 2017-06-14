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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;

import io.datty.msgpack.core.type.ArrayTypeInfoAdapter;
import io.datty.msgpack.core.type.ListTypeInfoAdapter;
import io.datty.msgpack.core.type.MapTypeInfoAdapter;
import io.datty.msgpack.core.type.SimpleTypeInfo;
import io.datty.msgpack.core.type.TypeInfo;
import io.datty.msgpack.core.type.TypeInfoProvider;
import io.datty.msgpack.core.type.TypeRegistry;

/**
 * BasicDattyPersistentProperty
 * 
 * @author Alex Shvid
 *
 */

public class BasicDattyPersistentProperty extends AnnotationBasedPersistentProperty<DattyPersistentProperty>
		implements DattyPersistentProperty {

	private static final String[] EMPTY = new String[] {};
	
	private final String primaryName;
	private final String[] otherNames;
	private final int code;
	private final boolean isEmbeddedType;
	private final boolean hasTransientModifier;
	private volatile TypeInfo<?> typeInfo;
	
	public BasicDattyPersistentProperty(Property property, DattyPersistentEntity<?> owner,
			SimpleTypeHolder simpleTypeHolder) {
		
		super(property, owner, simpleTypeHolder);
		
		Optional<Field> field = property.getField();
		
		if (field.isPresent()) {
			Field fieldInstance = field.get();
			Class<?> fieldType = fieldInstance.getType();

			PropertyName nameAnnotation = fieldInstance.getAnnotation(PropertyName.class);
			if (nameAnnotation != null) {
				this.primaryName = nameAnnotation.name().length() > 0 ? nameAnnotation.name() : getName();
				this.otherNames = nameAnnotation.otherNames();
				this.code =  nameAnnotation.code();
			}
			else {
				this.primaryName = getName();
				this.otherNames = EMPTY;
				this.code = 0;
			}

			this.isEmbeddedType = fieldType.isAnnotationPresent(Embedded.class);
			this.hasTransientModifier = Modifier.isTransient(field.get().getModifiers());
		}
		else {
			this.primaryName = getName();
			this.otherNames = EMPTY;
			this.code = 0;		
			this.isEmbeddedType = false;
			this.hasTransientModifier = false;
		}
		
		if (owner.numeric() && this.code == 0) {
			throw new MappingException("numeric entity has no code for " + property);
		}
	}
	
	@Override
	protected Association<DattyPersistentProperty> createAssociation() {
		return new Association<DattyPersistentProperty>(this, null);
	}

	@Override
	public String getPrimaryName() {
		return primaryName;
	}

	@Override
	public String[] getOtherNames() {
		return otherNames;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public boolean isEmbeddedType() {
		return isEmbeddedType;
	}
	
	@Override
	public boolean isTransient() {
		return hasTransientModifier || super.isTransient();
	}
	
	@Override
	public TypeInfo<?> getTypeInfo(TypeInfoProvider provider) {
		TypeInfo<?> cache = this.typeInfo;
		if (cache == null) {
			cache = discoveryType(provider);
			this.typeInfo = cache;
		}
		return cache;
	}

	private TypeInfo<?> discoveryType(TypeInfoProvider provider) {
		
		if (isArray()) {
			
			final Class<?> componentType = getComponentType().orElse(null);
			
			if (componentType == null) {
				throw new MappingException("component type not found for: " + this);
			}
			
			SimpleTypeInfo<?> simpleType = (SimpleTypeInfo<?>) provider.getTypeInfo(componentType);
			
			return new ArrayTypeInfoAdapter(getRawType(), simpleType);
			
		}
		
		else if (isCollectionLike()) {
			
			final Class<?> componentType = getComponentType().orElse(null);
			
			if (componentType == null) {
				throw new MappingException("component type not found for: " + this);
			}
			
			SimpleTypeInfo<?> simpleType = (SimpleTypeInfo<?>) provider.getTypeInfo(componentType);
			
			return new ListTypeInfoAdapter(getRawType(), simpleType);
			
		}
		
		else if (isMap()) {
			
			final Class<?> keyType = getComponentType().orElse(null);
			final Class<?> valueType = this.getMapValueType().orElse(null);
			
			if (keyType == null) {
				throw new MappingException("component type not found for: " + this);
			}
			
			if (valueType == null) {
				throw new MappingException("map value type not found for: " + this);
			}
			
			SimpleTypeInfo<?> simpleKeyType = (SimpleTypeInfo<?>) provider.getTypeInfo(keyType);
			SimpleTypeInfo<?> simplValueType = (SimpleTypeInfo<?>) provider.getTypeInfo(valueType);
			
			return new MapTypeInfoAdapter(getRawType(), simpleKeyType, simplValueType);
			
		}
		
		else if (isEmbeddedType()) {
			
			return provider.getTypeInfo(getRawType());
			
		}
		
		else {
			
			TypeInfo<?> typeInfo = TypeRegistry.findSimple(getRawType());
			
			if (typeInfo == null) {
				throw new MappingException("simple type not found for: " + this);
			}
			
			return typeInfo;
		}
		
	}
	
	@Override
	public String toString() {
		return "BasicDattyPersistentProperty [" + getName() + "]";
	}
	
}
