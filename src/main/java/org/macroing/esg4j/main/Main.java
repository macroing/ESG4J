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
package org.macroing.esg4j.main;

import java.io.File;

import org.macroing.cel4j.php.model.PDocument;
import org.macroing.esg4j.fortnox.Fortnox;
import org.macroing.esg4j.magento.database.Column;
import org.macroing.esg4j.magento.database.Database;
import org.macroing.esg4j.magento.database.Table;
import org.macroing.esg4j.magento.file.Composer;
import org.macroing.esg4j.magento.file.Registration;
import org.macroing.esg4j.magento.file.Composer.Author;
import org.macroing.esg4j.magento.file.Composer.Dependency;
import org.macroing.esg4j.magento.file.Composer.License;
import org.macroing.esg4j.magento.file.api.ModelInterface;
import org.macroing.esg4j.magento.file.api.ModelRepositoryInterface;
import org.macroing.esg4j.magento.file.block.Block;
import org.macroing.esg4j.magento.file.cron.CronJob;
import org.macroing.esg4j.magento.file.helper.Helper;
import org.macroing.esg4j.magento.file.model.ModelRepository;
import org.macroing.esg4j.magento.file.model.config.source.CustomerGroupSourceModel;
import org.macroing.esg4j.magento.file.model.config.source.ProductSourceModel;
import org.macroing.esg4j.magento.file.model.config.source.StoreViewSourceModel;
import org.macroing.esg4j.magento.file.model.resourcemodel.model.Collection;
import org.macroing.esg4j.magento.file.setup.InstallSchema;

public final class Main {
	private Main() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doGenerateFortnox();
		doGenerateMagento();
		doGenerateShopify();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doGenerateFortnox() {
		doGenerateFortnoxArticle();
		doGenerateFortnoxCustomer();
		doGenerateFortnoxDefaultDeliveryTypes();
		doGenerateFortnoxDefaultTemplates();
	}
	
	private static void doGenerateFortnoxArticle() {
		final
		PDocument pDocument = Fortnox.createArticleModel().toDocument(PDocument.toNamespace("org", "macroing", "fortnox", "article"));
		pDocument.setGeneratingComment(true);
		pDocument.write(new File("./generated/fortnox/src/org/macroing/fortnox/article/Article.php"), true);
	}
	
	private static void doGenerateFortnoxCustomer() {
		final
		PDocument pDocument = Fortnox.createCustomerModel().toDocument(PDocument.toNamespace("org", "macroing", "fortnox", "customer"));
		pDocument.setGeneratingComment(true);
		pDocument.write(new File("./generated/fortnox/src/org/macroing/fortnox/customer/Customer.php"), true);
	}
	
	private static void doGenerateFortnoxDefaultDeliveryTypes() {
		final
		PDocument pDocument = Fortnox.createDefaultDeliveryTypesModel().toDocument(PDocument.toNamespace("org", "macroing", "fortnox", "customer"));
		pDocument.setGeneratingComment(true);
		pDocument.write(new File("./generated/fortnox/src/org/macroing/fortnox/customer/DefaultDeliveryTypes.php"), true);
	}
	
	private static void doGenerateFortnoxDefaultTemplates() {
		final
		PDocument pDocument = Fortnox.createDefaultTemplatesModel().toDocument(PDocument.toNamespace("org", "macroing", "fortnox", "customer"));
		pDocument.setGeneratingComment(true);
		pDocument.write(new File("./generated/fortnox/src/org/macroing/fortnox/customer/DefaultTemplates.php"), true);
	}
	
