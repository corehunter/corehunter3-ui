
package org.corehunter.ui;

import org.corehunter.data.GenotypeDataFormat;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uno.informatics.data.Dataset;
import uno.informatics.data.io.FileType;
import uno.informatics.data.pojo.DatasetPojo;
import org.eclipse.swt.widgets.Combo;

public class CreateDatasetDialog extends TitleAreaDialog {
    
    private static final FileType[] FILE_TYPES = new FileType[] {FileType.CSV, FileType.TXT};
    private static final String[] FILE_TYPE_NAMES = new String[] {FileType.CSV.getName(), FileType.TXT.getName()};
    
    private static final GenotypeDataFormat[] GENOTYPE_DATA_FORMATS = new GenotypeDataFormat[] {GenotypeDataFormat.BIPARENTAL, GenotypeDataFormat.FREQUENCY, GenotypeDataFormat.DEFAULT};
    private static final String[] GENOTYPE_DATA_FORMAT_NAMES = new String[] {GenotypeDataFormat.BIPARENTAL.name(), GenotypeDataFormat.FREQUENCY.name(), GenotypeDataFormat.DEFAULT.name()};

    private Text datasetNameText;
    private Text datasetDescriptionText;
    private Text datasetAbbreviationText;
    private Text phenotypicDataText;
    private Text genotypicDataText;
    private Text distancesDataText;

    private DatasetPojo dataset;
    private String phenotypicDataPath;
    private String genotypicDataPath;
    private String distancesDataPath;
    private Combo phenotypicDataTypeCombo;
    private FileType phenotypicDataType;
    private FileType genotypicDataType;
    private FileType distancesDataType;
    private GenotypeDataFormat genotypeDataFormat;
    private Combo distancesDataTypeCombo;
    private Combo genotypicDataTypeCombo;
    private Combo genotypicDataFormatCombo;

    public CreateDatasetDialog(Shell parentShell) {
        super(parentShell);
   
    }

    /**
     * @see org.eclipse.jface.window.Window#create() We complete the dialog with
     *      a title and a message
     */
    public void create() {
        super.create();
        setTitle("Create new dataset!");
        setMessage("Please load at least one data file.");
    }

    public final Dataset getDataset() {
        return dataset;
    }

    public final String getPhenotypicDataPath() {
        return phenotypicDataPath;
    }

    public final String getGenotypicDataPath() {
        return genotypicDataPath;
    }

    public final String getDistancesDataPath() {
        return distancesDataPath;
    }

    public final FileType getPhenotypicDataType() {
        return phenotypicDataType;
    }

    public final FileType getGenotypicDataType() {
        return genotypicDataType;
    }

    public final FileType getDistancesDataType() {
        return distancesDataType;
    }

    public final GenotypeDataFormat getGenotypeDataFormat() {
        return genotypeDataFormat;
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#
     *      createDialogArea(org.eclipse.swt.widgets.Composite) Here we fill the
     *      center area of the dialog
     */
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite) super.createDialogArea(parent);

