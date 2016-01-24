package org.corehunter.ui.mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.DatasetClient;

import uno.informatics.common.DataTypeConstants;
import uno.informatics.common.io.FileProperties;
import uno.informatics.common.io.FileType;
import uno.informatics.common.model.DataType;
import uno.informatics.common.model.Dataset;
import uno.informatics.common.model.Feature;
import uno.informatics.common.model.FeatureDataset;
import uno.informatics.common.model.ScaleType;
import uno.informatics.common.model.impl.FeatureImpl;
import uno.informatics.data.feature.ColumnFeature;
import uno.informatics.data.feature.ColumnFeatureImpl;
import uno.informatics.data.feature.array.ArrayFeatureDataset;
import uno.informatics.data.utils.DatasetUtils;

public class DatasetClientMock implements DatasetClient
{

  private static final String DATA_FILE3 = "data3.csv";
  private static final String UID3 = "test3";
  private static final String NAME3 = "test3";
  private static final String DESCRIPTION3 = "Test dataset editor with data read from a text file. No row headers";


  private static final String DATA_FILE4 = "data4.csv";
  private static final String UID4 = "test4";
  private static final String NAME4 = "test4";
  private static final String DESCRIPTION4 = "Test dataset editor with data read from a text file. Row headers in the firs column";

  private static List<Dataset> datasets = new LinkedList<Dataset>() ;
  
  static
  {
    datasets.add(createTestDataset1()) ;
    datasets.add(createTestDataset2()) ;
    //datasets.add(createTestDataset3()) ;
    //datasets.add(createTestDataset4()) ;
  }
  
  @Override
  public List<Dataset> getAllDatasets()
  {
    return datasets;
  }
  
  private static FeatureDataset createTestDataset1()
  {       
    Object[][] array = new Object[100][5];
    
    for (int i = 0 ; i < 100 ; ++i)
      array[i] = new Object[] {i,i/10.0,"R"+i+"C3", true, "12/12/2012",i,i/10.0,"R"+i+"C3", true, "12/12/2012"} ;

    ArrayList<Feature> feature = new ArrayList<Feature>();
    
    feature.add(new ColumnFeatureImpl("id1", "Col1", "Description1", DataType.INTEGER, ScaleType.INTERVAL, DataTypeConstants.STRING | DataTypeConstants.INT | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id2", "Col2", "Description2", DataType.DOUBLE, ScaleType.RATIO, DataTypeConstants.STRING | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id3", "Col3", "Description3", DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING)) ;
    feature.add(new ColumnFeatureImpl("id4", "Col4", "Description4", DataType.BOOLEAN, ScaleType.NOMINAL, DataTypeConstants.STRING | DataTypeConstants.BOOLEAN)) ;
    feature.add(new ColumnFeatureImpl("id5", "Col5", "Description5", DataType.DATE, ScaleType.NOMINAL, DataTypeConstants.STRING | DataTypeConstants.DATE)) ;
    
    feature.add(new ColumnFeatureImpl("id11", "Col11", "Description11", DataTypeConstants.STRING | DataTypeConstants.INT | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id12", "Col12", "Description12", DataTypeConstants.STRING | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id13", "Col13", "Description13", DataTypeConstants.STRING)) ;
    feature.add(new ColumnFeatureImpl("id14", "Col14", "Description14", DataTypeConstants.STRING | DataTypeConstants.BOOLEAN)) ;
    feature.add(new ColumnFeatureImpl("id15", "Col15", "Description15", DataTypeConstants.STRING | DataTypeConstants.DATE)) ;
    
    return new ArrayFeatureDataset("test1", "test1", "Test dataset editor with hard coded data. No row headers", feature, array) ;
  }

