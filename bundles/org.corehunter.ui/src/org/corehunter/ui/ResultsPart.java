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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.services.CoreHunterRun;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class ResultsPart {
    
    public static final String ID = "org.corehunter.ui.part.results" ;
    
    private CoreHunterRunTable resultTable;
    private Button btnRemove;
    private Button btnView;
    private Button btnClear;
    private CoreHunterRun selectedCorehunterRun;
    private PartUtilitiies partUtilitiies;
    private ShellUtilitiies shellUtilitiies;
    private Button btnRefresh;

    @Inject
    public ResultsPart() {
        
 
    }

    @PostConstruct
    public void postConstruct(Composite parent, EPartService partService, EModelService modelService,
            MApplication application) {
        
        shellUtilitiies = new ShellUtilitiies(parent.getShell()) ;
        partUtilitiies = new PartUtilitiies(partService, modelService, application);
        
        parent.setLayout(new GridLayout(1, false));

        Group grpResults = new Group(parent, SWT.NONE);
        grpResults.setText("Results");
        grpResults.setLayout(new GridLayout(1, false));
        grpResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        resultTable = new CoreHunterRunTable();

        resultTable.createPartControl(grpResults);

        resultTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                resultsSelectionChanged();
            }
        });
        
        resultTable.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                viewResult();
            }
        });

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        composite.setLayout(new GridLayout(4, false));

        btnRemove = new Button(composite, SWT.NONE);
        btnRemove.setText("Remove");
        btnRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeResult();
            }
        });

        btnView = new Button(composite, SWT.NONE);
        btnView.setText("View");
        btnView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewResult();
            }
        });

        btnClear = new Button(composite, SWT.NONE);
        btnClear.setText("Clear");
        
        btnClear.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clearSelection();
            }
        });
        
        btnRefresh = new Button(composite, SWT.NONE);
        btnRefresh.setText("Refresh");
        
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateViewer();
            }
        });

        updateButtons();
    }

    protected void resultsSelectionChanged() {
        selectedCorehunterRun = resultTable.getSelectedCorehunterRun();

        updateButtons();
    }

    private void updateButtons() {
        btnRemove.setEnabled(selectedCorehunterRun != null);
        btnView.setEnabled(selectedCorehunterRun != null);
        btnClear.setEnabled(selectedCorehunterRun != null);
        btnRefresh.setEnabled(!resultTable.isEmpty());
    }

    private void updateViewer() {
        resultTable.updateViewer();
    }

    protected void clearSelection() {
        resultTable.cleaerSelectedCorehunterRun();
        selectedCorehunterRun = resultTable.getSelectedCorehunterRun();
        updateButtons();
    }

    protected void removeResult() {
        try {
            Activator.getDefault().getCoreHunterRunServices().removeCoreHunterRun(resultTable.getSelectedCorehunterRun().getUniqueIdentifier());
            updateViewer();
        } catch (Exception e) {
            shellUtilitiies.handleError("Can not remove result!",
                    "Result could not be removed, see error message for more details!", e);
        }
    }

    protected void viewResult() {
        partUtilitiies.openPart(new PartInput(selectedCorehunterRun, ResultPart.ID));
    }
}