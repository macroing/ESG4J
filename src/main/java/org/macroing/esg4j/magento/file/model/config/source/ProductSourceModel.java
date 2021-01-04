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
package org.macroing.esg4j.magento.file.model.config.source;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.macroing.cel4j.php.model.PClass;
import org.macroing.cel4j.php.model.PConstructor;
import org.macroing.cel4j.php.model.PDocument;
import org.macroing.cel4j.php.model.PField;
import org.macroing.cel4j.php.model.PMethod;
import org.macroing.cel4j.php.model.PParameterArgument;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.util.Document;
import org.macroing.cel4j.util.Strings;

public final class ProductSourceModel {
	private String moduleName;
	private String vendorName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ProductSourceModel() {
		this("Module", "Vendor");
	}
	
	public ProductSourceModel(final String moduleName, final String vendorName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Model/Config/SourceModel/ProductSourceModel.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName())));
	}
	
	public PDocument toDocument() {
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pFieldCollectionFactory = new PField();
		pFieldCollectionFactory.setName("collectionFactory");
		pFieldCollectionFactory.setPrivate(true);
		
		final
		PField pFieldOptionArray = new PField();
		pFieldOptionArray.setName("optionArray");
		pFieldOptionArray.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("collectionFactory", PType.valueOf("CollectionFactory")));
		pConstructor.getBlock().addLine("$this->collectionFactory = $collectionFactory;");
		pConstructor.getBlock().addLine("$this->optionArray = null;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethodToOptionArray = new PMethod();
		pMethodToOptionArray.getBlock().addLine("if($this->optionArray === null) {");
		pMethodToOptionArray.getBlock().addLine("	$collection = $this->collectionFactory->create();");
		pMethodToOptionArray.getBlock().addLine("	$collection->addAttributeToSelect('sku');");
		pMethodToOptionArray.getBlock().addLine("	$collection->addAttributeToSelect('name');");
		pMethodToOptionArray.getBlock().addLine("	");
		pMethodToOptionArray.getBlock().addLine("	$this->optionArray = [];");
		pMethodToOptionArray.getBlock().addLine("	");
		pMethodToOptionArray.getBlock().addLine("	foreach($collection as $product) {");
		pMethodToOptionArray.getBlock().addLine("		$this->optionArray[] = ['value' => $product->getSku(), 'label' => $product->getName()];");
		pMethodToOptionArray.getBlock().addLine("	}");
		pMethodToOptionArray.getBlock().addLine("}");
		pMethodToOptionArray.getBlock().addLine("");
		pMethodToOptionArray.getBlock().addLine("return $this->optionArray;");
		pMethodToOptionArray.setName("toOptionArray");
		pMethodToOptionArray.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pFieldCollectionFactory);
		pClass.addField(pFieldOptionArray);
		pClass.addImplementedInterface("ArrayInterface");
		pClass.addMethod(pMethodToOptionArray);
		pClass.setConstructor(pConstructor);
		pClass.setName("ProductSourceModel");
		pClass.sort();
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Catalog\\Model\\ResourceModel\\Product\\CollectionFactory");
		pDocument.addUse("Magento\\Framework\\Option\\ArrayInterface");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Model", "Config", "SourceModel"));
		
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
		return String.format("new ProductSourceModel(\"%s\", \"%s\")", this.moduleName, this.vendorName);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ProductSourceModel)) {
			return false;
		} else if(!Objects.equals(this.moduleName, ProductSourceModel.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, ProductSourceModel.class.cast(object).vendorName)) {
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
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
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