	private static void doGenerateMagento() {
		final File directory = new File("./generated/magento/app/code/");
		
		final String moduleDescription = "This is a description.";
		final String moduleName = "MyCustomModule";
		final String moduleVersion = "1.0.0";
		final String vendorName = "Company";
		
		final
		Table table = new Table("form_item", "Form Item");
		table.addColumn(Column.newIntColumn(null, "ID", "id", true, false, false, true, false, false, true, 11));
		table.addColumn(Column.newIntColumn(null, "ID Sales Order", "id_sales_order", false, false, true, false, false, false, true, 11));
		table.addColumn(Column.newIntColumn(null, "ID Sales Order Item", "id_sales_order_item", false, false, true, false, false, false, true, 11));
		table.addColumn(Column.newTextColumn(null, "E-Mail Address", "e_mail_address", false, false, false, false, 255));
		table.addColumn(Column.newTextColumn(null, "First Name", "first_name", false, false, false, false, 255));
		table.addColumn(Column.newTextColumn(null, "Last Name", "last_name", false, false, false, false, 255));
		
		final
		Database database = new Database();
		database.addTable(table);
		
		doGenerateMagentoApiModelInterface(directory, moduleName, vendorName, table);
		doGenerateMagentoApiModelRepositoryInterface(directory, moduleName, vendorName, table);
		doGenerateMagentoBlock(directory, "MyBlock", moduleName, vendorName);
		doGenerateMagentoComposer(directory, moduleDescription, moduleName, moduleVersion, vendorName);
		doGenerateMagentoController(directory, "Index", "Index", moduleName, vendorName, org.macroing.esg4j.magento.file.controller.Controller.Type.PAGE);
		doGenerateMagentoControllerAdminHTML(directory, "Index", "Ajax", moduleName, vendorName, org.macroing.esg4j.magento.file.controller.adminhtml.Controller.Type.JSON);
		doGenerateMagentoCronJob(directory, "CronJob", moduleName, vendorName);
		doGenerateMagentoEtcAdminHTMLRoutes(directory, moduleName, vendorName);
		doGenerateMagentoEtcFrontendRoutes(directory, moduleName, vendorName);
		doGenerateMagentoEtcModule(directory, moduleName, moduleVersion, vendorName);
		doGenerateMagentoHelper(directory, "MyHelper", moduleName, vendorName);
		doGenerateMagentoInstallSchema(directory, database, moduleName, vendorName);
		doGenerateMagentoModelConfigSourceCustomerGroupSourceModel(directory, moduleName, vendorName);
		doGenerateMagentoModelConfigSourceProductSourceModel(directory, moduleName, vendorName);
		doGenerateMagentoModelConfigSourceStoreViewSourceModel(directory, moduleName, vendorName);
		doGenerateMagentoModelModel(directory, moduleName, vendorName, table);
		doGenerateMagentoModelModelRepository(directory, moduleName, vendorName, table);
		doGenerateMagentoModelResourceModelModel(directory, moduleName, vendorName, table);
		doGenerateMagentoModelResourceModelModelCollection(directory, moduleName, vendorName, table);
		doGenerateMagentoRegistration(directory, moduleName, vendorName);
		
		/*
		 * Add support for generation:
		 * - etc/adminhtml/system.xml
		 * - etc/config.xml
		 * - etc/crontab.xml
		 * - etc/di.xml
		 * - etc/events.xml
		 * - i18n/x_Y.csv
		 * - Observer/[Observer].php
		 * - Setup/UpgradeSchema.php
		 * 
		 * Can generate 20 file types:
		 * - Api/[ModelName]Interface.php
		 * - Api/[ModelName]RepositoryInterface.php
		 * - Block/[BlockName].php
		 * - Controller/Adminhtml/[ControllerName]/[ControllerAction].php
		 * - Controller/[ControllerName]/[ControllerAction].php
		 * - Cron/[CronJob].php
		 * - Helper/[HelperName].php
		 * - Model/Config/Source/CustomerGroupSourceModel.php
		 * - Model/Config/Source/ProductSourceModel.php
		 * - Model/Config/Source/StoreViewSourceModel.php
		 * - Model/[ModelName].php
		 * - Model/[ModelName]Repository.php
		 * - Model/ResourceModel/[ModelName].php
		 * - Model/ResourceModel/[ModelName]/Collection.php
		 * - Setup/InstallSchema.php - WIP
		 * - composer.json
		 * - etc/adminhtml/routes.xml
		 * - etc/frontend/routes.xml
		 * - etc/module.xml
		 * - registration.php
		 */
	}
	
