
package org.corehunter.ui;

import org.corehunter.services.CoreHunterRunServices;
import org.corehunter.services.DatasetServices;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class CoreHunterRunClient {
    private CoreHunterRunServices corehunterRunServices;

    public CoreHunterRunClient() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<?> serviceReference = bundleContext.getServiceReference(DatasetServices.class.getName());
        setCorehunterRunServices((CoreHunterRunServices) bundleContext.getService(serviceReference));
    }

    public synchronized final CoreHunterRunServices getDatasetServices() {
        return corehunterRunServices;
    }

    public synchronized final void setCorehunterRunServices(CoreHunterRunServices corehunterRunServices) {
        this.corehunterRunServices = corehunterRunServices;
    }
}
