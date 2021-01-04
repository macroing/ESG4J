/**
 * Copyright 2019 - 2021 J&#246;rgen Lundgren
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
package org.macroing.esg4j.magento.file.model.resourcemodel;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PReturnType;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;
import org.macroing.esg4j.magento.database.Column;
import org.macroing.esg4j.magento.database.Table;

public final class Model {
	private String moduleName;
	private String vendorName;
	private Table table;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Model() {
		this("Module", "Vendor", new Table());
	}
	
	public Model(final String moduleName, final String vendorName, final Table table) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
		this.table = Objects.requireNonNull(table, "table == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Model/ResourceModel/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getTable().getName())));
	}
	
	public PDocument toDocument() {
		final Table table = getTable();
		
		final List<Column> columns = table.getColumns();
		final List<Column> columnsPrimaryKey = table.getColumnsPrimaryKey();
		
		final String modelName = table.getName();
		final String modelNameCamelCase = Strings.formatCamelCase(modelName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String moduleNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		final String vendorNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(vendorName);
		
		if(columnsPrimaryKey.size() != 1) {
			throw new IllegalArgumentException(String.format("Table \"%s\" does not have exactly one column with a primary key.", table.getName()));
		}
		
		final Column columnPrimaryKey = columnsPrimaryKey.get(0);
		
		final String columnPrimaryKeyName = columnPrimaryKey.getName();
		final String columnPrimaryKeyNameCamelCase = Strings.formatCamelCase(columnPrimaryKeyName);
		final String columnPrimaryKeyNameCamelCaseModified = Strings.formatCamelCaseModified(columnPrimaryKeyName);
		final String columnPrimaryKeyNameUnderscoreSeparatedLowerCase = Strings.formatUnderscoreSeparatedLowerCase(columnPrimaryKeyName);
		
		final
		PMethod pMethodConstruct = new PMethod();
		pMethodConstruct.getBlock().addLinef("$this->_init('%s_%s_%s', '%s');", vendorNameUnseparatedLowerCase, moduleNameUnseparatedLowerCase, modelName, columnPrimaryKey.getName());
		pMethodConstruct.setEnclosedByClass();
		pMethodConstruct.setName("_construct");
		pMethodConstruct.setProtected(true);
		
		final
		PMethod pMethodGetIds = new PMethod();
		pMethodGetIds.getBlock().addLine("$connection = $this->getConnection();");
		pMethodGetIds.getBlock().addLine("");
		pMethodGetIds.getBlock().addLinef("$select = $connection->select()->from($this->getMainTable(), '%s');", columnPrimaryKeyNameUnderscoreSeparatedLowerCase);
		pMethodGetIds.getBlock().addLine("");
		pMethodGetIds.getBlock().addLine("$elements = $connection->fetchAll($select);");
		pMethodGetIds.getBlock().addLine("");
		pMethodGetIds.getBlock().addLine("$ids = array();");
		pMethodGetIds.getBlock().addLine("");
		pMethodGetIds.getBlock().addLine("foreach($elements as $key => $value) {");
		pMethodGetIds.getBlock().addLinef("	$ids[] = intval($value['%s']);", columnPrimaryKeyNameUnderscoreSeparatedLowerCase);
		pMethodGetIds.getBlock().addLine("}");
		pMethodGetIds.getBlock().addLine("");
		pMethodGetIds.getBlock().addLine("return $ids;");
		pMethodGetIds.setEnclosedByClass();
		pMethodGetIds.setName(String.format("get%ss", columnPrimaryKeyNameCamelCase));
		pMethodGetIds.setPublic(true);
		pMethodGetIds.setReturnType(new PReturnType(PType.ARRAY));
		
		final
		PClass pClass = new PClass();
		pClass.addMethod(pMethodConstruct);
		pClass.addMethod(pMethodGetIds);
		
		for(final Column column : columns) {
			if(column.equals(columnPrimaryKey)) {
				continue;
			}
			
			final String columnName = column.getName();
			final String columnNameCamelCase = Strings.formatCamelCase(columnName);
			final String columnNameCamelCaseModified = Strings.formatCamelCaseModified(columnName);
			final String columnNameUnderscoreSeparatedLowerCase = Strings.formatUnderscoreSeparatedLowerCase(columnName);
			
			final String columnDataType = column.getDataType().toPHP();
			
			final PType pType = !columnDataType.isEmpty() ? PType.valueOf(columnDataType) : null;
			
			final
			PMethod pMethodGetXsByY = new PMethod();
			pMethodGetXsByY.addParameterArgument(new PParameterArgument(columnNameCamelCaseModified, pType));
			pMethodGetXsByY.getBlock().addLine("$connection = $this->getConnection();");
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLinef("$select = $connection->select()->from($this->getMainTable(), '%s')->where('%s = :%s');", columnPrimaryKeyNameUnderscoreSeparatedLowerCase, columnNameUnderscoreSeparatedLowerCase, columnNameUnderscoreSeparatedLowerCase);
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLinef("$bind = [':%s' => strval($%s)];", columnNameUnderscoreSeparatedLowerCase, columnNameCamelCaseModified);
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLine("$elements = $connection->fetchAll($select, $bind);");
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLinef("$%ss = array();", columnPrimaryKeyNameCamelCaseModified);
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLine("foreach($elements as $key => $value) {");
			pMethodGetXsByY.getBlock().addLinef("	$%ss[] = intval($value['%s']);", columnPrimaryKeyNameCamelCaseModified, columnPrimaryKeyNameUnderscoreSeparatedLowerCase);
			pMethodGetXsByY.getBlock().addLine("}");
			pMethodGetXsByY.getBlock().addLine("");
			pMethodGetXsByY.getBlock().addLinef("return $%ss;", columnPrimaryKeyNameCamelCaseModified);
			pMethodGetXsByY.setEnclosedByClass();
			pMethodGetXsByY.setName(String.format("get%ssBy%s", columnPrimaryKeyNameCamelCase, columnNameCamelCase));
			pMethodGetXsByY.setPublic(true);
			pMethodGetXsByY.setReturnType(new PReturnType(PType.ARRAY));
			
			pClass.addMethod(pMethodGetXsByY);
		}
		
		pClass.setExtendedClass("AbstractDb");
		pClass.setName(modelNameCamelCase);
		pClass.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Framework\\Model\\ResourceModel\\Db\\AbstractDb");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Model", "ResourceModel"));
		
		return pDocument;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new ModelRepository(\"%s\", \"%s\", %s)", this.moduleName, this.vendorName, this.table);
	}
	
	public Table getTable() {
		return this.table;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Model)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Model.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Model.class.cast(object).vendorName)) {
			return false;
		} else if(!Objects.equals(this.table, Model.class.cast(object).table)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.moduleName, this.vendorName, this.table);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setModuleName(final String moduleName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
	}
	
	public void setTable(final Table table) {
		this.table = Objects.requireNonNull(table, "table == null");
	}
	
	public void setVendorName(final String vendorName) {
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doWriteToFile(final File file, final String string) {
		file.getParentFile().mkdirs();
		
		try {
			Files.write(file.toPath(), string.getBytes("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}