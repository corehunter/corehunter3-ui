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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jamesframework.core.subset.SubsetSolution;

import uno.informatics.data.SimpleEntity;

/**
 * @author Guy Davenport
 *
 */
public class DataGridViewer<ElementType extends Object, ColumnHeaderType extends Object> 
	implements SolutionContainer {
    private static final int MIM_COLUMN_SIZE = 5;
    private GridTableViewer gridViewer;

    private DataGridViewerRow<ElementType>[] rows ;
    private ColumnHeaderType[] columnHeaders;
	private IStructuredContentProvider contentProvider;
	private CellLabelProvider rowHeaderLabelProvider;
	private Map<Integer, GridViewerColumn> viewerColumns;

    /**
     * @param parent
     * @param configuration
     */
    public DataGridViewer() {
        contentProvider = new ArrayContentProvider() ;
        viewerColumns = new TreeMap<Integer, GridViewerColumn>() ;
        rowHeaderLabelProvider = new SimpleEntityCellLabelProvider<ElementType>() ;
    }
    
	public void setContent(DataGridViewerRow<ElementType>[] rows, ColumnHeaderType[] columnHeaders) {
        if (!ObjectUtils.equals(this.rows, rows) || 
        		!ObjectUtils.equals(this.columnHeaders, columnHeaders)) {
        	
    		if (rows == null) {
    			throw new NullPointerException("Rows must be defined!") ;
    		}
    		
    		if (columnHeaders == null) {
    			throw new NullPointerException("Column headers must be defined!") ;
    		}
    		
    		if (rows.length > 0 && rows[0].getSize() != columnHeaders.length) {
    			throw new IllegalStateException("Number of columns headers must match number of columns in first row!") ;
    		}
    		
            this.rows = rows;
            this.columnHeaders = columnHeaders;
            updateGridViewer() ;
        }
	}

	public final void setContentProvider(IStructuredContentProvider contentProvider) {
		if (gridViewer != null) {
			throw new IllegalStateException("Content provider must be set before creating the part.") ;
		}
		this.contentProvider = contentProvider;
	}
	
	public final void setRowHeaderLabelProvider(CellLabelProvider rowHeaderLabelProvider) {
		if (gridViewer != null) {
			throw new IllegalStateException("Row header label provider provider must be set before creating the part.") ;
		}
		this.rowHeaderLabelProvider = rowHeaderLabelProvider;
	}
	
	public void createPartControl(Composite parent) {
        gridViewer = new GridTableViewer(parent);

        gridViewer.setContentProvider(contentProvider);
        gridViewer.getGrid().setRowHeaderVisible(true);
        gridViewer.setRowHeaderLabelProvider(rowHeaderLabelProvider);
        gridViewer.getGrid().setHeaderVisible(true);

        gridViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleViewerSelectionChanged();
            }
        });
        
        updateGridViewer() ;
    }
    
    @Override
	public SubsetSolution getSolution() {
		return null ;
	}  
	
    @Override
	public void setSolution(SubsetSolution solution) {
		// TODO Auto-generated method stub
		
	}  

    protected void handleViewerSelectionChanged() {

    }

    private void updateGridViewer() {
        if (rows != null && rows.length > 0) {
            if (gridViewer != null) {
                int oldColumnCount = viewerColumns.size();
                int newColumnCount = columnHeaders.length ;

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

                Iterator<Integer> iterator = viewerColumns.keySet().iterator();
                GridViewerColumn gridViewerColumn;

                int index = 0;
                int i = 0 ;

                while (iterator.hasNext()) {
                    index = iterator.next();

                    gridViewerColumn = viewerColumns.get(index);
                    gridViewerColumn.setLabelProvider(createColumnLabelProvider(i));
                    
                    configureViewerColumn(gridViewerColumn, columnHeaders[index]) ;
                    
                    ++i ;
                }

                gridViewer.setInput(rows);
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
    
	protected CellLabelProvider createColumnLabelProvider(int i) {
    	
		return new DataGridLabelProvider<ElementType>(i);
	}
	
	protected void configureViewerColumn(GridViewerColumn gridViewerColumn, ColumnHeaderType columnHeader) {
    	
    	if (columnHeader instanceof SimpleEntity) {
    		String name = ((SimpleEntity)columnHeader).getName() ;
    		gridViewerColumn.getColumn().setText(name);	
    		gridViewerColumn.getColumn().setWidth(guessColumnWidth(name));
    		gridViewerColumn.getColumn().setHeaderWordWrap(true);
            
    	} else {
    		String name = columnHeader.toString() ;
    		gridViewerColumn.getColumn().setText(name);	
    		gridViewerColumn.getColumn().setWidth(guessColumnWidth(name));
    		gridViewerColumn.getColumn().setHeaderWordWrap(true);
    	}
	}

    private int guessColumnWidth(String columnHeader) {
        int size = 0;

        if (columnHeader != null && columnHeader.length() > 0)
            size = (columnHeader.length() * 10) + 10;

        if (size < MIM_COLUMN_SIZE)
            size = MIM_COLUMN_SIZE;

        return size;
    }

}
