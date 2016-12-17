package org.corehunter.ui;

import org.eclipse.core.runtime.IProgressMonitor;

public interface CoreHunterJobMonitor extends IProgressMonitor {

	public String getMonitorId() ;
	
	public void setMonitorId(String monitorId) ;
}
