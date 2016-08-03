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

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.matrix.array.ArrayMatrixData;

/**
 * @author Guy Davenport
 *
 */
public abstract class ArrayMatrixDataViewer<ValueType extends Object>  {
    private static final int MIM_COLUMN_SIZE = 5;
    private GridTableViewer gridViewer;

    private ArrayMatrixData<ValueType> value;
    private Map<Integer, GridViewerColumn> viewerColumns;

    /**
     * @param parent
     * @param configuration
     */
    public ArrayMatrixDataViewer() {
        viewerColumns = new TreeMap<Integer, GridViewerColumn>();
    }

    public void setValue(ArrayMatrixData<ValueType> value) {
        if (!ObjectUtils.equals(this.value, value)) {
            this.value = value;
        }
    }

    public void createPartControl(Composite parent) {
        gridViewer = new GridTableViewer(parent);

        gridViewer.setContentProvider(new ArrayMatrixDataContentProvider());
        gridViewer.getGrid().setHeaderVisible(true);

        updateGridViewer();

        gridViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleViewerSelectionChanged();
            }
        });

    }

    protected void handleViewerSelectionChanged() {

    }

    private void updateGridViewer() {
        if (value != null && value.getSize() > 0) {
            if (gridViewer != null) {
                int oldColumnCount = viewerColumns.size();
                int newColumnCount = value.getSize() ;

                // create new columns
                if (newColumnCount > oldColumnCount) {
                    for (int i = oldColumnCount; i < newColumnCount; ++i) {
                        viewerColumns.put(i, new GridViewerColumn(gridViewer, SWT.NONE, i));
                    }
                } else {
                    // remove columns
                    if (newColumnCount < oldColumnCount) {
                        for (int i = newColumnCount; i > oldColumnCount; --i) {
                            GridViewerColumn gridViewerColumn = viewerColumns.get(i - 1);

                            if (gridViewerColumn != null)
                                gridViewerColumn.getColumn().dispose();
                        }
                    }
                }

                GridViewerColumn gridViewerColumn;
                
                String headerName ;

                for (int index = 0 ; index < value.getSize() ; ++index) {
                    
                    gridViewerColumn = viewerColumns.get(index);
                    
                    headerName = value.getHeader(index).getName() ;

                    gridViewerColumn.getColumn().setText(headerName);

                    gridViewerColumn.setLabelProvider(createLabelProvider(index));

                    gridViewerColumn.getColumn().setWidth(guessColumnWidth(headerName));
                }

                // NOT in RAP
                //gridViewer.getGrid().setRowHeaderVisible(true);

                gridViewer.setRowHeaderLabelProvider(new RowHeaderLabelProvider());

                gridViewer.setInput(value);
            }
        } else {
            if (gridViewer != null) {
                Iterator<GridViewerColumn> iterator = viewerColumns.values().iterator();

                while (iterator.hasNext())
                    iterator.next().getColumn().dispose();

                viewerColumns.clear();

                gridViewer.setInput(null);
            }
        }
    }

    protected abstract CellLabelProvider createLabelProvider(int columnIndex) ;

    private int guessColumnWidth(String columnHeader) {
        int size = 0;

        if (columnHeader != null && columnHeader.length() > 0)
            size = (columnHeader.length() * 10) + 10;

        if (size < MIM_COLUMN_SIZE)
            size = MIM_COLUMN_SIZE;

        return size;
    }
}
