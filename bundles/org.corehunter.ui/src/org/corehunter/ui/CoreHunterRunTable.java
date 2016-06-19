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

import java.text.DateFormat;

import javax.inject.Inject;

import org.corehunter.services.CoreHunterRunServices;
import org.corehunter.services.CoreHunterRun;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import uno.informatics.data.Dataset;

public class CoreHunterRunTable {
    private CoreHunterRunComparator comparator;

    private TableViewer viewer;

    private CoreHunterRunServices corehunterRunClient;

    private DateTimeFormatter dateTimeFormatter;

    private CoreHunterRun selectedCorehunterRun;

    @Inject
    public CoreHunterRunTable() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<?> serviceReference = bundleContext.getServiceReference(CoreHunterRunServices.class.getName());
        setResultClient((CoreHunterRunServices) bundleContext.getService(serviceReference));

        dateTimeFormatter = DateTimeFormat.shortDateTime();

    }

    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText("Search: ");
        final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        createViewer(parent);

        CorehunterRunFilter corehunterRunFilter = new CorehunterRunFilter();

        searchText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                corehunterRunFilter.setSearchText(searchText.getText());
                viewer.refresh();
            }
        });

        viewer.addFilter(corehunterRunFilter);

        viewer.setContentProvider(new ArrayContentProvider());

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();

                selectedCorehunterRun = (CoreHunterRun) selection.getFirstElement();
            }
        });

        // Set the sorter for the table
        comparator = new CoreHunterRunComparator();
        viewer.setComparator(comparator);
    }

    public CoreHunterRun getSelectedCorehunterRun() {

        return selectedCorehunterRun;
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        viewer.addSelectionChangedListener(listener);
    }

    private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        updateViewer();

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
    }

    public void updateViewer() {
        viewer.setInput(corehunterRunClient.getAllCoreHunterRuns());
    }

    // This will create the columns for the table
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Name", "Start", "End", "Status" };
        int[] bounds = { 100, 100, 100, 100 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterRun corehunterRun = (CoreHunterRun) element;
                return corehunterRun.getName();
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterRun corehunterRun = (CoreHunterRun) element;
                return dateTimeFormatter.print(corehunterRun.getStartDate());
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterRun corehunterRun = (CoreHunterRun) element;
                return dateTimeFormatter.print(corehunterRun.getEndDate());
            }
        });

        col = createTableViewerColumn(titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterRun corehunterRun = (CoreHunterRun) element;
                return corehunterRun.getStatus().getName();
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

    public void cleaerSelectedCorehunterRun() {
        viewer.getTable().deselectAll();
        selectedCorehunterRun = null;
    }

    private synchronized final void setResultClient(CoreHunterRunServices corehunterRunClient) {
        this.corehunterRunClient = corehunterRunClient;
    }

    private class CorehunterRunFilter extends ViewerFilter {

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
            CoreHunterRun corehunterRun = (CoreHunterRun) element;
            if (corehunterRun.getName() != null && corehunterRun.getName().matches(searchString)) {
                return true;
            }

            return false;
        }
    }

}
