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
