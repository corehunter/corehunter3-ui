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

import org.corehunter.services.CorehunterRunServices;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ResultsPart
{
  
  @SuppressWarnings("unused")
  private CorehunterRunServices corehunterRunServices;
  private CorehunterRunTable  resultTable;
  
  @Inject
  public ResultsPart()
  {
    BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    
    ServiceReference<?> serviceReference = bundleContext.getServiceReference(CorehunterRunServices.class.getName());
    setResultClient((CorehunterRunServices) bundleContext.getService(serviceReference));
  }
  
  @PostConstruct
  public void postConstruct(Composite parent)
  {  
    parent.setLayout(new GridLayout(1, false));
    
    Group grpResults = new Group(parent, SWT.NONE);
    grpResults.setText("Results");
    grpResults.setLayout(new GridLayout(1, false));
    grpResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    resultTable = new CorehunterRunTable();
    
    resultTable.createPartControl(grpResults);
  }
  
  private synchronized final void setResultClient(CorehunterRunServices corehunterRunServices)
  {
    this.corehunterRunServices = corehunterRunServices;
  }
  
}