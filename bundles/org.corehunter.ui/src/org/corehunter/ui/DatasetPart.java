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

import org.corehunter.services.DatasetType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import uno.informatics.data.Dataset;
import uno.informatics.data.FeatureDataset;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class DatasetPart extends DatasetServiceClient {
    private DatasetTable datasetTable = null;
    private Button btnAddDataset;
    private Spinner spinnerSize;
    private Spinner spinnerIntensity;
    private Button btnStartButton;
    private Button btnResetButton;
    private Dataset selectedDataset;
    private int selectedDatasetSize;
    private Button btnRemoveDataset;
    private Label lblDatasetSize;

    @Inject
    public DatasetPart() {
    }

    @PostConstruct
    public void postConstruct(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        Group grpDatasets = new Group(parent, SWT.NONE);
        grpDatasets.setText("Datasets");
        grpDatasets.setLayout(new GridLayout(1, false));
        grpDatasets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        
        Composite datasetTableComposite = new Composite(grpDatasets, SWT.NONE);
        datasetTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        datasetTable = new DatasetTable();

        datasetTable.createPartControl(datasetTableComposite);

        Composite datasetButtonComposite = new Composite(grpDatasets, SWT.NONE);
        datasetButtonComposite.setLayout(new GridLayout(2, false));

        btnAddDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnAddDataset.setText("Add");

        btnAddDataset.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addDataset();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        btnRemoveDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnRemoveDataset.setText("Remove");

        btnRemoveDataset.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDataset();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        datasetTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                databaseSelectionChanged();
            }
        });

        Group corehunterRunArgumentsGroup = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
        corehunterRunArgumentsGroup.setLayout(new GridLayout(5, false));
        corehunterRunArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        corehunterRunArgumentsGroup.setText("Core Hunter Arguments");

        lblDatasetSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblDatasetSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblCoreSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblCoreSize.setText("Core Size");
        lblCoreSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

        spinnerSize = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
        
        spinnerSize.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerSizeUpdated() ;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
        });

        Label lblIntensity = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblIntensity.setText("Intensity");

        spinnerIntensity = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
        spinnerIntensity.setMinimum(0);
        spinnerIntensity.setSelection(20); // TODO get from properties file
        spinnerIntensity.setMaximum(100);
        
        spinnerIntensity.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerIntensityUpdated() ;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
        });
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnStartButton = new Button(composite, SWT.NONE);
        btnStartButton.setText("Start");

        btnStartButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startCorehunterRun();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        btnResetButton = new Button(composite, SWT.NONE);
        btnResetButton.setText("Reset");

        btnResetButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                resetArguments();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        updateDatasetButtons();
        updateDatasetSize() ;
        updateCorehunterArguments() ;
        updateStartButton();
    }

    protected void spinnerSizeUpdated() {
        spinnerIntensity.setSelection(getIntensityFromSize(selectedDatasetSize, spinnerSize.getSelection())); 
        
    }
    
    protected void spinnerIntensityUpdated() {
        spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection())); 
        
    }

    private void removeDataset() {
        // TODO Auto-generated method stub

    }

    private void addDataset() {
        // TODO Auto-generated method stub

    }

    private void databaseSelectionChanged() {
        selectedDataset = datasetTable.getSelectedDataset();

        if (selectedDataset instanceof FeatureDataset)
            selectedDatasetSize = ((FeatureDataset) selectedDataset).getRowCount();
        else
            selectedDatasetSize = 0 ;
        
        updateDatasetSize() ;
        updateDatasetButtons() ;
        updateCorehunterArguments() ;
    }

    private void updateDatasetButtons() {
        btnRemoveDataset.setEnabled(datasetTable.getSelectedDataset() != null);
    }

    private void updateStartButton() {
        btnStartButton.setEnabled(datasetTable.getSelectedDataset() != null);
    }

    private void resetArguments() {
        datasetTable.cleaerSelectedDataset();
        selectedDatasetSize = 0 ;
        updateStartButton();
        updateDatasetButtons() ;
        updateCorehunterArguments() ;
    }
    
    private void updateDatasetSize() {
        lblDatasetSize.setText("Dataset Size : " + selectedDatasetSize);
    }

    
    private void updateCorehunterArguments() {
        if (selectedDatasetSize > 1)
        {
            spinnerSize.setMaximum(selectedDatasetSize - 1);
            spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection())); 
            
            System.out.println(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection()));
        }
        else
        {
            spinnerSize.setMinimum(0);
            spinnerSize.setSelection(0);
        }
    }

    private int getSizeFromIntensity(int datasetSize, int intensity) {
        return (int)((double)datasetSize * ((double)intensity / 100.0));
    }
    
    private int getIntensityFromSize(int datasetSize, int coreSize) {
        return (int)(((double)coreSize / (double)datasetSize) * 100.0);
    }

    private void startCorehunterRun() {
        // TODO Auto-generated method stub

    }
}