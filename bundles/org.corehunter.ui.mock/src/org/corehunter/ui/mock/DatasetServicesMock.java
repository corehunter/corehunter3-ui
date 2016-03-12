package org.corehunter.ui.mock;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.DatasetServices;
import org.corehunter.services.DatasetType;

import uno.informatics.common.io.FileType;
import uno.informatics.data.DataType;
import uno.informatics.data.DataTypeConstants;
import uno.informatics.data.Dataset;
import uno.informatics.data.Feature;
import uno.informatics.data.FeatureDataset;
import uno.informatics.data.ScaleType;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.feature.ColumnFeaturePojo;
import uno.informatics.data.feature.array.ArrayFeatureDataset;

public class DatasetServicesMock implements DatasetServices
{
    // TODO need relative path
  private static final String DATA_FILE3 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data3.csv";
  // TODO need relative path
  private static final String DATA_FILE4 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data4.csv";

  private static List<Dataset> datasets = new LinkedList<Dataset>() ;
  
  private static List<SimpleEntity> datasetDescriptions = new LinkedList<SimpleEntity>() ;
  
  static
  {
    for (int i = 0 ; i < 10 ; ++i)
    {
      addDataset(createTestDataset1(i)) ;
      addDataset(createTestDataset2(i)) ;
      addDataset(createTestDataset3(i)) ;
      addDataset(createTestDataset4(i)) ;
    }
  }
  
  @Override
  public List<Dataset> getAllDatasets()
  {
    return datasets;
  }
  
  @Override
  public String addDataset(Path path, FileType fileType, DatasetType datasetType) throws DatasetException
  {
    throw new DatasetException("Add dataset not support in Mock") ;
  }
  
  @Override
  public List<SimpleEntity> getDatasetDescriptions()
  {
    return datasetDescriptions;
  }

  @Override
  public Dataset getDataset(String datasetId)
  {
    Iterator<Dataset> iterator = datasets.iterator() ;
    
    Dataset dataset = null ;
    
    while (iterator.hasNext() && dataset == null)
    {
      dataset = iterator.next() ;
      
      if (datasetId.equals(dataset.getUniqueIdentifier()))
        dataset = null ;
    }
    
    return dataset;
  }

  @Override
  public boolean removeDataset(String datasetId)
  {
      return false ;
  }
  
  private static void addDataset(FeatureDataset dataset)
  {
    datasets.add(dataset) ;
    datasetDescriptions.add(dataset) ;
  }

  private static FeatureDataset createTestDataset1(int index)
  {       
    Object[][] array = new Object[100][5];
    
    for (int i = 0 ; i < 100 ; ++i)
      array[i] = new Object[] {i,i/10.0,"R"+i+"C3", true, "12/12/2012",i,i/10.0,"R"+i+"C3", true, "12/12/2012"} ;

    ArrayList<Feature> feature = new ArrayList<Feature>();
    
    feature.add(new ColumnFeaturePojo("id1", "Col1", "Description1", DataType.INTEGER, ScaleType.INTERVAL, DataTypeConstants.STRING_ID | DataTypeConstants.INT_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id2", "Col2", "Description2", DataType.DOUBLE, ScaleType.RATIO, DataTypeConstants.STRING_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id3", "Col3", "Description3", DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING_ID)) ;
    feature.add(new ColumnFeaturePojo("id4", "Col4", "Description4", DataType.BOOLEAN, ScaleType.NOMINAL, DataTypeConstants.STRING_ID | DataTypeConstants.BOOLEAN_ID)) ;
    feature.add(new ColumnFeaturePojo("id5", "Col5", "Description5", DataType.DATE, ScaleType.NOMINAL, DataTypeConstants.STRING_ID | DataTypeConstants.DATE_ID)) ;
    
    feature.add(new ColumnFeaturePojo("id11", "Col11", "Description11", DataTypeConstants.STRING_ID | DataTypeConstants.INT_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id12", "Col12", "Description12", DataTypeConstants.STRING_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id13", "Col13", "Description13", DataTypeConstants.STRING_ID)) ;
    feature.add(new ColumnFeaturePojo("id14", "Col14", "Description14", DataTypeConstants.STRING_ID | DataTypeConstants.BOOLEAN_ID)) ;
    feature.add(new ColumnFeaturePojo("id15", "Col15", "Description15", DataTypeConstants.STRING_ID | DataTypeConstants.DATE_ID)) ;
    
    return new ArrayFeatureDataset("test1"+index, "test1", "Test dataset editor with hard coded data. No row headers", feature, array) ;
  }

