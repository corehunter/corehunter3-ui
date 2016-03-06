 
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.FeatureDataset;
import uno.informatics.data.dataset.DatasetException;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class FeatureDatasetPart extends DatasetServiceClient
{
  private FeatureDatasetViewer featureDatasetViewer ;
  
  private String datasetId ;
  
	@Inject
	public FeatureDatasetPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, MPart part) {
	    
	  parent.setLayout(new GridLayout(1, true));
	  
	  featureDatasetViewer = new FeatureDatasetViewer() ;
	  
	  try {
        featureDatasetViewer.setValue((FeatureDataset)getDatasetServices().getDataset(datasetId));
    } catch (DatasetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
	  
	  featureDatasetViewer.createPartControl(parent);
	}

  public synchronized final String getDatasetId()
  {
    return datasetId;
  }

  public synchronized final void setDatasetId(String datasetId)
  {
    this.datasetId = datasetId;
  }	
}