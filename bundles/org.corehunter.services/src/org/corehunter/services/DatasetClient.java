package org.corehunter.services;

import java.nio.file.Path;
import java.util.List;

import uno.informatics.common.io.FileType;
import uno.informatics.data.Dataset;

// TODO more to standard maven model in uno infomatrics data
public interface DatasetClient
{
  public void addDataset(Path path, FileType fileType) ;
  
  public List<Dataset> getAllDatasets() ;
}
