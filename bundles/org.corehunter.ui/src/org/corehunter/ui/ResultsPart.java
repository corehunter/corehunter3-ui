 
package org.corehunter.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.corehunter.services.CorehunterRunClient;
import org.corehunter.services.UploadClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ResultsPart {
  
  @SuppressWarnings("unused")
  private CorehunterRunClient resultClient;
  private CorehunterRunTable resultTable;
  
  @Inject
	public ResultsPart() {
    BundleContext bundleContext = 
        FrameworkUtil.
        getBundle(this.getClass()).
        getBundleContext(); 

    ServiceReference<?> serviceReference = bundleContext.
        getServiceReference(CorehunterRunClient.class.getName());
    setResultClient((CorehunterRunClient) bundleContext.
        getService(serviceReference)); 
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
	  
	   parent.setLayout(new GridLayout(1, false)) ;
	    
	    Group grpResults = new Group(parent, SWT.NONE);
	    grpResults.setText("Results");
	    grpResults.setLayout(new FillLayout(SWT.HORIZONTAL));
	    grpResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
	    
	    resultTable = new CorehunterRunTable() ;
      
	    resultTable.createPartControl(grpResults) ;
	}

  private synchronized final void setResultClient(CorehunterRunClient resultClient)
  {
    this.resultClient = resultClient;
  } 
	
}