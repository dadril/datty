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

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import io.datty.spring.core.DattyId;
import io.datty.spring.core.DattyTemplate;

/**
 * DattyRepositoryFactoryBean
 * 
 * @author Alex Shvid
 *
 */

public class DattyRepositoryFactoryBean<T extends Repository<S, DattyId>, S> extends
		RepositoryFactoryBeanSupport<T, S, DattyId> {

	private DattyTemplate template;

	public DattyRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}
	
	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {
		return new DattyRepositoryFactory(template);
	}

	public void setTemplate(DattyTemplate template) {
		this.template = template;
		setMappingContext(template.getConverter().getMappingContext());
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(template, "template must not be null!");
		super.afterPropertiesSet();
	}

}
