package org.corehunter.services;

import java.util.List;

import uno.informatics.common.model.Dataset;

// TODO more to standard maven model in uno infomatrics data
public interface DatasetClient
{
  public List<Dataset> getAllDatasets() ;
}