  /**
   * @return
   */
  private static FeatureDataset createTestDataset2()
  {       
    Object[][] array = new Object[100][5];
    
    for (int i = 0 ; i < 100 ; ++i)
      array[i] = new Object[] {"row"+i, i,i/10.0,"R"+i+"C3", true, "12/12/2012",i,i/10.0,"R"+i+"C3", true, "12/12/2012"} ;
    
    ColumnFeatureImpl rowHeaderFeature = new ColumnFeatureImpl("id", "id", "Description1", 
        DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING) ;

    ArrayList<Feature> feature = new ArrayList<Feature>();
    
    feature.add(new ColumnFeatureImpl("id1", "Col1", "Description1", DataType.INTEGER, ScaleType.INTERVAL, DataTypeConstants.STRING | DataTypeConstants.INT | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id2", "Col2", "Description2", DataType.DOUBLE, ScaleType.RATIO, DataTypeConstants.STRING | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id3", "Col3", "Description3", DataType.STRING, ScaleType.NOMINAL, DataTypeConstants.STRING)) ;
    feature.add(new ColumnFeatureImpl("id4", "Col4", "Description4", DataType.BOOLEAN, ScaleType.NOMINAL, DataTypeConstants.STRING | DataTypeConstants.BOOLEAN)) ;
    feature.add(new ColumnFeatureImpl("id5", "Col5", "Description5", DataType.DATE, ScaleType.NOMINAL, DataTypeConstants.STRING | DataTypeConstants.DATE)) ;
    
    feature.add(new ColumnFeatureImpl("id11", "Col11", "Description11", DataTypeConstants.STRING | DataTypeConstants.INT | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id12", "Col12", "Description12", DataTypeConstants.STRING | DataTypeConstants.DOUBLE)) ;
    feature.add(new ColumnFeatureImpl("id13", "Col13", "Description13", DataTypeConstants.STRING)) ;
    feature.add(new ColumnFeatureImpl("id14", "Col14", "Description14", DataTypeConstants.STRING | DataTypeConstants.BOOLEAN)) ;
    feature.add(new ColumnFeatureImpl("id15", "Col15", "Description15", DataTypeConstants.STRING | DataTypeConstants.DATE)) ;
    
    return new ArrayFeatureDataset("test2", "test2", "Test dataset editor with hard coded data. With row headers", feature, array, rowHeaderFeature) ;
  }
  
  /**
   * @return
   */
  private static FeatureDataset createTestDataset3()
  {           
    try
    {
      FileProperties fileProperties = new FileProperties(DATA_FILE3, FileType.CSV) ;
      
      fileProperties.setColumnHeaderPosition(0) ;
      fileProperties.setDataRowPosition(1) ;
      
      List<ColumnFeature> features = DatasetUtils.generateDatasetFeatures(fileProperties, null, 10) ;
      
      return ArrayFeatureDataset.createFeatureDataset(UID3, NAME3, DESCRIPTION3, createFeatures(features), fileProperties) ;

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return null ;
  }
  
  private static final FeatureDataset createTestDataset4()
  {           
    try
    {
      FileProperties fileProperties = new FileProperties(DatasetClientMock.class.getResource(DATA_FILE4).toString(), FileType.CSV) ;
      
      fileProperties.setColumnHeaderPosition(0) ;
      fileProperties.setDataRowPosition(1) ;
      fileProperties.setRowHeaderPosition(0) ;
      
      List<ColumnFeature> features = DatasetUtils.generateDatasetFeatures(fileProperties, null, 10) ;
      
      return ArrayFeatureDataset.createFeatureDataset(UID4, NAME4, DESCRIPTION4, createFeatures(features), fileProperties) ;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null ;
  }
  
  /**
   * @param items
   * @return
   */
  // TODO replace with DataUtils.createFeatures
  private static List<Feature> createFeatures(List<ColumnFeature> items)
  {
    Iterator<ColumnFeature> iterator = items.iterator() ;
    
    List<Feature> features = new ArrayList<Feature>() ;
    
    while (iterator.hasNext())
      features.add(new FeatureImpl(iterator.next())) ;
    
    return features;
  }
}
