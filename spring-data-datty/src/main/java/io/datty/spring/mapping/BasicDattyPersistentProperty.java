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
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;

/**
 * BasicDattyPersistentProperty
 * 
 * @author Alex Shvid
 *
 */

public class BasicDattyPersistentProperty extends AnnotationBasedPersistentProperty<DattyPersistentProperty>
		implements DattyPersistentProperty {

	private final boolean isEmbeddedType;
	private final boolean hasTransientModifier;
	
	public BasicDattyPersistentProperty(Property property, DattyPersistentEntity<?> owner,
			SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
		
		Optional<Field> field = property.getField();
		
		if (field.isPresent()) {
			this.isEmbeddedType = field.get().getType().isAnnotationPresent(Embedded.class);
			this.hasTransientModifier = Modifier.isTransient(field.get().getModifiers());
		}
		else {
			this.isEmbeddedType = false;
			this.hasTransientModifier = false;
		}
		
	}
	
	@Override
	protected Association<DattyPersistentProperty> createAssociation() {
		return new Association<DattyPersistentProperty>(this, null);
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
	public String toString() {
		return "BasicDattyPersistentProperty [" + getName() + "]";
	}
	
}
