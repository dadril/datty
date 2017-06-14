package io.datty.spring.convert;

import io.datty.spring.mapping.DattyPersistentProperty;

/**
 * MinorKeyConverter
 * 
 * @author Alex Shvid
 *
 */

public enum MinorKeyFormatter {

	INSTANCE;
	
	public String getMinorKey(DattyPersistentProperty property, boolean numeric) {
		return numeric ? Integer.toString(property.getCode()) : property.getPrimaryName();
	}
	
}
