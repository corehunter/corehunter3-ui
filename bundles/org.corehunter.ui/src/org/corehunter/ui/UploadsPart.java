package org.corehunter.ui;
 

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.corehunter.services.UploadClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public class UploadsPart 
{
  @SuppressWarnings("unused")
  private UploadClient uploadClient ;
  private UploadTable uploadTable;
  
	@Inject
	public UploadsPart() 
	{
	  BundleContext bundleContext = 
        FrameworkUtil.
        getBundle(this.getClass()).
        getBundleContext(); 

	  ServiceReference<?> serviceReference = bundleContext.
	      getServiceReference(UploadClient.class.getName());
	  setUploadClient((UploadClient) bundleContext.
	      getService(serviceReference)); 

	}
	
	@PostConstruct
	public void postConstruct(Composite parent) 
	{
	   parent.setLayout(new GridLayout(1, false));
	    
	    Group grpParameters = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
	    grpParameters.setLayout(new GridLayout(6, false));
	    grpParameters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    grpParameters.setText("Upload Dataset");
	    
	    Label typeLabel = new Label(grpParameters, SWT.NONE);
	    typeLabel.setText("Type");
	    
	    Combo typeCombo = new Combo(grpParameters, SWT.NONE);
	    typeCombo.setItems(new String[] {"Phenotypic", "Bi-allelic Genotypic", "Multi-allelic Genotypic"});
	    
	    Label fileLabel = new Label(grpParameters, SWT.NONE);
	    fileLabel.setText("File");
	    
	    Text fileText = new Text(grpParameters, SWT.BORDER) ;
	    fileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Button btnBrowse = new Button(grpParameters, SWT.NONE);
	    btnBrowse.setText("Browse...");
	    
	    Button btnUpload = new Button(grpParameters, SWT.NONE);
	    btnUpload.setText("Upload");
	    
	    Group grpUploads = new Group(parent, SWT.NONE);
	    grpUploads.setText("Uploaded Datasets");
	    grpUploads.setLayout(new FillLayout(SWT.HORIZONTAL));
	    grpUploads.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
	    
	    uploadTable = new UploadTable() ;
	    
	    uploadTable.createPartControl(grpUploads) ;
	}

  private synchronized final void setUploadClient(UploadClient uploadClient)
  {
    this.uploadClient = uploadClient;
  }	
}