package org.corehunter.ui;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgressMonitorBar extends NullProgressMonitor {

	private ProgressBar progressBar;

	public void createPartControl(Composite parent) {
    	progressBar = new ProgressBar(parent, SWT.HORIZONTAL) ;  	
    }
	
    @Override
	public void beginTask(String name, int totalWork) {
    	progressBar.setSelection(0);
    	progressBar.setMaximum(totalWork);
	}

	@Override
	public void worked(int work) {
		System.out.println(work);
		progressBar.setSelection(progressBar.getSelection() + work);
	}
}
