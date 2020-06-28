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
package org.macroing.esg4j.magento.file;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class Composer {
	private final List<Author> authors;
	private final List<Dependency> dependencies;
	private final List<License> licenses;
	private String moduleDescription;
	private String moduleName;
	private String moduleVersion;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Composer() {
		this("", "Module", "1.0.0", "Vendor");
	}
	
	public Composer(final String moduleDescription, final String moduleName, final String moduleVersion, final String vendorName) {
		this.authors = new ArrayList<>();
		this.dependencies = new ArrayList<>();
		this.licenses = new ArrayList<>();
		this.moduleDescription = Objects.requireNonNull(moduleDescription, "moduleDescription == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.moduleVersion = Objects.requireNonNull(moduleVersion, "moduleVersion == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Document toDocument() {
		final List<Author> authors = getAuthors();
		final List<Dependency> dependencies = getDependencies();
		final List<License> licenses = getLicenses();
		
		final String moduleDescription = getModuleDescription();
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String moduleNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(moduleName);
		final String moduleVersion = getModuleVersion();
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		final String vendorNameUnseparatedLowerCase = Strings.formatUnseparatedLowerCase(vendorName);
		
		final
		Document document = new Document();
		document.line("{");
		document.indent();
		document.linef("\"name\": \"%s/%s\",", vendorNameUnseparatedLowerCase, moduleNameUnseparatedLowerCase);
		document.linef("\"description\": \"%s\",", moduleDescription);
		document.line("\"type\": \"magento2-module\",");
		
		if(licenses.size() > 0) {
			if(licenses.size() == 1) {
				document.linef("\"license\": \"%s\",", licenses.get(0).getName());
			} else {
				document.line("\"license\": [");
				document.indent();
				
				for(int i = 0; i < licenses.size(); i++) {
					if(i + 1 < licenses.size()) {
						document.linef("\"%s\",", licenses.get(i).getName());
					} else {
						document.linef("\"%s\"", licenses.get(i).getName());
					}
				}
				
				document.outdent();
				document.line("],");
			}
		}
		
		document.linef("\"version\": \"%s\",", moduleVersion);
		
		if(authors.size() > 0) {
			document.line("\"authors\": [");
			document.indent();
			
			for(int i = 0; i < authors.size(); i++) {
				document.line("{");
				document.indent();
				document.linef("\"name\": \"%s\",", authors.get(i).getName());
				document.linef("\"email\": \"%s\"", authors.get(i).getEMailAddress());
				document.outdent();
				
				if(i + 1 < authors.size()) {
					document.line("},");
				} else {
					document.line("}");
				}
			}
			
			document.outdent();
			document.line("],");
		}
		
		if(dependencies.size() > 0) {
			document.line("\"require\": {");
			document.indent();
			
			for(int i = 0; i < dependencies.size(); i++) {
				final Dependency dependency = dependencies.get(i);
				
				final String dependencyName = dependency.getName();
				final String dependencyNameDashSeparatedLowerCase = Strings.formatDashSeparatedLowerCase(dependencyName);
				final String dependencyVendor = dependency.getVendor();
				final String dependencyVendorDashSeparatedLowerCase = Strings.formatDashSeparatedLowerCase(dependencyVendor);
				final String dependencyVersion = dependency.getVersion();
				
				if(!dependencyNameDashSeparatedLowerCase.isEmpty() && !dependencyVendorDashSeparatedLowerCase.isEmpty()) {
					document.linef("\"%s/%s\": \"%s\"%s", dependencyVendorDashSeparatedLowerCase, dependencyNameDashSeparatedLowerCase, dependencyVersion, i + 1 < dependencies.size() ? "," : "");
				} else if(!dependencyNameDashSeparatedLowerCase.isEmpty()) {
					document.linef("\"%s\": \"%s\"%s", dependencyNameDashSeparatedLowerCase, dependencyVersion, i + 1 < dependencies.size() ? "," : "");
				} else if(!dependencyVendorDashSeparatedLowerCase.isEmpty()) {
					document.linef("\"%s\": \"%s\"%s", dependencyVendorDashSeparatedLowerCase, dependencyVersion, i + 1 < dependencies.size() ? "," : "");
				}
			}
			
			document.outdent();
			document.line("},");
		}
		
		document.line("\"autoload\": {");
		document.indent();
		document.line("\"files\": [");
		document.indent();
		document.line("\"registration.php\"");
		document.outdent();
		document.line("],");
		document.line("\"psr-4\": {");
		document.indent();
		document.linef("\"%s\\\\%s\\\\\": \".\"", vendorNameCamelCase, moduleNameCamelCase);
		document.outdent();
		document.line("}");
		document.outdent();
		document.line("}");
		document.outdent();
		document.line("}");
		
		return document;
	}
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/composer.json", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName())));
	}
	
	public List<Author> getAuthors() {
		return new ArrayList<>(this.authors);
	}
	
	public List<Dependency> getDependencies() {
		return new ArrayList<>(this.dependencies);
	}
	
	public List<License> getLicenses() {
		return new ArrayList<>(this.licenses);
	}
	
	public String getModuleDescription() {
		return this.moduleDescription;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getModuleVersion() {
		return this.moduleVersion;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new Composer(\"%s\", \"%s\", \"%s\", \"%s\")", this.moduleDescription, this.moduleName, this.moduleVersion, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Composer)) {
			return false;
		} else if(!Objects.equals(this.authors, Composer.class.cast(object).authors)) {
			return false;
		} else if(!Objects.equals(this.dependencies, Composer.class.cast(object).dependencies)) {
			return false;
		} else if(!Objects.equals(this.licenses, Composer.class.cast(object).licenses)) {
			return false;
		} else if(!Objects.equals(this.moduleDescription, Composer.class.cast(object).moduleDescription)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Composer.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.moduleVersion, Composer.class.cast(object).moduleVersion)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Composer.class.cast(object).vendorName)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.authors, this.dependencies, this.licenses, this.moduleDescription, this.moduleName, this.moduleVersion, this.vendorName);
	}
	
	public void addAuthor(final Author author) {
		this.authors.add(Objects.requireNonNull(author, "author == null"));
	}
	
	public void addDependency(final Dependency dependency) {
		this.dependencies.add(Objects.requireNonNull(dependency, "dependency == null"));
	}
	
	public void addLicense(final License license) {
		this.licenses.add(Objects.requireNonNull(license, "license == null"));
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().toString());
	}
	
	public void removeAuthor(final Author author) {
		this.authors.remove(Objects.requireNonNull(author, "author == null"));
	}
	
	public void removeDependency(final Dependency dependency) {
		this.dependencies.remove(Objects.requireNonNull(dependency, "dependency == null"));
	}
	
	public void removeLicense(final License license) {
		this.licenses.remove(Objects.requireNonNull(license, "license == null"));
	}
	
	public void setModuleDescription(final String moduleDescription) {
		this.moduleDescription = Objects.requireNonNull(moduleDescription, "moduleDescription == null");
	}
	
	public void setModuleName(final String moduleName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
	}
	
	public void setModuleVersion(final String moduleVersion) {
		this.moduleVersion = Objects.requireNonNull(moduleVersion, "moduleVersion == null");
	}
	
	public void setVendorName(final String vendorName) {
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final class Author {
		private String eMailAddress;
		private String name;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Author() {
			this("john.doe@example.com");
		}
		
		public Author(final String eMailAddress) {
			this(eMailAddress, "John Doe");
		}
		
		public Author(final String eMailAddress, final String name) {
			this.eMailAddress = Objects.requireNonNull(eMailAddress, "eMailAddress == null");
			this.name = Objects.requireNonNull(name, "name == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getEMailAddress() {
			return this.eMailAddress;
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return String.format("new Author(\"%s\", \"%s\")", this.eMailAddress, this.name);
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Author)) {
				return false;
			} else if(!Objects.equals(this.eMailAddress, Author.class.cast(object).eMailAddress)) {
				return false;
			} else if(!Objects.equals(this.name, Author.class.cast(object).name)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.eMailAddress, this.name);
		}
		
		public void setEMailAddress(final String eMailAddress) {
			this.eMailAddress = Objects.requireNonNull(eMailAddress, "eMailAddress == null");
		}
		
		public void setName(final String name) {
			this.name = Objects.requireNonNull(name, "name == null");
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final class Dependency {
		private String name;
		private String vendor;
		private String version;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Dependency() {
			this("Name");
		}
		
		public Dependency(final String name) {
			this(name, "Vendor");
		}
		
		public Dependency(final String name, final String vendor) {
			this(name, vendor, "1.0.0");
		}
		
		public Dependency(final String name, final String vendor, final String version) {
			this.name = Objects.requireNonNull(name, "name == null");
			this.vendor = Objects.requireNonNull(vendor, "vendor == null");
			this.version = Objects.requireNonNull(version, "version == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getName() {
			return this.name;
		}
		
		public String getVendor() {
			return this.vendor;
		}
		
		public String getVersion() {
			return this.version;
		}
		
		@Override
		public String toString() {
			return String.format("new Dependency(\"%s\", \"%s\", \"%s\")", this.name, this.vendor, this.version);
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Dependency)) {
				return false;
			} else if(!Objects.equals(this.name, Dependency.class.cast(object).name)) {
				return false;
			} else if(!Objects.equals(this.vendor, Dependency.class.cast(object).vendor)) {
				return false;
			} else if(!Objects.equals(this.version, Dependency.class.cast(object).version)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.vendor, this.version);
		}
		
		public void setName(final String name) {
			this.name = Objects.requireNonNull(name, "name == null");
		}
		
		public void setVendor(final String vendor) {
			this.vendor = Objects.requireNonNull(vendor, "vendor == null");
		}
		
		public void setVersion(final String version) {
			this.version = Objects.requireNonNull(version, "version == null");
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final class License {
		private String name;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public License() {
			this("proprietary");
		}
		
		public License(final String name) {
			this.name = Objects.requireNonNull(name, "name == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return String.format("new License(\"%s\")", this.name);
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof License)) {
				return false;
			} else if(!Objects.equals(this.name, License.class.cast(object).name)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.name);
		}
		
		public void setName(final String name) {
			this.name = Objects.requireNonNull(name, "name == null");
		}
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