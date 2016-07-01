package org.corehunter.ui.test;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.ui.DatasetPart;
import org.corehunter.ui.FeatureDataViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.corehunter.ui.Activator;

import uno.informatics.data.dataset.FeatureData;

public class MockFeatureDatasetPart {
    
    public final static String ID = DatasetPart.class.getName() ;
    
    private FeatureDataViewer featureDatasetViewer;

    private String uniqueIdentifier;

    @Inject
    public MockFeatureDatasetPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent) {

        parent.setLayout(new GridLayout(1, true));

        featureDatasetViewer = new FeatureDataViewer();

        featureDatasetViewer.setValue((FeatureData) Activator.getDefault().getDatasetServices().getDataset(uniqueIdentifier));

        featureDatasetViewer.createPartControl(parent);
    }

    public final String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public final void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }
}
