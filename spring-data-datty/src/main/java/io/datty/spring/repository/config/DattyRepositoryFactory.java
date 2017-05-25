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
package io.datty.spring.repository.config;

import java.io.Serializable;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.util.Assert;

import io.datty.spring.core.DattyTemplate;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import io.datty.spring.repository.support.DattyEntityInformation;
import io.datty.spring.repository.support.MappingDattyEntityInformation;
import io.datty.spring.repository.support.SimpleDattyRepository;

/**
 * DattyRepositoryFactory
 * 
 * @author Alex Shvid
 *
 */

public class DattyRepositoryFactory extends RepositoryFactorySupport {

	private final DattyTemplate template;
	private final MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext;

	public DattyRepositoryFactory(DattyTemplate template) {

		Assert.notNull(template, "template is empty");

		this.template = template;
		this.mappingContext = template.getConverter().getMappingContext();
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleDattyRepository.class;
	}
	
	

	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		
		DattyEntityInformation<?> entityInformation = getEntityInformationOverride(metadata.getDomainType());
		
		return new SimpleDattyRepository(entityInformation, metadata.getRepositoryInterface(), template);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		return (EntityInformation<T, ID>) getEntityInformationOverride(domainClass);
	}

	@SuppressWarnings("unchecked")
	public <T> DattyEntityInformation<T> getEntityInformationOverride(Class<T> domainClass) {

		DattyPersistentEntity<?> entity = mappingContext.getPersistentEntity((Class<?>) domainClass).orElse(null);

		if (entity == null) {
			throw new MappingException(String.format("Could not lookup mapping metadata for domain class %s!",
					domainClass.getName()));
		}

		return new MappingDattyEntityInformation<T>(template, (DattyPersistentEntity<T>) entity);
	}
}
