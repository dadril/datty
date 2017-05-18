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

import java.lang.reflect.Array;
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
import io.datty.msgpack.MessageReader;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.ArrayMessageWriter;
import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.StringMapMessageReader;
import io.datty.msgpack.core.ValueMessageReader;
import io.datty.msgpack.core.ValueMessageWriter;
import io.datty.msgpack.core.writer.ValueWriters;
import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import io.datty.spring.mapping.Identifiable;
import io.datty.support.exception.DattyException;
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
			MessageWriter writer = MapMessageWriter.INSTANCE;
			ByteBuf updatedBufer = writeEntity(writer, entity, source, buffer, entity.copy());
			if (updatedBufer != buffer) {
				sink.putValue(entity.getMinorKey(), updatedBufer, false);
			}
		}
		else {
			writeCrossEntity(entity, source, sink, entity.copy());
		}
		
	}

	private ByteBuf writeEntity(MessageWriter writer, DattyPersistentEntity<?> entity, Object source, final ByteBuf sink, boolean copy) {
		
		int headerIndex = writer.skipHeader(entity.getPropertiesCount(), sink);
		
		EntityWriter entityWriter = new EntityWriter(writer, source, sink, copy);
		entity.doWithProperties(entityWriter);
		
		writer.writeHeader(entityWriter.size(), entity.getPropertiesCount(), headerIndex, sink);
		
		return entityWriter.getSink();
	}
	
	public final class EntityWriter implements PropertyHandler<DattyPersistentProperty> {

		private final MessageWriter writer;
		private final BeanWrapper wrapper;
		private ByteBuf sink;
		private int size;
		private final boolean copy;
		
		public EntityWriter(MessageWriter writer, Object source, ByteBuf sink, boolean copy) {
			this.writer = writer;
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.sink = sink;
			this.copy = copy;
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
			
			if (propValue == null) {
				return;
			}

			if (property.isEmbeddedType()) {
				
				final DattyPersistentEntity<?> propEntity = mappingContext.getPersistentEntity(propType)
						.orElseThrow(new Supplier<MappingException>() {

							@Override
							public MappingException get() {
								return new MappingException("unknown embedded entity type: " + propType);
							}
							
						});
				
				writer.writeKey(propName, sink);
				sink = writeEntity(writer, propEntity, propValue, sink, copy);
				size++;
				
			}
			else if (!property.isTransient()) {
							
				writer.writeKey(propName, sink);
				sink = writeValue(writer, property, propType, propValue, sink, copy);
				size++;
				
			}
			
		}
		
	}
	
	private ByteBuf writeValue(MessageWriter writer, DattyPersistentProperty property, Class<?> propType,
			Object propValue, ByteBuf sink, boolean copy) {
		
		if (property.isArray()) {
			return writeArrayValue(property, propType, propValue, sink, copy);
		}
		else {
			return writer.writeValue((Class<Object>)propType, propValue, sink, copy);
		}
		
	}
	
	private ByteBuf writeArrayValue(DattyPersistentProperty property, Class<?> propType,
			Object propValue, ByteBuf sink, boolean copy) {
		
		final Class<?> elementType = property.getComponentType().orElse(null);
		
		if (elementType == null) {
			throw new MappingException("component type not found for: " + property);
		}
		
		DattyPersistentEntity<?> elementEntity = null;
		
		if (ValueWriters.find(elementType) == null) {
			
			elementEntity = mappingContext.getPersistentEntity(elementType)
					.orElseThrow(new Supplier<MappingException>() {

						@Override
						public MappingException get() {
							return new MappingException("unknown embedded entity type: " + elementType);
						}
						
					});
			
		}
		
		int size = Array.getLength(propValue);
		
		ArrayMessageWriter writer = ArrayMessageWriter.INSTANCE;
		
		writer.writeHeader(size, sink);
		
		for (int i = 0; i != size; ++i) {
			
			Object elementValue = Array.get(propValue, i);
			
			if (elementValue == null) {
				writer.writeNull(sink);
			}
			else if (elementEntity != null) {
				sink = writeEntity(writer, elementEntity, elementValue, sink, copy);
			}
			else {
				sink = writer.writeValue((Class<Object>)elementType, elementValue, sink, copy);
			}
			
		}
		
		return sink;
	}
		
	
	private void writeCrossEntity(DattyPersistentEntity<?> entity, Object source, DattyRow sink, boolean copy) {
		
		entity.doWithProperties(new CrossEntityWriter(source, sink, copy));
		
	}	
	
	public final class CrossEntityWriter implements PropertyHandler<DattyPersistentProperty> {

		private final BeanWrapper wrapper;
		private final DattyRow sink;
		private int size;
		private final boolean copy;
		
		public CrossEntityWriter(Object source, DattyRow sink, boolean copy) {
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.sink = sink;
			this.copy = copy;
		}
		
		public int size() {
			return size;
		}

		@Override
		public void doWithPersistentProperty(DattyPersistentProperty property) {
			
			String propName = property.getName();
			final Class<?> propType = property.getRawType();
			Object propValue = wrapper.getPropertyValue(propName);
			
			if (propValue == null) {
				return;
			}
			
			if (property.isEmbeddedType()) {
				
				final DattyPersistentEntity<?> propEntity = mappingContext.getPersistentEntity(propType)
						.orElseThrow(new Supplier<MappingException>() {

							@Override
							public MappingException get() {
								return new MappingException("unknown embedded entity type: " + propType);
							}
							
						});
				
				ByteBuf valueBuffer = sink.addValue(propName);
				MessageWriter writer = MapMessageWriter.INSTANCE;
				ByteBuf updatedBufer = writeEntity(writer, propEntity, propValue, valueBuffer, copy);
				if (updatedBufer != valueBuffer) {
					sink.putValue(propName, updatedBufer, false);
				}
				size++;
				
			}
			else if (!property.isTransient()) {
				ByteBuf valueBuffer = sink.addValue(propName);
				ByteBuf updatedBuffer = writeValue(ValueMessageWriter.INSTANCE, property, propType, propValue, valueBuffer, copy);
				if (updatedBuffer != valueBuffer) {
					sink.putValue(propName, updatedBuffer, copy);
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
			
			return readFromBuffer(type, entity, buffer, entity.copy());
		}
		else {
			
			return readCross(type, entity, source, entity.copy());
			
		}
		
	}

	@SuppressWarnings("unchecked")
	private <R> R readFromBuffer(Class<R> type, DattyPersistentEntity<R> entity, ByteBuf buffer, boolean copy) {
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);
		
		if (!ValueMessageReader.INSTANCE.hasNext(buffer)) {
			return (R) wrapper.getWrappedInstance();
		}
		
		if (!ValueMessageReader.INSTANCE.isMap(buffer)) {
			throw new MappingException("expected message pack string map for " + entity);
		}
		
		int size = ValueMessageReader.INSTANCE.readMapHeader(buffer);
		
		MessageReader<String> reader = new StringMapMessageReader(size);
		
		for (int i = 0; i != size; ++i) {
			
			String propName = reader.readKey(buffer);
			
			Optional<DattyPersistentProperty> prop = entity.getPersistentProperty(propName);
			if (!prop.isPresent()) {
				throw new MappingException("property '" + propName + "' not found for " + entity);
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = null;
			if (property.isEmbeddedType()) {
				
				final DattyPersistentEntity<?> propEntity = mappingContext.getPersistentEntity(propType)
						.orElseThrow(new Supplier<DattyException>() {

							@Override
							public DattyException get() {
								return new DattyException("unknown embedded entity type: " + propType);
							}
							
						});
				
				value = readFromBuffer((Class<Object>)propEntity.getType(), (DattyPersistentEntity<Object>) propEntity, buffer, copy);
			}
			else {
				value = reader.readValue(propType, buffer, copy);
			}
			
			wrapper.setPropertyValue(propName, value);
			
		}
		
		return (R) wrapper.getWrappedInstance();
	}
	
	@SuppressWarnings("unchecked")
	private <R> R readCross(Class<R> type, DattyPersistentEntity<R> entity, DattyRow source, boolean copy) {
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);

		for (Map.Entry<String, ByteBuf> e : source.getValues().entrySet()) {
			
			String propName = e.getKey();
			ByteBuf buffer = e.getValue();

			Optional<DattyPersistentProperty> prop = entity.getPersistentProperty(propName);
			if (!prop.isPresent()) {
				throw new MappingException("property '" + propName + "' not found for " + entity);
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = null;
			if (buffer != null) {
				
				if (property.isEmbeddedType()) {
					
					final DattyPersistentEntity<?> propEntity = mappingContext.getPersistentEntity(propType)
							.orElseThrow(new Supplier<DattyException>() {

								@Override
								public DattyException get() {
									return new DattyException("unknown embedded entity type: " + propType);
								}
								
							});
					
					value = readFromBuffer((Class<Object>)propEntity.getType(), (DattyPersistentEntity<Object>) propEntity, buffer, copy);
					
				}
				else {
					value = ValueMessageReader.INSTANCE.readValue(propType, buffer, copy);
				}
				
			}
			
			wrapper.setPropertyValue(propName, value);
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
