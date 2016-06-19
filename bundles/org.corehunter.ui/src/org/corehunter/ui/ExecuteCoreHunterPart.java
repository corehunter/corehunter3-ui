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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.CoreHunterMeasure;
import org.corehunter.CoreHunterObjective;
import org.corehunter.CoreHunterObjectiveType;
import org.corehunter.data.CoreHunterData;
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
import org.eclipse.swt.widgets.Spinner;

import uno.informatics.data.Dataset;
import uno.informatics.data.dataset.DatasetException;

public class ExecuteCoreHunterPart extends DatasetServiceClient {
    private static final CoreHunterObjectiveType DEFAULT_OBJECTIVE = CoreHunterObjectiveType.AV_ACCESSION_TO_NEAREST_ENTRY ;
    private static final CoreHunterMeasure DEFAULT_GENOTYPE_MEASURE = CoreHunterMeasure.MODIFIED_ROGERS;
    
    private DatasetViewer datasetViewer = null;
    private Button btnAddDataset;
    private Spinner spinnerSize;
    private Spinner spinnerIntensity;
    private Button btnStart;
    private Button btnReset;
    private Dataset selectedDataset;
    private int selectedDatasetSize;
    private Button btnRemoveDataset;
    private Label lblDatasetSize;
    private Button btnView;
    private PartUtilitiies partUtilitiies;
    private ObjectiveViewer objectiveViewer;
    private Button btnAddObjective;
    private Button btnRemoveObjective;
    
    private Map<String, List<CoreHunterObjective>> objectivesMap;
    private List<CoreHunterObjective> objectives;

    @Inject
    public ExecuteCoreHunterPart() {
        
        objectivesMap = new HashMap<String, List<CoreHunterObjective>>() ;
    }

