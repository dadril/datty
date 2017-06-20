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

import java.util.Optional;

import org.springframework.data.repository.core.support.AbstractEntityInformation;

import io.datty.spring.core.DattyId;
import io.datty.spring.core.DattyTemplate;
import io.datty.spring.mapping.DattyPersistentEntity;

/**
 * MappingDattyEntityInformation
 * 
 * @author Alex Shvid
 *
 */

public class MappingDattyEntityInformation<T> extends AbstractEntityInformation<T, DattyId> implements
		DattyEntityInformation<T> {

	private final DattyTemplate template;
	private final DattyPersistentEntity<T> entityMetadata;
	private final String setName;
	private final boolean numeric;

	public MappingDattyEntityInformation(DattyTemplate template, DattyPersistentEntity<T> entity) {
		this(template, entity, null, false);
	}
	
	public MappingDattyEntityInformation(DattyTemplate template, DattyPersistentEntity<T> entity,
			String setName, boolean numeric) {
		super(entity.getType());
		this.template = template;
		this.entityMetadata = entity;
		this.setName = isEmpty(setName) ? entity.getSetName() : setName;
		this.numeric = numeric;
	}

	private boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	@Override
	public Optional<DattyId> getId(T entity) {
		return template.getId(entity);
	}

	@Override
	public Class<DattyId> getIdType() {
		return DattyId.class;
	}

	@Override
	public String getSetName() {
		return setName;
	}

	@Override
	public boolean numeric() {
		return numeric;
	}

	@Override
	public int ttlSeconds() {
		return entityMetadata.getTtlSeconds();
	}

	@Override
	public int timeoutMillis() {
		return entityMetadata.getTimeoutMillis();
	}
	

}
