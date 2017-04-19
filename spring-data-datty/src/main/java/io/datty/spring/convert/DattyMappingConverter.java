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

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.ClassUtils;

import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import io.netty.buffer.ByteBuf;

/**
 * DattyMappingConverter
 * 
 * Implementation of the DattyConverter that is using schema mapping
 * 
 * @author Alex Shvid
 *
 */

public class DattyMappingConverter extends AbstractDattyConverter implements BeanClassLoaderAware {

	private final MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext;

	private ClassLoader beanClassLoader;

	public DattyMappingConverter(
			MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext) {
		this(mappingContext, new DefaultConversionService());
	}
	
	public DattyMappingConverter(
			MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext, GenericConversionService conversionService) {
		super(conversionService);
		this.mappingContext = mappingContext;
	}
	
	@Override
	public MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> getMappingContext() {
		return mappingContext;
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}
	
	
	@Override
	public DattyId toDattyId(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Object source, ByteBuf sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <R> R read(Class<R> type, ByteBuf source) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> transformClassToBeanClassLoaderClass(Class<T> entity) {
		try {
			return (Class<T>) ClassUtils.forName(entity.getName(), beanClassLoader);
		} catch (ClassNotFoundException e) {
			return entity;
		} catch (LinkageError e) {
			return entity;
		}
	}

	@Override
	public String toString() {
		return "DattyMappingConverter [mappingContext=" + mappingContext + ", beanClassLoader=" + beanClassLoader + "]";
	}

}
