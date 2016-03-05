package org.corehunter.ui;

import org.corehunter.services.CorehunterRunServices;
import org.corehunter.services.DatasetServices;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class CorehunterRunClient
{
  private CorehunterRunServices corehunterRunServices;
  
  public CorehunterRunClient()
  {
    BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    
    ServiceReference<?> serviceReference = bundleContext.getServiceReference(DatasetServices.class.getName());
    setCorehunterRunServices((CorehunterRunServices) bundleContext.getService(serviceReference));
  }
  
  public synchronized final CorehunterRunServices getDatasetServices()
  {
    return corehunterRunServices;
  }
  
  public synchronized final void setCorehunterRunServices(CorehunterRunServices corehunterRunServices)
  {
    this.corehunterRunServices = corehunterRunServices;
  }
}
