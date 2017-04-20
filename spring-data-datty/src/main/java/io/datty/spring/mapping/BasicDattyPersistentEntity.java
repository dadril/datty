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
package io.datty.spring.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import io.datty.api.DattyConstants;

/**
 * BasicDattyPersistentEntity
 * 
 * @author Alex Shvid
 *
 */

public class BasicDattyPersistentEntity<T> extends BasicPersistentEntity<T, DattyPersistentProperty> implements
DattyPersistentEntity<T>, ApplicationContextAware {

	private final String cacheName;
	private final String minorKey;
	private final int ttlSeconds;
	private final int timeoutMillis;
	
	private final SpelExpressionParser parser;
	private final StandardEvaluationContext context;
	
	public BasicDattyPersistentEntity(TypeInformation<T> typeInformation) {

		super(typeInformation);

		this.parser = new SpelExpressionParser();
		this.context = new StandardEvaluationContext();

		Class<T> rawType = typeInformation.getType();

		if (rawType.isAnnotationPresent(Entity.class)) {
			Entity entity = rawType.getAnnotation(Entity.class);
			this.cacheName = expression(entity.cacheName());
			this.minorKey = expression(entity.minorKey());
			this.ttlSeconds = entity.ttlSeconds();
			this.timeoutMillis = entity.timeoutMillis();
		} else {
			this.cacheName = rawType.getSimpleName();
			this.minorKey = "";
			this.ttlSeconds = DattyConstants.UNSET_TTL;
			this.timeoutMillis = DattyConstants.UNSET_TIMEOUT;
		}

	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.addPropertyAccessor(new BeanFactoryAccessor());
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		context.setRootObject(applicationContext);
	}

	@Override
	public String getCacheName() {
		return cacheName;
	}

	@Override
	public boolean hasMinorKey() {
		return !minorKey.isEmpty();
	}

	@Override
	public String getMinorKey() {
		return minorKey;
	}

	@Override
	public int getTtlSeconds() {
		return ttlSeconds;
	}

	@Override
	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	private String expression(String value) {
		Expression expression = parser.parseExpression(value, ParserContext.TEMPLATE_EXPRESSION);
		return expression.getValue(context, String.class);
	}
	
}
