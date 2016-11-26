package org.corehunter.ui.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.corehunter.services.simple.SimpleCoreHunterRunServices;
import org.corehunter.ui.Activator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class CoreHunterRunServicesBootstraped extends SimpleCoreHunterRunServices {

	public static final String RESULTS_DIRECTORY = "results" ;

    public CoreHunterRunServicesBootstraped() throws IOException {
        super(Activator.getDefault().getDatasetServices());
        
		Location instanceLocation = Platform.getInstanceLocation();
		
		try {
			Path path = Paths.get(Paths.get(instanceLocation.getURL().toURI()).toString(), RESULTS_DIRECTORY) ;
			
			setPath(Files.createDirectories(path));
		} catch (URISyntaxException e) {
			throw new IOException("Can not create datasets directory", e) ;
		}
    }

}
