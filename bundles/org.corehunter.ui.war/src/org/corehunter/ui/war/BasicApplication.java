package org.corehunter.ui.war;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.e4.E4ApplicationConfig;
import org.eclipse.rap.e4.E4EntryPointFactory;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.client.WebClient;


public class BasicApplication implements ApplicationConfiguration {

    public void configure(Application application) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebClient.PAGE_TITLE, "Core Hunter");
        application.addEntryPoint("/corehunter", new E4EntryPointFactory(E4ApplicationConfig.create("platform:/plugin/org.corehunter.ui/Application.e4xmi")), properties);
        application.addEntryPoint("/hello", BasicEntryPoint.class, properties);
        application.setOperationMode( OperationMode.SWT_COMPATIBILITY );
    }

}
