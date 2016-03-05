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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class DatasetPart extends DatasetServiceClient
{
  private DatasetTable datasetTable;
  
  @Inject
  public DatasetPart()
  {
  }
  
  @PostConstruct
  public void postConstruct(Composite parent)
  {
    parent.setLayout(new GridLayout(1, false));
    
    Group grpParameters = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
    grpParameters.setLayout(new GridLayout(6, false));
    grpParameters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    grpParameters.setText("Add Dataset");
    
    Label typeLabel = new Label(grpParameters, SWT.NONE);
    typeLabel.setText("Type");
    
    Combo typeCombo = new Combo(grpParameters, SWT.NONE);
    typeCombo.setItems(new String[] { "Phenotypic", "Bi-allelic Genotypic", "Multi-allelic Genotypic" });
    
    Label fileLabel = new Label(grpParameters, SWT.NONE);
    fileLabel.setText("File");
    
    Text fileText = new Text(grpParameters, SWT.BORDER);
    fileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    
    Button btnBrowse = new Button(grpParameters, SWT.NONE);
    btnBrowse.setText("Browse...");
    
    Button btnUpload = new Button(grpParameters, SWT.NONE);
    btnUpload.setText("Add");
    
    Group grpUploads = new Group(parent, SWT.NONE);
    grpUploads.setText("Datasets");
    grpUploads.setLayout(new FillLayout(SWT.HORIZONTAL));
    grpUploads.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
    
    datasetTable = new DatasetTable();
    
    datasetTable.createPartControl(grpUploads);
    
    Group corehunterRunArgumentsGroup = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
    corehunterRunArgumentsGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
    corehunterRunArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    corehunterRunArgumentsGroup.setText("Core Hunter Arguments");
    
    Label lblMin = new Label(corehunterRunArgumentsGroup, SWT.NONE);
    lblMin.setText("Size");
    
    Spinner spinner1 = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
    
    Label lblIntensity = new Label(corehunterRunArgumentsGroup, SWT.NONE);
    lblIntensity.setText("Intensity");
    
    Spinner spinner_2 = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
    
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    
    Button btnNewButton = new Button(composite, SWT.NONE);
    btnNewButton.setText("Start");
    
    Button btnNewButton_1 = new Button(composite, SWT.NONE);
    btnNewButton_1.setText("Reset");
  }
}