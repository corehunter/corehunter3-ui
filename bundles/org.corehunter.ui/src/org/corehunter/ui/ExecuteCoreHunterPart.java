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

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.API;
import org.corehunter.CoreHunterObjective;
import org.corehunter.data.CoreHunterData;
import org.corehunter.data.CoreHunterDataType;
import org.corehunter.services.simple.CoreHunterRunArgumentsPojo;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import uno.informatics.data.Dataset;
import uno.informatics.data.dataset.DatasetException;

public class ExecuteCoreHunterPart {

    public static final String ID = "org.corehunter.ui.part.executeCoreHunter" ;
    
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
    private ObjectiveViewer objectiveViewer;
    private Button btnAddObjective;
    private Button btnRemoveObjective;

    private ShellUtilitiies shellUtilitiies;
    private PartUtilitiies partUtilitiies;
    
    private Map<String, List<CoreHunterObjective>> objectivesMap;
    private List<CoreHunterObjective> objectives;

    @Inject
    public ExecuteCoreHunterPart() {

        objectivesMap = new HashMap<String, List<CoreHunterObjective>>();
    }

    @PostConstruct
    public void postConstruct(Composite parent, EPartService partService, EModelService modelService,
            MApplication application) {

        partUtilitiies = new PartUtilitiies(partService, modelService, application);
        shellUtilitiies = new ShellUtilitiies(parent.getShell()) ;
        parent.setLayout(new FillLayout(SWT.VERTICAL));

        Group grpDatasets = new Group(parent, SWT.NONE);
        grpDatasets.setText("Datasets");
        grpDatasets.setLayout(new GridLayout(1, false));

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
        
        datasetViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                viewDataset();
            }
        });

        datasetViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                databaseSelectionChanged();
            }
        });

        Group corehunterRunArgumentsGroup = new Group(parent, SWT.NONE);
        corehunterRunArgumentsGroup.setText("Core Hunter Arguments");
        corehunterRunArgumentsGroup.setLayout(new GridLayout(5, false));

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

        btnRemoveObjective.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedObjective();
            }
        });
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);

        Composite objectiveViewerComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
        objectiveViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

        objectiveViewer = new ObjectiveViewer();

        objectiveViewer.createPartControl(objectiveViewerComposite);
        
        objectiveViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                objectivesViewerSelectionChanged();
            }

        });

        Composite composite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

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
        updateObjectiveButtons();
        updateDatasetSize();
        updateCorehunterArguments();
        updateStartButton();
    }

    private void addNewObjective() {

        CoreHunterObjective newObjective = createNewObjective(selectedDataset);

        if (newObjective != null) {
            objectiveViewer.addObjective(newObjective);

            objectives = objectiveViewer.getObjectives();

            updateObjectiveButtons();
        }
    }

    private void removeSelectedObjective() {
        objectiveViewer.removeSelectedObjective();

        objectives = objectiveViewer.getObjectives();

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

    private void addDataset() {

        CreateDatasetDialog dialog = new CreateDatasetDialog(shellUtilitiies.getShell());
        dialog.create();
        if (dialog.open() == Window.OK) {
            try {
                Activator.getDefault().getDatasetServices().addDataset(dialog.getDataset());
                
                if (dialog.getPhenotypicDataPath() != null) {
                	Activator.getDefault().getDatasetServices().loadData(dialog.getDataset(), 
                        Paths.get(dialog.getPhenotypicDataPath()), dialog.getPhenotypicDataType(), CoreHunterDataType.PHENOTYPIC) ;
                }
                if (dialog.getGenotypicDataPath() != null) {
                	Activator.getDefault().getDatasetServices().loadData(dialog.getDataset(), 
                        Paths.get(dialog.getGenotypicDataPath()), dialog.getGenotypicDataType(), CoreHunterDataType.GENOTYPIC, dialog.getGenotypeDataFormat()) ;
                }
                if (dialog.getDistancesDataPath() != null) {
                	Activator.getDefault().getDatasetServices().loadData(dialog.getDataset(), 
                        Paths.get(dialog.getDistancesDataPath()), dialog.getDistancesDataType(), CoreHunterDataType.DISTANCES) ;
                }
                                         
                updateViewer();
            } catch (Exception e) {
                shellUtilitiies.handleError("Dataset not be added!",
                        "Dataset could not be added, see error message for more details!", e);
                
                if (dialog.getDataset() != null && dialog.getDataset().getUniqueIdentifier() != null) {
                    Dataset dataset = Activator.getDefault().getDatasetServices().getDataset(dialog.getDataset().getUniqueIdentifier()) ;
                    
                    if (dataset != null)
                        try {
                            Activator.getDefault().getDatasetServices().removeDataset(dataset.getUniqueIdentifier()) ;
                        } catch (DatasetException e1) {
                            shellUtilitiies.handleError("Dataset not be removed!",
                                    "Dataset was added, but load failed, and now dataset can not be removed, see error message for more details!", e);
                        }
                }
            }
        }
    }

    private void removeDataset() {
        try {
            Activator.getDefault().getDatasetServices().removeDataset(selectedDataset.getUniqueIdentifier());
            updateViewer();
        } catch (DatasetException e) {
            shellUtilitiies.handleError("Dataset not be removed!",
                    "Dataset could not be remove, see error message for more details!", e);
        }
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(selectedDataset, DatasetPart.ID));
    }

    private void databaseSelectionChanged() {
        selectedDataset = datasetViewer.getSelectedDataset();

        updateObjectiveViewer();
        updateDatasetButtons();
        updateDatasetSize();
        updateCorehunterArguments();
        updateObjectiveButtons();
        updateStartButton();
    }

    private List<CoreHunterObjective> getObjectives(Dataset dataset) {
        if (dataset != null) {
            List<CoreHunterObjective> objectives = objectivesMap.get(dataset.getUniqueIdentifier());

            if (objectives == null) {
                objectives = createDefaultObjectives(dataset);

                objectivesMap.put(dataset.getUniqueIdentifier(), objectives);
            }

            return objectives;

        } else {
            return new LinkedList<CoreHunterObjective>();
        }
    }

    private List<CoreHunterObjective> createDefaultObjectives(Dataset dataset) {
        
        try {

            CoreHunterData coreHunterData = Activator.getDefault().getDatasetServices()
                    .getCoreHunterData(dataset.getUniqueIdentifier());
            
            return API.createDefaultObjectives(coreHunterData) ;

        } catch (DatasetException e) {
            shellUtilitiies.handleError("Can not create objective!",
                    "Objective could not be created, see error message for more details!", e);
            
            return new LinkedList<CoreHunterObjective>();
        }
    }
    
    private CoreHunterObjective createNewObjective(Dataset dataset) {
        
        try {

            CoreHunterData coreHunterData = Activator.getDefault().getDatasetServices()
                    .getCoreHunterData(dataset.getUniqueIdentifier());
            
            return API.createDefaultObjective(coreHunterData) ;

        } catch (DatasetException e) {
            shellUtilitiies.handleError("Can not create objective!",
                    "Objective could not be created, see error message for more details!", e);
            
            return null ;
        }
    }

    private void updateObjectiveViewer() {
        if (selectedDataset != null) {
            selectedDatasetSize = selectedDataset.getSize();
            
            try {
                CoreHunterData coreHunterData = Activator.getDefault().getDatasetServices()
                        .getCoreHunterData(selectedDataset.getUniqueIdentifier());
                objectiveViewer.setCoreHunterData(coreHunterData);

            } catch (DatasetException e) {
                shellUtilitiies.handleError("Can not update objective viewer!",
                        "Can not update objective viewer, see error message for more details!", e);
            }

        } else {
            selectedDatasetSize = 0;
            objectiveViewer.setCoreHunterData(null) ;
        }

        objectiveViewer.setObjectives(getObjectives(selectedDataset));


    }

    private void updateDatasetButtons() {
        btnRemoveDataset.setEnabled(datasetViewer.getSelectedDataset() != null);
        btnView.setEnabled(datasetViewer.getSelectedDataset() != null);
    }

    private void updateObjectiveButtons() {
        btnAddObjective.setEnabled(
                datasetViewer.getSelectedDataset() != null && datasetViewer.getSelectedDataset().getSize() > 1);
        btnRemoveObjective.setEnabled(
                objectiveViewer.getSelectedObjective() != null && objectiveViewer.getObjectives().size() > 1);
    }

    private void updateStartButton() {
        btnStart.setEnabled(datasetViewer.getSelectedDataset() != null && objectiveViewer.getObjectives().size() > 0);
    }

    private void resetArguments() {
        datasetViewer.cleaerSelectedDataset();
        selectedDataset = null;

        updateObjectiveViewer();
        updateDatasetButtons();
        updateDatasetSize();
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
        Activator.getDefault().getCoreHunterRunServices().executeCoreHunter(new CoreHunterRunArgumentsPojo(
                createRunName(), selectedDatasetSize, selectedDataset.getUniqueIdentifier(), objectives));

        MPart part = partUtilitiies.getPartService().findPart(ResultsPart.ID);

        partUtilitiies.getPartService().activate(part);
    }

    private String createRunName() {
        return String.format("Run for %s", selectedDataset.getName());
    }
}