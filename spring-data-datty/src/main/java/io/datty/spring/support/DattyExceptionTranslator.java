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
package io.datty.spring.support;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * DattyExceptionTranslator
 * 
 * Transforms internal Datty exceptions to DattyAccess exceptions that are
 * part of SpringData exceptions hierarchy
 * 
 * @author Alex Shvid
 *
 */

public class DattyExceptionTranslator implements PersistenceExceptionTranslator {

	public DataAccessException translateExceptionIfPossible(RuntimeException x) {

		if (x instanceof DataAccessException) {
			return (DataAccessException) x;
		}

		if (x instanceof DattyAccessException) {
			return (DattyAccessException) x;
		}

		return new DattyAccessException(x.getMessage(), x);
	}

}
