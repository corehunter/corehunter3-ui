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
