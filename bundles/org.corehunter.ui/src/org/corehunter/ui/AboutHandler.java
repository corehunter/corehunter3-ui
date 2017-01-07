 
package org.corehunter.ui;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		MessageDialog.openInformation(parentShell, 
				"About Core Hunter",
				"Core Hunter user interface\n" 
						+ "(c) Copyright Guy Davenport and Herman De Beukelaer 2009-2017.\n" 
						+ "All rights reserved.\n\n"
						+ "This product includes software developed by other open source projects\n"
						+ "including the Eclipse Foundation, Inc., https://www.eclipse.org/ \n"
						+ "and Apache Software Foundation, https://www.apache.org/.\n" 
						+ "For more details see http://www.corehunter.org");
	}
		
}