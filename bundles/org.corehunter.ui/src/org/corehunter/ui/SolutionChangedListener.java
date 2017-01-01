package org.corehunter.ui;

public interface SolutionChangedListener {
	
	/**
	 * Called by a class making solution changes
	 * 
	 * @param event the event indicating the solution changes
	 */
	public void solutionChanged(SolutionChangedEvent event) ;
}
