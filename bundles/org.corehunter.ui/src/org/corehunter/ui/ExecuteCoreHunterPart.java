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
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.API;
import org.corehunter.CoreHunterObjective;
import org.corehunter.data.CoreHunterData;
import org.corehunter.data.CoreHunterDataType;
import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.simple.CoreHunterRunArgumentsPojo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uno.informatics.data.Dataset;
import uno.informatics.data.dataset.DatasetException;

public class ExecuteCoreHunterPart {

    public static final String ID = "org.corehunter.ui.part.executeCoreHunter" ;
    
    private static final Logger logger = LoggerFactory.getLogger(PartUtilitiies.class);

    private DatasetViewer datasetViewer = null;
    private Button btnAddDataset;
    private Spinner spinnerSize;
    private Spinner spinnerIntensity;
    private Button btnStart;
    private Button btnReset;
    private Dataset selectedDataset;
    private int selectedDatasetSize;
    private Button btnRemoveDataset;
    private Button btnClearSelectedDataset;
    private Label lblDatasetSize;
    private Button btnViewDataset;
    private ObjectiveViewer objectiveViewer;
    private Button btnAddObjective;
    private Button btnRemoveObjective;

    private ShellUtilitiies shellUtilitiies;
    private PartUtilitiies partUtilitiies;
    
    private Map<String, List<CoreHunterObjective>> objectivesMap;
	private Map<String, CoreHunterData>  coreHunterDataMap;


