
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.FeatureDataset;
import uno.informatics.data.dataset.DatasetException;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;

public class FeatureDatasetPart extends DatasetServiceClient {
    private FeatureDatasetViewer featureDatasetViewer;

    private String datasetId;

    @Inject
    public FeatureDatasetPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent, MPart part) {

        parent.setLayout(new GridLayout(1, true));

        featureDatasetViewer = new FeatureDatasetViewer();

        try {
            featureDatasetViewer.setValue((FeatureDataset) getDatasetServices().getDataset(datasetId));
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        featureDatasetViewer.createPartControl(parent);
    }

    public synchronized final String getDatasetId() {
        return datasetId;
    }

    public synchronized final void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
}