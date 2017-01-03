package org.corehunter.ui;

import static org.corehunter.ui.Constants.FILE_TYPES;
import static org.corehunter.ui.Constants.FILE_TYPE_NAMES;
import static org.corehunter.ui.Constants.GENOTYPE_DATA_FORMATS;
import static org.corehunter.ui.Constants.GENOTYPE_DATA_FORMAT_NAMES;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.corehunter.data.GenotypeDataFormat;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uno.informatics.data.io.FileType;

public class ExportDataDialog extends TitleAreaDialog {

	public static final int NONE = -1;
	public static final int ALL_ROWS = 0;
	public static final int ALL_ROWS_WITH_SELECTION = 1;
	public static final int SELECTED_ROWS_ONLY = 3;
	
	private Button btnAllRows;
	private Button btnAllRowsWith;
	private Button btnSelectedRows;
	
	private String phenotypeDataPath = null ;
	
    private Text phenotypeDataText;
    private FileType phenotypeFileType;
    private Combo phenotypeFileTypeCombo;

    private String genotypeDataPath = null ;
    
    private Text genotypeDataText;
    private FileType genotypeFileType;
    private Combo genotypeFileTypeCombo;
    private Combo genotypeDataFormatCombo;
    private GenotypeDataFormat genotypeDataFormat;
    
    private String distancesDataPath = null ;
  
    private Text distancesDataText;
    private FileType distancesFileType;
    private Combo distancesFileTypeCombo;

   	boolean hasPhenotypeData ;
	boolean hasGenotypeData ;
	boolean hasDistancesData ;
	
    private String[] validGenotypeDataFormatNames;
    
	private int format = NONE ;
	
	public ExportDataDialog(Shell parentShell, boolean hasPhenotypeData, boolean hasGenotypeData, 
			boolean hasDistancesData, GenotypeDataFormat... validGenotypeDataFormats) {
		super(parentShell);
		
		this.hasPhenotypeData = hasPhenotypeData ;
		this.hasGenotypeData = hasGenotypeData ;
		this.hasDistancesData = hasDistancesData ;
		
		if (validGenotypeDataFormats != null && validGenotypeDataFormats.length > 0) {
			validGenotypeDataFormatNames = new String[validGenotypeDataFormats.length] ;
	        
	        for (int i = 0 ; i < validGenotypeDataFormats.length ; ++i) {
	        	validGenotypeDataFormatNames[i] = validGenotypeDataFormats[i].name() ;
	        }
		} else {
			validGenotypeDataFormatNames = GENOTYPE_DATA_FORMAT_NAMES ;
		}

	}

