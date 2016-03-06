package org.corehunter.ui.test;

import org.corehunter.ui.FeatureDatasetPart;

import uno.informatics.data.dataset.DatasetException;

public class MockFeatureDatasetPart extends FeatureDatasetPart
{

  public MockFeatureDatasetPart()
  {
    super();

    try {
        setDatasetId(getDatasetServices().getAllDatasets().get(3).getUniqueIdentifier()) ;
    } catch (DatasetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }

}
