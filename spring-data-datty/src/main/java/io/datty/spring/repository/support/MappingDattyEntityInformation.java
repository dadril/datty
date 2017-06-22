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

import io.datty.api.DattyConstants;
import io.datty.spring.core.DattyId;
import io.datty.spring.core.DattyTemplate;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.repository.RepositoryInfo;

/**
 * MappingDattyEntityInformation
 * 
 * @author Alex Shvid
 *
 */

public class MappingDattyEntityInformation<T> extends AbstractEntityInformation<T, DattyId> implements
		DattyEntityInformation<T> {

	private final DattyTemplate template;
	private final String setName;
	private final int ttlSeconds;
	private final int timeoutMillis;
	private final boolean numeric;

	public MappingDattyEntityInformation(DattyTemplate template, DattyPersistentEntity<T> entity) {
		super(entity.getType());
		this.template = template;
		this.setName = entity.getSetName();
		this.ttlSeconds = entity.getTtlSeconds();
		this.timeoutMillis = entity.getTimeoutMillis();
		this.numeric = false;
	}
	
	public MappingDattyEntityInformation(DattyTemplate template, DattyPersistentEntity<T> entity,
			RepositoryInfo repositoryAnnotation) {
		super(entity.getType());
		this.template = template;
		this.setName = isEmpty(repositoryAnnotation.setName()) ? entity.getSetName() : repositoryAnnotation.setName();
		this.ttlSeconds = repositoryAnnotation.ttlSeconds() != DattyConstants.UNSET_TTL ? repositoryAnnotation.ttlSeconds() : entity.getTtlSeconds();
		this.timeoutMillis = repositoryAnnotation.timeoutMillis() != DattyConstants.UNSET_TIMEOUT ? repositoryAnnotation.timeoutMillis() : entity.getTimeoutMillis();
		this.numeric = repositoryAnnotation.numeric();
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
		return ttlSeconds;
	}

	@Override
	public int timeoutMillis() {
		return timeoutMillis;
	}

}
