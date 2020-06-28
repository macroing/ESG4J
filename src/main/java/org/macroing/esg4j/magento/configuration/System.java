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

public final class System {
	private final List<Section> sections;
	private Tab tab;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public System() {
		this.sections = new ArrayList<>();
		this.tab = new Tab();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Section> getSections() {
		return new ArrayList<>(this.sections);
	}
	
	public Tab getTab() {
		return this.tab;
	}
	
	public void addSection(final Section section) {
		this.sections.add(Objects.requireNonNull(section, "section == null"));
	}
	
	public void removeSection(final Section section) {
		this.sections.remove(Objects.requireNonNull(section, "section == null"));
	}
	
	public void setTab(final Tab tab) {
		this.tab = Objects.requireNonNull(tab, "tab == null");
	}
}