  /**
   * @return
   */
  private static FeatureDataset createTestDataset2(int index)
  {       
    Object[][] array = new Object[100][5];
    
    for (int i = 0 ; i < 100 ; ++i)
      array[i] = new Object[] {"row"+i, i,i/10.0,"R"+i+"C3", true, "12/12/2012",i,i/10.0,"R"+i+"C3", true, "12/12/2012"} ;
    
    ColumnFeaturePojo rowHeaderFeature = new ColumnFeaturePojo("id", "id", "Description1", 
        DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING_ID) ;

    ArrayList<Feature> feature = new ArrayList<Feature>();
    
    feature.add(new ColumnFeaturePojo("id1", "Col1", "Description1", DataType.INTEGER, ScaleType.INTERVAL, DataTypeConstants.STRING_ID | DataTypeConstants.INT_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id2", "Col2", "Description2", DataType.DOUBLE, ScaleType.RATIO, DataTypeConstants.STRING_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id3", "Col3", "Description3", DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING_ID)) ;
    feature.add(new ColumnFeaturePojo("id4", "Col4", "Description4", DataType.BOOLEAN, ScaleType.NOMINAL, DataTypeConstants.STRING_ID | DataTypeConstants.BOOLEAN_ID)) ;
    feature.add(new ColumnFeaturePojo("id5", "Col5", "Description5", DataType.DATE, ScaleType.NOMINAL, DataTypeConstants.STRING_ID | DataTypeConstants.DATE_ID)) ;
    
    feature.add(new ColumnFeaturePojo("id11", "Col11", "Description11", DataTypeConstants.STRING_ID | DataTypeConstants.INT_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id12", "Col12", "Description12", DataTypeConstants.STRING_ID | DataTypeConstants.DOUBLE_ID)) ;
    feature.add(new ColumnFeaturePojo("id13", "Col13", "Description13", DataTypeConstants.STRING_ID)) ;
    feature.add(new ColumnFeaturePojo("id14", "Col14", "Description14", DataTypeConstants.STRING_ID | DataTypeConstants.BOOLEAN_ID)) ;
    feature.add(new ColumnFeaturePojo("id15", "Col15", "Description15", DataTypeConstants.STRING_ID | DataTypeConstants.DATE_ID)) ;
    
    return new ArrayFeatureDataset("test2"+index, "test2", "Test dataset editor with hard coded data. With row headers", feature, array, true) ;
  }
  
  /**
   * @return
   */
  private static FeatureDataset createTestDataset3(int index)
  {           
    try
    {
      ArrayFeatureDataset dataset = (ArrayFeatureDataset)ArrayFeatureDataset.readFeatureDatasetFromTextFile(
          new File(DATA_FILE3), FileType.CSV) ;
      
      dataset.setUniqueIdentifier("test3"+index);
      
      return dataset ;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return null ;
  }
  
  private static final FeatureDataset createTestDataset4(int index)
  {           
    try
    { 
      ArrayFeatureDataset dataset = (ArrayFeatureDataset)ArrayFeatureDataset.readFeatureDatasetFromTextFile(
          new File(DATA_FILE4), FileType.CSV) ;
      
      dataset.setUniqueIdentifier("test4"+index);
      
      return dataset ;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null ;
  }
}
