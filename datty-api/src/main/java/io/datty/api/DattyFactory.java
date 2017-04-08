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
package io.datty.api;

import io.datty.support.exception.DattyFactoryException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Datty Factory interface, creator of datty instance
 * 
 * @author dadril
 *
 */

public interface DattyFactory {

	/**
	 * Gets the unique name of the factory
	 * 
	 * @return not null name
	 */

	String getFactoryName();

	/**
	 * Creates new instance of the datty store manager
	 * 
	 * @param props
	 *            - not null props
	 * @return not null instance
	 * @throws IOException
	 */

	Datty newInstance(Properties props) throws IOException;

	/**
	 * Locator class to search and select factories
	 * 
	 * @author dadril
	 *
	 */

	public static final class Locator {

		private final ServiceLoader<DattyFactory> serviceLoader;

		/**
		 * Creates locator with a current class loader
		 */

		public Locator() {
			this.serviceLoader = ServiceLoader.load(DattyFactory.class);
		}

		/**
		 * Creates locator with the given class loader
		 * 
		 * @param classLoader
		 *            - class loader for class scanning
		 */

		public Locator(ClassLoader classLoader) {
			this.serviceLoader = ServiceLoader.load(DattyFactory.class,
					classLoader);
		}

		/**
		 * Gets any factory
		 * 
		 * @return not null factory
		 * @throws DattyFactoryException
		 *             if not found
		 */

		public DattyFactory getAny() {
			for (DattyFactory f : serviceLoader) {
				return f;
			}
			throw new DattyFactoryException(
					"there are no DattyFactory implementations in the classpath");
		}

		/**
		 * Gets store factory from classpath, make sure that it is alone
		 * 
		 * @return not null factory
		 * @throws DattyFactoryException
		 *             if not found or non unique
		 */

		public DattyFactory getSingle() {
			DattyFactory found = null;
			for (DattyFactory f : serviceLoader) {
				if (found == null) {
					found = f;
				} else {
					throw new DattyFactoryException(
							"expected unique DattyFactory implementation in the classpath");
				}
			}
			if (found != null) {
				return found;
			}
			throw new DattyFactoryException(
					"there is no DattyFactory implementation in the classpath");
		}

		/**
		 * Gets factory by name
		 * 
		 * @param name
		 *            - case sensitive name of the store
		 * @return not null factory
		 * @throws DattyFactoryException
		 *             if not found
		 */

		public DattyFactory getByName(String name) {
			for (DattyFactory f : serviceLoader) {
				if (f.getFactoryName().equals(name)) {
					return f;
				}
			}
			throw new DattyFactoryException(
					"there is no DattyFactory implementation in the classpath with a name '"
							+ name + "'");
		}

		/**
		 * Returns all available factories
		 * 
		 * @return not null set
		 */

		public Set<String> getAvailableFactories() {
			Set<String> set = new HashSet<String>();
			for (DattyFactory f : serviceLoader) {
				set.add(f.getFactoryName());
			}
			return set;
		}

	}

}
