package org.corehunter.ui.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.corehunter.services.DatasetType;
import org.corehunter.services.simple.FileBasedDatasetServices;

import uno.informatics.common.io.FileType;
import uno.informatics.data.dataset.DatasetException;

public class FileBasedDatasetServicesMock extends FileBasedDatasetServices {

  // TODO need relative path
  private static final String DATA_FILE3 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data3.csv";
  // TODO need relative path
  private static final String DATA_FILE4 = "/Users/daveneti/Repositories/corehunter3-ui/bundles/org.corehunter.ui.mock/data/data4.csv";
  
    public FileBasedDatasetServicesMock() throws DatasetException, IOException {
        super(Files.createTempDirectory(null));

        addDataset(Paths.get(DATA_FILE3), FileType.CSV, DatasetType.PHENOTYPIC);
        addDataset(Paths.get(DATA_FILE4), FileType.CSV, DatasetType.PHENOTYPIC);
    }
}
