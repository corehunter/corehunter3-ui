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
        } else {
            throw new IllegalStateException("Unable to get database service!") ;
        }
        
        serviceReference = context.getServiceReference(CoreHunterRunServices.class.getName());
        
        if (serviceReference != null) {
            setCorehunterRunServices((CoreHunterRunServices) context.getService(serviceReference));
        } else {
            throw new IllegalStateException("Unable to get Core Hunter service!") ;
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    	// TODO need to call shutdown
        setDatasetServices(null) ;
        setCorehunterRunServices(null) ;
    }
    
    
    public synchronized final CoreHunterRunServices getCoreHunterRunServices() {
        if (corehunterRunServices == null) {
            throw new IllegalStateException("Unable to get Core Hunter service!") ;
        }
        return corehunterRunServices;
    }

    public synchronized final void setCorehunterRunServices(CoreHunterRunServices corehunterRunServices) {
        this.corehunterRunServices = corehunterRunServices;
    }
    
    public synchronized final DatasetServices getDatasetServices() {
        if (datasetServices == null) {
            throw new IllegalStateException("Unable to get database service!") ;
        }
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
