package org.corehunter.ui.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.corehunter.data.DistanceMatrixData;
import org.corehunter.data.simple.SimpleDistanceMatrixData;
import org.corehunter.data.simple.SimpleGenotypeVariantData;
import org.corehunter.distance.GowersDistanceMatrixGenerator;
import org.corehunter.services.DataType;
import org.corehunter.services.simple.FileBasedDatasetServices;
import org.eclipse.core.runtime.FileLocator;

import uno.informatics.common.io.FileType;
import uno.informatics.data.Data;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.feature.array.ArrayFeatureData;
import uno.informatics.data.pojo.DatasetPojo;

public class FileBasedDatasetServicesMock extends FileBasedDatasetServices {

  private static final String PHENOTYPIC_DATA = "/phenotypic_data.csv";
  private static final String BIALLELIC_GENOTYPE_DATA = "/biallelic_genotypic_data.csv";
  private static final String DIPLOID_GENOTYPE_DATA = "/diploid_genotypic_data.csv";
  private static final String GENOTYPE_DATA = "/genotypic_data.csv";
  private static final String DISTANCES_DATA = "/distances_data.txt";

  
    public FileBasedDatasetServicesMock() throws DatasetException, IOException, URISyntaxException {
        super(Files.createTempDirectory(null));
        
        addDataset(new DatasetPojo("dataset0", "Empty dataset", "This is a empty Dataset!")) ;
        addDataset(new DatasetPojo("dataset1", "Phenotypic dataset", "This is a Phenotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset2", "Biallelic Genotypic Dataset", "This is a Biallelic Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset3", "Genotypic dataset", "This is a Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset4", "Distances Dataset", "This is a Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset5", "Phenotypic and Biallelic Genotypic dataset", "This is a Phenotypic and Biallelic Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset6", "Phenotypic and Genotypic dataset", "This is a Phenotypic and Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset7", "Phenotypic and Distances dataset","This is a Phenotypic and Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset8", "Biallelic Genotypic and Distances dataset", "This is a Phenotypic and Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset9", "Genotypic and Distances dataset", "This is a Genotypic and Distances Dataset!")) ;
        
        addDataset(new DatasetPojo("dataset10", "Genotypic, Distances and Phenotypic dataset", "This is a Genotypic, Distances and Genotypic Dataset!")) ;
        addDataset(new DatasetPojo("dataset11", "Biallelic Genotypic, Distances and Phenotypic dataset", "This is a Biallelic Genotypic, Distances and Genotypic Dataset!")) ;
    
        Path phenotypc_data = createTempFile(PHENOTYPIC_DATA) ;
        Path diplod_genotypic_data = createTempFile(DIPLOID_GENOTYPE_DATA) ;
        Path biallelic_genotypic_data = createTempFile(BIALLELIC_GENOTYPE_DATA) ;
        Path genotypic_data = createTempFile(GENOTYPE_DATA) ;
        Path distance_data = createTempFile(DISTANCES_DATA) ;
        
        //SimpleGenotypeVariantData data = SimpleGenotypeVariantData.readDiploidData(diplod_genotypic_data, FileType.CSV) ;
        
        //Files.delete(diplod_genotypic_data);
        
        //SimpleGenotypeVariantData.writeData(diplod_genotypic_data, data, FileType.CSV);
        
        //Path phenotype_data = createTempFile(PHENOTYPIC_DATA) ;
        
        //ArrayFeatureData data = ArrayFeatureData.readData(phenotype_data, FileType.CSV) ;
        
        //GowersDistanceMatrixGenerator generator = new GowersDistanceMatrixGenerator(data) ;
        
        //SimpleDistanceMatrixData distanceData = (SimpleDistanceMatrixData)generator.generateDistanceMatrix() ;
        
        //Files.delete(phenotype_data);
        
        //SimpleDistanceMatrixData.writeData(phenotype_data, distanceData, FileType.CSV); 
        
        loadData(getDataset("dataset1"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC);
        loadData(getDataset("dataset2"), biallelic_genotypic_data, FileType.CSV, DataType.BI_ALLELIC_GENOTYPIC);
        loadData(getDataset("dataset3"), genotypic_data, FileType.CSV, DataType.GENOTYPIC);
        loadData(getDataset("dataset4"), distance_data, FileType.CSV, DataType.DISTANCES);
        
        
        loadData(getDataset("dataset5"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC);
        //loadData(getDataset("dataset5"), biallelic_genotypic_data, FileType.CSV, DataType.BI_ALLELIC_GENOTYPIC);
        
        loadData(getDataset("dataset6"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC);
        loadData(getDataset("dataset6"), genotypic_data, FileType.CSV, DataType.GENOTYPIC);
        
        loadData(getDataset("dataset7"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC);   
        loadData(getDataset("dataset7"), distance_data, FileType.CSV, DataType.DISTANCES);
        
        //loadData(getDataset("dataset8"), biallelic_genotypic_data, FileType.CSV, DataType.BI_ALLELIC_GENOTYPIC);
        loadData(getDataset("dataset8"), distance_data, FileType.CSV, DataType.DISTANCES);
        
        loadData(getDataset("dataset9"), genotypic_data, FileType.CSV, DataType.GENOTYPIC);
        loadData(getDataset("dataset9"), distance_data, FileType.CSV, DataType.DISTANCES);     
        
        loadData(getDataset("dataset10"), genotypic_data, FileType.CSV, DataType.GENOTYPIC);
        loadData(getDataset("dataset10"), distance_data, FileType.CSV, DataType.DISTANCES);   
        loadData(getDataset("dataset10"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC); 
        
        //loadData(getDataset("dataset11"), biallelic_genotypic_data, FileType.CSV, DataType.BI_ALLELIC_GENOTYPIC);
        loadData(getDataset("dataset11"), distance_data, FileType.CSV, DataType.DISTANCES);   
        loadData(getDataset("dataset11"), phenotypc_data, FileType.CSV, DataType.PHENOTYPIC); 
    }


    private Path createTempFile(String file) throws IOException {
        InputStream stream = FileBasedDatasetServicesMock.class.getResourceAsStream(file) ;
        
        Path path = Files.createTempFile(getPath(), null, null) ;
        
        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING) ;
        
        stream.close();  

        return path;
    }
}
