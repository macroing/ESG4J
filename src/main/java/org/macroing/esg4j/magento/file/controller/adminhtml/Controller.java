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
package org.macroing.esg4j.magento.file.controller.adminhtml;

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

public final class Controller {
	private String controllerAction;
	private String controllerName;
	private String moduleName;
	private String vendorName;
	private Type type;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Controller() {
		this("Index", "Index", "Module", "Vendor", Type.PAGE);
	}
	
	public Controller(final String controllerAction, final String controllerName, final String moduleName, final String vendorName, final Type type) {
		this.controllerAction = Objects.requireNonNull(controllerAction, "controllerAction == null");
		this.controllerName = Objects.requireNonNull(controllerName, "controllerName == null");
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
		this.type = Objects.requireNonNull(type, "type == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public File getRelativeFile() {
		return new File(String.format("%s/%s/Controller%s/%s/%s.php", Strings.formatCamelCase(getVendorName()), Strings.formatCamelCase(getModuleName()), "/Adminhtml", Strings.formatCamelCase(getControllerName()), Strings.formatCamelCase(getControllerAction())));
	}
	
	public PDocument toDocument() {
		switch(getType()) {
			case FORWARD:
				return doToDocumentForward();
			case JSON:
				return doToDocumentJSON();
			case PAGE:
				return doToDocumentPage();
			case RAW:
				return doToDocumentRaw();
			case REDIRECT:
				return doToDocumentRedirect();
			default:
				throw new IllegalStateException();
		}
	}
	
	public String getControllerAction() {
		return this.controllerAction;
	}
	
	public String getControllerName() {
		return this.controllerName;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getVendorName() {
		return this.vendorName;
	}
	
	@Override
	public String toString() {
		return String.format("new Controller(\"%s\", \"%s\", \"%s\", \"%s\", %s)", this.controllerAction, this.controllerName, this.moduleName, this.vendorName, this.type);
	}
	
	public Type getType() {
		return this.type;
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Controller)) {
			return false;
		} else if(!Objects.equals(this.controllerAction, Controller.class.cast(object).controllerAction)) {
			return false;
		} else if(!Objects.equals(this.controllerName, Controller.class.cast(object).controllerName)) {
			return false;
		} else if(!Objects.equals(this.moduleName, Controller.class.cast(object).moduleName)) {
			return false;
		} else if(!Objects.equals(this.vendorName, Controller.class.cast(object).vendorName)) {
			return false;
		} else if(!Objects.equals(this.type, Controller.class.cast(object).type)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.controllerAction, this.controllerName, this.moduleName, this.vendorName, this.type);
	}
	
	public void generateTo(final File directory) {
		doWriteToFile(new File(Objects.requireNonNull(directory, "directory == null"), getRelativeFile().getPath()), toDocument().write(new Document(), true).toString());
	}
	
	public void setControllerAction(final String controllerAction) {
		this.controllerAction = Objects.requireNonNull(controllerAction, "controllerAction == null");
	}
	
	public void setControllerName(final String controllerName) {
		this.controllerName = Objects.requireNonNull(controllerName, "controllerName == null");
	}
	
	public void setModuleName(final String moduleName) {
		this.moduleName = Objects.requireNonNull(moduleName, "moduleName == null");
	}
	
	public void setType(final Type type) {
		this.type = Objects.requireNonNull(type, "type == null");
	}
	
