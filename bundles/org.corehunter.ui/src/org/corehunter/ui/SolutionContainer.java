package org.corehunter.ui;

import org.jamesframework.core.subset.SubsetSolution;

public interface SolutionContainer {
	
	/**
	 * Gets the solution held by this container
	 * @return the solution held by this container
	 */
	public SubsetSolution getSolution()  ;
	
	/**
	 * Sets the solution held by this container
	 * @param solution the solution held by this container
	 */
	public void setSolution(SubsetSolution solution)  ;
	
	/**
	 * Updates the solution held by this container
	 * @param event the event containing the updates
	 */
	public void updateSelection(SolutionChangedEvent event) ;
}
