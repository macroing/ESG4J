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
package org.macroing.esg4j.fortnox;

import java.util.Arrays;
import java.util.List;

import org.macroing.cel4j.php.generator.Model;
import org.macroing.cel4j.php.generator.Property;
import org.macroing.cel4j.php.generator.PropertyBuilder;
import org.macroing.cel4j.php.model.PType;
import org.macroing.cel4j.util.Pair;

public final class Fortnox {
	private Fortnox() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	Name:						Type:		Readable:	Writable:	Searchable:		Sortable:	Required:	Length:		Valid:
//	Active						Boolean		Yes			Yes			No				No			No			-			false or true
//	ArticleNumber				String		Yes			Yes			Yes				Yes			No			50			a-z, A-Z, 0-9, -, +, /, . and _
//	Bulky						Boolean		Yes			Yes			No				No			No			-			false or true
//	ConstructionAccount			Integer		Yes			Yes			No				No			No			4			-
//	CostCalculationMethod		String		Yes			Yes			No				No			No			-			'LAST_RELEASED_INBOUND' or 'MANUAL'
//	Depth						Integer		Yes			Yes			No				No			No			8			-
//	Description					String		Yes			Yes			Yes				No			Yes			200			-
//	DirectCost					Number		Yes			Yes			No				No			No			-			-
//	DisposableQuantity			Float		Yes			No			No				No			No			-			-
//	EAN							String		Yes			Yes			Yes				No			No			30			-
//	EUAccount					Integer		Yes			Yes			No				No			No			4			-
//	EUVATAccount				Integer		Yes			Yes			No				No			No			4			-
//	Expired						Boolean		Yes			Yes			No				No			No			-			false or true
//	ExportAccount				Integer		Yes			Yes			No				No			No			4			-
//	FreightCost					Number		Yes			Yes			No				No			No			-			-
//	Height						Integer		Yes			Yes			No				No			No			8			-
//	Housework					Boolean		Yes			Yes			No				No			No			-			false or true
//	HouseworkType				String		Yes			Yes			No				No			No			-			'BABYSITTING', 'CLEANING', 'CONSTRUCTION', 'COOKING', 'ELECTRICITY', 'GARDENING', 'GLASSMETALWORK', 'GROUNDDRAINAGEWORK', 'HVAC', 'MASONRY', 'OTHERCARE', 'OTHERCOSTS', 'PAINTINGWALLPAPERING', 'SNOWPLOWING', 'TEXTILECLOTHING', 'TUTORING' or ''
//	Manufacturer				String		Yes			Yes			Yes				No			No			50			-
//	ManufacturerArticleNumber	String		Yes			Yes			Yes				No			No			50			-
//	Note						String		Yes			Yes			No				No			No			10000		-
//	OtherCost					Number		Yes			Yes			No				No			No			-			-
//	PurchaseAccount				Integer		Yes			Yes			No				No			No			4			-
//	PurchasePrice				Float		Yes			Yes			No				No			No			14			-
//	QuantityInStock				Float		Yes			Yes			No				Yes			No			14			-
//	ReservedQuantity			Float		Yes			No			No				Yes			No			-			-
//	SalesAccount				Integer		Yes			Yes			No				No			No			4			-
//	SalesPrice					Float		Yes			No			No				No			No			-			-
//	StockAccount				Number		Yes			Yes			No				No			No			-			-
//	StockGoods					Boolean		Yes			Yes			No				No			No			-			false or true
//	StockPlace					String		Yes			Yes			No				No			No			100			-
//	StockValue					Float		Yes			No			No				Yes			No			-			-
//	StockWarning				Float		Yes			Yes			No				No			No			14			-
//	SupplierName				String		Yes			No			No				No			No			-			-
//	SupplierNumber				String		Yes			Yes			No				No			No			-			-
//	Type						String		Yes			Yes			No				No			No			-			'SERVICE' or 'STOCK'
//	Unit						String		Yes			Yes			No				No			No			-			-
//	Url							String		Yes			No			No				No			No			-			-
//	VAT							Float		Yes			Yes			No				No			No			-			-
//	WebshopArticle				Boolean		Yes			Yes			No				No			No			-			false or true
//	Weight						Integer		Yes			Yes			No				No			No			8			-
//	Width						Integer		Yes			Yes			No				No			No			8			-
	public static Model createArticleModel() {
		final List<Pair<String, String>> costCalculationMethodStringOptions = Arrays.asList(
			new Pair<>("LAST_RELEASED_INBOUND", "CostCalculationMethodLastReleasedInbound"),
			new Pair<>("MANUAL",                "CostCalculationMethodManual")
		);
		final List<Pair<String, String>> houseworkTypeStringOptions = Arrays.asList(
			new Pair<>("BABYSITTING",          "HouseworkTypeBabysitting"),
			new Pair<>("CLEANING",             "HouseworkTypeCleaning"),
			new Pair<>("CONSTRUCTION",         "HouseworkTypeConstruction"),
			new Pair<>("COOKING",              "HouseworkTypeCooking"),
			new Pair<>("ELECTRICITY",          "HouseworkTypeElectricity"),
			new Pair<>("GARDENING",            "HouseworkTypeGardening"),
			new Pair<>("GLASSMETALWORK",       "HouseworkTypeGlassMetalWork"),
			new Pair<>("GROUNDDRAINAGEWORK",   "HouseworkTypeGroundDrainageWork"),
			new Pair<>("HVAC",                 "HouseworkTypeHVAC"),
			new Pair<>("MASONRY",              "HouseworkTypeMasonry"),
			new Pair<>("",                     "HouseworkTypeNone"),
			new Pair<>("OTHERCARE",            "HouseworkTypeOtherCare"),
			new Pair<>("OTHERCOSTS",           "HouseworkTypeOtherCosts"),
			new Pair<>("PAINTINGWALLPAPERING", "HouseworkTypePaintingWallpapering"),
			new Pair<>("SNOWPLOWING",          "HouseworkTypeSnowplowing"),
			new Pair<>("TEXTILECLOTHING",      "HouseworkTypeTextileClothing"),
			new Pair<>("TUTORING",             "HouseworkTypeTutoring")
		);
		final List<Pair<String, String>> typeStringOptions = Arrays.asList(
			new Pair<>("SERVICE", "TypeService"),
			new Pair<>("STOCK",   "TypeStock")
		);
		
		final
		Model model = new Model("Article", "Article");
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "Active",                    "Active",                    ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringRegex("/^[a-zA-Z0-9\\-\\+\\/\\._]{1,50}$/"), "ArticleNumber",             "ArticleNumber",             ""));
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "Bulky",                     "Bulky",                     ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "ConstructionAccount",       "ConstructionAccount",       ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(costCalculationMethodStringOptions),  "CostCalculationMethod",     "CostCalculationMethod",     ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 99999999),                             "Depth",                     "Depth",                     ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 200),                              "Description",               "Description",               ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "DirectCost",                "DirectCost",                ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "DisposableQuantity",        "DisposableQuantity",        ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 30),                               "EAN",                       "EAN",                       ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "EUAccount",                 "EUAccount",                 ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "EUVATAccount",              "EUVATAccount",              ""));
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "Expired",                   "Expired",                   ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "ExportAccount",             "ExportAccount",             ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "FreightCost",               "FreightCost",               ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 99999999),                             "Height",                    "Height",                    ""));
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "Housework",                 "Housework",                 ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(houseworkTypeStringOptions),          "HouseworkType",             "HouseworkType",             ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 50),                               "Manufacturer",              "Manufacturer",              ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 50),                               "ManufacturerArticleNumber", "ManufacturerArticleNumber", ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 10000),                            "Note",                      "Note",                      ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "OtherCost",                 "OtherCost",                 ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "PurchaseAccount",           "PurchaseAccount",           ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "PurchasePrice",             "PurchasePrice",             ""));//14 Digits.
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "QuantityInStock",           "QuantityInStock",           ""));//14 Digits.
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "ReservedQuantity",          "ReservedQuantity",          ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 9999),                                 "SalesAccount",              "SalesAccount",              ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "SalesPrice",                "SalesPrice",                ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "StockAccount",              "StockAccount",              ""));
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "StockGoods",                "StockGoods",                ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringLength(0, 100),                              "StockPlace",                "StockPlace",                ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "StockValue",                "StockValue",                ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "StockWarning",              "StockWarning",              ""));//14 Digits.
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(),                                         "SupplierName",              "SupplierName",              ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(),                                         "SupplierNumber",            "SupplierNumber",            ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(typeStringOptions),                   "Type",                      "Type",                      ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(),                                         "Unit",                      "Unit",                      ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(),                                         "Url",                       "URL",                       ""));
		model.addProperty(new Property(PType.FLOAT,  PropertyBuilder.newDefault(),                                         "VAT",                       "VAT",                       ""));
		model.addProperty(new Property(PType.BOOL,   PropertyBuilder.newDefault(),                                         "WebshopArticle",            "WebshopArticle",            ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 99999999),                             "Weight",                    "Weight",                    ""));
		model.addProperty(new Property(PType.INT,    PropertyBuilder.newIntRange(0, 99999999),                             "Width",                     "Width",                     ""));
		
		return model;
	}
	
