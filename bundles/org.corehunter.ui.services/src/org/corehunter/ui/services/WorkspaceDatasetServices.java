package org.corehunter.ui.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.corehunter.services.simple.FileBasedDatasetServices;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;


public class WorkspaceDatasetServices extends FileBasedDatasetServices {

	public static final String DATASET_DIRECTORY = "datasets" ;
	
	public WorkspaceDatasetServices() throws IOException {
		super();
		
		Location instanceLocation = Platform.getInstanceLocation();
		
		try {
			Path path = Paths.get(Paths.get(instanceLocation.getURL().toURI()).toString(), DATASET_DIRECTORY) ;
			
			setPath(Files.createDirectories(path));
		} catch (URISyntaxException e) {
			throw new IOException("Can not create datasets directory", e) ;
		}
	}
}
