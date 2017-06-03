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
package io.datty.spring.config.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import io.datty.spring.config.DattyConfigConstants;
import io.datty.spring.repository.config.DattyRepositoryConfigurationExtension;

/**
 * DattyNamespaceHandler
 * 
 * @author Alex Shvid
 *
 */

public class DattyNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {

		RepositoryConfigurationExtension extension = new DattyRepositoryConfigurationExtension();
		RepositoryBeanDefinitionParser repositoryBeanDefinitionParser = new RepositoryBeanDefinitionParser(extension);
		registerBeanDefinitionParser(DattyConfigConstants.DATTY_REPOSITORIES_ELEMENT, repositoryBeanDefinitionParser);

		registerBeanDefinitionParser(DattyConfigConstants.DATTY_CONVERTER_ELEMENT, new DattyConverterParser());
		registerBeanDefinitionParser(DattyConfigConstants.DATTY_TEMPLATE_ELEMENT, new DattyTemplateParser());

	}
	
}
