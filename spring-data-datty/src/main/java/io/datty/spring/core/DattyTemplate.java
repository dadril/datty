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
import io.datty.api.DattyResult;
import io.datty.api.DattyRecord;
import io.datty.api.UpdatePolicy;
import io.datty.api.operation.ClearOperation;
import io.datty.api.operation.GetOperation;
import io.datty.api.operation.HeadOperation;
import io.datty.api.operation.PutOperation;
import io.datty.api.operation.RecordOperation;
import io.datty.api.operation.RemoveOperation;
import io.datty.api.operation.ScanOperation;
import io.datty.api.operation.SizeOperation;
import io.datty.api.result.GetResult;
import io.datty.api.result.HeadResult;
import io.datty.api.result.PutResult;
import io.datty.api.result.RecordResult;
import io.datty.spring.convert.DattyConverter;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

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
	public <T> Optional<DattyId> getId(T entity) {
		Assert.notNull(entity, "entity is null");

		@SuppressWarnings("unchecked")
		DattyPersistentEntity<?> entityMetadata = getPersistentEntity((Class<T>) entity.getClass());

		return getId(entityMetadata, entity);
	}

	@Override
	public <S extends T, T> Single<S> save(Class<T> entityClass, final S entity, boolean numeric) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entity, "entity is null");
		
		DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);

		return datty.execute(toPutOperation(entityMetadata, entity, numeric))
		
		.map(new Func1<PutResult, S>() {

			@Override
			public S call(PutResult t) {
				return entity;
			}
			
		});
		
	}

	protected PutOperation toPutOperation(DattyPersistentEntity<?> entityMetadata, Object entity, boolean numeric) {
		
		DattyId id = getId(entityMetadata, entity).orElse(null);
		
		if (id == null) {
			throw new MappingException("id property not found for " + entityMetadata);
		}
		
		DattyRecord rec = new DattyRecord();
		converter.write(entity, rec, numeric);
		
		return new PutOperation(entityMetadata.getSetName())
		.setSuperKey(id.getSuperKey())
		.setMajorKey(id.getMajorKey())
		.setTtlSeconds(entityMetadata.getTtlSeconds())
		.setRecord(rec)
		.setUpdatePolicy(UpdatePolicy.MERGE)
		.setUpstreamContext(entity)
		.setTimeoutMillis(entityMetadata.getTimeoutMillis());
		
	}
	
	@Override
	public <S extends T, T> Observable<S> save(Class<T> entityClass, Iterable<S> entities, boolean numeric) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entities, "entities is null");
		return save(entityClass, Observable.from(entities), numeric);
	}

	@Override
	public <S extends T, T> Observable<S> save(Class<T> entityClass, Observable<S> entityStream, final boolean numeric) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entityStream, "entityStream is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		Observable<RecordOperation> operations = entityStream.map(new Func1<S, RecordOperation>() {

			@Override
			public RecordOperation call(S entity) {
				return toPutOperation(entityMetadata, entity, numeric);
			}
			
		});
		
		return datty.executeSequence(operations)
		
		.map(new Func1<DattyResult, S>() {

			@SuppressWarnings("unchecked")
			@Override
			public S call(DattyResult res) {
				
				PutResult putResult = (PutResult) res;
				return (S) putResult.getOperation().getUpstreamContext();
			}

			
		});
		
	}

	
	protected GetOperation toGetOperation(DattyPersistentEntity<?> entityMetadata, DattyId id) {
		
		return new GetOperation(entityMetadata.getSetName())
		.setSuperKey(id.getSuperKey())
		.setMajorKey(id.getMajorKey())
		.setTimeoutMillis(entityMetadata.getTimeoutMillis());
		
	}
	
	@Override
	public <T> Single<T> findOne(final Class<T> entityClass, DattyId id) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(id, "id is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		GetOperation getOp = toGetOperation(entityMetadata, id);
		
		return datty.execute(getOp).map(new Func1<GetResult, T>() {

			@Override
			public T call(GetResult res) {
				
				DattyRecord rec = res.getRecord();
				if (rec != null) {
					return (T) converter.read(entityClass, rec);
					
				}
				
				return null;
			}
			
		});
		
	}

	@Override
	public <T> Single<T> findOne(final Class<T> entityClass, Single<DattyId> id) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(id, "id is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		Single<GetOperation> getOp = id.map(new Func1<DattyId, GetOperation>() {

			@Override
			public GetOperation call(DattyId dattyId) {
				return toGetOperation(entityMetadata, dattyId);
			}
			
		});
		
		return datty.execute(getOp).map(new Func1<GetResult, T>() {

			@Override
			public T call(GetResult res) {
				
				DattyRecord rec = res.getRecord();
				if (rec != null) {
					return (T) converter.read(entityClass, rec);
				}
				
				return null;
			}
			
		});
		
	}

	protected HeadOperation toExistsOperation(DattyPersistentEntity<?> entityMetadata, DattyId id) {
		
		HeadOperation op = new HeadOperation(entityMetadata.getSetName())
		.setSuperKey(id.getSuperKey())
		.setMajorKey(id.getMajorKey())
		.anyMinorKey()
		.setTimeoutMillis(entityMetadata.getTimeoutMillis());
		
		return op;
	}
	
	@Override
	public Single<Boolean> exists(final Class<?> entityClass, DattyId id) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(id, "id is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		HeadOperation existsOp = toExistsOperation(entityMetadata, id);
		
		return datty.execute(existsOp).map(new Func1<HeadResult, Boolean>() {

			@Override
			public Boolean call(HeadResult res) {
				return res.exists();
			}
			
		});
		
	}

	@Override
	public Single<Boolean> exists(Class<?> entityClass, Single<DattyId> id) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(id, "id is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		Single<HeadOperation> existsOp = id.map(new Func1<DattyId, HeadOperation>() {

			@Override
			public HeadOperation call(DattyId dattyId) {
				return toExistsOperation(entityMetadata, dattyId);
			}
			
		});
		
		return datty.execute(existsOp).map(new Func1<HeadResult, Boolean>() {

			@Override
			public Boolean call(HeadResult res) {
				return res.exists();
			}
			
		});
	}

	@Override
	public <T> Observable<T> findAll(final Class<T> entityClass) {
		Assert.notNull(entityClass, "entityClass is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		ScanOperation scanOp = new ScanOperation(entityMetadata.getSetName());
		scanOp.setTimeoutMillis(entityMetadata.getTimeoutMillis());
		
		return datty.execute(scanOp).map(new Func1<DattyResult, T>() {

			@Override
			public T call(DattyResult res) {

				RecordResult queryRes = (RecordResult) res;
				
				DattyRecord rec = queryRes.getRecord();
				if (rec != null) {
					return (T) converter.read(entityClass, rec);
				}
				
				return null;
			}
			
		});
		
	}

	@Override
	public <T> Observable<T> findAll(Class<T> entityClass, Iterable<DattyId> ids) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(ids, "ids is null");
		return findAll(entityClass, Observable.from(ids));
	}

	@Override
	public <T> Observable<T> findAll(final Class<T> entityClass, Observable<DattyId> idStream) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(idStream, "idStream is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		Observable<RecordOperation> operations = idStream.map(new Func1<DattyId, RecordOperation>() {

			@Override
			public RecordOperation call(DattyId dattyId) {
				return toGetOperation(entityMetadata, dattyId);
			}
			
		});
		
		return datty.executeSequence(operations).map(new Func1<DattyResult, T>() {

			@Override
			public T call(DattyResult res) {
				
				GetResult getRes = (GetResult) res;
				
				DattyRecord rec = getRes.getRecord();
				if (rec != null) {
					return (T) converter.read(entityClass, rec);
				}
				
				return null;
			}
			
		});
		
	}

	@Override
	public Single<Long> count(Class<?> entityClass) {
		Assert.notNull(entityClass, "entityClass is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		SizeOperation countOp = new SizeOperation(entityMetadata.getSetName());
		countOp.setTimeoutMillis(entityMetadata.getTimeoutMillis());

		return datty.execute(countOp).map(new Func1<RecordResult, Long>() {

			@Override
			public Long call(RecordResult res) {
				return res.count();
			}
			
		}).toSingle();
		
	}
	
	protected RemoveOperation toRemoveOperation(DattyPersistentEntity<?> entityMetadata, DattyId id) {
		
		RemoveOperation removeOp = new RemoveOperation(entityMetadata.getSetName())
		.setSuperKey(id.getSuperKey())
		.setMajorKey(id.getMajorKey())
		.allMinorKeys()
		.setTimeoutMillis(entityMetadata.getTimeoutMillis());
		
		return removeOp;
		
	}

	@Override
	public Completable delete(Class<?> entityClass, DattyId id) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(id, "id is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		RemoveOperation removeOp = toRemoveOperation(entityMetadata, id);
		
		return datty.execute(removeOp).toCompletable();
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, T entity) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entity, "entity is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		DattyId id = getId(entityMetadata, entity).orElse(null);
		
		if (id == null) {
			throw new MappingException("id property not found for " + entityMetadata);
		}
		
		RemoveOperation removeOp = toRemoveOperation(entityMetadata, id);
		
		return datty.execute(removeOp).toCompletable();
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, Iterable<? extends T> entities) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entities, "entities is null");
		return delete(entityClass, Observable.from(entities));
	}

	@Override
	public <T> Completable delete(Class<T> entityClass, Observable<? extends T> entityStream) {
		Assert.notNull(entityClass, "entityClass is null");
		Assert.notNull(entityStream, "entityStream is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		Observable<RecordOperation> operations = entityStream
				.map(new Func1<T, RecordOperation>() {

					@Override
					public RecordOperation call(T entity) {
						DattyId id = getId(entityMetadata, entity).orElse(null);
						if (id == null) {
							throw new MappingException("id property not found for " + entityMetadata);
						}
						return toRemoveOperation(entityMetadata, id);
					}
					
				});
		
		
		return datty.executeSequence(operations).toCompletable();
	}

	@Override
	public Completable deleteAll(Class<?> entityClass) {
		Assert.notNull(entityClass, "entityClass is null");
		
		final DattyPersistentEntity<?> entityMetadata = getPersistentEntity(entityClass);
		
		ClearOperation deleteOp = new ClearOperation(entityMetadata.getSetName())
			.setTimeoutMillis(entityMetadata.getTimeoutMillis());

		return datty.execute(deleteOp).toCompletable();
	}

	@Override
	public DattyConverter getConverter() {
		return converter;
	}

	@Override
	public Datty getDatty() {
		return datty;
	}

	protected Optional<DattyId> getId(DattyPersistentEntity<?> entityMetadata, Object entity) {
		
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
			
			return Optional.empty();
			
		}
		
		DattyId dattyId = converter.toDattyId(id);
		
		return Optional.of(dattyId);
		
	}
	
	@Override
	public  String getSetName(Class<?> entityClass) {
		return getPersistentEntity(entityClass).getSetName();
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
