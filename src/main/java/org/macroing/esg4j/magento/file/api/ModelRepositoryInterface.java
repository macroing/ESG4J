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
package org.macroing.esg4j.magento.file.api;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PInterface;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PReturnType;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;
import org.macroing.esg4j.magento.database.Column;
import org.macroing.esg4j.magento.database.Table;

public final class ModelRepositoryInterface {
	private String moduleName;
	private String vendorName;
	private Table table;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ModelRepositoryInterface() {
		this("Module", "Vendor", new Table());
	}
	
	public ModelRepositoryInterface(final String moduleName, final String vendorName, final Table table) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
		this.table = Objects.requireNonNull(table, "table == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Api/%sRepositoryInterface.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getTable().getName())));
	}
	
	public PDocument toDocument() {
		final Table table = getTable();
		
		final List<Column> columns = table.getColumns();
		
		final String modelName = table.getName();
		final String modelNameCamelCase = Strings.formatCamelCase(modelName);
		final String modelNameCamelCaseModified = Strings.formatCamelCaseModified(modelName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PInterface pInterface = new PInterface();
		pInterface.addMethod(PMethod.newInterfaceMethod("delete", new PParameterArgument[] {new PParameterArgument(modelNameCamelCaseModified, PType.valueOf(modelNameCamelCase + "Interface"))}, new PReturnType(PType.BOOL)));
		pInterface.addMethod(PMethod.newInterfaceMethod("getAll", new PParameterArgument[] {}, new PReturnType(PType.ARRAY)));
		pInterface.addMethod(PMethod.newInterfaceMethod("save", new PParameterArgument[] {new PParameterArgument(modelNameCamelCaseModified, PType.valueOf(modelNameCamelCase + "Interface"))}, new PReturnType(PType.BOOL)));
		
		for(final Column column : columns) {
			final String columnName = column.getName();
			final String columnNameCamelCase = Strings.formatCamelCase(columnName);
			final String columnNameCamelCaseModified = Strings.formatCamelCaseModified(columnName);
			final String columnDataType = column.getDataType().toPHP();
			
			final boolean hasDataType = !columnDataType.isEmpty();
			final boolean hasPrimaryKey = column.hasPrimaryKey();
			final boolean hasUniqueKey = column.hasUniqueKey();
			
			final
			PMethod pMethod = new PMethod();
			pMethod.addParameterArgument(new PParameterArgument(columnNameCamelCaseModified, hasDataType ? PType.valueOf(columnDataType) : null));
			pMethod.setEnclosedByInterface(true);
			pMethod.setName("getBy" + columnNameCamelCase);
			pMethod.setReturnType(hasPrimaryKey || hasUniqueKey ? new PReturnType(PType.valueOf(modelNameCamelCase + "Interface"), true) : new PReturnType(PType.ARRAY));
			
			pInterface.addMethod(pMethod);
		}
		
		pInterface.setName(String.format("%sRepositoryInterface", modelNameCamelCase));
		pInterface.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addInterface(pInterface);
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Api"));
		
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
		return String.format("new ModelRepositoryInterface(\"%s\", \"%s\", %s)", this.moduleName, this.vendorName, this.table);
	}
	
	public Table getTable() {
		return this.table;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ModelRepositoryInterface)) {
			return false;
		} else if(!Objects.equals(this.moduleName, ModelRepositoryInterface.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, ModelRepositoryInterface.class.cast(object).vendorName)) {
			return false;
		} else if(!Objects.equals(this.table, ModelRepositoryInterface.class.cast(object).table)) {
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