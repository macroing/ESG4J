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
package org.macroing.esg4j.magento.file.helper;

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
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class Helper {
	private String helperName;
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Helper() {
		this("Helper", "Module", "Vendor");
	}
	
	public Helper(final String helperName, final String moduleName, final String vendorName) {
		this.helperName = Objects.requireNonNull(helperName, "helperName == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Helper/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getHelperName())));
	}
	
	public PDocument toDocument() {
		final String helperName = getHelperName();
		final String helperNameCamelCase = Strings.formatCamelCase(helperName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.setExtendedClass("AbstractHelper");
		pClass.setConstructor(pConstructor);
		pClass.setName(helperNameCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Framework\\App\\Helper\\AbstractHelper");
		pDocument.addUse("Magento\\Framework\\App\\Helper\\Context");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Helper"));
		
		return pDocument;
	}
	
	public String getHelperName() {
		return this.helperName;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new Helper(\"%s\", \"%s\", \"%s\")", this.helperName, this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Helper)) {
			return false;
		} else if(!Objects.equals(this.helperName, Helper.class.cast(object).helperName)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Helper.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Helper.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.helperName, this.moduleName, this.vendorName);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setHelperName(final String helperName) {
		this.helperName = Objects.requireNonNull(helperName, "helperName == null");
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