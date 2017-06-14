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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import io.datty.api.DattyRow;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.ValueMessageReader;
import io.datty.msgpack.core.ValueMessageWriter;
import io.datty.msgpack.core.reader.IntegerReader;
import io.datty.msgpack.core.reader.StringReader;
import io.datty.msgpack.core.reader.ValueReader;
import io.datty.msgpack.core.type.SimpleTypeInfo;
import io.datty.msgpack.core.type.TypeInfo;
import io.datty.msgpack.core.type.TypeInfoProvider;
import io.datty.msgpack.core.type.TypeRegistry;
import io.datty.msgpack.core.writer.ValueWriter;
import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import io.datty.spring.mapping.Identifiable;
import io.netty.buffer.ByteBuf;

/**
 * DattyMappingConverter
 * 
 * Implementation of the DattyConverter that is using schema mapping
 * 
 * @author Alex Shvid
 *
 */

public class DattyMappingConverter extends AbstractDattyConverter implements BeanClassLoaderAware {

	private final MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext;

	private ClassLoader beanClassLoader;

	public DattyMappingConverter(
			MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext) {
		this(mappingContext, new DefaultConversionService());
	}
	
	public DattyMappingConverter(
			MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> mappingContext, GenericConversionService conversionService) {
		super(conversionService);
		this.mappingContext = mappingContext;
	}
	
	@Override
	public MappingContext<? extends DattyPersistentEntity<?>, DattyPersistentProperty> getMappingContext() {
		return mappingContext;
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}
	
	
	@Override
	public DattyId toDattyId(Object id) {
		Assert.notNull(id, "id is null");
		
		if (id instanceof Identifiable) {
			
			Identifiable identifiable = (Identifiable) id;
			return identifiable.getId();
		}
		
		String majorKey = conversionService.convert(id, String.class);

		return new DattyId().setMajorKey(majorKey);
	}

	@Override
	public void write(Object source, DattyRow sink) {
		Assert.notNull(source, "source is null");
		Assert.notNull(sink, "sink is null");
		
		if (source == null) {
			sink.clear();
			return;
		}
		
		Class<?> beanClassLoaderClass = transformClassToBeanClassLoaderClass(source.getClass());
		
		DattyPersistentEntity<?> entity = (DattyPersistentEntity<?>) mappingContext
				.getPersistentEntity(beanClassLoaderClass).orElse(null);
		
		if (entity == null) {
			throw new MappingException("No mapping metadata found for " + source.getClass());
		}

		if (entity.hasMinorKey()) {
			ByteBuf buffer = sink.addValue(entity.getMinorKey());
			ByteBuf updatedBufer = writeEntity(entity, source, buffer);
			if (updatedBufer != buffer) {
				sink.putValue(entity.getMinorKey(), updatedBufer, false);
			}
		}
		else {
			writeCrossEntity(entity, source, sink);
		}
		
	}

	private ByteBuf writeEntity(DattyPersistentEntity<?> entity, Object source, final ByteBuf sink) {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		
		int headerIndex = writer.skipHeader(entity.getPropertiesCount(), sink);
		
		WriteEntityHandler entityWriter = new WriteEntityHandler(writer, source, entity.numeric(), sink);
		entity.doWithProperties(entityWriter);
		
		writer.writeHeader(entityWriter.size(), entity.getPropertiesCount(), headerIndex, sink);
		
		return entityWriter.getSink();
	}
	
	public final class WriteEntityHandler implements PropertyHandler<DattyPersistentProperty> {

		private final MessageWriter writer;
		private final BeanWrapper wrapper;
		private final boolean numeric;
		private ByteBuf sink;
		private int size;
		
		public WriteEntityHandler(MessageWriter writer, Object source, boolean numeric, ByteBuf sink) {
			this.writer = writer;
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.numeric = numeric;
			this.sink = sink;
		}
		
		public Object getWrappedInstance() {
			return wrapper.getWrappedInstance();
		}
		
		public int size() {
			return size;
		}
		
		public ByteBuf getSink() {
			return sink;
		}

