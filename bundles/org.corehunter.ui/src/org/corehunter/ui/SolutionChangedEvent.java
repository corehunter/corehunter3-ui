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

import java.util.Set;
import java.util.TreeSet;

public class SolutionChangedEvent {
	
	private Object source;
	private TreeSet<Integer> selected;
	private TreeSet<Integer> unselected;
	
	private SolutionChangedEvent(Object source) {
		super();
		
		this.source = source;
	}
	
	private SolutionChangedEvent(Object source, Integer selected, Integer unselected) {
		this(source);
		
		this.selected = new TreeSet<Integer>() ;
		this.unselected = new TreeSet<Integer>() ;
		
		if (selected != null) {
			this.selected.add(selected) ;
		}
		
		if (unselected != null) {
			this.unselected.add(unselected) ;
		}
	}

	private SolutionChangedEvent(Object source, TreeSet<Integer> selected, TreeSet<Integer> unselected) {
		this(source);
		
		if (selected != null) {
			this.selected = new TreeSet<Integer>(selected) ;
		}
		
		if (unselected != null) {
			this.unselected = new TreeSet<Integer>(unselected);
		}
	}

	/**
	 * Gets the source of change event
	 * @return
	 */
	public Object getSource() {
		return source ;
	}

	/**
	 * Gets the ids selected in the event
	 * @return ids selected in the event or empty set if no
	 * selections
	 */
	public final Set<Integer> getSelected() {
		return selected;
	}

	/**
	 * Gets the ids unselected in the event
	 * @return ids unselected in the event or empty set if no
	 * un-selections
	 */
	public final Set<Integer> getUnselected() {
		return unselected;
	}
	
	public static final SolutionChangedEvent createSelectionEvent(Object source, Integer selected) {
		return new SolutionChangedEvent(source, selected, null) ;
	}
	
	public static final SolutionChangedEvent createUnselectionEvent(Object source, Integer unselected) {
		return new SolutionChangedEvent(source, null, unselected) ;
	}

	public static final SolutionChangedEvent createMultipleEvent(Object source, Integer selected, Integer unselected) {
		return new SolutionChangedEvent(source, selected, unselected) ;
	}
	
	public static final SolutionChangedEvent createSelectionEvent(Object source, Set<Integer> selected) {
		return new SolutionChangedEvent(source, new TreeSet<Integer>(selected), new TreeSet<Integer>()) ;
	}
	
	public static final SolutionChangedEvent createUnselectionEvent(Object source, Set<Integer> unselected) {
		return new SolutionChangedEvent(source, new TreeSet<Integer>(), new TreeSet<Integer>(unselected)) ;
	}
	
	public static final SolutionChangedEvent createMultipleEvent(Object source, Set<Integer> selected, Set<Integer> unselected) {
		return new SolutionChangedEvent(source, new TreeSet<Integer>(selected), new TreeSet<Integer>(unselected)) ;
	}
}
