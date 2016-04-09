package org.corehunter.ui.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.corehunter.services.DataType;
import org.corehunter.services.simple.FileBasedDatasetServices;

import uno.informatics.common.io.FileType;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.pojo.DatasetPojo;

public class FileBasedDatasetServicesMock extends FileBasedDatasetServices {

  // TODO need relative path
  private static final String DATA_FILE3 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data3.csv";
  // TODO need relative path
  private static final String DATA_FILE4 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data4.csv";
  
    public FileBasedDatasetServicesMock() throws DatasetException, IOException {
        super(Files.createTempDirectory(null));
        
        addDataset(new DatasetPojo("dataset1", "Dataset 1", "This is dataset1!")) ;
        addDataset(new DatasetPojo("dataset2", "Dataset 2", "This is dataset2!")) ;
        
        loadData(getDataset("dataset1"), Paths.get(DATA_FILE3), FileType.CSV, DataType.PHENOTYPIC);
        loadData(getDataset("dataset2"), Paths.get(DATA_FILE4), FileType.CSV, DataType.PHENOTYPIC);
    }
}
