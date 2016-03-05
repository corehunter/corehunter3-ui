package org.corehunter.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uno.informatics.common.io.FileType;
import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.feature.array.ArrayFeatureDataset;
import uno.informatics.data.pojo.SimpleEntityPojo;

public class FileBasedDatasetServices implements DatasetServices
{
  private static List<SimpleEntity> datasetsIds ;
  
  private static Map<String, Dataset> datasetMap ;
  
  private static List<Dataset> datasetCache ;
  
  private Path path ;
  
  public FileBasedDatasetServices()
  {
    
  }
  
  public FileBasedDatasetServices(Path path)
  {
    setPath(path) ;
    
    initialise() ;
  }

  public synchronized final Path getPath()
  {
    return path;
  }

  protected synchronized final void setPath(Path path)
  {
    if (path == null)
      throw new IllegalArgumentException("Path must be defined!") ;
    
    this.path = path;
  }

  @Override
  public List<Dataset> getAllDatasets()
  {
    if (datasetCache == null)
    {
      datasetCache = new ArrayList<Dataset>() ;
      
      Iterator<SimpleEntity> iterator = datasetsIds.iterator() ;
      
      while (iterator.hasNext())
        datasetCache.add(datasetMap.get(iterator.next().getUniqueIdentifier())) ;
    }
    
    return datasetCache;
  }
  
  @Override
  public List<SimpleEntity> getDatasetIdenitifers()
  {
    return datasetsIds;
  }
  
  @Override
  public Dataset getDataset(String datasetId)
  {
    return datasetMap.get(datasetId);
  }
  
  @Override
  public void addDataset(Path path, FileType fileType, DatasetType datasetType) throws DatasetException
  {
    Dataset dataset = loadDataset(path, fileType, datasetType) ;
    
    if (!datasetMap.containsKey(dataset.getUniqueIdentifier()))
    {
      datasetsIds.add(new SimpleEntityPojo(dataset)) ;
      datasetMap.put(dataset.getUniqueIdentifier(), dataset) ;
      
      if (datasetCache != null)
        datasetCache.add(dataset) ;
    }
  }
  
  @Override
  public void removeDataset(String datasetId)
  {
    Dataset dataset = getDataset(datasetId) ;
    
    if (dataset != null)
    {
      datasetsIds.remove(datasetId) ;
      
      Dataset removed = datasetMap.remove(datasetId) ;
      
      if (removed != null && datasetCache != null)
        datasetCache.remove(dataset) ;
    }
  }


  private void initialise()
  {
    // TODO Auto-generated method stub
    
  }
  
  private Dataset loadDataset(Path path, FileType fileType, DatasetType datasetType) throws DatasetException
  {
    Dataset dataset = null ;
    
    switch (datasetType)
    {
      case BI_ALLELIC_GENOTYPIC:
        dataset = loadBiAllelicGenotypicDataset(path, fileType) ;
        break;
      case MULTI_ALLELIC_GENOTYPIC:
        dataset = loadMultiAllelicGenotypicDataset(path, fileType) ;
        break;
      case PHENOTYPIC:
        dataset = loadPhenotypicDataset(path, fileType) ;
        break;
      default:
        throw new IllegalArgumentException("Unknown dataset type : " + datasetType) ;
      
    }
    return dataset ;
  }

  private Dataset loadBiAllelicGenotypicDataset(Path path, FileType fileType) throws DatasetException
  {
    // TODO Auto-generated method stub
    return null;
  }

  private Dataset loadMultiAllelicGenotypicDataset(Path path, FileType fileType) throws DatasetException
  {
    // TODO Auto-generated method stub
    return null;
  }

  private Dataset loadPhenotypicDataset(Path path, FileType fileType) throws DatasetException
  {
    return ArrayFeatureDataset.readFeatureDatasetFromTextFile(path.toFile(), fileType);
  }

}