		@Override
		public void doWithPersistentProperty(DattyPersistentProperty property) {
			
			String propName = property.getName();
			final Class<?> propType = property.getRawType();
			Object propValue = wrapper.getPropertyValue(propName);
			
			if (propValue != null && !property.isTransient()) {
							
				if (numeric) {
					writer.writeKey(property.getCode(), sink);
				}
				else {
					writer.writeKey(property.getPrimaryName(), sink);
				}
				sink = writeProperty(property, propType, propValue, sink);
				size++;
				
			}
			
		}
		
	}
	
	private ByteBuf writeProperty(DattyPersistentProperty property, final Class<?> propType,
			Object propValue, ByteBuf sink) {
		
		return ValueMessageWriter.INSTANCE.writeValue(
				(TypeInfo<Object>)property.getTypeInfo(ENTITY_DISCOVERY), 
				propValue, sink, property.copy());
	}
		
	private void writeCrossEntity(DattyPersistentEntity<?> entity, Object source, DattyRow sink) {
		entity.doWithProperties(new CrossWriteEntityHandler(source, entity.numeric(), sink));
	}	
	
	private Object readProperty(DattyPersistentProperty property, Class<?> propType, ByteBuf buffer) {
		return ValueMessageReader.INSTANCE.readValue(property.getTypeInfo(ENTITY_DISCOVERY), buffer, property.copy());
	}
	
	public final class EntityReader<T> implements ValueReader<T> {
		
		private final Class<T> entityType;
		private final DattyPersistentEntity<T> entity;
		
		public EntityReader(Class<T> entityType, DattyPersistentEntity<T> entity) {
			this.entityType = entityType;
			this.entity = entity;
		}

		@Override
		public T read(ByteBuf buffer, boolean copy) {
			return readFromBuffer(entityType, entity, buffer);
		}

	}
	
	public final class EntityWriter<T> implements ValueWriter<T> {

		private final DattyPersistentEntity<?> entity;
		
		public EntityWriter(DattyPersistentEntity<?> entity) {
			this.entity = entity;
		}
		
		@Override
		public ByteBuf write(T value, ByteBuf sink, boolean copy) {
			return writeEntity(entity, value, sink);
		}
		
	}
	
	public final class EntityTypeInfo<T> implements SimpleTypeInfo<T> {

		private final Class<T> type;
		private final ValueReader<T> reader;
		private final ValueWriter<T> writer;
		
		public EntityTypeInfo(DattyPersistentEntity<T> entity) {
			this.type = entity.getType();
			this.reader = new EntityReader<T>(this.type, entity);
			this.writer = new EntityWriter<T>(entity);
		}
		
		@Override
		public Class<T> getType() {
			return type;
		}

		@Override
		public ValueReader<T> getValueReader() {
			return reader;
		}

		@Override
		public ValueWriter<T> getValueWriter() {
			return writer;
		}

		
	}
	
	public final TypeInfoProvider ENTITY_DISCOVERY = new TypeInfoProvider() {

		@Override
		public <T> TypeInfo<T> getTypeInfo(final Class<T> type) {

			TypeInfo<T> typeInfo = TypeRegistry.findSimple(type);
			
			if (typeInfo == null) {

				DattyPersistentEntity<?> entity = mappingContext.getPersistentEntity(type)
						.orElseThrow(new Supplier<MappingException>() {

							@Override
							public MappingException get() {
								return new MappingException("unknown entity type: " + type);
							}
							
						});
				
				return new EntityTypeInfo(entity);
				
			}
			
			return typeInfo;
			
		}
		
	};
	
	public final class CrossWriteEntityHandler implements PropertyHandler<DattyPersistentProperty> {

		private final BeanWrapper wrapper;
		private final boolean numeric;
		private final DattyRow sink;
		private int size;
		
		public CrossWriteEntityHandler(Object source, boolean numeric, DattyRow sink) {
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.numeric = numeric;
			this.sink = sink;
		}
		
		public int size() {
			return size;
		}

		@Override
		public void doWithPersistentProperty(DattyPersistentProperty property) {
			
			String propName = property.getName();
			final Class<?> propType = property.getRawType();
			Object propValue = wrapper.getPropertyValue(propName);
			
			if (propValue != null && !property.isTransient()) {
				
				String minorKey = MinorKeyFormatter.INSTANCE.getMinorKey(property, numeric);
				
				ByteBuf valueBuffer = sink.addValue(minorKey);
				ByteBuf updatedBuffer = writeProperty(property, propType, propValue, valueBuffer);
				if (updatedBuffer != valueBuffer) {
					sink.putValue(minorKey, updatedBuffer, true);
				}
				size++;
			}
			
		}
		
	}
	
