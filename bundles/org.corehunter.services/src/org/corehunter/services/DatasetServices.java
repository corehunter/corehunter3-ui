package org.corehunter.services;

import java.nio.file.Path;
import java.util.List;

import uno.informatics.common.io.FileType;
import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.dataset.DatasetException;

// TODO more to standard maven model in uno informatics data
public interface DatasetServices
{
  public List<Dataset> getAllDatasets() ;
  
  public List<SimpleEntity> getDatasetIdenitifers() ;

  public Dataset getDataset(String datasetId);

  public void addDataset(Path path, FileType fileType, DatasetType datasetType) throws DatasetException ;
  
  void removeDataset(String datasetId);
}
