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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.MappingException;
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

	private final String setName;
	private final String minorKey;
	private final int ttlSeconds;
	private final int timeoutMillis;
	private int propsCount = -1;
	
	private Map<String, DattyPersistentProperty> nameIndex;
	private Map<Integer, DattyPersistentProperty> codeIndex;
	
	private final SpelExpressionParser parser;
	private final StandardEvaluationContext context;
	
	public BasicDattyPersistentEntity(TypeInformation<T> typeInformation) {

		super(typeInformation);

		this.parser = new SpelExpressionParser();
		this.context = new StandardEvaluationContext();

		Class<T> rawType = typeInformation.getType();

		if (rawType.isAnnotationPresent(Entity.class)) {
			Entity entity = rawType.getAnnotation(Entity.class);
			this.setName = expression(entity.setName());
			this.minorKey = expression(entity.minorKey());
			this.ttlSeconds = entity.ttlSeconds();
			this.timeoutMillis = entity.timeoutMillis();
		} else {
			this.setName = rawType.getSimpleName();
			this.minorKey = "";
			this.ttlSeconds = DattyConstants.UNSET_TTL;
			this.timeoutMillis = DattyConstants.UNSET_TIMEOUT;
		}

	}
	
	private Map<String, DattyPersistentProperty> buildNameIndex() {
		NameIndexer indexer = new NameIndexer();
		doWithProperties(indexer);
		return indexer.getIndex();
	}
	
	public static final class NameIndexer implements SimplePropertyHandler {
		
		private final Map<String, DattyPersistentProperty> map = new HashMap<>();
		
		@Override
		public void doWithPersistentProperty(PersistentProperty<?> property) {
			DattyPersistentProperty prop = (DattyPersistentProperty) property;
			tryPut(prop.getPrimaryName(), prop);
			String[] otherNames = prop.getOtherNames();
			int length = otherNames.length;
			for (int i = 0; i != length; ++i) {
				tryPut(otherNames[i], prop);
			}
		}

		private void tryPut(String key, DattyPersistentProperty value) {
			
			DattyPersistentProperty conflict = map.get(key);
			
			if (conflict != null) {
				throw new MappingException("dublicate using of propertyName: " + key + ", already has value: " + conflict + " in try to add: " + value);
			}
			
			map.put(key, value);
		}

		public Map<String, DattyPersistentProperty> getIndex() {
			return map;
		}
	}
	
	private Map<Integer, DattyPersistentProperty> buildCodeIndex() {
		CodeIndexer indexer = new CodeIndexer();
		doWithProperties(indexer);
		return indexer.getIndex();
	}
	
	public static final class CodeIndexer implements SimplePropertyHandler {
		
		private final Map<Integer, DattyPersistentProperty> map = new HashMap<>();
		
		@Override
		public void doWithPersistentProperty(PersistentProperty<?> property) {
			DattyPersistentProperty prop = (DattyPersistentProperty) property;
			tryPut(prop.getCode(), prop);
		}

		private void tryPut(int key, DattyPersistentProperty value) {
			
			DattyPersistentProperty conflict = map.get(key);
			
			if (conflict != null) {
				throw new MappingException("dublicate using of tag number: " + key + ", already has value: " + conflict + " in try to add: " + value);
			}
			
			map.put(key, value);
		}
		
		public Map<Integer, DattyPersistentProperty> getIndex() {
			return map;
		}
	}
	
	
	private int doCountProperties() {
		PropertyCounter counter = new PropertyCounter();
		doWithProperties(counter);
		return counter.getCounter();
	}
	
	public static final class PropertyCounter implements SimplePropertyHandler {

		private int counter = 0;
		
		public int getCounter() {
			return counter;
		}

		@Override
		public void doWithPersistentProperty(PersistentProperty<?> property) {
			counter++;
		}
		
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.addPropertyAccessor(new BeanFactoryAccessor());
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		context.setRootObject(applicationContext);
	}

	@Override
	public String getSetName() {
		return setName;
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

	@Override
	public int getPropertiesCount() {
		if (propsCount == -1) {
			propsCount = doCountProperties();
		}
		return propsCount;
	}

	@Override
	public Set<String> getPropertyNames() {
		if (nameIndex == null) {
			nameIndex = buildNameIndex();
		}
		return nameIndex.keySet();
	}

	@Override
	public Optional<DattyPersistentProperty> findPropertyByName(String name) {
		if (nameIndex == null) {
			nameIndex = buildNameIndex();
		}
		return Optional.ofNullable(nameIndex.get(name));
	}
	
	@Override
	public Set<Integer> getPropertyCodes() {
		if (codeIndex == null) {
			codeIndex = buildCodeIndex();
		}
		return codeIndex.keySet();
	}

	@Override
	public Optional<DattyPersistentProperty> findPropertyByCode(Integer codeNumber) {
		if (codeIndex == null) {
			codeIndex = buildCodeIndex();
		}
		return Optional.ofNullable(codeIndex.get(codeNumber));
	}
	
	private String expression(String value) {
		Expression expression = parser.parseExpression(value, ParserContext.TEMPLATE_EXPRESSION);
		return expression.getValue(context, String.class);
	}

	@Override
	public String toString() {
		return "BasicDattyPersistentEntity [" + getType() + "]";
	}

}
