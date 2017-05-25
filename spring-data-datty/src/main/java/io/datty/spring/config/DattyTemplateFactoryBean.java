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
package io.datty.spring.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import io.datty.api.Datty;
import io.datty.spring.convert.DattyConverter;
import io.datty.spring.core.DattyTemplate;

/**
 * DattyTemplateFactoryBean
 * 
 * @author Alex Shvid
 *
 */

public class DattyTemplateFactoryBean implements FactoryBean<DattyTemplate>, InitializingBean {

	private Datty datty;
	private DattyConverter converter;

	private DattyTemplate template;

	@Override
	public void afterPropertiesSet() {

		Assert.notNull(datty, "datty property must be set");
		Assert.notNull(converter, "converter property must be set");

		template = new DattyTemplate(datty, converter);

	}

	@Override
	public DattyTemplate getObject() {
		return template;
	}

	@Override
	public Class<?> getObjectType() {
		return template != null ? template.getClass() : DattyTemplate.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setDatty(Datty datty) {
		this.datty = datty;
	}

	public void setConverter(DattyConverter converter) {
		this.converter = converter;
	}
	
}
