package org.corehunter.ui;

import uno.informatics.data.SimpleEntity;

public class DataGridViewerRow<ElementType> {
	SimpleEntity header ;
	ElementType[] elements ;

	public DataGridViewerRow(SimpleEntity header, ElementType[] elements) {
		super();
		
		if (header == null) {
			throw new NullPointerException("Header must be defined!") ;
		}
		
		if (elements == null) {
			throw new NullPointerException("Elements must be defined!") ;
		}
		this.header = header;
		this.elements = elements;
	}
	
	public final SimpleEntity getHeader() {
		return header;
	}
	
	public final ElementType[] getElements() {
		return elements;
	}
	
	public int getSize() {
		return elements.length ;
	}	
}