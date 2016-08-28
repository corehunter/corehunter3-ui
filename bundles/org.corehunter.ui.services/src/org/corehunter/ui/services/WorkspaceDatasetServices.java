package org.corehunter.ui.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	@Override
	protected Object readFromFile(Path path) throws IOException {
		ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path)) ;
		
		try {
			return objectInputStream.readObject() ;
		} catch (ClassNotFoundException e) {
			throw new IOException(e) ;
		}
	}

	@Override
	protected void writeToFile(Path path, Object object) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path)) ;
		
		objectOutputStream.writeObject(object) ;
	}	
}