	@Override
	public void create() {
		super.create();
		setTitle("Export format?");
		setMessage("Please selected the output format and file path(s)", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		Group buttonGroup = new Group(area, SWT.NONE);
		buttonGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		buttonGroup.setLayout(new GridLayout(1, false));
		buttonGroup.setText("Export format");

		btnAllRows = new Button(buttonGroup, SWT.RADIO);
		btnAllRows.setText("All Rows");

		btnAllRowsWith = new Button(buttonGroup, SWT.RADIO);
		btnAllRowsWith.setText("All Rows with extra column for selection");

		btnSelectedRows = new Button(buttonGroup, SWT.RADIO);
		btnSelectedRows.setText("Selected Rows only");
		
		btnAllRows.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				format = ALL_ROWS ;
				updateButtons();
			}
		});
		
		btnAllRowsWith.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				format = ALL_ROWS_WITH_SELECTION ;
				updateButtons();
			}
		});
		
		btnSelectedRows.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				format = SELECTED_ROWS_ONLY ;
				updateButtons();
			}
		});
		
		Group filePathComposite = new Group(area, SWT.NONE);
		filePathComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		filePathComposite.setLayout(new GridLayout(6, false));
		filePathComposite.setText("Export file Paths");
		
        if (hasPhenotypeData) {
            
	        Label phenotypicDataLabel = new Label(filePathComposite, SWT.NONE);
	        phenotypicDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	        phenotypicDataLabel.setText("Phenotypic Data File");
	
	        phenotypeDataText = new Text(filePathComposite, SWT.BORDER);
	        phenotypeDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	        phenotypeDataText.setEnabled(false) ;
	
	        Button phenotypicDataButton = new Button(filePathComposite, SWT.NONE);
	        phenotypicDataButton.setText("Select File");
	        phenotypicDataButton.addSelectionListener(new SelectionAdapter() {
	
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                openPhenotypicFileSelection();
	            }
	        });
	        
	        phenotypeFileTypeCombo = new Combo(filePathComposite, SWT.NONE);
	        phenotypeFileTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        phenotypeFileTypeCombo.setItems(FILE_TYPE_NAMES);
	        phenotypeFileTypeCombo.select(0);
	        phenotypeFileTypeCombo.addModifyListener(new ModifyListener() {
	            @Override
	            public void modifyText(ModifyEvent e) {
	                phenotypicDataTypeComboUpdated() ;
	            }});
	        
	        new Label(filePathComposite, SWT.NONE);
        }
        
        if (hasGenotypeData) {
        
	        Label genotypeDataLabel = new Label(filePathComposite, SWT.NONE);
	        genotypeDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	        genotypeDataLabel.setText("Genotypic Data File");
	
	        genotypeDataText = new Text(filePathComposite, SWT.BORDER);
	        genotypeDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	        genotypeDataText.setEnabled(false);
	        
	        Button genotypeDataButton = new Button(filePathComposite, SWT.NONE);
	        genotypeDataButton.setText("Select File");
	        genotypeDataButton.addSelectionListener(new SelectionAdapter() {
	
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                openGenotypicFileSelection();
	            }
	        });
	     
	        genotypeFileTypeCombo = new Combo(filePathComposite, SWT.NONE);
	        genotypeFileTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        genotypeFileTypeCombo.setItems(FILE_TYPE_NAMES);
	        genotypeFileTypeCombo.select(0);
	        genotypeFileTypeCombo.addModifyListener(new ModifyListener() {
	            @Override
	            public void modifyText(ModifyEvent e) {
	            	genotypicDataTypeComboUpdated() ;
	            }});
	        
	        genotypeDataFormatCombo = new Combo(filePathComposite, SWT.NONE);
	        genotypeDataFormatCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        
	        genotypeDataFormatCombo.setItems(validGenotypeDataFormatNames);
	        genotypeDataFormatCombo.select(0);
	        genotypeDataFormatCombo.addModifyListener(new ModifyListener() {
	            @Override
	            public void modifyText(ModifyEvent e) {
	                genotypeDataFormatComboUpdated() ;
	            }});
        }

        if (hasDistancesData) {
	        Label distancesDataLabel = new Label(filePathComposite, SWT.NONE);
	        distancesDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	        distancesDataLabel.setText("Distances Data File");
	
	        distancesDataText = new Text(filePathComposite, SWT.BORDER);
	        distancesDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	        distancesDataText.setEnabled(false);
	        
	        Button distancesDataButton = new Button(filePathComposite, SWT.NONE);
	        distancesDataButton.setText("Select File");
	        distancesDataButton.addSelectionListener(new SelectionAdapter() {
	
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                openDistancesFileSelection();
	            }
	        });
	        
	        distancesFileTypeCombo = new Combo(filePathComposite, SWT.NONE);
	        distancesFileTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        distancesFileTypeCombo.setItems(FILE_TYPE_NAMES);
	        distancesFileTypeCombo.select(0);
	        new Label(filePathComposite, SWT.NONE);
	
	        distancesFileTypeCombo.addModifyListener(new ModifyListener() {
	            @Override
	            public void modifyText(ModifyEvent e) {
	                distancesDataTypeComboUpdated() ;
	            }});
        }
        
		return area;
	}

	public final int getFormat() {
		return format ;
	}
	
    public final Path getPhenotypeDataPath() {
		if (phenotypeDataPath != null) {
			return Paths.get(phenotypeDataPath); 
		} else {
			return null ;
		}
	}

	public final FileType getPhenotypeFileType() {
		return phenotypeFileType;
	}

	public final Path getGenotypeDataPath() {
		if (genotypeDataPath != null) {
			return Paths.get(genotypeDataPath); 
		} else {
			return null ;
		}
	}

	public final FileType getGenotypeFileType() {
		return genotypeFileType;
	}

	public final GenotypeDataFormat getGenotypeDataFormat() {
		return genotypeDataFormat;
	}

	public final Path getDistancesDataPath() {
		if (distancesDataPath != null) {
			return Paths.get(distancesDataPath); 
		} else {
			return null ;
		}
	}

	public final FileType getDistancesFileType() {
		return distancesFileType;
	}

	protected void openPhenotypicFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);

        fileDialog.setText("Select Phenotypic Data File");

        String selected = fileDialog.open();
        
        if (selected != null) {
            phenotypeDataText.setText(selected);
            phenotypeDataPath = selected ;
        } else {
            phenotypeDataText.setText("");
            phenotypeDataPath = null ;      
        }
        
        updateButtons() ;
    }
    
    protected void openGenotypicFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);

        fileDialog.setText("Select Genotypic Data File");

        String selected = fileDialog.open();
        
        if (selected != null) {
            genotypeDataText.setText(selected);
            genotypeDataPath = selected ;
        } else {
            genotypeDataText.setText("");
            genotypeDataPath = null ;      
        }
        
        updateButtons() ;
    }
    
    protected void openDistancesFileSelection() {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);

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
        if (phenotypeFileTypeCombo.getSelectionIndex() >= 0) {
            phenotypeFileType = FILE_TYPES[phenotypeFileTypeCombo.getSelectionIndex()] ;
        } else {
            phenotypeFileType =  null ;
        }
    }
    
    private void genotypicDataTypeComboUpdated() {
        if (genotypeFileTypeCombo.getSelectionIndex() >= 0) {
            genotypeFileType = FILE_TYPES[genotypeFileTypeCombo.getSelectionIndex()] ;
        } else {
            genotypeFileType =  null ;
        }   
    }

    private void genotypeDataFormatComboUpdated() {
        if (genotypeDataFormatCombo.getSelectionIndex() >= 0) {
            genotypeDataFormat = GenotypeDataFormat.valueOf(validGenotypeDataFormatNames[genotypeDataFormatCombo.getSelectionIndex()]) ;
        } else {
            genotypeDataFormat =  null ;
        }   
    }

    private void distancesDataTypeComboUpdated() {
        if (distancesFileTypeCombo.getSelectionIndex() >= 0) {
            distancesFileType = FILE_TYPES[distancesFileTypeCombo.getSelectionIndex()] ;
        } else {
            distancesFileType =  null ;
        } 
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        
        updateButtons() ;
    }

	protected void updateButtons() {
        Button button = getButton(OK) ;
        
        boolean enabled = hasPhenotypeData || hasGenotypeData || hasDistancesData ;
        
        if (enabled && hasPhenotypeData) {
        	enabled = phenotypeDataPath != null ;
        }
        
        if (enabled && hasGenotypeData) {
        	enabled = genotypeDataPath != null ;
        }
        
        if (enabled && hasDistancesData) {
        	enabled = distancesDataPath != null ;
        }

		this.getButton(OK).setEnabled(enabled);
	}

}
