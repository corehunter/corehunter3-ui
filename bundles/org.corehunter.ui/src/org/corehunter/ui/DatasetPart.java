
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.data.CoreHunterData;
import org.corehunter.data.DistanceMatrixData;
import org.corehunter.data.GenotypeData;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.Dataset;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.dataset.FeatureData;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class DatasetPart extends DatasetServiceClient {
    
    public final static String ID = DatasetPart.class.getName() ;
    
    private FeatureDataViewer featureDatasetViewer;

    private PartInput partInput;
    private Text textName;

    private Text textAbbreviation;

    private Text textDescription;

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
            
            textDescription = new Text(composite, SWT.BORDER);
            textDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            
            partInput = (PartInput)part.getTransientData().get(PartUtilitiies.INPUT) ;
                
            if (partInput != null)
            {               
                Dataset dataset = getDatasetServices().getDataset(partInput.getUniqueIdentifier()) ;
                
                textName.setText(dataset.getName());
                if (dataset.getAbbreviation() != null)
                    textAbbreviation.setText(dataset.getAbbreviation());
                if (dataset.getDescription() != null)
                    textDescription.setText(dataset.getDescription());
                
                CoreHunterData data = getDatasetServices().getCoreHunterData(partInput.getUniqueIdentifier()) ;
                
                GenotypeData genotypes = null ;
                FeatureData phenotypes = null ;
                DistanceMatrixData distances = null ;
                
                if (data != null)
                {  
                    genotypes = data.getGenotypicData();
                    phenotypes = data.getPhenotypicData() ;
                    distances = data.getDistancesData();

                    if (genotypes != null) {
        
                        TabItem tbtmGenotypes = new TabItem(tabFolder, SWT.NONE);
                        tbtmGenotypes.setText(genotypes.getName());
                        
                        //genotypesDataViewer.createPartControl(parent);
                    }
                    
                    if (phenotypes != null) {
                        featureDatasetViewer = new FeatureDataViewer() ;
                        
                        TabItem tbtmPhenotypes = new TabItem(tabFolder, SWT.NONE);
                        tbtmPhenotypes.setText(phenotypes.getName());
                        
                        Composite phenotypesComposite = new Composite(tabFolder, SWT.NONE);
                        phenotypesComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
                        tbtmPhenotypes.setControl(phenotypesComposite);
                        
                        featureDatasetViewer.setValue(phenotypes);
                        
                        featureDatasetViewer.createPartControl(phenotypesComposite);
                    }
                    
                    if (distances != null) {
        
                        TabItem tbtmDistances = new TabItem(tabFolder, SWT.NONE);
                        tbtmDistances.setText(distances.getName());
                        
                        //distanceDataViewer.createPartControl(parent);
                    }
                }
            }
        } catch (DatasetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}