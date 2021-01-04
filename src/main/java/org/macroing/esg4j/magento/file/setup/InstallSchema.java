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
package org.macroing.esg4j.magento.file.setup;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

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
import org.macroing.esg4j.magento.database.Database;
import org.macroing.esg4j.magento.database.Table;

public final class InstallSchema {
	private Database database;
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public InstallSchema() {
		this(new Database(), "Module", "Vendor");
	}
	
	public InstallSchema(final Database database, final String moduleName, final String vendorName) {
		this.database = Objects.requireNonNull(database, "database == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Database getDatabase() {
		return this.database;
	}
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Setup/InstallSchema.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName())));
	}
	
	public PDocument toDocument() {
		final Database database = getDatabase();
		
		final List<Table> tables = database.getTables();
		
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String moduleNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		final String vendorNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(vendorName);
		
		final
		PMethod pMethodInstall = new PMethod();
		pMethodInstall.addParameterArgument(new PParameterArgument("schemaSetup", PType.valueOf("SchemaSetupInterface")));
		pMethodInstall.addParameterArgument(new PParameterArgument("moduleContext", PType.valueOf("ModuleContextInterface")));
		pMethodInstall.getBlock().addLine("try {");
		pMethodInstall.getBlock().addLine("	$schemaSetup->startSetup();");
		pMethodInstall.getBlock().addLine("	");
		pMethodInstall.getBlock().addLine("	$connection = $schemaSetup->getConnection();");
		pMethodInstall.getBlock().addLine("	");
		
		for(final Table table : tables) {
			final List<Column> columns = table.getColumns();
			
			final String tableName = table.getName();
			final String tableNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(tableName);
			
			pMethodInstall.getBlock().addLinef  ("	if(!$schemaSetup->tableExists(self::TABLE_%s)) {", tableNameUnderscoreSeparatedUpperCase);
			pMethodInstall.getBlock().addComment("		Initialize Table:");
			pMethodInstall.getBlock().addLinef  ("		$table = $connection->newTable($schemaSetup->getTable(self::TABLE_%s));", tableNameUnderscoreSeparatedUpperCase);
			pMethodInstall.getBlock().addLine   ("		");
			pMethodInstall.getBlock().addComment("		Add Columns:");
			
			final int maximumColumnNameLength = doGetMaximumColumnNameLength(columns);
			final int maximumColumnDataTypeLength = doGetMaximumColumnDataTypeLength(columns);
			
			for(final Column column : columns) {
				final String columnComment = column.getComment();
				final String columnName = column.getName();
				final String columnNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(columnName);
				final String columnType = column.getDataType().toMagentoConstant();
				
				final int currentColumnNameLength = columnNameUnderscoreSeparatedUpperCase.length();
				final int currentColumnDataTypeLength = columnType.length();
				
				final String spaces0 = doRepeat(" ", maximumColumnNameLength - currentColumnNameLength);
				final String spaces1 = doRepeat(" ", maximumColumnDataTypeLength - currentColumnDataTypeLength);
				
				pMethodInstall.getBlock().addLinef("		$table->addColumn(self::TABLE_%s_COLUMN_%s,%s Table::%s,%s null, self::doCreateArray(true, false, true, true), '%s');", tableNameUnderscoreSeparatedUpperCase, columnNameUnderscoreSeparatedUpperCase, spaces0, columnType, spaces1, columnComment);
			}
			
			pMethodInstall.getBlock().addLine   ("		");
			pMethodInstall.getBlock().addComment("		Add Foreign Keys:");
			pMethodInstall.getBlock().addLine   ("		");
			pMethodInstall.getBlock().addComment("		Set Comment:");
			pMethodInstall.getBlock().addLinef  ("		$table->setComment(self::TABLE_%s_COMMENT);", tableNameUnderscoreSeparatedUpperCase);
			pMethodInstall.getBlock().addLine   ("		");
			pMethodInstall.getBlock().addComment("		Create Table:");
			pMethodInstall.getBlock().addLine   ("		$connection->createTable($table);");
			pMethodInstall.getBlock().addLine   ("	}");
			pMethodInstall.getBlock().addLine   ("	");
		}
		
		pMethodInstall.getBlock().addLine("	$schemaSetup->endSetup();");
		pMethodInstall.getBlock().addLine("} catch(Exception $e) {");
		pMethodInstall.getBlock().addLine("	");
		pMethodInstall.getBlock().addLine("}");
		pMethodInstall.setName("install");
		pMethodInstall.setPublic(true);
		
		final
		PMethod pMethodDoCreateArray = new PMethod();
		pMethodDoCreateArray.addParameterArgument(new PParameterArgument("isIdentity", PType.BOOL));
		pMethodDoCreateArray.addParameterArgument(new PParameterArgument("isNullable", PType.BOOL));
		pMethodDoCreateArray.addParameterArgument(new PParameterArgument("isPrimary", PType.BOOL));
		pMethodDoCreateArray.addParameterArgument(new PParameterArgument("isUnsigned", PType.BOOL));
		pMethodDoCreateArray.getBlock().addLine("$array             = array();");
		pMethodDoCreateArray.getBlock().addLine("$array['identity'] = $isIdentity;");
		pMethodDoCreateArray.getBlock().addLine("$array['nullable'] = $isNullable;");
		pMethodDoCreateArray.getBlock().addLine("$array['primary']  = $isPrimary;");
		pMethodDoCreateArray.getBlock().addLine("$array['unsigned'] = $isUnsigned;");
		pMethodDoCreateArray.getBlock().addLine("");
		pMethodDoCreateArray.getBlock().addLine("return $array;");
		pMethodDoCreateArray.setName("doCreateArray");
		pMethodDoCreateArray.setPrivate(true);
		pMethodDoCreateArray.setReturnType(new PReturnType(PType.ARRAY));
		pMethodDoCreateArray.setStatic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addImplementedInterface("InstallSchemaInterface");
		pClass.addMethod(pMethodInstall);
		pClass.addMethod(pMethodDoCreateArray);
		pClass.setName("InstallSchema");
		
		for(final Table table : tables) {
			final List<Column> columns = table.getColumns();
			
			final String tableName = table.getName();
			final String tableNameUnderscoreSeparatedLowerCase = Strings.formatUnderscoreSeparatedLowerCase(tableName);
			final String tableNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(tableName);
			
			final PConst pConstTableX = new PConst(String.format("TABLE_%s", tableNameUnderscoreSeparatedUpperCase), PValue.valueOf(String.format("%s_%s_%s", vendorNameUnseparatedLowerCase, moduleNameUnseparatedLowerCase, tableNameUnderscoreSeparatedLowerCase)));
			final PConst pConstTableXComment = new PConst(String.format("TABLE_%s_COMMENT", tableNameUnderscoreSeparatedUpperCase), PValue.valueOf(table.getComment()));
			
			pClass.addConst(pConstTableX);
			pClass.addConst(pConstTableXComment);
			
			for(final Column column : columns) {
				final String columnName = column.getName();
				final String columnNameUnderscoreSeparatedLowerCase = Strings.formatUnderscoreSeparatedLowerCase(columnName);
				final String columnNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(columnName);
				
				final PConst pConstTableXColumnY = new PConst(String.format("TABLE_%s_COLUMN_%s", tableNameUnderscoreSeparatedUpperCase, columnNameUnderscoreSeparatedUpperCase), PValue.valueOf(columnNameUnderscoreSeparatedLowerCase));
				
				pClass.addConst(pConstTableXColumnY);
			}
		}
		
		pClass.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Exception");
		pDocument.addUse("Magento\\Framework\\DB\\Ddl\\Table");
		pDocument.addUse("Magento\\Framework\\Setup\\InstallSchemaInterface");
		pDocument.addUse("Magento\\Framework\\Setup\\ModuleContextInterface");
		pDocument.addUse("Magento\\Framework\\Setup\\SchemaSetupInterface");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Setup"));
		
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
		return String.format("new Database(%s, \"%s\", \"%s\")", this.database, this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof InstallSchema)) {
			return false;
		} else if(!Objects.equals(this.database, InstallSchema.class.cast(object).database)) {
			return false;
		} else if(!Objects.equals(this.moduleName, InstallSchema.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, InstallSchema.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.database, this.moduleName, this.vendorName);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setDatabase(final Database database) {
		this.database = Objects.requireNonNull(database, "database == null");
	}
	
	public void setModuleName(final String moduleName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
	}
	
	public void setVendorName(final String vendorName) {
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static String doRepeat(final String string, final int iterations) {
		final StringBuilder stringBuilder = new StringBuilder();
		
		for(int i = 0; i < iterations; i++) {
			stringBuilder.append(string);
		}
		
		return stringBuilder.toString();
	}
	
	private static int doGetMaximumColumnDataTypeLength(final List<Column> columns) {
		int maximumColumnDataTypeLength = 0;
		
		for(final Column column : columns) {
			final String columnDataType = column.getDataType().toMagentoConstant();
			
			if(columnDataType.length() > maximumColumnDataTypeLength) {
				maximumColumnDataTypeLength = columnDataType.length();
			}
		}
		
		return maximumColumnDataTypeLength;
	}
	
	private static int doGetMaximumColumnNameLength(final List<Column> columns) {
		int maximumColumnNameLength = 0;
		
		for(final Column column : columns) {
			final String columnName = column.getName();
			final String columnNameUnderscoreSeparatedUpperCase = Strings.formatUnderscoreSeparatedUpperCase(columnName);
			
			if(columnNameUnderscoreSeparatedUpperCase.length() > maximumColumnNameLength) {
				maximumColumnNameLength = columnNameUnderscoreSeparatedUpperCase.length();
			}
		}
		
		return maximumColumnNameLength;
	}
	
	private static void doWriteToFile(final File file, final String string) {
		file.getParentFile().mkdirs();
		
		try {
			Files.write(file.toPath(), string.getBytes("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}