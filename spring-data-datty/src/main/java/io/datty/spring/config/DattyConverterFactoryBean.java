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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import io.datty.spring.convert.DattyConverter;
import io.datty.spring.convert.DattyMappingConverter;
import io.datty.spring.mapping.DattyMappingContext;

/**
 * DattyConverterFactoryBean
 * 
 * @author Alex Shvid
 *
 */

public class DattyConverterFactoryBean implements FactoryBean<DattyConverter>, InitializingBean, ApplicationContextAware {

	private DattyMappingConverter dattyConverter;
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() {

		DattyMappingContext mappingContext = new DattyMappingContext();
		mappingContext.setApplicationContext(applicationContext);
		
		dattyConverter = new DattyMappingConverter(mappingContext);

	}

	@Override
	public DattyMappingConverter getObject() {
		return dattyConverter;
	}

	@Override
	public Class<?> getObjectType() {
		return dattyConverter != null ? dattyConverter.getClass() : DattyMappingConverter.class;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
}