        Composite composite = new Composite(container, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 6;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label datasetNameLabel = new Label(composite, SWT.NONE);
        datasetNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        datasetNameLabel.setText("Dataset Name");

        datasetNameText = new Text(composite, SWT.BORDER);
        datasetNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        datasetNameText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                datasetUpdated() ;
            }});

        Label datasetDescriptionLabel = new Label(composite, SWT.NONE);
        datasetDescriptionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        datasetDescriptionLabel.setText("Dataset Description");

        datasetDescriptionText = new Text(composite, SWT.BORDER);
        datasetDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        datasetDescriptionText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                datasetUpdated() ;
            }});
        
        Label datasetAbbreviationLabel = new Label(composite, SWT.NONE);
        datasetAbbreviationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        datasetAbbreviationLabel.setText("Dataset Abbreviation");

        datasetAbbreviationText = new Text(composite, SWT.BORDER);
        datasetAbbreviationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        datasetAbbreviationText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                datasetUpdated() ;
            }});

        Label phenotypicDataLabel = new Label(composite, SWT.NONE);
        phenotypicDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        phenotypicDataLabel.setText("Phenotypic Data File");

        phenotypicDataText = new Text(composite, SWT.BORDER);
        phenotypicDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        phenotypicDataText.setEnabled(false) ;

        Button phenotypicDataButton = new Button(composite, SWT.NONE);
        phenotypicDataButton.setText("Upload File");
        phenotypicDataButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openPhenotypicFileSelection();
            }
        });
        
        phenotypicDataTypeCombo = new Combo(composite, SWT.NONE);
        phenotypicDataTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        phenotypicDataTypeCombo.setItems(FILE_TYPE_NAMES);
        phenotypicDataTypeCombo.select(0);
        phenotypicDataTypeCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                phenotypicDataTypeComboUpdated() ;
            }});
        
        new Label(composite, SWT.NONE);
        
        Label genotypicDataLabel = new Label(composite, SWT.NONE);
        genotypicDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        genotypicDataLabel.setText("Genotypic Data File");

        genotypicDataText = new Text(composite, SWT.BORDER);
        genotypicDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        genotypicDataText.setEnabled(false);
        
        Button genotypicDataButton = new Button(composite, SWT.NONE);
        genotypicDataButton.setText("Upload File");
        genotypicDataButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openGenotypicFileSelection();
            }
        });
        
        genotypicDataTypeCombo = new Combo(composite, SWT.NONE);
        genotypicDataTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        genotypicDataTypeCombo.setItems(FILE_TYPE_NAMES);
        genotypicDataTypeCombo.select(0);
        genotypicDataTypeCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                phenotypicDataTypeComboUpdated() ;
            }});
        
        genotypicDataFormatCombo = new Combo(composite, SWT.NONE);
        genotypicDataFormatCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        genotypicDataFormatCombo.setItems(GENOTYPE_DATA_FORMAT_NAMES);
        genotypicDataFormatCombo.select(0);
        genotypicDataFormatCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                genotypeDataFormatComboUpdated() ;
            }});

        Label distancesDataLabel = new Label(composite, SWT.NONE);
        distancesDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        distancesDataLabel.setText("Distances Data File");

        distancesDataText = new Text(composite, SWT.BORDER);
        distancesDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        distancesDataText.setEnabled(false);
        
        Button distancesDataButton = new Button(composite, SWT.NONE);
        distancesDataButton.setText("Upload File");
        distancesDataButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openDistancesFileSelection();
            }
        });
        
        distancesDataTypeCombo = new Combo(composite, SWT.NONE);
        distancesDataTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        distancesDataTypeCombo.setItems(FILE_TYPE_NAMES);
        distancesDataTypeCombo.select(0);
        distancesDataTypeCombo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                distancesDataTypeComboUpdated() ;
            }});
        
        new Label(composite, SWT.NONE);
        
        phenotypicDataTypeComboUpdated() ;
        genotypicDataTypeComboUpdated() ;
        distancesDataTypeComboUpdated() ;
        
        return container;
    }

    protected void datasetUpdated() {
        
        if (datasetNameText.getText() != null && datasetNameText.getText().length() > 0) {
            if (dataset != null) {
                dataset.setName(datasetNameText.getText());
            } else {
                dataset = new DatasetPojo(datasetNameText.getText()) ;
            }  
            
            if (datasetDescriptionText.getText() != null && datasetDescriptionText.getText().length() > 0) {
                dataset.setDescription(datasetDescriptionText.getText());
            }
            
            if (datasetAbbreviationText.getText() != null && datasetAbbreviationText.getText().length() > 0) {
                dataset.setAbbreviation(datasetAbbreviationText.getText());
            }
        } else {
            dataset = null ;
        }
        
        updateButtons() ;
    }

    protected void openPhenotypicFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell());

        fileDialog.setText("Select Phenotypic Data File");

        String selected = fileDialog.open();
        
        if (selected != null) {
            phenotypicDataText.setText(selected);
            phenotypicDataPath = selected ;
        } else {
            phenotypicDataText.setText("");
            phenotypicDataPath = null ;      
        }
        
        updateButtons() ;
    }
    
    protected void openGenotypicFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell());

        fileDialog.setText("Select Genotypic Data File");

        String selected = fileDialog.open();
        
        if (selected != null) {
            genotypicDataText.setText(selected);
            genotypicDataPath = selected ;
        } else {
            genotypicDataText.setText("");
            genotypicDataPath = null ;      
        }
        
        updateButtons() ;
    }
    
    protected void openDistancesFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell());

        fileDialog.setText("Select Distances Data File");

        String selected = fileDialog.open();
        
        if (selected != null) {
            distancesDataText.setText(selected);
            distancesDataPath = selected ;
        } else {
            distancesDataText.setText("");
            distancesDataPath = null ;      
        }
        
        updateButtons() ;
    }
    
    protected void phenotypicDataTypeComboUpdated() {
        if (phenotypicDataTypeCombo.getSelectionIndex() >= 0) {
            phenotypicDataType = FILE_TYPES[phenotypicDataTypeCombo.getSelectionIndex()] ;
        } else {
            phenotypicDataType =  null ;
        }
    }
    
    private void genotypicDataTypeComboUpdated() {
        if (genotypicDataTypeCombo.getSelectionIndex() >= 0) {
            genotypicDataType = FILE_TYPES[genotypicDataTypeCombo.getSelectionIndex()] ;
        } else {
            genotypicDataType =  null ;
        }   
    }

    private void genotypeDataFormatComboUpdated() {
        if (genotypicDataFormatCombo.getSelectionIndex() >= 0) {
            genotypeDataFormat = GENOTYPE_DATA_FORMATS[genotypicDataFormatCombo.getSelectionIndex()] ;
        } else {
            genotypeDataFormat =  null ;
        }   
    }

    private void distancesDataTypeComboUpdated() {
        if (distancesDataTypeCombo.getSelectionIndex() >= 0) {
            distancesDataType = FILE_TYPES[distancesDataTypeCombo.getSelectionIndex()] ;
        } else {
            distancesDataType =  null ;
        } 
    }

    
    private void updateButtons() {
        Button button = getButton(OK) ;
        
        button.setEnabled(datasetNameText.getText() != null && datasetNameText.getText().length() > 0 && 
                (phenotypicDataPath != null || genotypicDataPath != null || distancesDataPath != null));
    }
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        
        updateButtons() ;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 350);
    }
}