//	Name:						Type:		Readable:	Writable:	Searchable:		Sortable:	Required:	Length:		Valid:
//	@url						String		Yes			No			No				No			No			-			-
//	Active						Boolean		Yes			Yes			No				No			No			-			false or true
//	Address1					String		Yes			Yes			No				No			No			1024		-
//	Address2					String		Yes			Yes			No				No			No			1024		-
//	City						String		Yes			Yes			Yes				No			No			1024		-
//	Comments					String		Yes			Yes			No				No			No			1024		-
//	CostCenter					String		Yes			Yes			No				No			No			-			-
//	Country						String		Yes			No			No				No			No			-			-
//	CountryCode					String		Yes			Yes			No				No			No			2			-
//	Currency					String		Yes			Yes			No				No			No			3			-
//	CustomerNumber				String		Yes			Yes			Yes				Yes			No			1024		-
//	DefaultDeliveryTypes		Object		Yes			Yes			No				No			No			-			-
//	DefaultTemplates			Object		Yes			Yes			No				No			No			-			-
//	DeliveryAddress1			String		Yes			Yes			No				No			No			1024		-
//	DeliveryAddress2			String		Yes			Yes			No				No			No			1024		-
//	DeliveryCity				String		Yes			Yes			No				No			No			1024		-
//	DeliveryCountry				String		Yes			No			No				No			No			1024		-
//	DeliveryCountryCode			String		Yes			Yes			No				No			No			2			-
//	DeliveryFax					String		Yes			Yes			No				No			No			1024		-
//	DeliveryName				String		Yes			Yes			No				No			No			1024		-
//	DeliveryPhone1				String		Yes			Yes			No				No			No			1024		-
//	DeliveryPhone2				String		Yes			Yes			No				No			No			1024		-
//	DeliveryZipCode				String		Yes			Yes			No				No			No			10			-
//	Email						String		Yes			Yes			Yes				No			No			1024		-
//	EmailInvoice				String		Yes			Yes			No				No			No			1024		-
//	EmailInvoiceBCC				String		Yes			Yes			No				No			No			1024		-
//	EmailInvoiceCC				String		Yes			Yes			No				No			No			1024		-
//	EmailOffer					String		Yes			Yes			No				No			No			1024		-
//	EmailOfferBCC				String		Yes			Yes			No				No			No			1024		-
//	EmailOfferCC				String		Yes			Yes			No				No			No			1024		-
//	EmailOrder					String		Yes			Yes			No				No			No			1024		-
//	EmailOrderBCC				String		Yes			Yes			No				No			No			1024		-
//	EmailOrderCC				String		Yes			Yes			No				No			No			1024		-
//	Fax							String		Yes			Yes			No				No			No			1024		-
//	GLN							String		Yes			Yes			Yes				No			No			1024		-
//	GLNDelivery					String		Yes			Yes			Yes				No			No			1024		-
//	InvoiceAdministrationFee	Float		Yes			Yes			No				No			No			11			-
//	InvoiceDiscount				Float		Yes			Yes			No				No			No			11			-
//	InvoiceFreight				Float		Yes			Yes			No				No			No			11			-
//	InvoiceRemark				String		Yes			Yes			No				No			No			1024		-
//	Name						String		Yes			Yes			Yes				Yes			Yes			1024		-
//	OrganisationNumber			String		Yes			Yes			Yes				No			No			30			-
//	OurReference				String		Yes			Yes			No				No			No			50			-
//	Phone1						String		Yes			Yes			Yes				No			No			1024		-
//	Phone2						String		Yes			Yes			No				No			No			1024		-
//	PriceList					String		Yes			Yes			No				No			No			-			-
//	Project						String		Yes			Yes			No				No			No			-			-
//	SalesAccount				String		Yes			Yes			No				No			No			4			-
//	ShowPriceVATIncluded		Boolean		Yes			Yes			No				No			No			-			false or true
//	TermsOfDelivery				String		Yes			Yes			No				No			No			-			-
//	TermsOfPayment				String		Yes			Yes			No				No			No			-			-
//	Type						String		Yes			Yes			No				No			No			-			'COMPANY' or 'PRIVATE'
//	VATNumber					String		Yes			Yes			No				No			No			-			-
//	VATType						String		Yes			Yes			No				No			No			-			'EUREVERSEDVAT', 'EUVAT', 'EXPORT', 'SEREVERSEDVAT' or 'SEVAT'
//	VisitingAddress				String		Yes			Yes			No				No			No			128			-
//	VisitingCity				String		Yes			Yes			No				No			No			128			-
//	VisitingCountry				String		Yes			No			No				No			No			-			-
//	VisitingCountryCode			String		Yes			Yes			No				No			No			2			-
//	VisitingZipCode				String		Yes			Yes			No				No			No			10			-
//	WWW							String		Yes			Yes			No				No			No			128			-
//	WayOfDelivery				String		Yes			Yes			No				No			No			-			-
//	YourReference				String		Yes			Yes			No				No			No			50			-
//	ZipCode						String		Yes			Yes			Yes				No			No			10			-
	public static Model createCustomerModel() {
		final List<Pair<String, String>> typeStringOptions = Arrays.asList(
			new Pair<>("COMPANY", "TypeCompany"),
			new Pair<>("PRIVATE", "TypePrivate")
		);
		final List<Pair<String, String>> vATTypeStringOptions = Arrays.asList(
			new Pair<>("EUREVERSEDVAT", "VATTypeEUReversedVAT"),
			new Pair<>("EUVAT",         "VATTypeEUVAT"),
			new Pair<>("EXPORT",        "VATTypeExport"),
			new Pair<>("SEREVERSEDVAT", "VATTypeSEReversedVAT"),
			new Pair<>("SEVAT",         "VATTypeSEVAT")
		);
		
		final
		Model model = new Model("Customer", "Customer");
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "@url",                     "URL",                      ""));
		model.addProperty(new Property(PType.BOOL,                            PropertyBuilder.newDefault(),                          "Active",                   "Active",                   ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Address1",                 "Address_1",                ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Address2",                 "Address_2",                ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "City",                     "City",                     ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Comments",                 "Comments",                 ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "CostCenter",               "CostCenter",               ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "Country",                  "Country",                  ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 2),                 "CountryCode",              "CountryCode",              ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 3),                 "Currency",                 "Currency",                 ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "CustomerNumber",           "CustomerNumber",           ""));
		model.addProperty(new Property(PType.valueOf("DefaultDeliveryTypes"), PropertyBuilder.newDefault(),                          "DefaultDeliveryTypes",     "DefaultDeliveryTypes",     ""));
		model.addProperty(new Property(PType.valueOf("DefaultTemplates"),     PropertyBuilder.newDefault(),                          "DefaultTemplates",         "DefaultTemplates",         ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryAddress1",         "DeliveryAddress_1",        ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryAddress2",         "DeliveryAddress_2",        ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryCity",             "DeliveryCity",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryCountry",          "DeliveryCountry",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 2),                 "DeliveryCountryCode",      "DeliveryCountryCode",      ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryFax",              "DeliveryFax",              ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryName",             "DeliveryName",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryPhone1",           "DeliveryPhone_1",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "DeliveryPhone2",           "DeliveryPhone_2",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 10),                "DeliveryZipCode",          "DeliveryZipCode",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Email",                    "EMail",                    ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailInvoice",             "EMailInvoice",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailInvoiceBCC",          "EMailInvoiceBCC",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailInvoiceCC",           "EMailInvoiceCC",           ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOffer",               "EMailOffer",               ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOfferBCC",            "EMailOfferBCC",            ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOfferCC",             "EMailOfferCC",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOrder",               "EMailOrder",               ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOrderBCC",            "EMailOrderBCC",            ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "EmailOrderCC",             "EMailOrderCC",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Fax",                      "Fax",                      ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "GLN",                      "GLN",                      ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "GLNDelivery",              "GLNDelivery",              ""));
		model.addProperty(new Property(PType.FLOAT,                           PropertyBuilder.newDefault(),                          "InvoiceAdministrationFee", "InvoiceAdministrationFee", ""));//11 Digits.
		model.addProperty(new Property(PType.FLOAT,                           PropertyBuilder.newDefault(),                          "InvoiceDiscount",          "InvoiceDiscount",          ""));//11 Digits.
		model.addProperty(new Property(PType.FLOAT,                           PropertyBuilder.newDefault(),                          "InvoiceFreight",           "InvoiceFreight",           ""));//11 Digits.
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "InvoiceRemark",            "InvoiceRemark",            ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Name",                     "Name",                     ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 30),                "OrganisationNumber",       "OrganisationNumber",       ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 50),                "OurReference",             "OurReference",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Phone1",                   "Phone_1",                  ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 1024),              "Phone2",                   "Phone_2",                  ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "PriceList",                "PriceList",                ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "Project",                  "Project",                  ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 4),                 "SalesAccount",             "SalesAccount",             ""));
		model.addProperty(new Property(PType.BOOL,                            PropertyBuilder.newDefault(),                          "ShowPriceVATIncluded",     "ShowPriceVATIncluded",     ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "TermsOfDelivery",          "TermsOfDelivery",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "TermsOfPayment",           "TermsOfPayment",           ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringOption(typeStringOptions),    "Type",                     "Type",                     ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "VATNumber",                "VATNumber",                ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringOption(vATTypeStringOptions), "VATType",                  "VATType",                  ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 128),               "VisitingAddress",          "VisitingAddress",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 128),               "VisitingCity",             "VisitingCity",             ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "VisitingCountry",          "VisitingCountry",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 2),                 "VisitingCountryCode",      "VisitingCountryCode",      ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 10),                "VisitingZipCode",          "VisitingZipCode",          ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 128),               "WWW",                      "WWW",                      ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newDefault(),                          "WayOfDelivery",            "WayOfDelivery",            ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 50),                "YourReference",            "YourReference",            ""));
		model.addProperty(new Property(PType.STRING,                          PropertyBuilder.newStringLength(0, 10),                "ZipCode",                  "ZipCode",                  ""));
		
		return model;
	}
	
