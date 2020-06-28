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

public final class Table {
	private final List<Column> columns;
	private String comment;
	private String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Table() {
		this("table");
	}
	
	public Table(final String name) {
		this(name, "");
	}
	
	public Table(final String name, final String comment) {
		this.columns = new ArrayList<>();
		this.name = Objects.requireNonNull(name, "name == null");
		this.comment = Objects.requireNonNull(comment, "comment == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Column> getColumns() {
		return new ArrayList<>(this.columns);
	}
	
	public List<Column> getColumnsForeignKey() {
		return this.columns.stream().filter(column -> column.hasForeignKey()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	public List<Column> getColumnsPrimaryKey() {
		return this.columns.stream().filter(column -> column.hasPrimaryKey()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	public List<Column> getColumnsUniqueKey() {
		return this.columns.stream().filter(column -> column.hasUniqueKey()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return String.format("new Table(\"%s\")", this.name);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Table)) {
			return false;
		} else if(!Objects.equals(this.columns, Table.class.cast(object).columns)) {
			return false;
		} else if(!Objects.equals(this.comment, Table.class.cast(object).comment)) {
			return false;
		} else if(!Objects.equals(this.name, Table.class.cast(object).name)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.columns, this.comment, this.name);
	}
	
	public void addColumn(final Column column) {
		this.columns.add(Objects.requireNonNull(column, "column == null"));
	}
	
	public void removeColumn(final Column column) {
		this.columns.remove(Objects.requireNonNull(column, "column == null"));
	}
	
	public void setComment(final String comment) {
		this.comment = Objects.requireNonNull(comment, "comment == null");
	}
	
	public void setName(final String name) {
		this.name = Objects.requireNonNull(name, "name == null");
	}
}