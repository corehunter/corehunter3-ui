package org.corehunter.ui;
import org.corehunter.services.CoreHunterRunServices;
import org.corehunter.services.DatasetServices;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    private static Activator activator;
    private CoreHunterRunServices corehunterRunServices;
    private DatasetServices datasetServices;

    @Override
    public void start(BundleContext context) throws Exception {
        
        activator = this ;
        
        ServiceReference<?> serviceReference = context.getServiceReference(DatasetServices.class.getName());
        
        if (serviceReference != null) {
            setDatasetServices((DatasetServices) context.getService(serviceReference));
        }
        
        serviceReference = context.getServiceReference(CoreHunterRunServices.class.getName());
        
        if (serviceReference != null) {
            setCorehunterRunServices((CoreHunterRunServices) context.getService(serviceReference));
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        setDatasetServices(null) ;
        setCorehunterRunServices(null) ;
    }
    
    
    public synchronized final CoreHunterRunServices getCoreHunterRunServices() {
        return corehunterRunServices;
    }

    public synchronized final void setCorehunterRunServices(CoreHunterRunServices corehunterRunServices) {
        this.corehunterRunServices = corehunterRunServices;
    }
    
    public synchronized final DatasetServices getDatasetServices() {
        return datasetServices;
    }

    public synchronized final void setDatasetServices(DatasetServices datasetServices) {
        this.datasetServices = datasetServices;
    }
    
    /**
     * Get the default activator.
     *
     * @return a BundleActivator
     */
    public static Activator getDefault() {
            return activator;
    }
}
