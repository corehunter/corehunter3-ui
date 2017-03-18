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
import org.eclipse.jface.dialogs.MessageDialog;
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

public class ExecuteCoreHunterPart implements Refreshable{

	public static final String ID = "org.corehunter.ui.part.executeCoreHunter";

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
	private Map<String, CoreHunterData> coreHunterDataMap;
	private Composite argumentsComposite;

	private Spinner spinnerTime;

	private Spinner spinnerMaxImprovement;

	private Button btnTime;

	private Button btnMaxImprovement;

	@Inject
	public ExecuteCoreHunterPart() {
		objectivesMap = new HashMap<String, List<CoreHunterObjective>>();
		coreHunterDataMap = new HashMap<String, CoreHunterData>();
	}

	@PostConstruct
	public void postConstruct(Composite parent, EPartService partService, EModelService modelService,
			MApplication application) {

		partUtilitiies = new PartUtilitiies(partService, modelService, application);
		shellUtilitiies = new ShellUtilitiies(parent.getShell());

		parent.setLayout(new FillLayout(SWT.VERTICAL));

		Group grpDatasets = new Group(parent, SWT.NONE);
		grpDatasets.setText("Datasets");
		grpDatasets.setLayout(new GridLayout(1, false));

		Composite datasetViewerComposite = new Composite(grpDatasets, SWT.NONE);
		datasetViewerComposite.setToolTipText("List of available datasets");
		datasetViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		datasetViewer = new DatasetViewer();

		datasetViewer.createPartControl(datasetViewerComposite);

		Composite datasetButtonComposite = new Composite(grpDatasets, SWT.NONE);
		datasetButtonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		datasetButtonComposite.setLayout(new GridLayout(5, false));

		btnAddDataset = new Button(datasetButtonComposite, SWT.NONE);
		btnAddDataset.setText("Add Dataset");
		btnAddDataset.setToolTipText("Click here to add a new dataset to the list above.");

		btnAddDataset.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addDataset();
			}
		});

		btnRemoveDataset = new Button(datasetButtonComposite, SWT.NONE);
		btnRemoveDataset.setText("Remove Selected Dataset");
		btnRemoveDataset.setToolTipText("Click to remove the currently selected dataset.");

		btnRemoveDataset.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeDataset();
			}
		});

		btnViewDataset = new Button(datasetButtonComposite, SWT.NONE);
		btnViewDataset.setText("View Selected Dataset");
		btnViewDataset.setToolTipText("Click to view in more detail the currently selected dataset.");
		
		btnViewDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewDataset();
			}
		});

		btnClearSelectedDataset = new Button(datasetButtonComposite, SWT.NONE);
		btnClearSelectedDataset.setText("Clear Dataset Selection");
		btnClearSelectedDataset.setToolTipText("Click to deselected the currently selected dataset.");
		
		new Label(datasetButtonComposite, SWT.NONE);

		btnClearSelectedDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSelectedDataset();
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
				datasetSelectionChanged();
			}
		});

		Group corehunterRunArgumentsGroup = new Group(parent, SWT.NONE);
		corehunterRunArgumentsGroup.setText("Core Hunter Arguments");
		corehunterRunArgumentsGroup.setLayout(new GridLayout(1, false));

		argumentsComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
		argumentsComposite.setLayout(new GridLayout(9, false));
		argumentsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		lblDatasetSize = new Label(argumentsComposite, SWT.NONE);
		lblDatasetSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblDatasetSize.setToolTipText("The size of currently selected dataset.");
		
		Label lblCoreSize = new Label(argumentsComposite, SWT.NONE);
		lblCoreSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblCoreSize.setText("Core Size");
		lblCoreSize.setToolTipText("The size of required core");

		spinnerSize = new Spinner(argumentsComposite, SWT.BORDER);
		spinnerSize.setToolTipText("Explicitly give the size of the core") ;
		
		Label lblIntensity = new Label(argumentsComposite, SWT.NONE);
		lblIntensity.setText("Intensity (%)");
		lblIntensity.setToolTipText("Set the size of the core as percentage of the size of the dataset") ;

		spinnerIntensity = new Spinner(argumentsComposite, SWT.BORDER);
		spinnerIntensity.setToolTipText("Set the size of the core as percentage of the size of the dataset");
		
		btnTime = new Button(argumentsComposite, SWT.CHECK);
		btnTime.setToolTipText("Tick this box if you want the Core Hunter to be stopped after a set time");
		btnTime.setText("Time (secs)");
		btnTime.setSelection(true);

		spinnerTime = new Spinner(argumentsComposite, SWT.BORDER);
		spinnerTime.setToolTipText("Time in seconds after which Core Hunter will be stopped.");
		spinnerTime.setMinimum(1);
		
		btnMaxImprovement = new Button(argumentsComposite, SWT.CHECK);
		btnMaxImprovement.setToolTipText("Tick this box if you want the Core Hunter to be stopped if there is no improvement in the core after a set time");
		btnMaxImprovement.setText("Max Improvement (secs)");
		btnMaxImprovement.setSelection(false);

		spinnerMaxImprovement = new Spinner(argumentsComposite, SWT.BORDER);
		spinnerMaxImprovement.setToolTipText("Time in seconds after which Core Hunter will be stopped when there has been no improvement in the core.");
		spinnerMaxImprovement.setMinimum(1);
		spinnerMaxImprovement.setEnabled(false);
		
		spinnerIntensity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinnerIntensityUpdated();
			}
		});

		spinnerSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinnerSizeUpdated();
			}
		});
		
		btnTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinnerTime.setEnabled(btnTime.getSelection());
				
				if (!btnTime.getSelection() && !btnMaxImprovement.getSelection()) {
					btnMaxImprovement.setSelection(true);
					spinnerMaxImprovement.setEnabled(true);
				}
			}
		});

		btnMaxImprovement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinnerMaxImprovement.setEnabled(btnMaxImprovement.getSelection());
				
				if (!btnMaxImprovement.getSelection() && !btnTime.getSelection()) {
					btnTime.setSelection(true);
					spinnerTime.setEnabled(true);
				}
			}
		});

		resetSpinnerTime();
		resetMaxImprovement();
		resetSpinnerIntensity();

		Composite objectiveViewerComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
		objectiveViewerComposite.setToolTipText("The objectives to be applied to Core Hunter when started.");
		objectiveViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		objectiveViewer = new ObjectiveViewer();

		objectiveViewer.createPartControl(objectiveViewerComposite);
		
		objectiveViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				objectivesViewerSelectionChanged();
			}

		});

		Composite objectiveButtonComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
		objectiveButtonComposite.setLayout(new GridLayout(2, false));
		objectiveButtonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		btnAddObjective = new Button(objectiveButtonComposite, SWT.NONE);
		btnAddObjective.setToolTipText("Adds another objective. The type of objective is guessed based on the existing objectives.");
		btnAddObjective.setText("Add Objective");

		btnRemoveObjective = new Button(objectiveButtonComposite, SWT.NONE);
		btnRemoveObjective.setToolTipText("Removes the currently selected objective");
		btnRemoveObjective.setText("Remove Objective");

		btnRemoveObjective.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedObjective();
			}
		});

		btnAddObjective.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewObjective();
			}
		});

		Composite executeComposite = new Composite(corehunterRunArgumentsGroup, SWT.NONE);
		executeComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		executeComposite.setLayout(new GridLayout(2, false));

		btnStart = new Button(executeComposite, SWT.NONE);
		btnStart.setToolTipText("Starts Core Hunter with the current arguments.");
		btnStart.setText("Start New Run");

		btnStart.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				startCorehunterRun();
			}
		});

		btnReset = new Button(executeComposite, SWT.NONE);
		btnReset.setToolTipText("Resets the current arguments to their defaults");
		btnReset.setText("Reset Arguments");

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

	@Override
	public void refresh() {
		updateDatasetViewer() ;
	}

	private void resetDatasetObjectives() {
		if (selectedDataset != null) {
			resetObjectives(selectedDataset);
		}
	}
	
	private void resetSpinnerIntensity() {
		spinnerIntensity.setMinimum(0);
		spinnerIntensity.setSelection(20); // TODO get from properties file
		spinnerIntensity.setMaximum(100);

		spinnerIntensityUpdated();
	}
	
	private void resetSpinnerTime() {
		spinnerTime.setSelection(5); // TODO get from properties file
	}
	
	private void resetMaxImprovement() {
		spinnerIntensity.setSelection(1); // TODO get from properties file
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

	private void updateDatasetViewer() {
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
							Paths.get(dialog.getPhenotypicDataPath()), dialog.getPhenotypicDataType(),
							CoreHunterDataType.PHENOTYPIC);
				}
				if (dialog.getGenotypicDataPath() != null) {
					Activator.getDefault().getDatasetServices().loadData(dialog.getDataset(),
							Paths.get(dialog.getGenotypicDataPath()), dialog.getGenotypicDataType(),
							CoreHunterDataType.GENOTYPIC, dialog.getGenotypeDataFormat());
				}
				if (dialog.getDistancesDataPath() != null) {
					Activator.getDefault().getDatasetServices().loadData(dialog.getDataset(),
							Paths.get(dialog.getDistancesDataPath()), dialog.getDistancesDataType(),
							CoreHunterDataType.DISTANCES);
				}

				// this caches data to speed up the interface
				getCoreHunterData(dialog.getDataset().getUniqueIdentifier()); 

				updateDatasetViewer();
			} catch (Exception e) {
				shellUtilitiies.handleError("Dataset could not be added!", "Dataset could not be added!", e);

				if (dialog.getDataset() != null && dialog.getDataset().getUniqueIdentifier() != null) {
					Dataset dataset = Activator.getDefault().getDatasetServices()
							.getDataset(dialog.getDataset().getUniqueIdentifier());

					if (dataset != null)
						try {
							Activator.getDefault().getDatasetServices().removeDataset(dataset.getUniqueIdentifier());
						} catch (DatasetException e1) {
							shellUtilitiies.handleError("Dataset could not be removed!",
									"Dataset was added, but load failed, and now dataset can not be removed", e);
						}
				}
			}
		}
	}

	private void removeDataset() {

		boolean answer = MessageDialog.openQuestion(shellUtilitiies.getShell(), "Are you sure?",
				String.format(
						"Click on 'Yes' if you want to remove the dataset '%s' otherwise please click 'No'. "
								+ "If you answer 'Yes' the dataset and all its data will be deleted!",
						selectedDataset.getName()));
		
		if (answer) {
			try {
				Activator.getDefault().getDatasetServices().removeDataset(selectedDataset.getUniqueIdentifier());
				coreHunterDataMap.remove(selectedDataset.getUniqueIdentifier());
				updateDatasetViewer();
			} catch (DatasetException e) {
				shellUtilitiies.handleError("Dataset not be removed!",
						"Dataset could not be remove, see error message for more details!", e);
			}
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
		return API.createDefaultObjectives(getCoreHunterData(dataset.getUniqueIdentifier()));
	}

	private CoreHunterObjective createNewObjective(Dataset dataset) {

		if (dataset != null) {
			List<CoreHunterObjective> objectives = objectivesMap.get(dataset.getUniqueIdentifier());

			CoreHunterData coreHunterData = getCoreHunterData(dataset.getUniqueIdentifier());

			if (objectives != null && !objectives.isEmpty()) {
				return API.createDefaultObjective(coreHunterData, objectives);

			} else {
				return API.createDefaultObjective(coreHunterData);
			}

		} else {
			shellUtilitiies.handleError("Can not create objective!",
					"Objective could not be created, dataset is undefined!");
		}

		return null;
	}

	private void updateObjectiveViewer() {
		if (selectedDataset != null) {
			selectedDatasetSize = selectedDataset.getSize();

			objectiveViewer.setCoreHunterData(getCoreHunterData(selectedDataset.getUniqueIdentifier()));

		} else {
			selectedDatasetSize = 0;
			objectiveViewer.setCoreHunterData(null);
		}

		objectiveViewer.setObjectives(getObjectives(selectedDataset));
	}

	private CoreHunterData getCoreHunterData(String uniqueIdentifier) {

		CoreHunterData coreHunterData = null;

		if (selectedDataset != null) {
			coreHunterData = coreHunterDataMap.get(selectedDataset.getUniqueIdentifier());

			GetCoreHunterDataRunnable runnable = new GetCoreHunterDataRunnable();

			if (coreHunterData == null) {
				BusyIndicator.showWhile(Display.getDefault(), runnable);

				coreHunterData = runnable.getCoreHunterData();

				if (coreHunterData != null) {
					coreHunterDataMap.put(selectedDataset.getUniqueIdentifier(), coreHunterData);
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
		resetDatasetObjectives();
		resetSpinnerIntensity();
		resetSpinnerTime() ;
		resetMaxImprovement() ;

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

		// create the part if not already created
		MPart part = partUtilitiies.getPartService().showPart(ResultsPart.ID, PartState.CREATE); 

		String name = createRunName();

		CoreHunterRunArgumentsPojo arguments = new CoreHunterRunArgumentsPojo(name, spinnerSize.getSelection(),
				selectedDataset.getUniqueIdentifier(), objectiveViewer.getObjectives());

		if (btnTime.getSelection()) {
			arguments.setTimeLimit(spinnerTime.getSelection());
		}
		
		if (btnMaxImprovement.getSelection()) {
			arguments.setMaxTimeWithoutImprovement(spinnerMaxImprovement.getSelection());
		}
		
		CoreHunterRunJob job = new CoreHunterRunJob(name, arguments);

		job.schedule();

		partUtilitiies.getPartService().showPart(part, PartState.ACTIVATE);
	}

	private String createRunName() {
		return String.format("Run for %s", selectedDataset.getName());
	}

	private class CoreHunterRunJob extends Job implements CoreHunterJob {

		private CoreHunterRunArgumentsPojo arguments;
		private CoreHunterRun coreHunterRun;
		private String runId;

		public CoreHunterRunJob(String name, CoreHunterRunArgumentsPojo arguments) {
			super(name);
			this.arguments = arguments;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			int time = (int) arguments.getTimeLimit();

			// SubMonitor subMonitor = SubMonitor.convert(monitor, time);

			monitor.beginTask(arguments.getName(), time);

			coreHunterRun = Activator.getDefault().getCoreHunterRunServices().executeCoreHunter(arguments);

			runId = coreHunterRun.getUniqueIdentifier();

			if (monitor instanceof CoreHunterJobMonitor) {
				((CoreHunterJobMonitor) monitor).setMonitorId(runId);
			}

			boolean finished = false;

			while (!finished) {

				coreHunterRun = Activator.getDefault().getCoreHunterRunServices().getCoreHunterRun(runId);
				
				switch (coreHunterRun.getStatus()) {
				case FAILED:
				case FINISHED:
					finished = true;
					break;
				case NOT_STARTED:
				case RUNNING:
				default:
					finished = false;
					break;
				}

				try {
					TimeUnit.SECONDS.sleep(1);
					monitor.worked(1);
				} catch (InterruptedException e) {
					e.printStackTrace();

					logger.error(e.getMessage(), e);
					finished = true;
				}
				// subMonitor.split(1) ;
			}

			monitor.done();

			btnAddDataset.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					partUtilitiies.openPart(new PartInput(coreHunterRun, ResultPart.ID));
				}
			});

			return Status.OK_STATUS;
		}

		public final CoreHunterRunArgumentsPojo getArguments() {
			return arguments;
		}

		public final CoreHunterRun getCoreHunterRun() {
			return coreHunterRun;
		}
	}

	private class GetCoreHunterDataRunnable implements Runnable {

		CoreHunterData coreHunterData;

		public void run() {
			try {
				coreHunterData = Activator.getDefault().getDatasetServices()
						.getCoreHunterData(selectedDataset.getUniqueIdentifier());
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