    @Inject
    public ExecuteCoreHunterPart() {
        objectivesMap = new HashMap<String, List<CoreHunterObjective>>();
        coreHunterDataMap = new HashMap<String, CoreHunterData>();
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
        datasetButtonComposite.setLayout(new GridLayout(5, false));

        btnAddDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnAddDataset.setText("Add Dataset");

        btnAddDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addDataset();
            }
        });

        btnRemoveDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnRemoveDataset.setText("Remove Selected Dataset");

        btnRemoveDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDataset();
            }
        });

        btnViewDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnViewDataset.setText("View Selected Dataset");
        
        btnViewDataset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewDataset();
            }
        });
        
        btnClearSelectedDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnClearSelectedDataset.setText("Clear Selected Dataset");
        
        btnClearSelectedDataset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clearSelectedDataset();
            }
        });
        
        new Label(datasetButtonComposite, SWT.NONE);


        datasetViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                viewDataset();
            }
        });

        datasetViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                datasetSelectionChanged();
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
        
        resetSpinnerIntensity() ;

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
        btnStart.setText("Start New Run");

        btnStart.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startCorehunterRun();
            }
        });

        btnReset = new Button(composite, SWT.NONE);
        btnReset.setText("Reset Arguments");
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);

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

    private void resetDatasetObjectives() {
    	if (selectedDataset != null) {
    		resetObjectives(selectedDataset) ;
    	}
	}

    private void resetSpinnerIntensity() {
        spinnerIntensity.setMinimum(0);
        spinnerIntensity.setSelection(20); // TODO get from properties file
        spinnerIntensity.setMaximum(100);
        
        spinnerIntensityUpdated() ;
	}

	private void addNewObjective() {

        CoreHunterObjective newObjective = createNewObjective(selectedDataset);

        if (newObjective != null) {
            objectiveViewer.addObjective(newObjective);

            updateObjectiveButtons();
        }
    }

    private void removeSelectedObjective() {
        objectiveViewer.removeSelectedObjective();

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
                
                getCoreHunterData(dialog.getDataset().getUniqueIdentifier()) ; // this caches data to speed up the interface
                                         
                updateViewer();
            } catch (Exception e) {
                shellUtilitiies.handleError("Dataset could not be added!", "Dataset could not be added!", e);
                
                if (dialog.getDataset() != null && dialog.getDataset().getUniqueIdentifier() != null) {
                    Dataset dataset = Activator.getDefault().getDatasetServices().getDataset(dialog.getDataset().getUniqueIdentifier()) ;
                    
                    if (dataset != null)
                        try {
                            Activator.getDefault().getDatasetServices().removeDataset(dataset.getUniqueIdentifier()) ;
                        } catch (DatasetException e1) {
                            shellUtilitiies.handleError(
                            		"Dataset could not be removed!", "Dataset was added, but load failed, and now dataset can not be removed", e);
                        }
                }
            }
        }
    }

    private void removeDataset() {
        try {
            Activator.getDefault().getDatasetServices().removeDataset(selectedDataset.getUniqueIdentifier());
            coreHunterDataMap.remove(selectedDataset.getUniqueIdentifier()) ;
            updateViewer();
        } catch (DatasetException e) {
            shellUtilitiies.handleError("Dataset not be removed!",
                    "Dataset could not be remove, see error message for more details!", e);
        }
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(selectedDataset, DatasetPart.ID));
    }
    
    private void clearSelectedDataset() {
        datasetViewer.clearSelectedDataset();
        selectedDataset = null;

        updateObjectiveViewer();
        updateDatasetButtons();
        updateDatasetSize();
        updateCorehunterArguments();
        updateObjectiveButtons();
        updateStartButton();
    }

    private void datasetSelectionChanged() {
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
    
    private void resetObjectives(Dataset dataset) {
        if (dataset != null) {
             objectivesMap.remove(dataset.getUniqueIdentifier());
        }
    }

    private List<CoreHunterObjective> createDefaultObjectives(Dataset dataset) {
        return API.createDefaultObjectives(getCoreHunterData(dataset.getUniqueIdentifier())) ;
    }
    
    private CoreHunterObjective createNewObjective(Dataset dataset) {
        
            if (dataset != null) {
                List<CoreHunterObjective> objectives = objectivesMap.get(dataset.getUniqueIdentifier());
                
        		CoreHunterData coreHunterData = getCoreHunterData(dataset.getUniqueIdentifier()) ;	
                
            	if (objectives != null && !objectives.isEmpty()) {
            		return API.createDefaultObjective(coreHunterData, objectives) ;
                    
            	} else {
            		return API.createDefaultObjective(coreHunterData) ;
            	}     

            } else {
                shellUtilitiies.handleError("Can not create objective!",
                        "Objective could not be created, dataset is undefined!");
            }

        
        return null ;
    }

    private void updateObjectiveViewer() {
        if (selectedDataset != null) {
            selectedDatasetSize = selectedDataset.getSize();
            
             objectiveViewer.setCoreHunterData(getCoreHunterData(selectedDataset.getUniqueIdentifier()));


        } else {
            selectedDatasetSize = 0;
            objectiveViewer.setCoreHunterData(null) ;
        }

        objectiveViewer.setObjectives(getObjectives(selectedDataset));
    }

    private CoreHunterData getCoreHunterData(String uniqueIdentifier) {
    	
    	CoreHunterData coreHunterData = null ;
    	
    	if (selectedDataset != null) {
    		coreHunterData = coreHunterDataMap.get(selectedDataset.getUniqueIdentifier()) ;
    		
    		GetCoreHunterDataRunnable runnable = new GetCoreHunterDataRunnable() ;
    		
        	if (coreHunterData == null) {
        		BusyIndicator.showWhile(Display.getDefault(), runnable) ;
        		
        		coreHunterData = runnable.getCoreHunterData() ;
        		
            	if (coreHunterData != null) {
            		coreHunterDataMap.put(selectedDataset.getUniqueIdentifier(), coreHunterData) ;
            	}  
        	}
    	}

		return coreHunterData;
	}

	private void updateDatasetButtons() {
        btnRemoveDataset.setEnabled(datasetViewer.getSelectedDataset() != null);
        btnViewDataset.setEnabled(datasetViewer.getSelectedDataset() != null);
        btnClearSelectedDataset.setEnabled(datasetViewer.getSelectedDataset() != null);
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
    	resetDatasetObjectives() ;
    	resetSpinnerIntensity() ;
    	
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
    	
    	MPart part = partUtilitiies.getPartService().showPart(ResultsPart.ID, PartState.CREATE); // create the part if not already created
    	
    	String name = createRunName() ;
    	
    	CoreHunterRunArgumentsPojo arguments = new CoreHunterRunArgumentsPojo(
    			name, spinnerSize.getSelection(), selectedDataset.getUniqueIdentifier(), objectiveViewer.getObjectives()) ;
    	
    	arguments.setTimeLimit(5);
    	
		CoreHunterRunJob job = new CoreHunterRunJob(name, arguments);
    	
    	job.schedule();    
    	
    	partUtilitiies.getPartService().showPart(part, PartState.ACTIVATE) ;
    }

    private String createRunName() {
        return String.format("Run for %s", selectedDataset.getName());
    }
    
    private class CoreHunterRunJob extends Job implements CoreHunterJob {
    	
    	private CoreHunterRunArgumentsPojo arguments ;
		private CoreHunterRun coreHunterRun;
		private String monitorId ;

		public CoreHunterRunJob(String name, CoreHunterRunArgumentsPojo arguments) {
			super(name);
			this.arguments = arguments;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			
			int time = (int)arguments.getTimeLimit() ;
			
			//SubMonitor subMonitor = SubMonitor.convert(monitor, time);
			
			monitor.beginTask(arguments.getName(), time);
			
			coreHunterRun = Activator.getDefault().getCoreHunterRunServices().executeCoreHunter(arguments);
			
			monitorId = coreHunterRun.getUniqueIdentifier() ;
			
			if (monitor instanceof CoreHunterJobMonitor) {
				((CoreHunterJobMonitor) monitor).setMonitorId(monitorId);
			}	
				
			boolean finished = false ;
			
			while (!finished) {
				
				coreHunterRun = Activator.getDefault().getCoreHunterRunServices().executeCoreHunter(arguments);
				
				switch(coreHunterRun.getStatus()) {
					case FAILED:
					case FINISHED:
						finished = true ;
						break;
					case NOT_STARTED:
					case RUNNING:
					default:
						finished = false ;
						break;
					}
					
					try {
						TimeUnit.SECONDS.sleep(1);
						monitor.worked(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
						
						logger.error(e.getMessage(), e);
						finished = true ;
					}	
				//subMonitor.split(1) ;
			}
	        
			monitor.done(); 
			
	        partUtilitiies.openPart(new PartInput(coreHunterRun, ResultPart.ID));    
	        
			return Status.OK_STATUS ;
		}

		public final CoreHunterRunArgumentsPojo getArguments() {
			return arguments;
		}

		public final CoreHunterRun getCoreHunterRun() {
			return coreHunterRun;
		}
    	
    }
    
    private class GetCoreHunterDataRunnable implements Runnable {
    	
    	CoreHunterData coreHunterData ;
    	
	    public void run(){
	    	try {
				coreHunterData = Activator.getDefault().getDatasetServices().getCoreHunterData(
						selectedDataset.getUniqueIdentifier());
	        } catch (DatasetException e) {
	            shellUtilitiies.handleError("Can not get core hunter data!",
	                    "Can not get core hunter data, see error message for more details!", e);
			}
	    }

		public final CoreHunterData getCoreHunterData() {
			return coreHunterData;
		}
    }
}