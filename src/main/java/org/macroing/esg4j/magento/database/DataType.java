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

public enum DataType {
	BIGINT   ("TYPE_BIGINT",    "int"),
//	BINARY   ("",               ""),
//	BLOB     ("TYPE_BLOB",      ""),
//	CHAR     ("",               "string"),
	DATE     ("TYPE_DATE",      "string"),
	DATETIME ("TYPE_DATETIME",  "string"),
	DECIMAL  ("TYPE_DECIMAL",   "float"),
//	DOUBLE   ("",               "double"),
	FLOAT    ("TYPE_FLOAT",     "float"),
	INT      ("TYPE_INTEGER",   "int"),
//	MEDIUMINT("",               "int"),
	NUMERIC  ("TYPE_NUMERIC",   "int"),
	SMALLINT ("TYPE_SMALLINT",  "int"),
	TEXT     ("TYPE_TEXT",      "string"),
	TIMESTAMP("TYPE_TIMESTAMP", "int"),
	TINYINT  ("TYPE_BOOLEAN",   "bool"),
	VARBINARY("TYPE_VARBINARY", "string");
//	VARCHAR  ("",               "string");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String toMagentoConstant;
	private final String toPHP;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private DataType(final String toMagentoConstant, final String toPHP) {
		this.toMagentoConstant = toMagentoConstant;
		this.toPHP = toPHP;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String toMagentoConstant() {
		return this.toMagentoConstant;
	}
	
	public String toPHP() {
		return this.toPHP;
	}
}