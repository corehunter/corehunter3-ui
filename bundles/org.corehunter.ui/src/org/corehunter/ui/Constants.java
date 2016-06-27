
package org.corehunter.ui;

import org.corehunter.CoreHunterMeasure;
import org.corehunter.CoreHunterObjectiveType;
import org.corehunter.data.CoreHunterDataType;
import org.corehunter.data.GenotypeDataFormat;

import uno.informatics.data.io.FileType;

public class Constants {

    public static final CoreHunterDataType[] DATA_TYPES = new CoreHunterDataType[] { CoreHunterDataType.PHENOTYPIC,
            CoreHunterDataType.GENOTYPIC, CoreHunterDataType.DISTANCES };

    public static final String[] DATA_TYPE_NAMES = new String[] { CoreHunterDataType.PHENOTYPIC.getName(),
            CoreHunterDataType.GENOTYPIC.getName(), CoreHunterDataType.DISTANCES.getName() };

    public static final FileType[] FILE_TYPES = new FileType[] { FileType.CSV, FileType.TXT };
    public static final String[] FILE_TYPE_NAMES = new String[] { FileType.CSV.getName(), FileType.TXT.getName() };

    public static final GenotypeDataFormat[] GENOTYPE_DATA_FORMATS = new GenotypeDataFormat[] {
            GenotypeDataFormat.BIPARENTAL, GenotypeDataFormat.FREQUENCY, GenotypeDataFormat.DEFAULT };
    public static final String[] GENOTYPE_DATA_FORMAT_NAMES = new String[] { GenotypeDataFormat.BIPARENTAL.name(),
            GenotypeDataFormat.FREQUENCY.name(), GenotypeDataFormat.DEFAULT.name() };

    public static final CoreHunterObjectiveType[] CORE_HUNTER_OBJECTIVE_TYPES = new CoreHunterObjectiveType[] {
            CoreHunterObjectiveType.AV_ACCESSION_TO_NEAREST_ENTRY, CoreHunterObjectiveType.AV_ENTRY_TO_ENTRY,
            CoreHunterObjectiveType.AV_ENTRY_TO_NEAREST_ENTRY };
    
    public static final CoreHunterMeasure[] CORE_HUNTER_MEASURES = new CoreHunterMeasure[] {
            CoreHunterMeasure.GOWERS, CoreHunterMeasure.CAVALLI_SFORZA_EDWARDS, CoreHunterMeasure.MODIFIED_ROGERS, CoreHunterMeasure.PRECOMPUTED_DISTANCE};

}
