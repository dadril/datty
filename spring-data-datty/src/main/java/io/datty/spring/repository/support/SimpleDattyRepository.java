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
package io.datty.spring.repository.support;

import org.springframework.util.Assert;

import io.datty.spring.core.DattyId;
import io.datty.spring.core.DattyTemplate;
import io.datty.spring.repository.RxDattyRepository;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * SimpleDattyRepository implementation
 * 
 * @author Alex Shvid
 *
 */

public class SimpleDattyRepository<T> implements RxDattyRepository<T> {

	private final DattyEntityInformation<T> entityInformation;
	private final DattyTemplate template;
	private final Class<T> entityClass;

	public SimpleDattyRepository(DattyEntityInformation<T> entityInformation, Class<?> repositoryInterface,
			DattyTemplate template) {

		Assert.notNull(entityInformation, "empty entityInformation");
		Assert.notNull(repositoryInterface, "empty repositoryInterface");
		Assert.notNull(template, "empty template");

		this.entityInformation = entityInformation;
		this.template = template;
		this.entityClass = entityInformation.getJavaType();
	}

	@Override
	public <S extends T> Single<S> save(S entity) {
		return template.save(entityClass, entity, entityInformation.numeric());
	}

	@Override
	public <S extends T> Observable<S> save(Iterable<S> entities) {
		return template.save(entityClass, entities, entityInformation.numeric()); 
	}

	@Override
	public <S extends T> Observable<S> save(Observable<S> entityStream) {
		return template.save(entityClass, entityStream, entityInformation.numeric()); 
	}

	@Override
	public Observable<T> findOne(DattyId id) {
		return template.findOne(entityClass, id).toObservable();
	}

	@Override
	public Observable<T> findOne(Single<DattyId> id) {
		return template.findOne(entityClass, id).toObservable();
	}

	@Override
	public Single<Boolean> exists(DattyId id) {
		return template.exists(entityClass, id);
	}

	@Override
	public Single<Boolean> exists(Single<DattyId> id) {
		return template.exists(entityClass, id);
	}

	@Override
	public Observable<T> findAll() {
		return template.findAll(entityClass);
	}

	@Override
	public Observable<T> findAll(Iterable<DattyId> ids) {
		return template.findAll(entityClass, ids);
	}

	@Override
	public Observable<T> findAll(Observable<DattyId> idStream) {
		return template.findAll(entityClass, idStream);
	}

	@Override
	public Single<Long> count() {
		return template.count(entityClass);
	}

	@Override
	public Completable delete(DattyId id) {
		return template.delete(entityClass, id);
	}

	@Override
	public Completable delete(T entity) {
		return template.delete(entityClass, entity);
	}

	@Override
	public Completable delete(Iterable<? extends T> entities) {
		return template.delete(entityClass, entities);
	}

	@Override
	public Completable delete(Observable<? extends T> entityStream) {
		return template.delete(entityClass, entityStream);
	}

	@Override
	public Completable deleteAll() {
		return template.deleteAll(entityClass);
	}

	protected DattyTemplate getDattyTemplate() {
		return this.template;
	}

	protected DattyEntityInformation<T> getEntityInformation() {
		return entityInformation;
	}

}
