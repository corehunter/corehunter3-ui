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

import static org.corehunter.ui.Constants.CORE_HUNTER_OBJECTIVE_TYPES;

import java.util.List;

import org.corehunter.API;
import org.corehunter.CoreHunterMeasure;
import org.corehunter.CoreHunterObjective;
import org.corehunter.CoreHunterObjectiveType;
import org.corehunter.data.CoreHunterData;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ObjectiveViewer {
    private TableViewer viewer;

    private List<CoreHunterObjective> objectives;

    private CoreHunterObjective selectedObjective;
    
    private CoreHunterData coreHunterData ;

    public ObjectiveViewer() {

    }
    
    public final CoreHunterData getCoreHunterData() {
        return coreHunterData;
    }

    public final void setCoreHunterData(CoreHunterData coreHunterData) {
        this.coreHunterData = coreHunterData;
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
        
        viewer.getTable().addListener(SWT.MeasureItem, new Listener() {
            public void handleEvent(Event event) {
               event.height = 20;
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
        
        int[] bounds = { 250, 200, 50};
        
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return objective.getObjectiveType() != null ? objective.getObjectiveType().getName()  : "";
            }
        });
        col.setEditingSupport(new ObjectiveTypeEditingSupport(viewer)); 

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return objective.getMeasure() != null ? objective.getMeasure().getName() : "";
            }
        });
        col.setEditingSupport(new MeasureEditingSupport(viewer)); 
        
        col = createTableViewerColumn(titles[2], bounds[2], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                CoreHunterObjective objective = (CoreHunterObjective) element;
                return String.format("%e", objective.getWeight());
            }
        });
        col.setEditingSupport(new WeightEditingSupport(viewer)); 

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

    public class ObjectiveTypeEditingSupport extends EditingSupport {

        private final TableViewer viewer;
        private final ComboBoxViewerCellEditor editor;

        public ObjectiveTypeEditingSupport(TableViewer viewer) {
          super(viewer);
          this.viewer = viewer;
          this.editor = new ComboBoxViewerCellEditor(viewer.getTable());
          this.editor.setContentProvider(new ArrayContentProvider());
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            if (coreHunterData != null)
                this.editor.setInput(API.getAllowedObjectives(coreHunterData));
            return editor;
        }

        @Override
        protected boolean canEdit(Object element) {
          return true;
        }

        @Override
        protected Object getValue(Object element) {
          return ((CoreHunterObjective) element).getObjectiveType();
        }

        @Override
        protected void setValue(Object element, Object userInputValue) {
            CoreHunterObjective objective = ((CoreHunterObjective) element) ;
            objective.setObjectiveType((CoreHunterObjectiveType)userInputValue);
           
           List<CoreHunterMeasure> measures = API.getAllowedMeasures(coreHunterData, objective.getObjectiveType()) ;
           
           if (measures.isEmpty()) {
               objective.setMeasure(null);
           } else {   
               if (objective.getMeasure() == null || !measures.contains(objective.getMeasure())) {
                   objective.setMeasure(measures.get(0));
               }
           }
        
          viewer.update(element, null);
        }
      } 
    
    public class MeasureEditingSupport extends EditingSupport {

        private final TableViewer viewer;
        private final ComboBoxViewerCellEditor editor;

        public MeasureEditingSupport(TableViewer viewer) {
          super(viewer);
          this.viewer = viewer;
          this.editor = new ComboBoxViewerCellEditor(viewer.getTable());
          this.editor.setContentProvider(new ArrayContentProvider()) ;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
          editor.setInput(API.getAllowedMeasures(coreHunterData, ((CoreHunterObjective) element).getObjectiveType()));
          return editor;
        }

        @Override
        protected boolean canEdit(Object element) {
          return true;
        }

        @Override
        protected Object getValue(Object element) {
          return ((CoreHunterObjective) element).getObjectiveType();
        }

        @Override
        protected void setValue(Object element, Object userInputValue) {
           ((CoreHunterObjective) element).setMeasure((CoreHunterMeasure)userInputValue);
          viewer.update(element, null);
        }
      } 
    
    public class WeightEditingSupport extends EditingSupport {

        private final TableViewer viewer;
        private final TextCellEditor editor;

        public WeightEditingSupport(TableViewer viewer) {
          super(viewer);
          this.viewer = viewer;
          this.editor = new TextCellEditor(viewer.getTable());
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
          return editor;
        }

        @Override
        protected boolean canEdit(Object element) {
          return true;
        }

        @Override
        protected Object getValue(Object element) {
          return ((CoreHunterObjective) element).getObjectiveType();
        }

        @Override
        protected void setValue(Object element, Object userInputValue) {
           ((CoreHunterObjective) element).setMeasure((CoreHunterMeasure)userInputValue);
          viewer.update(element, null);
        }
      } 
}