	private static void doGenerateMagentoApiModelInterface(final File directory, final String moduleName, final String vendorName, final Table table) {
		new ModelInterface(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoApiModelRepositoryInterface(final File directory, final String moduleName, final String vendorName, final Table table) {
		new ModelRepositoryInterface(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoBlock(final File directory, final String blockName, final String moduleName, final String vendorName) {
		new Block(blockName, moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoComposer(final File directory, final String moduleDescription, final String moduleName, final String moduleVersion, final String vendorName) {
		final
		Composer composer = new Composer(moduleDescription, moduleName, moduleVersion, vendorName);
		composer.addAuthor(new Author("john.doe@example.com", "John Doe"));
		composer.addDependency(new Dependency("Php", "", "~7.0.13|~7.1.0|~7.2.0"));
		composer.addDependency(new Dependency("Framework", "Magento", "^100.1||^101.0||^102.0"));
		composer.addLicense(new License("proprietary"));
		composer.generateTo(directory);
	}
	
	private static void doGenerateMagentoController(final File directory, final String controllerAction, final String controllerName, final String moduleName, final String vendorName, final org.macroing.esg4j.magento.file.controller.Controller.Type type) {
		new org.macroing.esg4j.magento.file.controller.Controller(controllerAction, controllerName, moduleName, vendorName, type).generateTo(directory);
	}
	
	private static void doGenerateMagentoControllerAdminHTML(final File directory, final String controllerAction, final String controllerName, final String moduleName, final String vendorName, final org.macroing.esg4j.magento.file.controller.adminhtml.Controller.Type type) {
		new org.macroing.esg4j.magento.file.controller.adminhtml.Controller(controllerAction, controllerName, moduleName, vendorName, type).generateTo(directory);
	}
	
	private static void doGenerateMagentoCronJob(final File directory, final String cronJobName, final String moduleName, final String vendorName) {
		new CronJob(cronJobName, moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoEtcAdminHTMLRoutes(final File directory, final String moduleName, final String vendorName) {
		new org.macroing.esg4j.magento.file.etc.adminhtml.Routes(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoEtcFrontendRoutes(final File directory, final String moduleName, final String vendorName) {
		new org.macroing.esg4j.magento.file.etc.frontend.Routes(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoEtcModule(final File directory, final String moduleName, final String moduleVersion, final String vendorName) {
		new org.macroing.esg4j.magento.file.etc.Module(moduleName, moduleVersion, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoHelper(final File directory, final String helperName, final String moduleName, final String vendorName) {
		new Helper(helperName, moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoInstallSchema(final File directory, final Database database, final String moduleName, final String vendorName) {
		new InstallSchema(database, moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelConfigSourceCustomerGroupSourceModel(final File directory, final String moduleName, final String vendorName) {
		new CustomerGroupSourceModel(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelConfigSourceProductSourceModel(final File directory, final String moduleName, final String vendorName) {
		new ProductSourceModel(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelConfigSourceStoreViewSourceModel(final File directory, final String moduleName, final String vendorName) {
		new StoreViewSourceModel(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelModel(final File directory, final String moduleName, final String vendorName, final Table table) {
		new org.macroing.esg4j.magento.file.model.Model(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelModelRepository(final File directory, final String moduleName, final String vendorName, final Table table) {
		new ModelRepository(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelResourceModelModel(final File directory, final String moduleName, final String vendorName, final Table table) {
		new org.macroing.esg4j.magento.file.model.resourcemodel.Model(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoModelResourceModelModelCollection(final File directory, final String moduleName, final String vendorName, final Table table) {
		new Collection(moduleName, vendorName, table).generateTo(directory);
	}
	
	private static void doGenerateMagentoRegistration(final File directory, final String moduleName, final String vendorName) {
		new Registration(moduleName, vendorName).generateTo(directory);
	}
	
	private static void doGenerateShopify() {
//		TODO: Implement!
	}
}