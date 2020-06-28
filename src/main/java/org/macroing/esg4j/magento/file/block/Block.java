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
package org.macroing.esg4j.magento.file.block;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PConstructor;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.php.model.PValue;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class Block {
	private String blockName;
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Block() {
		this("Block", "Module", "Vendor");
	}
	
	public Block(final String blockName, final String moduleName, final String vendorName) {
		this.blockName = Objects.requireNonNull(blockName, "blockName == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Block/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getBlockName())));
	}
	
	public PDocument toDocument() {
		final String blockName = getBlockName();
		final String blockNameCamelCase = Strings.formatCamelCase(blockName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("data", PType.ARRAY, PValue.ARRAY));
		pConstructor.getBlock().addLine("parent::__construct($context, $data);");
		pConstructor.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Template");
		pClass.setName(blockNameCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\Block\\Template\\Context");
		pDocument.addUse("Magento\\Framework\\View\\Element\\Template");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Block"));
		
		return pDocument;
	}
	
	public String getBlockName() {
		return this.blockName;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new Block(\"%s\", \"%s\", \"%s\")", this.blockName, this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Block)) {
			return false;
		} else if(!Objects.equals(this.blockName, Block.class.cast(object).blockName)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Block.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Block.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.blockName, this.moduleName, this.vendorName);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setBlockName(final String blockName) {
		this.blockName = Objects.requireNonNull(blockName, "blockName == null");
	}
	
	public void setModuleName(final String moduleName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
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