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
package org.macroing.esg4j.magento.file.cron;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PConstructor;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class CronJob {
	private String cronJobName;
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CronJob() {
		this("CronJob", "Module", "Vendor");
	}
	
	public CronJob(final String cronJobName, final String moduleName, final String vendorName) {
		this.cronJobName = Objects.requireNonNull(cronJobName, "cronJobName == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Cron/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), Strings.formatCamelCase(getCronJobName())));
	}
	
	public PDocument toDocument() {
		final String cronJobName = getCronJobName();
		final String cronJobNameCamelCase = Strings.formatCamelCase(cronJobName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.getBlock().addLine("");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("try {");
		pMethod.getBlock().addLine("	");
		pMethod.getBlock().addLine("} catch(Exception $e) {");
		pMethod.getBlock().addLine("	");
		pMethod.getBlock().addLine("}");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setName(cronJobNameCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Exception");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Cron"));
		
		return pDocument;
	}
	
	public String getCronJobName() {
		return this.cronJobName;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new CronJob(\"%s\", \"%s\", \"%s\")", this.cronJobName, this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CronJob)) {
			return false;
		} else if(!Objects.equals(this.cronJobName, CronJob.class.cast(object).cronJobName)) {
			return false;
		} else if(!Objects.equals(this.moduleName, CronJob.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, CronJob.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.cronJobName, this.moduleName, this.vendorName);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setCronJobName(final String cronJobName) {
		this.cronJobName = Objects.requireNonNull(cronJobName, "cronJobName == null");
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