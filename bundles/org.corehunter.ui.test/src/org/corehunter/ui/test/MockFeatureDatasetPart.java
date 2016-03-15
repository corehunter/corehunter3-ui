package org.corehunter.ui.test;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.ui.DatasetServiceClient;
import org.corehunter.ui.FeatureDatasetPart;
import org.corehunter.ui.FeatureDatasetViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.FeatureDataset;
import uno.informatics.data.dataset.DatasetException;

public class MockFeatureDatasetPart extends DatasetServiceClient {
    
    public final static String ID = FeatureDatasetPart.class.getName() ;
    
    private FeatureDatasetViewer featureDatasetViewer;

    private String uniqueIdentifier;

    @Inject
    public MockFeatureDatasetPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent) {

        parent.setLayout(new GridLayout(1, true));

        featureDatasetViewer = new FeatureDatasetViewer();

        try {
            featureDatasetViewer.setValue((FeatureDataset) getDatasetServices().getDataset(uniqueIdentifier));
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        featureDatasetViewer.createPartControl(parent);
    }

    public final String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public final void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }
}
