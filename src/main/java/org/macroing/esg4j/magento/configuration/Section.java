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
package org.macroing.esg4j.magento.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Section {
	private final List<Group> groups;
	private String id;
	private String label;
	private String resource;
	private Tab tab;
	private boolean hasSeparatorAtTop;
	private boolean isShowingInDefault;
	private boolean isShowingInStore;
	private boolean isShowingInWebsite;
	private boolean isTranslatingLabel;
	private int sortOrder;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Section(final String id, final String label, final String resource, final Tab tab, final boolean hasSeparatorAtTop, final boolean isShowingInDefault, final boolean isShowingInStore, final boolean isShowingInWebsite, final boolean isTranslatingLabel, final int sortOrder) {
		this.groups = new ArrayList<>();
		this.id = Objects.requireNonNull(id, "id == null");
		this.label = Objects.requireNonNull(label, "label == null");
		this.resource = Objects.requireNonNull(resource, "resource == null");
		this.tab = Objects.requireNonNull(tab, "tab == null");
		this.hasSeparatorAtTop = hasSeparatorAtTop;
		this.isShowingInDefault = isShowingInDefault;
		this.isShowingInStore = isShowingInStore;
		this.isShowingInWebsite = isShowingInWebsite;
		this.isTranslatingLabel = isTranslatingLabel;
		this.sortOrder = sortOrder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Group> getGroups() {
		return new ArrayList<>(this.groups);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String getResource() {
		return this.resource;
	}
	
	public Tab getTab() {
		return this.tab;
	}
	
	public boolean hasSeparatorAtTop() {
		return this.hasSeparatorAtTop;
	}
	
	public boolean isShowingInDefault() {
		return this.isShowingInDefault;
	}
	
	public boolean isShowingInStore() {
		return this.isShowingInStore;
	}
	
	public boolean isShowingInWebsite() {
		return this.isShowingInWebsite;
	}
	
	public boolean isTranslatingLabel() {
		return this.isTranslatingLabel;
	}
	
	public int getSortOrder() {
		return this.sortOrder;
	}
	
	public void addGroup(final Group group) {
		this.groups.add(Objects.requireNonNull(group, "group == null"));
	}
	
	public void removeGroup(final Group group) {
		this.groups.remove(Objects.requireNonNull(group, "group == null"));
	}
	
	public void setId(final String id) {
		this.id = Objects.requireNonNull(id, "id == null");
	}
	
	public void setLabel(final String label) {
		this.label = Objects.requireNonNull(label, "label == null");
	}
	
	public void setResource(final String resource) {
		this.resource = Objects.requireNonNull(resource, "resource == null");
	}
	
	public void setSeparatorAtTop(final boolean hasSeparatorAtTop) {
		this.hasSeparatorAtTop = hasSeparatorAtTop;
	}
	
	public void setShowingInDefault(final boolean isShowingInDefault) {
		this.isShowingInDefault = isShowingInDefault;
	}
	
	public void setShowingInStore(final boolean isShowingInStore) {
		this.isShowingInStore = isShowingInStore;
	}
	
	public void setShowingInWebsite(final boolean isShowingInWebsite) {
		this.isShowingInWebsite = isShowingInWebsite;
	}
	
	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public void setTab(final Tab tab) {
		this.tab = Objects.requireNonNull(tab, "tab == null");
	}
	
	public void setTranslatingLabel(final boolean isTranslatingLabel) {
		this.isTranslatingLabel = isTranslatingLabel;
	}
}