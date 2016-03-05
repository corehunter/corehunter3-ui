package org.corehunter.ui;

import org.corehunter.services.DatasetServices;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class DatasetServiceClient
{
  private DatasetServices datasetServices;
  
  public DatasetServiceClient()
  {
    BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    
    ServiceReference<?> serviceReference = bundleContext.getServiceReference(DatasetServices.class.getName());
    setDatasetServices((DatasetServices) bundleContext.getService(serviceReference));
  }

  public synchronized final DatasetServices getDatasetServices()
  {
    return datasetServices;
  }

  public synchronized final void setDatasetServices(DatasetServices datasetServices)
  {
    this.datasetServices = datasetServices;
  }
}
