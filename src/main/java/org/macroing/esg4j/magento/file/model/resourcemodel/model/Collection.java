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
package org.macroing.esg4j.magento.file.model.resourcemodel.model;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;
import org.macroing.esg4j.magento.database.Table;

public final class Collection {
	private String moduleName;
	private String vendorName;
	private Table table;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Collection() {
		this("Module", "Vendor", new Table());
	}
	
	public Collection(final String moduleName, final String vendorName, final Table table) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
		this.table = Objects.requireNonNull(table, "table == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Model/ResourceModel/%s/Collection.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getTable().getName())));
	}
	
	public PDocument toDocument() {
		final Table table = getTable();
		
		final String modelName = table.getName();
		final String modelNameCamelCase = Strings.formatCamelCase(modelName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final String classNameA = String.format("\\%s\\%s\\Model\\%s", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase);
		final String classNameB = String.format("\\%s\\%s\\Model\\ResourceModel\\%s", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine(String.format("$this->_init(%s::class, %s::class);", classNameA, classNameB));
		pMethod.setName("_construct");
		pMethod.setProtected(true);
		
		final
		PClass pClass = new PClass();
		pClass.addMethod(pMethod);
		pClass.setExtendedClass("AbstractCollection");
		pClass.setName("Collection");
		pClass.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Framework\\Model\\ResourceModel\\Db\\Collection\\AbstractCollection");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Model", "ResourceModel", modelNameCamelCase));
		
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
		return String.format("new Collection(\"%s\", \"%s\", %s)", this.moduleName, this.vendorName, this.table);
	}
	
	public Table getTable() {
		return this.table;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Collection)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Collection.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Collection.class.cast(object).vendorName)) {
			return false;
		} else if(!Objects.equals(this.table, Collection.class.cast(object).table)) {
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