	@Override
	public <R> R read(Class<R> type, DattyRow source) {
		Assert.notNull(type, "type is null");
		Assert.notNull(source, "source is null");
		
		if (source.isEmpty()) {
			return null;
		}
		
		Class<R> beanClassLoaderClass = transformClassToBeanClassLoaderClass(type);
		
		@SuppressWarnings("unchecked")
		DattyPersistentEntity<R> entity = (DattyPersistentEntity<R>) mappingContext
				.getPersistentEntity(beanClassLoaderClass).orElse(null);
		
		if (entity == null) {
			throw new MappingException("No mapping metadata found for " + type);
		}

		if (entity.hasMinorKey()) {
			
			ByteBuf buffer = source.get(entity.getMinorKey());
			if (buffer == null) {
				return null;
			}
			
			return readFromBuffer(type, entity, buffer);
		}
		else {
			
			return readCross(type, entity, source);
			
		}
		
	}

	@SuppressWarnings("unchecked")
	private <R> R readFromBuffer(Class<R> type, DattyPersistentEntity<R> entity, ByteBuf buffer) {
		
		boolean numeric = entity.numeric();
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);
		
		if (!ValueMessageReader.INSTANCE.hasNext(buffer)) {
			return (R) wrapper.getWrappedInstance();
		}
		
		if (!ValueMessageReader.INSTANCE.isMap(buffer)) {
			throw new MappingException("expected message pack string map for " + entity);
		}
		
		int size = ValueMessageReader.INSTANCE.readMapHeader(buffer);
		
		for (int i = 0; i != size; ++i) {
			
			Optional<DattyPersistentProperty> prop;
			
			if (numeric) {
				Integer code = IntegerReader.INSTANCE.read(buffer, true);
				prop = entity.findPropertyByCode(code);
				if (!prop.isPresent()) {
					throw new MappingException("property with code '" + code + "' not found for " + entity);
				}
			}
			else {
				String name = StringReader.INSTANCE.read(buffer, true);
				prop = entity.findPropertyByName(name);
				if (!prop.isPresent()) {
					throw new MappingException("property name '" + name + "' not found for " + entity);
				}
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = readProperty(property, propType, buffer);
			wrapper.setPropertyValue(property.getName(), value);
			
		}
		
		return (R) wrapper.getWrappedInstance();
	}
	
	@SuppressWarnings("unchecked")
	private <R> R readCross(Class<R> type, DattyPersistentEntity<R> entity, DattyRow source) {
		
		boolean numeric = entity.numeric();
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);

		for (Map.Entry<String, ByteBuf> e : source.getValues().entrySet()) {
			
			String name = e.getKey();
			ByteBuf buffer = e.getValue();

			Optional<DattyPersistentProperty> prop;
			if (numeric) {
				int tagNumber;
				try {
					tagNumber = Integer.parseInt(name);
				}
				catch(NumberFormatException nfe) {
					throw new MappingException("invalid tag number for property '" + name + "' in payload for entity" + entity);
				}
				prop = entity.findPropertyByCode(tagNumber);
				if (!prop.isPresent()) {
					throw new MappingException("property with tag '" + tagNumber + "' not found for " + entity);
				}
			}
			else {
				prop = entity.findPropertyByName(name);
				if (!prop.isPresent()) {
					throw new MappingException("property '" + name + "' not found for " + entity);
				}
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = null;
			if (buffer != null) {
				value = readProperty(property, propType, buffer);
			}
			
			wrapper.setPropertyValue(property.getName(), value);
		}
		
		return (R) wrapper.getWrappedInstance();
		
	}
	


	@SuppressWarnings("unchecked")
	private <T> Class<T> transformClassToBeanClassLoaderClass(Class<T> entity) {
		try {
			return (Class<T>) ClassUtils.forName(entity.getName(), beanClassLoader);
		} catch (ClassNotFoundException e) {
			return entity;
		} catch (LinkageError e) {
			return entity;
		}
	}

	@Override
	public String toString() {
		return "DattyMappingConverter [mappingContext=" + mappingContext + ", beanClassLoader=" + beanClassLoader + "]";
	}

}
