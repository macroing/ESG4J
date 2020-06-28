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

import java.util.Objects;

public final class Tab {
	private String id;
	private String label;
	private boolean isTranslatingLabel;
	private int sortOrder;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Tab() {
		this("Vendor", "Module");
	}
	
	public Tab(final String id, final String label) {
		this(id, label, true, 0);
	}
	
	public Tab(final String id, final String label, final boolean isTranslatingLabel, final int sortOrder) {
		this.id = Objects.requireNonNull(id, "id == null");
		this.label = Objects.requireNonNull(label, "label == null");
		this.isTranslatingLabel = isTranslatingLabel;
		this.sortOrder = sortOrder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getId() {
		return this.id;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public boolean isTranslatingLabel() {
		return this.isTranslatingLabel;
	}
	
	public int getSortOrder() {
		return this.sortOrder;
	}
	
	public void setId(final String id) {
		this.id = Objects.requireNonNull(id, "id == null");
	}
	
	public void setLabel(final String label) {
		this.label = Objects.requireNonNull(label, "label == null");
	}
	
	public void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public void setTranslatingLabel(final boolean isTranslatingLabel) {
		this.isTranslatingLabel = isTranslatingLabel;
	}
}