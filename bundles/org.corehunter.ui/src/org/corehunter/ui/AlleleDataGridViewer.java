package org.corehunter.ui;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;

public class AlleleDataGridViewer<ElementType extends Object> extends DataGridViewer<ElementType, Allele> {

	private Map<String, GridColumnGroup> columnsGroups;
	private Map<String, GridColumn> columns;

	/**
	 * @param parent
	 * @param configuration
	 */
	public AlleleDataGridViewer() {
		super();
		columnsGroups = new TreeMap<String, GridColumnGroup>();
		columns = new TreeMap<String, GridColumn>();
	}

	@Override
	protected void updateGridViewer(GridTableViewer gridViewer, int size, Allele[] columnHeaders) {

		if (columns.size() > 0) {
			// remove all columns
			Iterator<GridColumn> iterator = columns.values().iterator();

			while (iterator.hasNext()) {
				iterator.next().dispose();
			}

			columns.clear();
		}

		if (columnsGroups.size() > 0) {
			// remove all column groups
			Iterator<GridColumnGroup> iterator = columnsGroups.values().iterator();

			while (iterator.hasNext()) {
				iterator.next().dispose();
			}

			columnsGroups.clear();
		}
		
		if (viewerColumns.size() > 0) {
			// remove all viewer column
			Iterator<GridViewerColumn> iterator = viewerColumns.values().iterator();

			while (iterator.hasNext()) {
				iterator.next().getColumn().dispose();
			}

			viewerColumns.clear();
		}

		if (size > 0) {

			GridColumnGroup columnGroup;
			GridColumn column;
			GridViewerColumn viewerColumn ;

			for (int index = 0; index < columnHeaders.length; ++index) {

				columnGroup = columnsGroups.get(columnHeaders[index].getMarker().getUniqueIdentifier());

				if (columnGroup == null) {
					columnGroup = new GridColumnGroup(gridViewer.getGrid(), SWT.TOGGLE);
					columnsGroups.put(columnHeaders[index].getMarker().getUniqueIdentifier(), columnGroup);
					columnGroup.setText(columnHeaders[index].getMarker().getName());
				}

				column = new GridColumn(columnGroup, SWT.NONE);
				column.setText(columnHeaders[index].getName());
				
				viewerColumn = new GridViewerColumn(gridViewer, column) ;
				
                viewerColumns.put(index, viewerColumn);
                
				viewerColumn.setLabelProvider(createColumnLabelProvider(index));
                
                configureViewerColumn(viewerColumn, columnHeaders[index]) ;
			}
		}
	}
}