//	Name:						Type:		Readable:	Writable:	Searchable:		Sortable:	Required:	Length:		Valid:
//	Invoice						String		Yes			Yes			No				No			No			-			'EMAIL', 'PRINT' or 'PRINTSERVICE'
//	Offer						String		Yes			Yes			No				No			No			-			'EMAIL', 'PRINT' or 'PRINTSERVICE'
//	Order						String		Yes			Yes			No				No			No			-			'EMAIL', 'PRINT' or 'PRINTSERVICE'
	public static Model createDefaultDeliveryTypesModel() {
		final List<Pair<String, String>> invoiceStringOptions = Arrays.asList(
			new Pair<>("EMAIL", "InvoiceEMail"),
			new Pair<>("PRINT", "InvoicePrint"),
			new Pair<>("PRINTSERVICE", "InvoicePrintService")
		);
		final List<Pair<String, String>> offerStringOptions = Arrays.asList(
			new Pair<>("EMAIL", "OfferEMail"),
			new Pair<>("PRINT", "OfferPrint"),
			new Pair<>("PRINTSERVICE", "OfferPrintService")
		);
		final List<Pair<String, String>> orderStringOptions = Arrays.asList(
			new Pair<>("EMAIL", "OrderEMail"),
			new Pair<>("PRINT", "OrderPrint"),
			new Pair<>("PRINTSERVICE", "OrderPrintService")
		);
		
		final
		Model model = new Model("DefaultDeliveryTypes", "DefaultDeliveryTypes");
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(invoiceStringOptions), "Invoice", "Invoice", ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(offerStringOptions),   "Offer",   "Offer",   ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newStringOption(orderStringOptions),   "Order",   "Order",   ""));
		
		return model;
	}
	
//	Name:						Type:		Readable:	Writable:	Searchable:		Sortable:	Required:	Length:		Valid:
//	CashInvoice					String		Yes			Yes			No				No			No			-			-
//	Invoice						String		Yes			Yes			No				No			No			-			-
//	Offer						String		Yes			Yes			No				No			No			-			-
//	Order						String		Yes			Yes			No				No			No			-			-
	public static Model createDefaultTemplatesModel() {
		final
		Model model = new Model("DefaultTemplates", "DefaultTemplates");
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(), "CashInvoice", "CashInvoice", ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(), "Invoice",     "Invoice",     ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(), "Offer",       "Offer",       ""));
		model.addProperty(new Property(PType.STRING, PropertyBuilder.newDefault(), "Order",       "Order",       ""));
		
		return model;
	}
}