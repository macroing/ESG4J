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
package org.macroing.esg4j.magento.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Database {
	private final List<Table> tables;
	private String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Database() {
		this("database");
	}
	
	public Database(final String name) {
		this.tables = new ArrayList<>();
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Table> getTables() {
		return new ArrayList<>(this.tables);
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return String.format("new Database(\"%s\")", this.name);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Database)) {
			return false;
		} else if(!Objects.equals(this.tables, Database.class.cast(object).tables)) {
			return false;
		} else if(!Objects.equals(this.name, Database.class.cast(object).name)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.tables, this.name);
	}
	
	public void addTable(final Table table) {
		this.tables.add(Objects.requireNonNull(table, "table == null"));
	}
	
	public void removeTable(final Table table) {
		this.tables.remove(Objects.requireNonNull(table, "table == null"));
	}
	
	public void setName(final String name) {
		this.name = Objects.requireNonNull(name, "name == null");
	}
}