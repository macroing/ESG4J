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
package org.macroing.esg4j.magento.file.model;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PConstructor;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PField;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PReturnType;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;
import org.macroing.esg4j.magento.database.Column;
import org.macroing.esg4j.magento.database.Table;

public final class ModelRepository {
	private String moduleName;
	private String vendorName;
	private Table table;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ModelRepository() {
		this("Module", "Vendor", new Table());
	}
	
	public ModelRepository(final String moduleName, final String vendorName, final Table table) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
		this.table = Objects.requireNonNull(table, "table == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Model/%sRepository.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getTable().getName())));
	}
	
	public PDocument toDocument() {
		final Table table = getTable();
		
		final List<Column> columns = table.getColumns();
		final List<Column> columnsPrimaryKey = table.getColumnsPrimaryKey();
		
		final String modelName = table.getName();
		final String modelNameCamelCase = Strings.formatCamelCase(modelName);
		final String modelNameCamelCaseModified = Strings.formatCamelCaseModified(modelName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		if(columnsPrimaryKey.size() != 1) {
			throw new IllegalArgumentException(String.format("Table \"%s\" does not have exactly one column with a primary key.", table.getName()));
		}
		
		final Column columnPrimaryKey = columnsPrimaryKey.get(0);
		
		final String columnPrimaryKeyName = columnPrimaryKey.getName();
		final String columnPrimaryKeyNameCamelCase = Strings.formatCamelCase(columnPrimaryKeyName);
		final String columnPrimaryKeyNameCamelCaseModified = Strings.formatCamelCaseModified(columnPrimaryKeyName);
		
		final
		PField pFieldModelFactory = new PField();
		pFieldModelFactory.setName(modelNameCamelCaseModified + "Factory");
		pFieldModelFactory.setPrivate(true);
		
		final
		PField pFieldModelResourceModel = new PField();
		pFieldModelResourceModel.setName(modelNameCamelCaseModified + "ResourceModel");
		pFieldModelResourceModel.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument(modelNameCamelCaseModified + "Factory", PType.valueOf(modelNameCamelCase + "Factory")));
		pConstructor.addParameterArgument(new PParameterArgument(modelNameCamelCaseModified + "ResourceModel", PType.valueOf(modelNameCamelCase + "ResourceModel")));
		pConstructor.getBlock().addLinef("$this->%sFactory = $%sFactory;", modelNameCamelCaseModified, modelNameCamelCaseModified);
		pConstructor.getBlock().addLinef("$this->%sResourceModel = $%sResourceModel;", modelNameCamelCaseModified, modelNameCamelCaseModified);
		pConstructor.setPublic(true);
		
		final
		PMethod pMethodDelete = new PMethod();
		pMethodDelete.addParameterArgument(new PParameterArgument(modelNameCamelCaseModified, PType.valueOf(modelNameCamelCase + "Interface")));
		pMethodDelete.getBlock().addLine("try {");
		pMethodDelete.getBlock().addLinef("	$this->%sResourceModel->delete($%s);", modelNameCamelCaseModified, modelNameCamelCaseModified);
		pMethodDelete.getBlock().addLine("	");
		pMethodDelete.getBlock().addLine("	return true;");
		pMethodDelete.getBlock().addLine("} catch(Exception $e) {");
		pMethodDelete.getBlock().addLine("	return false;");
		pMethodDelete.getBlock().addLine("}");
		pMethodDelete.setName("delete");
		pMethodDelete.setPublic(true);
		pMethodDelete.setReturnType(new PReturnType(PType.BOOL));
		
		final
		PMethod pMethodGetAll = new PMethod();
		pMethodGetAll.getBlock().addLinef("$%ss = array();", modelNameCamelCaseModified);
		pMethodGetAll.getBlock().addLine("");
		pMethodGetAll.getBlock().addLinef("foreach($this->%sResourceModel->get%ss() as $%s) {", modelNameCamelCaseModified, columnPrimaryKeyNameCamelCase, columnPrimaryKeyNameCamelCaseModified);
		pMethodGetAll.getBlock().addLinef("	$%ss[] = $this->getBy%s($%s);", modelNameCamelCaseModified, columnPrimaryKeyNameCamelCase, columnPrimaryKeyNameCamelCaseModified);
		pMethodGetAll.getBlock().addLine("}");
		pMethodGetAll.getBlock().addLine("");
		pMethodGetAll.getBlock().addLinef("return $%ss;", modelNameCamelCaseModified);
		pMethodGetAll.setName("getAll");
		pMethodGetAll.setPublic(true);
		pMethodGetAll.setReturnType(new PReturnType(PType.ARRAY));
		
		final
		PMethod pMethodSave = new PMethod();
		pMethodSave.addParameterArgument(new PParameterArgument(modelNameCamelCaseModified, PType.valueOf(modelNameCamelCase + "Interface")));
		pMethodSave.getBlock().addLine("try {");
		pMethodSave.getBlock().addLinef("	$this->%sResourceModel->save($%s);", modelNameCamelCaseModified, modelNameCamelCaseModified);
		pMethodSave.getBlock().addLine("	");
		pMethodSave.getBlock().addLine("	return true;");
		pMethodSave.getBlock().addLine("} catch(Exception $e) {");
		pMethodSave.getBlock().addLine("	return false;");
		pMethodSave.getBlock().addLine("}");
		pMethodSave.setName("save");
		pMethodSave.setPublic(true);
		pMethodSave.setReturnType(new PReturnType(PType.BOOL));
		
		final
		PClass pClass = new PClass();
		pClass.addImplementedInterface(modelNameCamelCase + "RepositoryInterface");
		pClass.addField(pFieldModelFactory);
		pClass.addField(pFieldModelResourceModel);
		pClass.addMethod(pMethodDelete);
		pClass.addMethod(pMethodGetAll);
		pClass.addMethod(pMethodSave);
		
		for(final Column column : columns) {
			final String columnName = column.getName();
			final String columnNameCamelCase = Strings.formatCamelCase(columnName);
			final String columnNameCamelCaseModified = Strings.formatCamelCaseModified(columnName);
			final String columnDataType = column.getDataType().toPHP();
			
			final boolean hasDataType = !columnDataType.isEmpty();
			final boolean hasPrimaryKey = column.hasPrimaryKey();
			final boolean hasUniqueKey = column.hasUniqueKey();
			
			final
			PMethod pMethodGetByX = new PMethod();
			pMethodGetByX.addParameterArgument(new PParameterArgument(columnNameCamelCaseModified, hasDataType ? PType.valueOf(columnDataType) : null));
			
			if(hasPrimaryKey || hasUniqueKey) {
				pMethodGetByX.getBlock().addLinef("$%s = $this->%sFactory->create();", modelNameCamelCaseModified, modelNameCamelCaseModified);
				pMethodGetByX.getBlock().addLine("");
				pMethodGetByX.getBlock().addLinef("$this->%sResourceModel->load($%s, $%s);", modelNameCamelCaseModified, modelNameCamelCaseModified, columnNameCamelCaseModified);
				pMethodGetByX.getBlock().addLine("");
				pMethodGetByX.getBlock().addLinef("if(!$%s->get%s()) {", modelNameCamelCaseModified, columnNameCamelCase);
				pMethodGetByX.getBlock().addLine("	return null;");
				pMethodGetByX.getBlock().addLine("}");
				pMethodGetByX.getBlock().addLine("");
				pMethodGetByX.getBlock().addLinef("return $%s;", modelNameCamelCaseModified);
			} else {
				pMethodGetByX.getBlock().addLinef("$%ss = array();", modelNameCamelCaseModified);
				pMethodGetByX.getBlock().addLine("");
				pMethodGetByX.getBlock().addLinef("foreach($this->%sResourceModel->get%ssBy%s($%s) as $%s) {", modelNameCamelCaseModified, columnPrimaryKeyNameCamelCase, columnNameCamelCase, columnNameCamelCaseModified, columnPrimaryKeyNameCamelCaseModified);
				pMethodGetByX.getBlock().addLinef("	$%ss[] = $this->getBy%s($%s);", modelNameCamelCaseModified, columnPrimaryKeyNameCamelCase, columnPrimaryKeyNameCamelCaseModified);
				pMethodGetByX.getBlock().addLine("}");
				pMethodGetByX.getBlock().addLine("");
				pMethodGetByX.getBlock().addLinef("return $%ss;", modelNameCamelCaseModified);
			}
			
			pMethodGetByX.setEnclosedByClass();
			pMethodGetByX.setName("getBy" + columnNameCamelCase);
			pMethodGetByX.setPublic(true);
			pMethodGetByX.setReturnType(hasPrimaryKey || hasUniqueKey ? new PReturnType(PType.valueOf(modelNameCamelCase + "Interface"), true) : new PReturnType(PType.ARRAY));
			
			pClass.addMethod(pMethodGetByX);
		}
		
		pClass.setConstructor(pConstructor);
		pClass.setName(modelNameCamelCase + "Repository");
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Exception");
		pDocument.addUse("Magento\\Framework\\Exception\\NoSuchEntityException");
		pDocument.addUse(String.format("%s\\%s\\Api\\%sInterface", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase));
		pDocument.addUse(String.format("%s\\%s\\Api\\%sRepositoryInterface", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase));
		pDocument.addUse(String.format("%s\\%s\\Model\\ResourceModel\\%s as %sResourceModel", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase, modelNameCamelCase));
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Model"));
		
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
		} else if(!(object instanceof ModelRepository)) {
			return false;
		} else if(!Objects.equals(this.moduleName, ModelRepository.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, ModelRepository.class.cast(object).vendorName)) {
			return false;
		} else if(!Objects.equals(this.table, ModelRepository.class.cast(object).table)) {
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