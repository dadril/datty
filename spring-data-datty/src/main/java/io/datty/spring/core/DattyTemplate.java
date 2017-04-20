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
package io.datty.spring.core;

import java.util.Optional;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;

import io.datty.api.Datty;
import io.datty.spring.convert.DattyConverter;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * DattyTemplate
 * 
 * @author Alex Shvid
 *
 */

public class DattyTemplate implements DattyOperations {

	private final Datty datty;
	private final DattyConverter converter;
	private final MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext;

	public DattyTemplate(Datty datty, DattyConverter converter) {
		Assert.notNull(datty, "datty is null");
		Assert.notNull(converter, "converter is null");
		this.datty = datty;
		this.converter = converter;
		this.mappingContext = converter.getMappingContext();
	}

	@Override
	public <T> DattyId getId(T entity) {
		Assert.notNull(entity, "entity is null");

		@SuppressWarnings("unchecked")
		DattyPersistentEntity<?> entityMetadata = getPersistentEntity((Class<T>) entity.getClass());

		Optional<DattyPersistentProperty> idProperty = entityMetadata.getIdProperty();

		Object id;
		
		if (idProperty.isPresent()) {

			try {
				id = new BeanWrapperImpl(entity).getPropertyValue(idProperty.get().getName());
			} catch (Exception e) {
				throw new MappingException("fail to access id property: " + idProperty.get().getName(), e);
			}
		}
		else {
			
			id = entity;
			
		}
		
		DattyId dattyId = converter.toDattyId(id);
		
		if (entityMetadata.hasMinorKey()) {
			dattyId.setMinorKey(entityMetadata.getMinorKey());
		}
		
		return dattyId;
		
	}

	@Override
	public <S extends T, T> Single<S> save(Class<T> entityClass, S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T, T> Observable<S> save(Class<T> entityClass, Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T, T> Observable<S> save(Class<T> entityClass, Observable<S> entityStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Observable<T> findOne(Class<T> entityClass, DattyId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Observable<T> findOne(Class<T> entityClass, Single<DattyId> id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<Boolean> exists(Class<?> entityClass, DattyId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<Boolean> exists(Class<?> entityClass, Single<DattyId> id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Observable<T> findAll(Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Observable<T> findAll(Class<T> entityClass, Iterable<DattyId> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Observable<T> findAll(Class<T> entityClass, Observable<DattyId> idStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<Long> count(Class<?> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable delete(Class<?> entityClass, DattyId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, Iterable<? extends T> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, Observable<? extends T> entityStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable deleteAll(Class<?> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DattyConverter getConverter() {
		return converter;
	}

	@Override
	public Datty getDatty() {
		return datty;
	}

	@Override
	public  String getCacheName(Class<?> entityClass) {
		return getPersistentEntity(entityClass).getCacheName();
	}
	
	protected DattyPersistentEntity<?> getPersistentEntity(Class<?> entityClass) {

		Assert.notNull(entityClass, "empty entityClass");

		DattyPersistentEntity<?> entity = mappingContext.getPersistentEntity(entityClass)
				.orElse(null);

		if (entity == null) {
			throw new MappingException("persistent entity not found for a given class " + entityClass);
		}

		return entity;
	}

	
}
