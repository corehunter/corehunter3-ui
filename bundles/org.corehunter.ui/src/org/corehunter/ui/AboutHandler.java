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