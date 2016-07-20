package org.corehunter.ui.services;

import org.corehunter.services.simple.SimpleCoreHunterRunServices;
import org.corehunter.ui.Activator;

public class CoreHunterRunServicesBootstraped extends SimpleCoreHunterRunServices {

    public CoreHunterRunServicesBootstraped() {
        super(Activator.getDefault().getDatasetServices());
    }

}
