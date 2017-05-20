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
package io.datty.spring.converter.embedded;

import org.springframework.data.annotation.Id;

import io.datty.spring.mapping.Entity;

/**
 * ContainerEntity
 * 
 * @author Alex Shvid
 *
 */

@Entity(cacheName="TEST_CACHE", minorKey="def")
public class SimpleEntity {

	@Id
	private long id;
	
	private EmbeddedEntity embedded;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EmbeddedEntity getEmbedded() {
		return embedded;
	}

	public void setEmbedded(EmbeddedEntity embedded) {
		this.embedded = embedded;
	}

}