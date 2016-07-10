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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import uno.informatics.data.Data;
import uno.informatics.data.SimpleEntity;

public class HeaderViewer {
    private SimpleEntityComparator comparator;

    private CheckboxTableViewer viewer;

    protected SimpleEntity[] headers;
    
    protected List<SimpleEntity> checkedHeaders;

    protected SimpleEntity selectedHeader;

    public HeaderViewer() {
        
    }

    /**
     * @wbp.parser.entryPoint
     */
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText("Search: ");
        final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        createViewer(parent);

        SimpleEntityFilter filter = new SimpleEntityFilter();

        searchText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                filter.setSearchText(searchText.getText());
                viewer.refresh();
                
                // TODO need to maintain check on filter.
            }
        });

        viewer.addFilter(filter);

        // Set the sorter for the table
        comparator = new SimpleEntityComparator();
        viewer.setComparator(comparator);

    }

    private void createViewer(Composite parent) {
        viewer = CheckboxTableViewer.newCheckList(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                
                selectedHeader = (SimpleEntity)selection.getFirstElement() ;
            }
  
        });

        viewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                
                if (event.getChecked()) {
                    checkedHeaders.add((SimpleEntity)event.getElement()) ;
                } else {
                    checkedHeaders.remove((SimpleEntity)event.getElement()) ;
                }
                
            }});
        
        updateViewer();

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
    }

    public final void updateViewer() {
        viewer.setInput(headers);
    }

    // This will create the columns for the table
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "ID", "Name"};
        int[] bounds = { 150, 150 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                SimpleEntity header = (SimpleEntity) element;
                return header.getUniqueIdentifier();
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                SimpleEntity header = (SimpleEntity) element;
                return header.getName();
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        column.addSelectionListener(getSelectionAdapter(column, colNumber));
        return viewerColumn;
    }

    private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
                viewer.refresh();
            }
        };
        return selectionAdapter;
    }

    /**
     * Passing the focus request to the viewer's control.
     */

    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
    public final SimpleEntity[] getHeaders() {
        return headers;
    }

    public final void setHeaders(SimpleEntity[] headers) {
       this.headers = headers ;
       checkedHeaders = new ArrayList<SimpleEntity>(headers.length) ;
       updateViewer(); 
    }

    public final SimpleEntity getSelectedHeader() {

        return selectedHeader;
    }

    public void cleaerSelectedHeader() {
        viewer.getTable().deselectAll();
        selectedHeader = null;
    }
    
    public void setChecked(SimpleEntity[] headers) {      
        viewer.setCheckedElements(headers) ; 
    }
    
    public void clearChecked() {
        viewer.setAllChecked(false) ;
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (viewer != null)
            viewer.addSelectionChangedListener(listener);
    }
    
    public void addDoubleClickListener(IDoubleClickListener listener) {
        if (viewer != null)
            viewer.addDoubleClickListener(listener);
    }

    private class SimpleEntityFilter extends ViewerFilter {

        private String searchString;

        public void setSearchText(String s) {
            // ensure that the value can be used for matching
            this.searchString = ".*" + s + ".*";
        }

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (searchString == null || searchString.length() == 0) {
                return true;
            }
            
            SimpleEntity entity = (SimpleEntity) element;
            if (entity.getName() != null && entity.getName().matches(searchString)) {
                return true;
            }
            
            if (entity.getUniqueIdentifier() != null && entity.getUniqueIdentifier().matches(searchString)) {
                return true;
            }

            return false;
        }
    }
}