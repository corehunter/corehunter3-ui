package org.corehunter.ui.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.corehunter.data.CoreHunterDataType;
import org.corehunter.data.GenotypeDataFormat;
import org.corehunter.services.simple.FileBasedDatasetServices;

import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.io.FileType;
import uno.informatics.data.pojo.DatasetPojo;

public class FileBasedDatasetServicesMock extends FileBasedDatasetServices {

  private static final String PHENOTYPIC_DATA = "/phenotypic_data.csv";
  private static final String BIPARENTAL_GENOTYPE_DATA = "/biparental_genotypic_data.csv";
  private static final String FREQUENCY_GENOTYPE_DATA = "/frequency_genotypic_data.csv";
  private static final String FREQUENCY_ALT_GENOTYPE_DATA = "/frequency_alt_genotypic_data.txt";
  private static final String DIPLOID_GENOTYPE_DATA = "/diploid_genotypic_data.csv";
  private static final String HOMOZYGOUS_GENOTYPE_DATA = "/homozygous_genotypic_data.csv";
  private static final String DISTANCES_DATA = "/distances_data.csv";

  
    public FileBasedDatasetServicesMock() throws DatasetException, IOException, URISyntaxException {
        super(Files.createTempDirectory(null));
        
        addDataset(new DatasetPojo("dataset0", "Empty dataset", "This is a empty Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset1", "Phenotypic dataset", "This is a Phenotypic Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset2a", "Biparental Genotypic Dataset", "This is a Biparental Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset2b1", "Frequency Genotypic dataset", "This is a Frequency Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset2b2", "Frequency Alt Genotypic dataset", "This is a Alternative Frequency Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset2c", "Diploid Genotypic  Dataset", "This is a Diploid Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset2d", "Homozygous Genotypic  Dataset", "This is a Homozygous Genotypic Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset3", "Distances Dataset", "This is a Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset4a", "Phenotypic and Biparental Genotypic dataset", "This is a Phenotypic and Biparental Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset4b", "Phenotypic and Frequency Genotypic dataset", "This is a Phenotypic and Frequency Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset4c", "Phenotypic and Diploid Genotypic dataset", "This is a Phenotypic and Diploid Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset4d", "Phenotypic and Homozygous Genotypic dataset", "This is a Phenotypic and Homozygous Genotypic Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset5", "Phenotypic and Distances dataset","This is a Phenotypic and Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset6a", "Distances and Biparental Genotypic dataset", "This is a Distances and Biparental Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset6b", "Distances and Frequency Genotypic dataset", "This is a Distances and Frequency Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset6c", "Distances and Diploid Genotypic dataset", "This is a Distances and Diploid Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset6d", "Distances and Homozygous Genotypic dataset", "This is a Distances and Homozygous Genotypic Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset7a", "Phenotypic, Distances and Biparental Genotypic dataset", "This is a Phenotypic, Distances and Biparental Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset7b", "Phenotypic, Distances and Frequency Genotypic dataset", "This is a Phenotypic, Distances and Frequency Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset7c", "Phenotypic, Distances and Diploid Genotypic dataset", "This is a Phenotypic, Distances and Diploid Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset7d", "Phenotypic, Distances and Homozygous Genotypic dataset", "This is a Phenotypic, Distances and Homozygous Genotypic Dataset!")) ;
        
        Path phenotypc_data = createTempFile(PHENOTYPIC_DATA) ;
        Path biparental_genotypic_data = createTempFile(BIPARENTAL_GENOTYPE_DATA) ;
        Path frequency_genotypic_data = createTempFile(FREQUENCY_GENOTYPE_DATA) ;
        Path frequency_alt_genotypic_data = createTempFile(FREQUENCY_ALT_GENOTYPE_DATA) ;
        Path diplod_genotypic_data = createTempFile(DIPLOID_GENOTYPE_DATA) ;
        Path homozyous_genotypic_data = createTempFile(HOMOZYGOUS_GENOTYPE_DATA) ;
        Path distance_data = createTempFile(DISTANCES_DATA) ;
        
        loadData(getDataset("dataset1"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        
        /*loadData(getDataset("dataset2a"), biparental_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.BIPARENTAL);
        loadData(getDataset("dataset2b1"), frequency_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.FREQUENCY);
        loadData(getDataset("dataset2b2"), frequency_alt_genotypic_data, FileType.TXT, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.FREQUENCY);   
        loadData(getDataset("dataset2c"), diplod_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);       
        loadData(getDataset("dataset2d"), homozyous_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);
        
        loadData(getDataset("dataset3"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        
        loadData(getDataset("dataset4a"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset4a"), biparental_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.BIPARENTAL);
        loadData(getDataset("dataset4b"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset4b"), frequency_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.FREQUENCY);
        loadData(getDataset("dataset4c"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset4c"), diplod_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);    
        loadData(getDataset("dataset4d"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset4d"), homozyous_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);
        
        loadData(getDataset("dataset5"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset5"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        
        loadData(getDataset("dataset6a"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset6a"), biparental_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.BIPARENTAL);
        loadData(getDataset("dataset6b"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset6b"), frequency_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.FREQUENCY);
        loadData(getDataset("dataset6c"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset6c"), diplod_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);    
        loadData(getDataset("dataset6d"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset6d"), homozyous_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);
        
        loadData(getDataset("dataset7a"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset7a"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset7a"), biparental_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.BIPARENTAL);
        loadData(getDataset("dataset7b"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset7b"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset7b"), frequency_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.FREQUENCY);
        loadData(getDataset("dataset7c"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset7c"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset7c"), diplod_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);    */
        loadData(getDataset("dataset7d"), distance_data, FileType.CSV, CoreHunterDataType.DISTANCES);
        loadData(getDataset("dataset7d"), phenotypc_data, FileType.CSV, CoreHunterDataType.PHENOTYPIC);
        loadData(getDataset("dataset7d"), homozyous_genotypic_data, FileType.CSV, CoreHunterDataType.GENOTYPIC, GenotypeDataFormat.DEFAULT);
    }


    private Path createTempFile(String file) throws IOException {
        InputStream stream = FileBasedDatasetServicesMock.class.getResourceAsStream(file) ;
        
        if (stream == null) {
            throw new IOException(String.format("Can not open stream for %s",file)) ;
        }
        
        Path path = Files.createTempFile(getPath(), null, null) ;
        
        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING) ;
        
        stream.close();  

        return path;
    }
}
