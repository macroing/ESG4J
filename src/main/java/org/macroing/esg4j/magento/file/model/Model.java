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
package org.macroing.esg4j.magento.file.model;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PConst;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PReturnType;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.php.model.PValue;
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
		return new File(String.format("%s/%s/Model/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getTable().getName())));
	}
	
	public PDocument toDocument() {
		final Table table = getTable();
		
		final List<Column> columns = table.getColumns();
		
		final String modelName = table.getName();
		final String modelNameCamelCase = Strings.formatCamelCase(modelName);
		final String modelNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(modelName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String moduleNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		final String vendorNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(vendorName);
		
		final
		PMethod pMethodConstruct = new PMethod();
		pMethodConstruct.getBlock().addLine(String.format("$this->_init(\\%s\\%s\\Model\\ResourceModel\\%s::class);", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase));
		pMethodConstruct.setName("_construct");
		pMethodConstruct.setProtected(true);
		
		final
		PMethod pMethodGetIdentities = new PMethod();
		pMethodGetIdentities.getBlock().addLine("return [self::CACHE_TAG.'_'.$this->getId()];");
		pMethodGetIdentities.setEnclosedByClass();
		pMethodGetIdentities.setName("getIdentities");
		pMethodGetIdentities.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addConst(new PConst("CACHE_TAG", PValue.valueOf(String.format("%s_%s_%s", vendorNameUnseparatedLowerCase, moduleNameUnseparatedLowerCase, modelNameUnseparatedLowerCase))));
		pClass.addImplementedInterface(modelNameCamelCase + "Interface");
		pClass.addImplementedInterface("IdentityInterface");
		
		final Set<String> doConvertToTypes = new LinkedHashSet<>();
		
		for(final Column column : columns) {
			final String columnName = column.getName();
			final String columnNameCamelCase = Strings.formatCamelCase(columnName);
			final String columnNameCamelCaseModified = Strings.formatCamelCaseModified(columnName);
			final String columnNameUnderscoreSeparatedLowerCase = Strings.formatUnderscoreSeparatedLowerCase(columnName);
			final String columnNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(columnName);
			final String columnDataType = column.getDataType().toPHP();
			
			final PType pType = !columnDataType.isEmpty() && !columnNameCamelCase.equals("Id") ? PType.valueOf(columnDataType) : null;
			
			pClass.addConst(new PConst(String.format("KEY_%s", columnNameUnderscoreSeparatedUpperCase), PValue.valueOf(columnNameUnderscoreSeparatedLowerCase)));
			
			if(!columnNameCamelCase.equals("Id")) {
				final
				PMethod pMethodGet = PMethod.newClassMethodGet(columnNameCamelCase, pType, false);
				pMethodGet.getBlock().clear();
				
				final
				PMethod pMethodSet = PMethod.newClassMethodSet(columnNameCamelCase, pType, null, false);
				pMethodSet.getBlock().clear();
				pMethodSet.getBlock().addLine(String.format("$this->setData(self::KEY_%s, $%s);", columnNameUnderscoreSeparatedUpperCase, columnNameCamelCaseModified));
				
				if(pType != null) {
					doConvertToTypes.add(pType.getName());
					
					pMethodGet.getBlock().addLine(String.format("return self::doConvertTo%s($this->_getData(self::KEY_%s));", Strings.formatCamelCase(pType.getName()), columnNameUnderscoreSeparatedUpperCase));
				} else {
					pMethodGet.getBlock().addLine(String.format("return $this->_getData(self::KEY_%s);", columnNameUnderscoreSeparatedUpperCase));
				}
				
				pClass.addMethod(pMethodGet);
				pClass.addMethod(pMethodSet);
			}
		}
		
		for(final String doConvertToType : doConvertToTypes) {
			final String type = doConvertToType;
			final String typeCamelCase = Strings.formatCamelCase(type);
			
			final
			PMethod pMethodDoConvertToType = new PMethod();
			pMethodDoConvertToType.addParameterArgument(new PParameterArgument("value"));
			pMethodDoConvertToType.addParameterArgument(new PParameterArgument(String.format("default%s", typeCamelCase), PType.valueOf(type), PValue.valueOf(PType.valueOf(type))));
			pMethodDoConvertToType.getBlock().addLine(String.format("return is_%s($value) ? $value : (is_null($value) ? $default%s : %sval($value));", type, typeCamelCase, type.equals("string") ? "str" : type));
			pMethodDoConvertToType.setEnclosedByClass();
			pMethodDoConvertToType.setFinal(true);
			pMethodDoConvertToType.setName(String.format("doConvertTo%s", typeCamelCase));
			pMethodDoConvertToType.setPrivate(true);
			pMethodDoConvertToType.setReturnType(new PReturnType(PType.valueOf(type)));
			pMethodDoConvertToType.setStatic(true);
			
			pClass.addMethod(pMethodDoConvertToType);
		}
		
		pClass.addMethod(pMethodConstruct);
		pClass.addMethod(pMethodGetIdentities);
		pClass.setExtendedClass("AbstractModel");
		pClass.setName(modelNameCamelCase);
		pClass.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Framework\\DataObject\\IdentityInterface");
		pDocument.addUse("Magento\\Framework\\Model\\AbstractModel");
		pDocument.addUse(String.format("%s\\%s\\Api\\%sInterface", vendorNameCamelCase, moduleNameCamelCase, modelNameCamelCase));
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