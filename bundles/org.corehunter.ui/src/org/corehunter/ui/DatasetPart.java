
package org.corehunter.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.corehunter.data.BiAllelicGenotypeData;
import org.corehunter.data.CoreHunterDataType;
import org.corehunter.data.DistanceMatrixData;
import org.corehunter.data.GenotypeData;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jamesframework.core.subset.SubsetSolution;

import uno.informatics.data.Data;
import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.dataset.FeatureData;
import uno.informatics.data.pojo.DatasetPojo;

public class DatasetPart {

	public static final String ID = "org.corehunter.ui.part.dataset";

	private HeaderViewer headerViewer;
	
	private FeatureDataViewer phenotypeDatasetViewer;

	private DataGridViewer<Double, String> genotypeDataViewer;

	private DataGridViewer<Double, SimpleEntity> distanceDataViewer;

	private DataGridViewer<Integer, String> biAllelicGenotypeDataViewer;

	private MPart part;
	private PartInput partInput;
	@Inject
	private MDirtyable dirty;

	private ShellUtilitiies shellUtilitiies;
	
	private PartUtilitiies partUtilitiies;

	private Text textName;

	private Text textAbbreviation;

	private Text textDescription;

	private Button btnSave;

	private DatasetPojo savedDataset;

	@Inject
	public DatasetPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, MPart part, EPartService partService, EModelService modelService,
			MApplication application) {

		try {
			this.part = part;
			shellUtilitiies = new ShellUtilitiies(parent.getShell());
			partUtilitiies = new PartUtilitiies(partService, modelService, application);

			parent.setLayout(new FillLayout(SWT.HORIZONTAL));

			TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText("Dataset");

			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			composite.setLayout(new GridLayout(2, false));

			Label lblName = new Label(composite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");

			textName = new Text(composite, SWT.BORDER);
			textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			textName.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					dirty.setDirty(isDirty());
					updateSaveButton();
				}
			});

			Label lblAbbreviation = new Label(composite, SWT.NONE);
			lblAbbreviation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblAbbreviation.setText("Abbreviation");

			textAbbreviation = new Text(composite, SWT.BORDER);
			textAbbreviation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			textAbbreviation.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					dirty.setDirty(isDirty());
					updateSaveButton();
				}
			});
			Label lblDescription = new Label(composite, SWT.NONE);
			lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblDescription.setText("Description");

			// TODO needs to be multi line
			textDescription = new Text(composite, SWT.BORDER);
			textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			textDescription.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					dirty.setDirty(isDirty());
					updateSaveButton();
				}
			});
			
			Composite buttonComposite = new Composite(composite, SWT.NONE);
			buttonComposite.setLayout(new GridLayout(2, false));
			buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			
						btnSave = new Button(buttonComposite, SWT.NONE);
						btnSave.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
						btnSave.setText("Save Dataset Details");
						
						Button btnExportAll = new Button(buttonComposite, SWT.NONE);
						btnExportAll.setText("Export All");
						btnSave.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								saveDataset();
							}
						});

			Group headerViewerComposite = new Group(composite, SWT.NONE);
			headerViewerComposite.setText("Headers");
			headerViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

			headerViewer = new HeaderViewer();
			headerViewer.setEditable(true);

			headerViewer.createPartControl(headerViewerComposite);

			headerViewer.addSolutionChangedListener(new SolutionChangedListener() {

				@Override
				public void solutionChanged(SolutionChangedEvent event) {
					handleSolutionChanged(event) ;
				}});

			partInput = (PartInput) part.getTransientData().get(PartUtilitiies.INPUT);

			if (partInput != null && partInput.getUniqueIdentifier() != null) {

				Dataset dataset = Activator.getDefault().getDatasetServices()
						.getDataset(partInput.getUniqueIdentifier());
				if (dataset == null) {
					shellUtilitiies.handleError("Can not find dataset!", "Can not find dataset!");
				}

				SimpleEntity[] headers = Activator.getDefault().getDatasetServices()
						.getHeaders(dataset.getUniqueIdentifier());

				headerViewer.setHeaders(headers);

				Data phenotypicData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.PHENOTYPIC);

				Data genotypicData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.GENOTYPIC);

				Data distancesData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.DISTANCES);

				if (phenotypicData != null && phenotypicData instanceof FeatureData) {

					TabItem phenotypesTabItem = new TabItem(tabFolder, SWT.NONE);
					phenotypesTabItem.setText("Phenotypic Data");

					Composite phenotypesComposite = new Composite(tabFolder, SWT.NONE);

					phenotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

					phenotypesTabItem.setControl(phenotypesComposite);
					
					phenotypeDatasetViewer = createFeatureDataViewer((FeatureData) phenotypicData, phenotypesComposite);
					
					phenotypeDatasetViewer.addSolutionChangedListener(new SolutionChangedListener() {

						@Override
						public void solutionChanged(SolutionChangedEvent event) {
							handleSolutionChanged(event) ;
						}});
				}

				if (genotypicData != null) {

					TabItem genotypesTabItem = new TabItem(tabFolder, SWT.NONE);
					genotypesTabItem.setText("Genotypic Data");

					Composite genotypesComposite = new Composite(tabFolder, SWT.NONE);

					genotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

					genotypesTabItem.setControl(genotypesComposite);

					if (genotypicData instanceof BiAllelicGenotypeData) {

						biAllelicGenotypeDataViewer = createBiAllelicGenotypeDataViewer(
								(BiAllelicGenotypeData) genotypicData, genotypesComposite);
						
						biAllelicGenotypeDataViewer.addSolutionChangedListener(new SolutionChangedListener() {

							@Override
							public void solutionChanged(SolutionChangedEvent event) {
								handleSolutionChanged(event) ;
							}});

					} else {
						if (genotypicData instanceof GenotypeData) {

							genotypeDataViewer = createGenotypeDataViewer((GenotypeData) genotypicData,
									genotypesComposite);
							
							genotypeDataViewer.addSolutionChangedListener(new SolutionChangedListener() {

								@Override
								public void solutionChanged(SolutionChangedEvent event) {
									handleSolutionChanged(event) ;		
								}});
						}
					}
				}

				if (distancesData != null && distancesData instanceof DistanceMatrixData) {

					TabItem distancesTabItem = new TabItem(tabFolder, SWT.NONE);
					distancesTabItem.setText("Distances Data");

					Composite distancesComposite = new Composite(tabFolder, SWT.NONE);
					distancesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
					distancesTabItem.setControl(distancesComposite);

					distanceDataViewer = createDistanceDataViewer((DistanceMatrixData) distancesData,
							distancesComposite);
					
					distanceDataViewer.addSolutionChangedListener(new SolutionChangedListener() {

						@Override
						public void solutionChanged(SolutionChangedEvent event) {
							handleSolutionChanged(event) ;
						}});
				}

				updatePart();
			} else {
				shellUtilitiies.handleError("Can not load dataset!", "Can not load dataset!");
			}

		} catch (DatasetException e) {
			e.printStackTrace();
		}
	}

	protected void handleSolutionChanged(SolutionChangedEvent event) {
		
		System.out.println("selected = " + event.getSelected()) ;
		System.out.println("unselected = " + event.getUnselected()) ;
		
		if (event.getSource() != headerViewer) {
			headerViewer.updateSelection(event) ;
		}
		
		if (phenotypeDatasetViewer != null && event.getSource() != phenotypeDatasetViewer) {
			phenotypeDatasetViewer.updateSelection(event) ;
		}
		
		if (distanceDataViewer != null && event.getSource() != distanceDataViewer) {
			distanceDataViewer.updateSelection(event) ;
		}
		
		if (biAllelicGenotypeDataViewer != null && event.getSource() != biAllelicGenotypeDataViewer) {
			biAllelicGenotypeDataViewer.updateSelection(event) ;
		}
		
		if (distanceDataViewer != null && event.getSource() != distanceDataViewer) {
			distanceDataViewer.updateSelection(event) ;
		}
	}

	private FeatureDataViewer createFeatureDataViewer(FeatureData data, Composite parent) {

		FeatureDataViewer phenotypeDatasetViewer = new FeatureDataViewer();

		phenotypeDatasetViewer.setData(data);

		phenotypeDatasetViewer.createPartControl(parent);

		return phenotypeDatasetViewer;
	}

	private DataGridViewer<Double, String> createGenotypeDataViewer(GenotypeData data, Composite parent) {

		DataGridViewer<Double, String> genotypeDataViewer = new DataGridViewer<Double, String>();

		int totalNumberOfAlleles = data.getTotalNumberOfAlleles();
		int numberOfMarkers = data.getNumberOfMarkers();
		
		@SuppressWarnings("unchecked")
		DataGridViewerRow<Double>[] rows = new DataGridViewerRow[data.getSize()];

		Double[] elements ;

		String[] columnHeaders = new String[totalNumberOfAlleles];

		Iterator<Integer> iterator = data.getIDs().iterator();

		int i = 0;
		int j = 0;
		Integer id;

		if (iterator.hasNext()) {
			id = iterator.next();
			j = 0;

			elements = new Double[totalNumberOfAlleles];

			for (int markerIndex = 0; markerIndex < numberOfMarkers; ++markerIndex) {
				for (int alleleIndex = 0; alleleIndex < data.getNumberOfAlleles(markerIndex); ++alleleIndex) {
					columnHeaders[j] = createMarkerName(data.getMarkerName(markerIndex), data.getAlleleName(markerIndex, alleleIndex));
					elements[j] = data.getAlleleFrequency(id, markerIndex, alleleIndex);
					++j;
				}
				
				rows[i] = new DataGridViewerRow<Double>(data.getHeader(id), elements) ;
			}
			++i;

			while (iterator.hasNext()) {
				id = iterator.next();
				j = 0;

				elements = new Double[totalNumberOfAlleles];

				for (int markerIndex = 0; markerIndex < numberOfMarkers; ++markerIndex) {
					for (int alleleIndex = 0; alleleIndex < data.getNumberOfAlleles(markerIndex); ++alleleIndex) {

						elements[j] = data.getAlleleFrequency(id, markerIndex, alleleIndex);
						++j;
					}
					
					rows[i] = new DataGridViewerRow<Double>(data.getHeader(id), elements) ;
				}
				++i;
			}
		}

		genotypeDataViewer.setData(rows, columnHeaders);

		genotypeDataViewer.createPartControl(parent);

		return genotypeDataViewer;
	}
	
	private DataGridViewer<Integer, String> createBiAllelicGenotypeDataViewer(BiAllelicGenotypeData data,
			Composite parent) {

		DataGridViewer<Integer, String> biAllelicGenotypeData = new DataGridViewer<Integer, String>();

		int numberOfMarkers = data.getNumberOfMarkers();

		Integer[] elements  ;

		String[] columnHeaders = new String[numberOfMarkers];
		@SuppressWarnings("unchecked")
		DataGridViewerRow<Integer>[] rows = new DataGridViewerRow[data.getSize()];

		Iterator<Integer> iterator = data.getIDs().iterator();

		int i = 0;
		int j = 0;
		Integer id;

		if (iterator.hasNext()) {
			id = iterator.next();
			j = 0;

			elements = new Integer[numberOfMarkers];

			for (int markerIndex = 0; markerIndex < numberOfMarkers; ++markerIndex) {

				columnHeaders[j] = data.getMarkerName(markerIndex);
				elements[j] = data.getAlleleScore(id, markerIndex);
				rows[i] = new DataGridViewerRow<Integer>(data.getHeader(id), elements);
				++j;
			}
			++i;

			while (iterator.hasNext()) {
				id = iterator.next();
				j = 0;

				elements = new Integer[numberOfMarkers];

				for (int markerIndex = 0; markerIndex < numberOfMarkers; ++markerIndex) {

					elements[j] = data.getAlleleScore(id, markerIndex);

					++j;
				}
				
				rows[i] = new DataGridViewerRow<Integer>(data.getHeader(id), elements);
				
				++i;
			}
		}
		
		biAllelicGenotypeData.setData(rows, columnHeaders);

		biAllelicGenotypeData.createPartControl(parent);

		return biAllelicGenotypeData;
	}

	private DataGridViewer<Double, SimpleEntity> createDistanceDataViewer(DistanceMatrixData data,
			Composite distancesComposite) {

		DataGridViewer<Double, SimpleEntity> distanceDataViewer = new DataGridViewer<Double, SimpleEntity>();

		int size = data.getSize();

		Double[] elements ;

		SimpleEntity[] columnHeaders = new SimpleEntity[data.getSize()];
		@SuppressWarnings("unchecked")
		DataGridViewerRow<Double>[] rows = new DataGridViewerRow[data.getSize()];

		Iterator<Integer> iterator = data.getIDs().iterator();

		int i = 0;
		Integer id;

		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				id = iterator.next();
				elements = new Double[size];

				for (int j = 0; j < size; ++i) {
					elements[j] = data.getDistance(i, j);
				}
				++i;
				
				rows[i] = new DataGridViewerRow<Double>(data.getHeader(id), elements) ;
				columnHeaders[i] = rows[i].getHeader();
			}
		}

		distanceDataViewer.setData(rows, columnHeaders);

		distanceDataViewer.createPartControl(distancesComposite);
		return distanceDataViewer;
	}

	private String createMarkerName(String markerName, String alleleName) {
		return markerName + "\n" + alleleName;
	}

	@Persist
	public void save() {
		saveDataset();
	}

	public void setSolution(SubsetSolution solution) {
		if (ObjectUtils.notEqual(headerViewer.getSolution(), solution)) {
			boolean ok = MessageDialog.openQuestion(shellUtilitiies.getShell(), "Over write existing solution",
					"Do you want to over write the existing selected solution for this dataset?");

			if (ok) {
				updateSolution(solution);
			}
		} else {

			updateSolution(solution);
		}
	}

	private void updateSolution(SubsetSolution solution) {
		if (solution != null) {
			headerViewer.setSolution(solution);

			if (phenotypeDatasetViewer != null) {
				phenotypeDatasetViewer.setSolution(solution);
			}

			if (genotypeDataViewer != null) {
				genotypeDataViewer.setSolution(solution);
			}

			if (biAllelicGenotypeDataViewer != null) {
				biAllelicGenotypeDataViewer.setSolution(solution);
			}
			
			if (distanceDataViewer != null) {
				distanceDataViewer.setSolution(solution);
			}

		} else {
			headerViewer.clearSolution();
		}
	}

	private void updatePart() {

		Dataset dataset = Activator.getDefault().getDatasetServices().getDataset(partInput.getUniqueIdentifier());
		if (dataset == null) {
			shellUtilitiies.handleError("Can not find dataset!", "Can not find dataset!");
		}

		savedDataset = new DatasetPojo(dataset);
		updateSaveButton();

		dirty.setDirty(isDirty());

		partInput.setName(savedDataset.getName());
		part.setLabel(savedDataset.getName());

		textName.setText(savedDataset.getName());
		if (savedDataset.getAbbreviation() != null) {
			textAbbreviation.setText(savedDataset.getAbbreviation());
		} else {
			textAbbreviation.setText("");
		}

		if (savedDataset.getDescription() != null) {
			textDescription.setText(savedDataset.getDescription());
		} else {
			textDescription.setText("");
		}
	}

	private void saveDataset() {
		try {
			if (partInput != null && partInput.getUniqueIdentifier() != null) {

				Dataset dataset = Activator.getDefault().getDatasetServices()
						.getDataset(partInput.getUniqueIdentifier());

				if (dataset != null) {
					DatasetPojo updatedDataset = new DatasetPojo(dataset);

					updatedDataset.setName(textName.getText());
					updatedDataset.setAbbreviation(textAbbreviation.getText());
					updatedDataset.setDescription(textDescription.getText());

					Activator.getDefault().getDatasetServices().updateDataset(updatedDataset);
					
					partUtilitiies.refreshPart(ExecuteCoreHunterPart.ID) ;
				} else {

					shellUtilitiies.handleError("Can not find dataset!", "Can not find dataset!");
				}

				updatePart();
			}
		} catch (Exception e) {
			this.shellUtilitiies.handleError("Can not save result name!", "Can not save result name!", e);
		}
	}

	private boolean isDirty() {
		if (savedDataset != null) {
			return !ObjectUtils.equals(savedDataset.getName() != null ? savedDataset.getName() : "", textName.getText())
					|| !ObjectUtils.equals(savedDataset.getAbbreviation() != null ? savedDataset.getAbbreviation() : "",
							textAbbreviation.getText())
					|| !ObjectUtils.equals(savedDataset.getDescription() != null ? savedDataset.getDescription() : "",
							textDescription.getText());

		} else {
			return false;
		}
	}

	private void updateSaveButton() {
		btnSave.setEnabled(dirty.isDirty());
	}
}