
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.corehunter.data.CoreHunterDataType;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
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
import uno.informatics.data.matrix.array.DoubleArrayMatrixData;
import uno.informatics.data.matrix.array.ObjectArrayMatrixData;
import uno.informatics.data.pojo.DatasetPojo;

public class DatasetPart {

	public static final String ID = "org.corehunter.ui.part.dataset";

	private FeatureDataViewer phenotypeDatasetViewer;

	private ObjectArrayMatrixDataViewer genotypeDataViewer;

	private DoubleArrayMatrixDataViewer distanceDataViewer;

	private MPart part;
	private PartInput partInput;
	@Inject
	private MDirtyable dirty;

	private ShellUtilitiies shellUtilitiies;

	private Text textName;

	private Text textAbbreviation;

	private Text textDescription;

	private HeaderViewer headerViewer;

	private Button btnSave;

	private DatasetPojo savedDataset;

	@Inject
	public DatasetPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, MPart part) {

		try {
	        this.part = part ; 
			shellUtilitiies = new ShellUtilitiies(parent.getShell());

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
	        textName.addModifyListener(new ModifyListener(){
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
			textAbbreviation.addModifyListener(new ModifyListener(){
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
			textDescription.addModifyListener(new ModifyListener(){
			      public void modifyText(ModifyEvent event) {
			    	  dirty.setDirty(isDirty());
			    	  updateSaveButton();
			      }
			});

			btnSave = new Button(composite, SWT.NONE);
			btnSave.setText("Save");
			btnSave.addSelectionListener(new SelectionAdapter() {

	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                saveDataset();
	            }
	        });
			new Label(composite, SWT.NONE);

			Group headerViewerComposite = new Group(composite, SWT.NONE);
			headerViewerComposite.setText("Headers");
			headerViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

			headerViewer = new HeaderViewer();
			headerViewer.setEditable(true);

			headerViewer.createPartControl(headerViewerComposite);

			partInput = (PartInput) part.getTransientData().get(PartUtilitiies.INPUT);

			if (partInput != null && partInput.getUniqueIdentifier() != null) {

				Dataset dataset = Activator.getDefault().getDatasetServices()
						.getDataset(partInput.getUniqueIdentifier());
				if (dataset == null) {
					shellUtilitiies.handleError("Can not find dataset!", "Can not find dataset!");
				}

                SimpleEntity[] headers = Activator.getDefault().getDatasetServices().getHeaders(dataset.getUniqueIdentifier()) ;

                headerViewer.setHeaders(headers);
                
				Data phenotypicData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.PHENOTYPIC);

				Data genotypicData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.GENOTYPIC);

				Data distancesData = Activator.getDefault().getDatasetServices()
						.getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.DISTANCES);

				if (phenotypicData != null && phenotypicData instanceof FeatureData) {
					phenotypeDatasetViewer = new FeatureDataViewer();

					TabItem phenotypesTabItem = new TabItem(tabFolder, SWT.NONE);
					phenotypesTabItem.setText("Phenotypic Data");

					Composite phenotypesComposite = new Composite(tabFolder, SWT.NONE);
					phenotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
					phenotypesTabItem.setControl(phenotypesComposite);

					phenotypeDatasetViewer.setValue((FeatureData) phenotypicData);

					phenotypeDatasetViewer.createPartControl(phenotypesComposite);
				}

				if (genotypicData != null && phenotypicData instanceof ObjectArrayMatrixData) {

					TabItem genotypesTabItem = new TabItem(tabFolder, SWT.NONE);
					genotypesTabItem.setText("Genotypic Data");

					Composite genotypesComposite = new Composite(tabFolder, SWT.NONE);
					genotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
					genotypesTabItem.setControl(genotypesComposite);

					genotypeDataViewer.setValue((ObjectArrayMatrixData) genotypicData);

					genotypeDataViewer.createPartControl(genotypesComposite);

					genotypeDataViewer.createPartControl(parent);
				}

				if (distancesData != null && phenotypicData instanceof DoubleArrayMatrixData) {

					TabItem distancesTabItem = new TabItem(tabFolder, SWT.NONE);
					distancesTabItem.setText("Distances Data");

					Composite distancesComposite = new Composite(tabFolder, SWT.NONE);
					distancesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
					distancesTabItem.setControl(distancesComposite);

					distanceDataViewer.setValue((DoubleArrayMatrixData) distancesData);

					distanceDataViewer.createPartControl(distancesComposite);

					distanceDataViewer.createPartControl(parent);
				}
				
				updatePart() ;
			} else {
				shellUtilitiies.handleError("Can not load dataset!", "Can not load dataset!");
			}

		} catch (DatasetException e) {
			e.printStackTrace();
		}
	}

	@Persist
	public void save() {
		saveDataset();
	}
	
	public void setSolution(SubsetSolution solution) {
		if (ObjectUtils.notEqual(headerViewer.getSolution(), solution)) {
	        boolean ok = MessageDialog.openQuestion(shellUtilitiies.getShell(), "Over write existing solution", "Do you want to over write the existing selected solution for this dataset?") ;
	        
	        if (ok) {
	        	updateSolution(solution) ;
	        }
		} else {

			updateSolution(solution) ;
		}		
	}
	
	private void updateSolution(SubsetSolution solution) {
        if (solution != null) {
            headerViewer.setSolution(solution) ;
        } else {
        	headerViewer.clearSolution() ;
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
					
					Activator.getDefault().getDatasetServices().updateDataset(updatedDataset) ;
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
					|| !ObjectUtils.equals(savedDataset.getAbbreviation() != null ? savedDataset.getAbbreviation() : "", textAbbreviation.getText())
					|| !ObjectUtils.equals(savedDataset.getDescription() != null ? savedDataset.getDescription() : "", textDescription.getText());

		} else {
			return false;
		}
	}

	private void updateSaveButton() {
		btnSave.setEnabled(dirty.isDirty());
	}
}