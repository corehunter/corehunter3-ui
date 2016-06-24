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

import java.util.List;

import org.corehunter.CoreHunterObjective;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ObjectiveViewer {
    private TableViewer viewer;

    private List<CoreHunterObjective> objectives;

    private CoreHunterObjective selectedObjective;

    public ObjectiveViewer() {

    }

    /**
     * @wbp.parser.entryPoint
     */
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout(1, false);
        parent.setLayout(layout);
        
        createViewer(parent);
    }

    private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();

                selectedObjective = (CoreHunterObjective) selection.getFirstElement();
            }
        });

        updateViewer();

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
    }

    public final void updateViewer() {
        viewer.setInput(objectives);
    }
    
    public final List<CoreHunterObjective> getObjectives() {
        return objectives;
    }

    public final void setObjectives(List<CoreHunterObjective> objectives) {
        this.objectives = objectives;
        updateViewer();
    }

    // This will create the columns for the table
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Objective", "Measure", "Weight" };
        
        int[] bounds = { 200, 200, 50};

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return objective.getObjectiveType().getName() ;
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return objective.getMeasure().getName() ;
            }
        });
        
        col = createTableViewerColumn(titles[2], bounds[2], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return String.format("%e", objective.getWeight());
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
        return viewerColumn;
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */

    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public CoreHunterObjective getSelectedObjective() {

        return selectedObjective;
    }

    public void cleaerSelectedObjective() {
        viewer.getTable().deselectAll();
        selectedObjective = null;
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (viewer != null)
            viewer.addSelectionChangedListener(listener);
    }
    
    public void addDoubleClickListener(IDoubleClickListener listener) {
        if (viewer != null)
            viewer.addDoubleClickListener(listener);
    }
    
    public void addObjective(CoreHunterObjective objective) {
        if (objective != null) {
            objectives.add(objective) ;
            
            updateViewer(); 
        } 
    }
    
    public void removeSelectedObjective() {
        if (selectedObjective != null) {
            objectives.remove(selectedObjective) ;
            
            updateViewer(); 
        }
    }


}