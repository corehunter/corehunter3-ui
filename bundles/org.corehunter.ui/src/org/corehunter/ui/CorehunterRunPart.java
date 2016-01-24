 
package org.corehunter.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;

public class CorehunterRunPart {
  private DatasetTable datasetTable;
	@Inject
	public CorehunterRunPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
	  
	  parent.setLayout(new GridLayout(1, false));
	  
	  Group grpParameters = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
	  grpParameters.setLayout(new FillLayout(SWT.HORIZONTAL));
	  grpParameters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	  grpParameters.setText("Core Size");
	  
	  Label lblSelect = new Label(grpParameters, SWT.NONE);
	  lblSelect.setText("Select by");
	  
	  Combo combo = new Combo(grpParameters, SWT.NONE);
	  combo.setItems(new String[] {"Size", "Range", "Intensity"});
	  
	  Label lblMin = new Label(grpParameters, SWT.NONE);
	  lblMin.setText("Min");
	  
	  Spinner spinner_1 = new Spinner(grpParameters, SWT.BORDER);
	  
	  Label lblMax = new Label(grpParameters, SWT.NONE);
	  lblMax.setText("Max");
	  
	  Spinner spinner = new Spinner(grpParameters, SWT.BORDER);
	  
	  Label lblIntensity = new Label(grpParameters, SWT.NONE);
	  lblIntensity.setText("Intensity");
	  
	  Spinner spinner_2 = new Spinner(grpParameters, SWT.BORDER);
	  
	  Group grpDatasets = new Group(parent, SWT.NONE);
	  grpDatasets.setText("Available Datasets");
	  grpDatasets.setLayout(new FillLayout(SWT.HORIZONTAL));
	  grpDatasets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
	  
    datasetTable = new DatasetTable() ;
    
    datasetTable.createPartControl(grpDatasets) ;
	  
	  Composite composite = new Composite(parent, SWT.NONE);
	  composite.setLayout(new GridLayout(2, false));
	  composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	  
	  Button btnNewButton = new Button(composite, SWT.NONE);
	  btnNewButton.setText("Start");
	  
	  Button btnNewButton_1 = new Button(composite, SWT.NONE);
	  btnNewButton_1.setText("Reset");
		
	}
}