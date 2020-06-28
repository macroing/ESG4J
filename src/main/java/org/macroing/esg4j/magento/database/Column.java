/**
 * Copyright 2019 - 2020 J&#246;rgen Lundgren
 * 
 * This file is part of org.macroing.esg4j.
 * 
 * org.macroing.esg4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.macroing.esg4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with org.macroing.esg4j. If not, see <http://www.gnu.org/licenses/>.
 */
package org.macroing.esg4j.magento.database;

import java.util.Objects;

public final class Column {
	private DataType dataType;
	private Object defaultValue;
	private String comment;
	private String name;
	private boolean hasAutoIncrement;
	private boolean hasDefault;
	private boolean hasForeignKey;
	private boolean hasPrimaryKey;
	private boolean hasUniqueKey;
	private boolean isNullable;
	private boolean isUnsigned;
	private int length;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Column() {
		this.dataType = DataType.INT;
		this.defaultValue = null;
		this.comment = "";
		this.name = "id";
		this.hasAutoIncrement = true;
		this.hasDefault = false;
		this.hasForeignKey = false;
		this.hasPrimaryKey = true;
		this.hasUniqueKey = false;
		this.isNullable = false;
		this.isUnsigned = true;
		this.length = 11;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public DataType getDataType() {
		return this.dataType;
	}
	
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Column)) {
			return false;
		} else if(!Objects.equals(this.dataType, Column.class.cast(object).dataType)) {
			return false;
		} else if(!Objects.equals(this.defaultValue, Column.class.cast(object).defaultValue)) {
			return false;
		} else if(!Objects.equals(this.comment, Column.class.cast(object).comment)) {
			return false;
		} else if(!Objects.equals(this.name, Column.class.cast(object).name)) {
			return false;
		} else if(this.hasAutoIncrement != Column.class.cast(object).hasAutoIncrement) {
			return false;
		} else if(this.hasDefault != Column.class.cast(object).hasDefault) {
			return false;
		} else if(this.hasForeignKey != Column.class.cast(object).hasForeignKey) {
			return false;
		} else if(this.hasPrimaryKey != Column.class.cast(object).hasPrimaryKey) {
			return false;
		} else if(this.hasUniqueKey != Column.class.cast(object).hasUniqueKey) {
			return false;
		} else if(this.isNullable != Column.class.cast(object).isNullable) {
			return false;
		} else if(this.isUnsigned != Column.class.cast(object).isUnsigned) {
			return false;
		} else if(this.length != Column.class.cast(object).length) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasAutoIncrement() {
		return this.hasAutoIncrement;
	}
	
	public boolean hasDefault() {
		return this.hasDefault;
	}
	
	public boolean hasForeignKey() {
		return this.hasForeignKey;
	}
	
	public boolean hasPrimaryKey() {
		return this.hasPrimaryKey;
	}
	
	public boolean hasUniqueKey() {
		return this.hasUniqueKey;
	}
	
	public boolean isNullable() {
		return this.isNullable;
	}
	
	public boolean isUnsigned() {
		return this.isUnsigned;
	}
	
	public int getLength() {
		return this.length;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.dataType, this.defaultValue, this.comment, this.name, Boolean.valueOf(this.hasAutoIncrement), Boolean.valueOf(this.hasDefault), Boolean.valueOf(this.hasForeignKey), Boolean.valueOf(this.hasPrimaryKey), Boolean.valueOf(this.hasUniqueKey), Boolean.valueOf(this.isNullable), Boolean.valueOf(this.isUnsigned), Integer.valueOf(this.length));
	}
	
	public void setAutoIncrement(final boolean hasAutoIncrement) {
		this.hasAutoIncrement = hasAutoIncrement;
	}
	
	public void setComment(final String comment) {
		this.comment = Objects.requireNonNull(comment, "comment == null");
	}
	
	public void setDataType(final DataType dataType) {
		this.dataType = Objects.requireNonNull(dataType, "dataType == null");
	}
	
	public void setDefault(final boolean hasDefault) {
		this.hasDefault = hasDefault;
	}
	
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void setForeignKey(final boolean hasForeignKey) {
		this.hasForeignKey = hasForeignKey;
	}
	
	public void setLength(final int length) {
		this.length = length;
	}
	
	public void setName(final String name) {
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	public void setNullable(final boolean isNullable) {
		this.isNullable = isNullable;
	}
	
	public void setPrimaryKey(final boolean hasPrimaryKey) {
		this.hasPrimaryKey = hasPrimaryKey;
	}
	
	public void setUniqueKey(final boolean hasUniqueKey) {
		this.hasUniqueKey = hasUniqueKey;
	}
	
	public void setUnsigned(final boolean isUnsigned) {
		this.isUnsigned = isUnsigned;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Column newIntColumn(final Integer defaultValue, final String comment, final String name, final boolean hasAutoIncrement, final boolean hasDefault, final boolean hasForeignKey, final boolean hasPrimaryKey, final boolean hasUniqueKey, final boolean isNullable, final boolean isUnsigned, final int length) {
		final
		Column column = new Column();
		column.setAutoIncrement(hasAutoIncrement);
		column.setComment(comment);
		column.setDataType(DataType.INT);
		column.setDefault(hasDefault);
		column.setDefaultValue(defaultValue);
		column.setForeignKey(hasForeignKey);
		column.setLength(length);
		column.setName(name);
		column.setNullable(isNullable);
		column.setPrimaryKey(hasPrimaryKey);
		column.setUniqueKey(hasUniqueKey);
		column.setUnsigned(isUnsigned);
		
		return column;
	}
	
	public static Column newTextColumn(final String defaultValue, final String comment, final String name, final boolean hasDefault, final boolean hasPrimaryKey, final boolean hasUniqueKey, final boolean isNullable, final int length) {
		final
		Column column = new Column();
		column.setAutoIncrement(false);
		column.setComment(comment);
		column.setDataType(DataType.TEXT);
		column.setDefault(hasDefault);
		column.setDefaultValue(defaultValue);
		column.setLength(length);
		column.setName(name);
		column.setNullable(isNullable);
		column.setPrimaryKey(hasPrimaryKey);
		column.setUniqueKey(hasUniqueKey);
		column.setUnsigned(false);
		
		return column;
	}
}