	public void setVendorName(final String vendorName) {
		this.vendorName = Objects.requireNonNull(vendorName, "vendorName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static enum Type {
		FORWARD,
		JSON,
		PAGE,
		RAW,
		REDIRECT;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Type() {
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PDocument doToDocumentForward() {
		final String controllerAction = getControllerAction();
		final String controllerActionCamelCase = Strings.formatCamelCase(controllerAction);
		final String controllerName = getControllerName();
		final String controllerNameCamelCase = Strings.formatCamelCase(controllerName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pField = new PField();
		pField.setName("factory");
		pField.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("factory", PType.valueOf("Factory")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.getBlock().addLine("");
		pConstructor.getBlock().addLine("$this->factory = $factory;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("$result = $this->factory->create();");
		pMethod.getBlock().addLine("$result->forward('noroute');");
		pMethod.getBlock().addLine("");
		pMethod.getBlock().addLine("return $result;");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pField);
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Action");
		pClass.setName(controllerActionCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\App\\Action");
		pDocument.addUse("Magento\\Backend\\App\\Action\\Context");
		pDocument.addUse("Magento\\Framework\\Controller\\Result\\Forward\\Factory");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Controller", "Adminhtml", controllerNameCamelCase));
		
		return pDocument;
	}
	
	private PDocument doToDocumentJSON() {
		final String controllerAction = getControllerAction();
		final String controllerActionCamelCase = Strings.formatCamelCase(controllerAction);
		final String controllerName = getControllerName();
		final String controllerNameCamelCase = Strings.formatCamelCase(controllerName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pField = new PField();
		pField.setName("jsonFactory");
		pField.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("jsonFactory", PType.valueOf("JsonFactory")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.getBlock().addLine("");
		pConstructor.getBlock().addLine("$this->jsonFactory = $jsonFactory;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("$result = $this->jsonFactory->create();");
		pMethod.getBlock().addLine("$result->setData(array());");
		pMethod.getBlock().addLine("");
		pMethod.getBlock().addLine("return $result;");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pField);
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Action");
		pClass.setName(controllerActionCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\App\\Action");
		pDocument.addUse("Magento\\Backend\\App\\Action\\Context");
		pDocument.addUse("Magento\\Framework\\Controller\\Result\\JsonFactory");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Controller", "Adminhtml", controllerNameCamelCase));
		
		return pDocument;
	}
	
	private PDocument doToDocumentPage() {
		final String controllerAction = getControllerAction();
		final String controllerActionCamelCase = Strings.formatCamelCase(controllerAction);
		final String controllerName = getControllerName();
		final String controllerNameCamelCase = Strings.formatCamelCase(controllerName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pField = new PField();
		pField.setName("pageFactory");
		pField.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("pageFactory", PType.valueOf("PageFactory")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.getBlock().addLine("");
		pConstructor.getBlock().addLine("$this->pageFactory = $pageFactory;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("return $this->pageFactory->create();");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pField);
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Action");
		pClass.setName(controllerActionCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\App\\Action");
		pDocument.addUse("Magento\\Backend\\App\\Action\\Context");
		pDocument.addUse("Magento\\Framework\\View\\Result\\PageFactory");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Controller", "Adminhtml", controllerNameCamelCase));
		
		return pDocument;
	}
	
	private PDocument doToDocumentRaw() {
		final String controllerAction = getControllerAction();
		final String controllerActionCamelCase = Strings.formatCamelCase(controllerAction);
		final String controllerName = getControllerName();
		final String controllerNameCamelCase = Strings.formatCamelCase(controllerName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pField = new PField();
		pField.setName("rawFactory");
		pField.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("rawFactory", PType.valueOf("RawFactory")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.getBlock().addLine("");
		pConstructor.getBlock().addLine("$this->rawFactory = $rawFactory;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("$result = $this->rawFactory->create();");
		pMethod.getBlock().addLine("$result->setHeader('Content-Type', 'text/html');");
		pMethod.getBlock().addLine("$result->setContents('');");
		pMethod.getBlock().addLine("");
		pMethod.getBlock().addLine("return $result;");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pField);
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Action");
		pClass.setName(controllerActionCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\App\\Action");
		pDocument.addUse("Magento\\Backend\\App\\Action\\Context");
		pDocument.addUse("Magento\\Framework\\Controller\\Result\\RawFactory");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Controller", "Adminhtml", controllerNameCamelCase));
		
		return pDocument;
	}
	
	private PDocument doToDocumentRedirect() {
		final String controllerAction = getControllerAction();
		final String controllerActionCamelCase = Strings.formatCamelCase(controllerAction);
		final String controllerName = getControllerName();
		final String controllerNameCamelCase = Strings.formatCamelCase(controllerName);
		final String moduleName = getModuleName();
		final String moduleNameCamelCase = Strings.formatCamelCase(moduleName);
		final String vendorName = getVendorName();
		final String vendorNameCamelCase = Strings.formatCamelCase(vendorName);
		
		final
		PField pField = new PField();
		pField.setName("factory");
		pField.setPrivate(true);
		
		final
		PConstructor pConstructor = new PConstructor();
		pConstructor.addParameterArgument(new PParameterArgument("context", PType.valueOf("Context")));
		pConstructor.addParameterArgument(new PParameterArgument("factory", PType.valueOf("Factory")));
		pConstructor.getBlock().addLine("parent::__construct($context);");
		pConstructor.getBlock().addLine("");
		pConstructor.getBlock().addLine("$this->factory = $factory;");
		pConstructor.setPublic(true);
		
		final
		PMethod pMethod = new PMethod();
		pMethod.getBlock().addLine("$result = $this->factory->create();");
		pMethod.getBlock().addLine("$result->setPath('*/*/index');");
		pMethod.getBlock().addLine("");
		pMethod.getBlock().addLine("return $result;");
		pMethod.setName("execute");
		pMethod.setPublic(true);
		
		final
		PClass pClass = new PClass();
		pClass.addField(pField);
		pClass.addMethod(pMethod);
		pClass.setConstructor(pConstructor);
		pClass.setExtendedClass("Action");
		pClass.setName(controllerActionCamelCase);
		
		final
		PDocument pDocument = new PDocument();
		pDocument.addClass(pClass);
		pDocument.addUse("Magento\\Backend\\App\\Action");
		pDocument.addUse("Magento\\Backend\\App\\Action\\Context");
		pDocument.addUse("Magento\\Framework\\Controller\\Result\\Redirect\\Factory");
		pDocument.setNamespace(PDocument.toNamespace(vendorNameCamelCase, moduleNameCamelCase, "Controller", "Adminhtml", controllerNameCamelCase));
		
		return pDocument;
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