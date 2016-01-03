package org.corehunter.ui;
 

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import uno.informatics.data.client.DatasetClient;

public class DatasetListPart 
{
  DatasetClient datasetClient ;
  
	@Inject
	public DatasetListPart() 
	{
	  BundleContext bundleContext = 
        FrameworkUtil.
        getBundle(this.getClass()).
        getBundleContext(); 

	  ServiceReference<?> serviceReference = bundleContext.
	      getServiceReference(DatasetClient.class.getName());
	  setDatasetClient((DatasetClient) bundleContext.
	      getService(serviceReference)); 

	}
	
	@PostConstruct
	public void postConstruct(Composite parent) 
	{
	  /*if (datasetClient != null)
	    System.out.println(datasetClient.getAllDatasets());
	  else
	    System.out.println("No client");*/
	}

  public synchronized final DatasetClient getDatasetClient()
  {
    return datasetClient;
  }

  public synchronized final void setDatasetClient(DatasetClient datasetClient)
  {
    this.datasetClient = datasetClient;
  }	
}