    @PostConstruct
    public void postConstruct(Composite parent, EPartService partService, EModelService modelService,
            MApplication application) {

        partUtilitiies = new PartUtilitiies(partService, modelService, application);

        parent.setLayout(new GridLayout(1, false));

        Group grpDatasets = new Group(parent, SWT.NONE);
        grpDatasets.setText("Datasets");
        grpDatasets.setLayout(new GridLayout(1, false));
        grpDatasets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        Composite datasetViewerComposite = new Composite(grpDatasets, SWT.NONE);
        datasetViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        datasetViewer = new DatasetViewer();

        datasetViewer.createPartControl(datasetViewerComposite);

        Composite datasetButtonComposite = new Composite(grpDatasets, SWT.NONE);
        datasetButtonComposite.setLayout(new GridLayout(3, false));

        btnAddDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnAddDataset.setText("Add");

        btnAddDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addDataset();
            }
        });

        btnRemoveDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnRemoveDataset.setText("Remove");

        btnRemoveDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDataset();
            }
        });

        btnView = new Button(datasetButtonComposite, SWT.NONE);
        btnView.setText("View");

        btnView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewDataset();
            }
        });

        datasetViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                databaseSelectionChanged();
            }
        });
        
        datasetViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                viewDataset();
            }
        });

        Group corehunterRunArgumentsGroup = new Group(parent, SWT.NONE);
        corehunterRunArgumentsGroup.setText("Core Hunter Arguments");
        corehunterRunArgumentsGroup.setLayout(new GridLayout(5, false));
        corehunterRunArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        lblDatasetSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblDatasetSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblCoreSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblCoreSize.setText("Core Size");
        lblCoreSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

        spinnerSize = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);

        spinnerSize.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerSizeUpdated();
            }
        });

        Label lblIntensity = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblIntensity.setText("Intensity");

        spinnerIntensity = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
        spinnerIntensity.setMinimum(0);
        spinnerIntensity.setSelection(20); // TODO get from properties file
        spinnerIntensity.setMaximum(100);

        spinnerIntensity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerIntensityUpdated();
            }
        });
        
        btnAddObjective = new Button(corehunterRunArgumentsGroup, SWT.NONE);
        btnAddObjective.setText("Add Objective");
        
        btnAddObjective.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addNewObjective();
            }
        });
        
        btnRemoveObjective = new Button(corehunterRunArgumentsGroup, SWT.NONE);
        btnRemoveObjective.setText("Remove Objective");
        
        btnView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedObjective();
            }
        });
        
        Composite objectiveViewerComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
        objectiveViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

        objectiveViewer = new ObjectiveViewer();
        
        objectiveViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                objectivesViewerSelectionChanged();
            }
            
        });

        objectiveViewer.createPartControl(objectiveViewerComposite);
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnStart = new Button(composite, SWT.NONE);
        btnStart.setText("Start");

        btnStart.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startCorehunterRun();
            }
        });

        btnReset = new Button(composite, SWT.NONE);
        btnReset.setText("Reset");

        btnReset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                resetArguments();
            }
        });

        updateDatasetButtons();
        updateObjectiveButtons() ;
        updateDatasetSize();
        updateCorehunterArguments();
        updateStartButton();
    }

    private void addNewObjective() {
        
        CoreHunterObjective newObjective = createNewObjective(selectedDataset) ;
        
        if (newObjective != null) {
            objectiveViewer.addObjective(newObjective) ;
            
            objectives = objectiveViewer.getObjectives() ;   
    
            updateObjectiveButtons();
        }
    }

    private void removeSelectedObjective() {
        objectiveViewer.removeSelectedObjective() ;
        
        objectives = objectiveViewer.getObjectives() ;
        
        updateObjectiveButtons();
    }

    private void objectivesViewerSelectionChanged() {
        updateObjectiveButtons();
    }

    private void updateViewer() {
        datasetViewer.updateViewer();
    }

    protected void spinnerSizeUpdated() {
        spinnerIntensity.setSelection(getIntensityFromSize(selectedDatasetSize, spinnerSize.getSelection()));

    }

    protected void spinnerIntensityUpdated() {
        spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection()));

    }

    private void removeDataset() {
        try {
            getDatasetServices().removeDataset(selectedDataset.getUniqueIdentifier());
            updateViewer();
        } catch (DatasetException e) {
            // TODO Auto-generated catch block
            handleException(e);
        }
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(selectedDataset, DatasetPart.ID));
    }

    private void addDataset() {

    }

    private void databaseSelectionChanged() {
        selectedDataset = datasetViewer.getSelectedDataset();

        if (selectedDataset != null) {
            selectedDatasetSize = selectedDataset.getSize() ;
        } else {
            selectedDatasetSize = 0;
        }
        
        objectiveViewer.setObjectives(getObjectives(selectedDataset));

        updateDatasetButtons();
        updateDatasetSize() ;
        updateCorehunterArguments();
        updateObjectiveButtons();
        updateStartButton();
    }

    private List<CoreHunterObjective> getObjectives(Dataset dataset) {
        if (dataset != null) {
            List<CoreHunterObjective> objectives = objectivesMap.get(dataset.getUniqueIdentifier()) ;
            
            if (objectives == null) {
                objectives = createDefaultObjectives(dataset) ;
                
                objectivesMap.put(dataset.getUniqueIdentifier(), objectives) ;
            }
            
            return objectives ;
            
        } else {
            return new LinkedList<CoreHunterObjective>() ;
        }
    }

    private List<CoreHunterObjective> createDefaultObjectives(Dataset dataset) {
        List<CoreHunterObjective> objectives = new LinkedList<CoreHunterObjective>() ;
        
        try {
            CoreHunterData coreHunterData = getDatasetServices().getCoreHunterData(dataset.getUniqueIdentifier()) ;
            
            if (coreHunterData != null) {
                double count = 0.0 ;
                
                if (coreHunterData.hasPhenotypes()) {
                    ++count ;
                }
                
                if (coreHunterData.hasGenotypes()) {
                    ++count ;
                }
                
                if (coreHunterData.hasDistances()) {
                    ++count ;
                }
                
                if (coreHunterData.hasPhenotypes()) {
                    objectives.add(new CoreHunterObjective(DEFAULT_OBJECTIVE, CoreHunterMeasure.GOWERS, 1.0 / count)) ;
                }
                
                if (coreHunterData.hasGenotypes()) {
                    objectives.add(new CoreHunterObjective(DEFAULT_OBJECTIVE, DEFAULT_GENOTYPE_MEASURE, 1.0 / count)) ;
                }
                
                if (coreHunterData.hasDistances()) {
                    objectives.add(new CoreHunterObjective(DEFAULT_OBJECTIVE, CoreHunterMeasure.PRECOMPUTED_DISTANCE, 1.0 / count)) ;
                }
            }
            
        } catch (DatasetException e) {

            e.printStackTrace();      
        }
        
        return objectives;
    }
    

    private CoreHunterObjective createNewObjective(Dataset dataset) {
        
        try {
            CoreHunterData coreHunterData = getDatasetServices().getCoreHunterData(dataset.getUniqueIdentifier()) ;
            
            if (coreHunterData != null) {
            
                if (coreHunterData.hasPhenotypes()) {
                    return new CoreHunterObjective(DEFAULT_OBJECTIVE, CoreHunterMeasure.GOWERS, 0.0) ;
                }
                
                if (coreHunterData.hasGenotypes()) {
                    return new CoreHunterObjective(DEFAULT_OBJECTIVE, DEFAULT_GENOTYPE_MEASURE, 0.0) ;
                }
                
                if (coreHunterData.hasDistances()) {
                    return new CoreHunterObjective(DEFAULT_OBJECTIVE, CoreHunterMeasure.PRECOMPUTED_DISTANCE, 0.0) ;
                }
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }
        
        return null ;
    }

    private void updateDatasetButtons() {
        btnRemoveDataset.setEnabled(datasetViewer.getSelectedDataset() != null);
        btnView.setEnabled(datasetViewer.getSelectedDataset() != null);
    }
    

    private void updateObjectiveButtons() {
        btnAddObjective.setEnabled(datasetViewer.getSelectedDataset() != null && datasetViewer.getSelectedDataset().getSize() > 1);
        btnRemoveObjective.setEnabled(objectiveViewer.getSelectedObjective() != null && objectiveViewer.getObjectives().size() > 1);
    }

    private void updateStartButton() {
        btnStart.setEnabled(datasetViewer.getSelectedDataset() != null && objectiveViewer.getObjectives().size() > 0);
    }

    private void resetArguments() {
        datasetViewer.cleaerSelectedDataset();
        selectedDatasetSize = 0;
        
        updateDatasetButtons();
        updateDatasetSize() ;
        updateCorehunterArguments();
        updateObjectiveButtons();
        updateStartButton();
    }

    private void updateDatasetSize() {
        lblDatasetSize.setText(String.format("Dataset Size : %d ", selectedDatasetSize));
    }

    private void updateCorehunterArguments() {
        if (selectedDatasetSize > 1) {
            spinnerSize.setMaximum(selectedDatasetSize - 1);
            spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection()));
            spinnerSize.setEnabled(true);
            spinnerIntensity.setEnabled(true);
        } else {
            spinnerSize.setMinimum(0);
            spinnerSize.setSelection(0);
            spinnerSize.setEnabled(false);
            spinnerIntensity.setEnabled(false);
        }
    }

    private int getSizeFromIntensity(int datasetSize, int intensity) {
        return (int) ((double) datasetSize * ((double) intensity / 100.0));
    }

    private int getIntensityFromSize(int datasetSize, int coreSize) {
        return (int) (((double) coreSize / (double) datasetSize) * 100.0);
    }

    private void startCorehunterRun() {
        // TODO Auto-generated method stub

    }

    private void handleException(DatasetException e) {
        // TODO Auto-generated method stub

    }
}