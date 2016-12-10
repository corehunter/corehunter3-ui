
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.data.CoreHunterData;
import org.corehunter.data.CoreHunterDataType;
import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

public class DatasetPart {

    public static final String ID = "org.corehunter.ui.part.dataset" ;

    private FeatureDataViewer phenotypeDatasetViewer;

    private ObjectArrayMatrixDataViewer genotypeDataViewer;

    private DoubleArrayMatrixDataViewer distanceDataViewer;

    private PartInput partInput;
    private Text textName;

    private Text textAbbreviation;

    private Text textDescription;

    private HeaderViewer headerViewer;

    @Inject
    public DatasetPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent, MPart part) {

        try {
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

            Label lblAbbreviation = new Label(composite, SWT.NONE);
            lblAbbreviation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            lblAbbreviation.setText("Abbreviation");

            textAbbreviation = new Text(composite, SWT.BORDER);
            textAbbreviation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

            Label lblDescription = new Label(composite, SWT.NONE);
            lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            lblDescription.setText("Description");

            // TODO needs to be multi line
            textDescription = new Text(composite, SWT.BORDER);
            textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            
            Group headerViewerComposite = new Group(composite, SWT.NONE);
            headerViewerComposite.setText("Headers");
            headerViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

            headerViewer = new HeaderViewer();
            headerViewer.setEditable(true) ;

            headerViewer.createPartControl(headerViewerComposite);

            partInput = (PartInput) part.getTransientData().get(PartUtilitiies.INPUT);

            if (partInput != null) {
            	CoreHunterRun run = Activator.getDefault().getCoreHunterRunServices().getCoreHunterRun(partInput.getUniqueIdentifier()) ;
            	
            	Dataset dataset = null ;
            	
            	if (run != null) {
            		CoreHunterRunArguments arguments = Activator.getDefault().getCoreHunterRunServices().getArguments(run.getUniqueIdentifier()) ;
            		
                	if (arguments != null) {
                		dataset = Activator.getDefault().getDatasetServices()
                                .getDataset(arguments.getDatasetId());
                	} else {
                		// TODO error
                	}
            	} else {
            		dataset = Activator.getDefault().getDatasetServices()
                            .getDataset(partInput.getUniqueIdentifier());
            	}
            	
            	if (dataset == null) {
            		// TODO error
            	}
            	
                textName.setText(dataset.getName());
                if (dataset.getAbbreviation() != null)
                    textAbbreviation.setText(dataset.getAbbreviation());
                if (dataset.getDescription() != null)
                    textDescription.setText(dataset.getDescription());
               
                Data phenotypicData = Activator.getDefault().getDatasetServices()
                        .getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.PHENOTYPIC);

                Data genotypicData = Activator.getDefault()
                        .getDatasetServices()
                        .getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.GENOTYPIC);

                Data distancesData = Activator.getDefault()
                        .getDatasetServices()
                        .getOriginalData(partInput.getUniqueIdentifier(), CoreHunterDataType.DISTANCES);
                
                SimpleEntity[] headers = Activator.getDefault().getDatasetServices().getHeaders(dataset.getUniqueIdentifier()) ;

            	if (run != null) {
            		
            		SubsetSolution solution = Activator.getDefault().getCoreHunterRunServices().getSubsetSolution(run.getUniqueIdentifier()) ;
            		headerViewer.setHeadersWithSolution(headers, solution);
            	} else {
            		headerViewer.setHeaders(headers) ;            		
            	}
            		
                
                if (phenotypicData != null && phenotypicData instanceof FeatureData) {
                    phenotypeDatasetViewer = new FeatureDataViewer();

                    TabItem phenotypesTabItem = new TabItem(tabFolder, SWT.NONE);
                    phenotypesTabItem.setText("Phenotypic Data");

                    Composite phenotypesComposite = new Composite(tabFolder, SWT.NONE);
                    phenotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
                    phenotypesTabItem.setControl(phenotypesComposite);

                    phenotypeDatasetViewer.setValue((FeatureData)phenotypicData);

                    phenotypeDatasetViewer.createPartControl(phenotypesComposite);
                }

                if (genotypicData != null && phenotypicData instanceof ObjectArrayMatrixData) {

                    TabItem genotypesTabItem = new TabItem(tabFolder, SWT.NONE);
                    genotypesTabItem.setText("Genotypic Data");

                    Composite genotypesComposite = new Composite(tabFolder, SWT.NONE);
                    genotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
                    genotypesTabItem.setControl(genotypesComposite);

                    genotypeDataViewer.setValue((ObjectArrayMatrixData)genotypicData);

                    genotypeDataViewer.createPartControl(genotypesComposite);

                    genotypeDataViewer.createPartControl(parent);
                }

                if (distancesData != null && phenotypicData instanceof DoubleArrayMatrixData) {

                    TabItem distancesTabItem = new TabItem(tabFolder, SWT.NONE);
                    distancesTabItem.setText("Distances Data");

                    Composite distancesComposite = new Composite(tabFolder, SWT.NONE);
                    distancesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
                    distancesTabItem.setControl(distancesComposite);

                    distanceDataViewer.setValue((DoubleArrayMatrixData)distancesData);

                    distanceDataViewer.createPartControl(distancesComposite);

                    distanceDataViewer.createPartControl(parent);
                }
            } else {
            	// TODO error
            }
            	
        } catch (DatasetException e) {
            e.printStackTrace();
        }
    }
}