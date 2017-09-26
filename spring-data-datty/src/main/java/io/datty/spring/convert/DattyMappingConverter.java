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

import io.datty.api.ByteBufValue;
import io.datty.api.DattyConstants;
import io.datty.api.DattyRow;
import io.datty.api.DattyValue;
import io.datty.msgpack.MessageWriter;
import io.datty.msgpack.core.MapMessageReader;
import io.datty.msgpack.core.MapMessageWriter;
import io.datty.msgpack.core.ValueMessageReader;
import io.datty.msgpack.core.ValueMessageWriter;
import io.datty.msgpack.core.reader.ValueReader;
import io.datty.msgpack.core.type.SimpleTypeInfo;
import io.datty.msgpack.core.type.TypeInfo;
import io.datty.msgpack.core.type.TypeInfoProvider;
import io.datty.msgpack.core.type.TypeRegistry;
import io.datty.msgpack.core.writer.ValueWriter;
import io.datty.spring.DattySpringConstants;
import io.datty.spring.core.DattyId;
import io.datty.spring.mapping.DattyPersistentEntity;
import io.datty.spring.mapping.DattyPersistentProperty;
import io.datty.spring.mapping.Identifiable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

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

	private static final ByteBufAllocator ALLOC = DattyConstants.ALLOC;
	
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
	
	/**
	 * Gets datty id from the possible id object
	 */
	
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

	/**
	 * Writes entity to the row
	 */
	
	@Override
	public void write(Object source, DattyRow sink) {
		write(source, sink, DattySpringConstants.USE_NUMERIC);
	}
	
	/**
	 * Writes entity to the row
	 */
	
	@Override
	public void write(Object source, DattyRow sink, boolean numeric) {
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


		writeEntity(entity, source, sink, numeric);
		
	}

	/**
	 * Writes embedded entity to the buffer
	 * 
	 * @param entity - entity metadata
	 * @param source - entity instance
	 * @param sink - output buffer
	 * @param numeric - if true use numeric keys
	 * 
	 * @return old or new sink buffer
	 */
	
	private ByteBuf writeEmbeddedEntity(DattyPersistentEntity<?> entity, Object source, final ByteBuf sink, boolean numeric) {
		
		MessageWriter writer = MapMessageWriter.INSTANCE;
		
		int headerIndex = writer.skipHeader(entity.getPropertiesCount(), sink);
		
		WriteEmbeddedEntityHandler entityWriter = new WriteEmbeddedEntityHandler(writer, source, sink, numeric);
		entity.doWithProperties(entityWriter);
		
		writer.writeHeader(entityWriter.size(), entity.getPropertiesCount(), headerIndex, sink);
		
		return entityWriter.getSink();
	}
	
	/**
	 * 
	 * Embedded entity writer
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public final class WriteEmbeddedEntityHandler implements PropertyHandler<DattyPersistentProperty> {

		private final MessageWriter writer;
		private final BeanWrapper wrapper;
		private ByteBuf sink;
		private final boolean numeric;
		private int size;
		
		public WriteEmbeddedEntityHandler(MessageWriter writer, Object source, ByteBuf sink, boolean numeric) {
			this.writer = writer;
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.sink = sink;
			this.numeric = numeric;			
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
				
				sink = writeProperty(property, propType, propValue, sink, numeric);
				size++;
				
			}
			
		}
		
	}
	
	/**
	 * Writes property to the output buffer 
	 * 
	 * @param property - property metadata
	 * @param propType - property class type
	 * @param propValue - property instance
	 * @param sink - output buffer
	 * @param numeric - if true use numeric keys
	 * 
	 * @return old or new sink instance
	 */
	
	private ByteBuf writeProperty(DattyPersistentProperty property, final Class<?> propType,
			Object propValue, 
			ByteBuf sink,
			boolean numeric) {
		
		return ValueMessageWriter.INSTANCE.writeValue(
				(TypeInfo<Object>)property.getTypeInfo(ENTITY_PROVIDER), 
				propValue, sink, property.copy(), numeric);
	}
		
	/**
	 * Writes entity to the row
	 * 
	 * @param entity - entity metadata
	 * @param source - entity instance
	 * @param sink - output row
	 * @param numeric - if true use numeric keys
	 */
	
	private void writeEntity(DattyPersistentEntity<?> entity, Object source, DattyRow sink, boolean numeric) {
		entity.doWithProperties(new WriteEntityHandler(source, sink, numeric));
	}	
	
	/**
	 * Reads property from the input buffer
	 * 
	 * @param property - property metadata
	 * @param propType - property class type
	 * @param buffer - input buffer
	 * 
	 * @return property instance
	 */
	
	private Object readProperty(DattyPersistentProperty property, Class<?> propType, ByteBuf buffer) {
		return ValueMessageReader.INSTANCE.readValue(property.getTypeInfo(ENTITY_PROVIDER), buffer, property.copy());
	}
	
	/**
	 * Reads embedded entity
	 * 
	 * @author Alex Shvid
	 *
	 * @param <T> - entity type
	 */
	
	public final class EmbeddedEntityReader<T> implements ValueReader<T> {
		
		private final Class<T> entityType;
		private final DattyPersistentEntity<T> entity;
		
		public EmbeddedEntityReader(Class<T> entityType, DattyPersistentEntity<T> entity) {
			this.entityType = entityType;
			this.entity = entity;
		}

		@Override
		public T read(ByteBuf buffer, boolean copy) {
			return readEmbeddedEntity(entityType, entity, buffer);
		}

	}
	
	/**
	 * Writes embedded entity to the buffer
	 * 
	 * @author Alex Shvid
	 *
	 * @param <T> - entity type
	 */
	
	public final class EmbeddedEntityWriter<T> implements ValueWriter<T> {

		private final DattyPersistentEntity<?> entity;
		
		public EmbeddedEntityWriter(DattyPersistentEntity<?> entity) {
			this.entity = entity;
		}
		
		@Override
		public ByteBuf write(T value, ByteBuf sink, boolean copy, boolean numeric) {
			return writeEmbeddedEntity(entity, value, sink, numeric);
		}
		
	}
	
	/**
	 * Entity type info
	 * 
	 * Uses for serialization and deserialization of embedded entities
	 * 
	 * @author Alex Shvid
	 *
	 * @param <T> - entity type
	 */
	
	public final class EntityTypeInfo<T> implements SimpleTypeInfo<T> {

		private final Class<T> type;
		private final ValueReader<T> reader;
		private final ValueWriter<T> writer;
		
		public EntityTypeInfo(DattyPersistentEntity<T> entity) {
			this.type = entity.getType();
			this.reader = new EmbeddedEntityReader<T>(this.type, entity);
			this.writer = new EmbeddedEntityWriter<T>(entity);
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
	
	/**
	 * Entity info provider
	 * 
	 * Uses for embedded entities
	 * 
	 */
	
	public final TypeInfoProvider ENTITY_PROVIDER = new TypeInfoProvider() {

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
	
	/**
	 * Writes the entity in to the row
	 * 
	 * @author Alex Shvid
	 *
	 */
	
	public final class WriteEntityHandler implements PropertyHandler<DattyPersistentProperty> {

		private final BeanWrapper wrapper;
		private final DattyRow sink;
		private final boolean numeric;		
		private int size;
		
		public WriteEntityHandler(Object source, DattyRow sink, boolean numeric) {
			this.wrapper = new BeanWrapperImpl(source);		
			wrapper.setConversionService(conversionService);
			this.sink = sink;
			this.numeric = numeric;
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
				
				ByteBuf valueBuffer = writeProperty(property, propType, propValue, ALLOC.buffer(), numeric);
				sink.put(minorKey, new ByteBufValue(valueBuffer));
				size++;
			}
			
		}
		
	}
	
	/**
	 * Reads entity from the row
	 */
	
	@Override
	public <R> R read(Class<R> type, DattyRow source) {
		Assert.notNull(type, "type is null");
		Assert.notNull(source, "source is null");
		
		Class<R> beanClassLoaderClass = transformClassToBeanClassLoaderClass(type);
		
		@SuppressWarnings("unchecked")
		DattyPersistentEntity<R> entity = (DattyPersistentEntity<R>) mappingContext
				.getPersistentEntity(beanClassLoaderClass).orElse(null);
		
		if (entity == null) {
			throw new MappingException("No mapping metadata found for " + type);
		}

		return readEntity(type, entity, source);
		
	}

	/**
	 * Reads embedded entity from the buffer
	 * 
	 * @param type - entity type
	 * @param entity - entity metadata
	 * @param buffer - input buffer
	 * 
	 * @return entity instance
	 */
	
	@SuppressWarnings("unchecked")
	private <R> R readEmbeddedEntity(Class<R> type, DattyPersistentEntity<R> entity, ByteBuf buffer) {
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);
		
		if (!ValueMessageReader.INSTANCE.hasNext(buffer)) {
			return (R) wrapper.getWrappedInstance();
		}
		
		MapMessageReader reader = new MapMessageReader(buffer);
		
		for (int i = 0; i != reader.size(); ++i) {
			
			Optional<DattyPersistentProperty> prop;
			
			Object propKey = reader.readKey(buffer);
			
			if (propKey instanceof Integer) {
				prop = entity.findPropertyByCode((Integer) propKey);
			}
			else if (propKey instanceof String) {
				prop = entity.findPropertyByName((String) propKey);
			}
			else {
				throw new MappingException("unknown property key '" + propKey + "' for " + entity);
			}
			
			if (!prop.isPresent()) {
				throw new MappingException("property key '" + propKey + "' not found for " + entity);
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = readProperty(property, propType, buffer);
			wrapper.setPropertyValue(property.getName(), value);
			
		}
		
		return (R) wrapper.getWrappedInstance();
	}
	
	/**
	 * Reads entity from the row
	 * 
	 * @param type - entity type
	 * @param entity - entity metadata
	 * @param source - input row
	 * 
	 * @return entity instance 
	 */
	
	@SuppressWarnings("unchecked")
	private <R> R readEntity(Class<R> type, DattyPersistentEntity<R> entity, DattyRow source) {
		
		BeanWrapper wrapper = new BeanWrapperImpl(type);

		for (Map.Entry<String, DattyValue> e : source.getValues().entrySet()) {
			
			String name = e.getKey();
			DattyValue dattyValue = e.getValue();

			Optional<DattyPersistentProperty> prop;

			int code;
			try {
				code = Integer.parseInt(name);
				prop = entity.findPropertyByCode(code);
			}
			catch(NumberFormatException nfe) {
				prop = entity.findPropertyByName(name);
			}
			
			if (!prop.isPresent()) {
				throw new MappingException("property key '" + name + "' not found for " + entity);
			}
			
			DattyPersistentProperty property = prop.get();
			final Class<?> propType = property.getRawType();
			
			Object value = null;
			if (dattyValue != null && !dattyValue.isNull()) {
				value = readProperty(property, propType, dattyValue.asByteBuf());
			}
			
			wrapper.setPropertyValue(property.getName(), value);
		}
		
		return (R) wrapper.getWrappedInstance();
		
	}
	
	/**
	 * Uses specific bean class loader to lookup entity metadata
	 * 
	 * @param entity - incoming entity class type
	 * 
	 * @return entity class type found in bean class loader 
	 */

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
