/*******************************************************************************
 * Copyright 2016 Guy Davenport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
