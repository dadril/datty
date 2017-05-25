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
package io.datty.spring.config.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.datty.api.Datty;
import io.datty.spring.config.DattyConfigConstants;
import io.datty.spring.config.DattyConverterFactoryBean;
import io.datty.spring.config.DattyTemplateFactoryBean;


/**
 * AbstractDattyConfiguration
 * 
 * @author Alex Shvid
 *
 */

@Configuration
public abstract class AbstractDattyConfiguration {

	@Bean(name = DattyConfigConstants.DATTY_DEFAULT_ID)
	public abstract Datty datty();

	@Bean(name = DattyConfigConstants.DATTY_CONVERTER_DEFAULT_ID)
	public DattyConverterFactoryBean converter() {
		return new DattyConverterFactoryBean();
	}

	@Bean(name = DattyConfigConstants.DATTY_TEMPLATE_DEFAULT_ID)
	public DattyTemplateFactoryBean template() {

		DattyTemplateFactoryBean bean = new DattyTemplateFactoryBean();
		bean.setDatty(datty());
		bean.setConverter(converter().getObject());

		return bean;
	}

}
