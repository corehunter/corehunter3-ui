package org.corehunter.ui.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.DatasetClient;

import uno.informatics.common.io.FileProperties;
import uno.informatics.common.io.FileType;
import uno.informatics.data.DataType;
import uno.informatics.data.DataTypeConstants;
import uno.informatics.data.Dataset;
import uno.informatics.data.Feature;
import uno.informatics.data.FeatureDataset;
import uno.informatics.data.ScaleType;
import uno.informatics.data.feature.ColumnFeature;
import uno.informatics.data.feature.ColumnFeaturePojo;
import uno.informatics.data.feature.array.ArrayFeatureDataset;
import uno.informatics.data.pojo.FeaturePojo;
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
    
    return new ArrayFeatureDataset("test2", "test2", "Test dataset editor with hard coded data. With row headers", feature, array, rowHeaderFeature) ;
  }
  
  /**
   * @return
   */
  private static FeatureDataset createTestDataset3()
  {           
    try
    {
      return ArrayFeatureDataset.readFeatureDatasetFromTextFile(new File(DatasetClientMock.class.getResource(DATA_FILE3).toString()), FileType.CSV) ;

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
      return ArrayFeatureDataset.readFeatureDatasetFromTextFile(new File(DatasetClientMock.class.getResource(DATA_FILE4).toString()), FileType.CSV) ;
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
      features.add(new FeaturePojo(iterator.next())) ;
    
    return features;
  }
}
