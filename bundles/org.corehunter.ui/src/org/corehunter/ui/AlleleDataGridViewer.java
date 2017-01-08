/*******************************************************************************
 * Copyright 2016 Guy Davenport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
