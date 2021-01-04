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
package org.macroing.esg4j.magento.file.etc.adminhtml;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class Routes {
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Routes() {
		this("Module", "Vendor");
	}
	
	public Routes(final String moduleName, final String vendorName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Document toDocument() {
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String moduleNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		Document document = new Document();
		document.line("<?xml version=\"1.0\"?>");
		document.line("<config xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"urn:magento:framework:App/etc/routes.xsd\">");
		document.indent();
		document.line("<router id=\"admin\">");
		document.indent();
		document.linef("<route frontName=\"%s\" id=\"%s\">", moduleNameUnseparatedLowerCase, moduleNameUnseparatedLowerCase);
		document.indent();
		document.linef("<module name=\"%s_%s\"/>", vendorNameCamelCase, moduleNameCamelCase);
		document.outdent();
		document.line("</route>");
		document.outdent();
		document.line("</router>");
		document.outdent();
		document.line("</config>");
		
		return document;
	}
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/etc/adminhtml/routes.xml", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName())));
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new Routes(\"%s\", \"%s\")", this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Routes)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Routes.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Routes.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.moduleName, this.vendorName);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().toString());
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