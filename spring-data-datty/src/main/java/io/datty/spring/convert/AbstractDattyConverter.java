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
package io.datty.spring.convert;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.EntityInstantiators;

/**
 * AbstractDattyConverter
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractDattyConverter implements DattyConverter, InitializingBean {

	protected final GenericConversionService conversionService;
	protected EntityInstantiators instantiators = new EntityInstantiators();

	public AbstractDattyConverter(GenericConversionService conversionService) {
		this.conversionService = conversionService == null ? new DefaultConversionService() : conversionService;
	}

	public void setInstantiators(EntityInstantiators instantiators) {
		this.instantiators = instantiators == null ? new EntityInstantiators() : instantiators;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	@Override
	public void afterPropertiesSet() {
	}
	
}
