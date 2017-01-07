 
package org.corehunter.ui;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import javax.inject.Named ;
import org.eclipse.e4.ui.services.IServiceConstants ;

public class PreferencesHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		MessageDialog.openInformation(parentShell, "No preferences", "This version does not have any preferences to set");
	}
		
}