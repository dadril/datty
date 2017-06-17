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

import io.datty.api.Datty;
import io.datty.spring.convert.DattyConverter;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * DattyOperations
 * 
 * Interface for generic CRUD operations.
 * 
 * This repository follows reactive paradigms and uses RxJava 1 types.
 * 
 * @author Alex Shvid
 *
 */

public interface DattyOperations {

	/**
	 * Gets id of the entity
	 * 
	 * @param entity must not be {@literal null}.
	 * @return the datty id
	 */

	<T> Optional<DattyId> getId(T entity);
	
	/**
	 * Gets cache name of the entity
	 * 
	 * @param entityClass - not null entity class
	 * @return not null cache name
	 */
	
	 String getSetName(Class<?> entityClass);
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param entity must not be {@literal null}.
	 * @return the saved entity.
	 */
	<S extends T, T> Single<S> save(Class<T> entityClass, S entity, boolean numeric);

	/**
	 * Saves all given entities.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param entities must not be {@literal null}.
	 * @return the saved entities.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T, T> Observable<S> save(Class<T> entityClass, Iterable<S> entities, boolean numeric);

	/**
	 * Saves all given entities.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param entityStream must not be {@literal null}.
	 * @return the saved entities.
	 * @throws IllegalArgumentException in case the given {@code Publisher} is {@literal null}.
	 */
	<S extends T, T> Observable<S> save(Class<T> entityClass, Observable<S> entityStream, boolean numeric);	
	
	/**
	 * Retrieves an entity by its id.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@link Observable#empty()} if none found.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	<T> Single<T> findOne(Class<T> entityClass, DattyId id);
	
	/**
	 * Retrieves an entity by its id supplied by a {@link Single}.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@link Observable#empty()} if none found.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	<T> Single<T> findOne(Class<T> entityClass, Single<DattyId> id);
	
	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	Single<Boolean> exists(Class<?> entityClass, DattyId id);
	
	/**
	 * Returns whether an entity with the given id, supplied by a {@link Single}, exists.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	Single<Boolean> exists(Class<?> entityClass, Single<DattyId> id);
	
	/**
	 * Returns all instances of the type.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @return all entities.
	 */
	<T> Observable<T> findAll(Class<T> entityClass);
	
	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @param ids must not be {@literal null}.
	 * @return the found entities.
	 */
	<T> Observable<T> findAll(Class<T> entityClass, Iterable<DattyId> ids);
	
	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @param idStream must not be {@literal null}.
	 * @return the found entities.
	 */
	<T> Observable<T> findAll(Class<T> entityClass, Observable<DattyId> idStream);
	
	/**
	 * Returns the number of entities available.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @return the number of entities.
	 */
	Single<Long> count(Class<?> entityClass);
	
	/**
	 * Deletes the entity with the given id.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}.
	 */
	Completable delete(Class<?> entityClass, DattyId id);

	/**
	 * Deletes a given entity.
	 *
	 * @param entityClass must not be {@literal null}.
	 * @param entity must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<T> Completable delete(Class<T> entityClass, T entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @param entities must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	<T> Completable delete(Class<T> entityClass, Iterable<? extends T> entities);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @param entityStream must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@link Observable} is {@literal null}.
	 */
	<T> Completable delete(Class<T> entityClass, Observable<? extends T> entityStream);

	/**
	 * Deletes all entities managed by the repository.
	 * 
	 * @param entityClass must not be {@literal null}.
	 */
	Completable deleteAll(Class<?> entityClass);
	
	/**
	 * DattyConverter getter
	 * 
	 * @return not null converter
	 */

	DattyConverter getConverter();
	
	/**
	 * Gets datty instance
	 * 
	 * @return not null datty instance
	 */

	 Datty getDatty();
	 
}
