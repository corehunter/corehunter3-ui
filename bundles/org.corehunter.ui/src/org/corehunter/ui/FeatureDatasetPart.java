
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.FeatureDataset;
import uno.informatics.data.dataset.DatasetException;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;

public class FeatureDatasetPart extends DatasetServiceClient {
    
    public final static String ID = FeatureDatasetPart.class.getName() ;
    
    private FeatureDatasetViewer featureDatasetViewer;

    private PartInput partInput;

    @Inject
    public FeatureDatasetPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent, MPart part) {

        parent.setLayout(new GridLayout(1, true));

        featureDatasetViewer = new FeatureDatasetViewer();
        
        partInput = (PartInput)part.getTransientData().get(PartUtilitiies.INPUT) ;

        try {
            featureDatasetViewer.setValue((FeatureDataset) getDatasetServices().getDataset(partInput.getUniqueIdentifier()));
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        featureDatasetViewer.createPartControl(parent);
    }
}