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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SolutionChangedEventHandler {
	
	private Object source;
	private List<SolutionChangedListener> listeners ;
	
	public SolutionChangedEventHandler(Object source) {
		this.source = source ;
		listeners = new LinkedList<SolutionChangedListener>() ;
	}
	
	public void addSolutionChangedListener(SolutionChangedListener listener) {
		listeners.add(listener) ;
	}
	
	public void removeSolutionChangedListener(SolutionChangedListener listener) {
		listeners.remove(listener) ;
	}
	
	public final void fireSelectionEvent(Integer selected) {
		
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createSelectionEvent(source, selected));
		}
	}
	
	public final void fireUnselectionEvent(Integer unselected) {
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createUnselectionEvent(source, unselected));
		}
	}
	
	public final void fireMultipleEvent(Integer selected, Integer unselected) {
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createMultipleEvent(source, selected, unselected)) ;
		}
	}

	public final void fireSelectionEvent(Set<Integer> selected) {
		
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createSelectionEvent(source, selected));
		}
	}
	
	public final void fireUnselectionEvent(Set<Integer> unselected) {
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createUnselectionEvent(source, unselected));
		}
	}
	
	public final void fireMultipleEvent(Set<Integer> selected, Set<Integer> unselected) {
		Iterator<SolutionChangedListener> iterator = listeners.iterator() ;
		
		while (iterator.hasNext()) {
			iterator.next().solutionChanged(SolutionChangedEvent.createMultipleEvent(source, selected, unselected)) ;
		}
	}
}
