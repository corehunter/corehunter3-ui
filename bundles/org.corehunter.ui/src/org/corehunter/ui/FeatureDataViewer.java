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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.jamesframework.core.subset.SubsetSolution;

import uno.informatics.data.Feature;
import uno.informatics.data.SimpleEntity;
import uno.informatics.data.dataset.FeatureData;
import uno.informatics.data.dataset.FeatureDataRow;

/**
 * @author Guy Davenport
 *
 */
public class FeatureDataViewer implements SolutionContainer {
    private static final int MIM_COLUMN_SIZE = 5;

    private FeatureData data;
	private SubsetSolution solution;
	private ArrayList<Integer> identifiers;
	private List<String> headers;
	private List<String> selectedHeaders;
	
	private GridTableViewer gridViewer;
    private Map<Integer, GridViewerColumn> viewerColumns;
	
	private SolutionChangedEventHandler solutionChangedEventHandler;

    /**
     * @param parent
     * @param configuration
     */
    public FeatureDataViewer() {
        viewerColumns = new TreeMap<Integer, GridViewerColumn>() ;
		identifiers = new ArrayList<Integer>() ;
		headers = new ArrayList<String>() ;
		selectedHeaders = new ArrayList<String>() ;
		solutionChangedEventHandler = new SolutionChangedEventHandler(this) ;
    }
    
	public final void addSolutionChangedListener(SolutionChangedListener listener) {
		solutionChangedEventHandler.addSolutionChangedListener(listener) ;
	}
	
	public final void removeSolutionChangedListener(SolutionChangedListener listener) {
		solutionChangedEventHandler.removeSolutionChangedListener(listener) ;
	}

    public void setData(FeatureData data) {
    	
    	if (data == null) {
    		throw new NullPointerException("Data must be defined!") ;
    	}
    	
        if (!ObjectUtils.equals(this.data, data)) {
            this.data = data;
            
            int size = data.getRowCount() ;
            
    		Set<Integer> allIDs = new HashSet<Integer>(size);
    		
    		identifiers = new ArrayList<Integer>(size) ;

    		for (int index = 0; index < size; ++index) {
    			allIDs.add(index);
    			identifiers.add(index) ;
    		}

    		solution = new SubsetSolution(allIDs);
    		
    		headers  = new ArrayList<String>(data.getRowCount()) ;
    		
    		Iterator<SimpleEntity> iterator = data.getRowHeaders().iterator() ;
    		
    		while (iterator.hasNext()) {
    			headers.add(iterator.next().getUniqueIdentifier()) ;
    		}
    		
    		selectedHeaders = new ArrayList<String>() ;
        }
    }

    public void createPartControl(Composite parent) {
        gridViewer = new GridTableViewer(parent);

        gridViewer.setContentProvider(new FeatureDataContentProvider());
        gridViewer.setRowHeaderLabelProvider(new RowHeaderLabelProvider());
        gridViewer.getGrid().setHeaderVisible(true);
        gridViewer.getGrid().setRowHeaderVisible(true);

        updateGridViewer();
        
        gridViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @SuppressWarnings("unchecked")
			@Override
            public void selectionChanged(SelectionChangedEvent event) {
            	
            	if (event.getSelection() instanceof StructuredSelection) {
            		handleViewerSelectionChanged(((StructuredSelection)event.getSelection()).toList());
            	}
            }
        });
    }
    
    @Override
	public SubsetSolution getSolution() {
		return solution ;
	}  
	
    @Override
	public final void setSolution(SubsetSolution solution) {
    	
    	if (solution == null) {
    		throw new NullPointerException("Solution must be defined!") ;
    	}
    	
    	if (solution.getTotalNumIDs() != data.getRowCount()) {
    		throw new IllegalArgumentException("Solution size must match datasize!") ;
    	}
    	
		this.solution = new SubsetSolution(solution);
		
		// gives the order of the identifiers
		identifiers = new ArrayList<Integer>(solution.getAllIDs()) ;
		selectedHeaders = new ArrayList<String>(solution.getNumSelectedIDs()) ;
		
		Iterator<Integer> iterator = solution.getSelectedIDs().iterator() ;
		
		int[] indices = new int[solution.getNumSelectedIDs()] ;
		
		int i = 0 ;
		
		while(iterator.hasNext()) {
			indices[i] = identifiers.indexOf(iterator.next()) ;
			selectedHeaders.add(data.getHeader(indices[i]).getUniqueIdentifier()) ;
			++i ;
		}
		
		gridViewer.getGrid().deselectAll();
		gridViewer.getGrid().select(indices);
		
	}  

    @Override
	public void updateSelection(SolutionChangedEvent event) {
    	
		int[] selectedIndices = new int[event.getSelected().size()] ;
		
		Iterator<Integer> iterator1 = event.getSelected().iterator() ;
		
		int i = 0 ;
		Integer id ;
		
		while(iterator1.hasNext()) {
			id = iterator1.next() ;
			selectedIndices[i] = identifiers.indexOf(id) ;
			solution.select(id) ;
			++i ;
		}
		
		int[] unselectedIndices = new int[event.getUnselected().size()] ;
		
		Iterator<Integer> iterator2 = event.getUnselected().iterator() ;
		
		i = 0 ;
		
		while(iterator2.hasNext()) {
			id = iterator2.next() ;
			unselectedIndices[i] = identifiers.indexOf(id) ;
			solution.deselect(id) ;
			++i ;
		}
		
		gridViewer.getGrid().select(selectedIndices);
		gridViewer.getGrid().deselect(unselectedIndices);
	}

    protected void handleViewerSelectionChanged(List<Object> list) {
    	
    	Set<Integer> selected = new TreeSet<Integer>();
    	Set<Integer> unselected = new TreeSet<Integer>();
    	
		Iterator<Object> iterator1 = list.iterator() ;
		
		String rowHeader ;
		LinkedList<String> selectHeaders = new LinkedList<String>() ;
		LinkedList<String> deSelectHeaders = new LinkedList<String>(selectedHeaders) ;
		
		selectedHeaders = new ArrayList<String>(list.size()) ;
	
		while (iterator1.hasNext()) {
			rowHeader = ((FeatureDataRow) iterator1.next()).getHeader().getUniqueIdentifier() ;
			
			if (deSelectHeaders.contains(rowHeader)) {
				deSelectHeaders.remove(rowHeader) ;
			} else {
				selectHeaders.add(rowHeader) ;
			}
			
			selectedHeaders.add(rowHeader) ;
		}		
		
		Iterator<String> iterator2 = selectHeaders.iterator() ;
		
		while (iterator2.hasNext()) {
			selected.add(headers.indexOf(iterator2.next())) ;
		}
		
		iterator2 = deSelectHeaders.iterator() ;
		
		while (iterator2.hasNext()) {
			unselected.add(headers.indexOf(iterator2.next())) ;
		}
		
		solutionChangedEventHandler.fireMultipleEvent(selected, unselected); 
	}

    private void updateGridViewer() {
        if (data != null && !data.getFeatures().isEmpty()) {
            if (gridViewer != null) {
                int oldColumnCount = viewerColumns.size();
                int newColumnCount = data.getFeatures().size();

                // create new columns
                if (newColumnCount > oldColumnCount) {
                    for (int i = oldColumnCount; i < newColumnCount; ++i) {
                        viewerColumns.put(i, new GridViewerColumn(gridViewer, SWT.NONE, i));
                    }
                } else {
                    // remove columns
                    if (newColumnCount < oldColumnCount) {
                        for (int i = newColumnCount; i > oldColumnCount; --i) {
                            GridViewerColumn gridViewerColumn = viewerColumns.get(i - 1);

                            if (gridViewerColumn != null)
                                gridViewerColumn.getColumn().dispose();
                        }
                    }
                }

                Iterator<Integer> iterator = viewerColumns.keySet().iterator();
                GridViewerColumn gridViewerColumn;

                int index = 0;
                Feature feature;

                while (iterator.hasNext()) {
                    index = iterator.next();

                    feature = data.getFeatures().get(index);

                    gridViewerColumn = viewerColumns.get(index);

                    gridViewerColumn.getColumn().setText(feature.getName());

                    gridViewerColumn.setLabelProvider(new FeatureDataLabelProvider(feature, index));

                    gridViewerColumn.getColumn().setWidth(guessColumnWidth(feature.getName()));
                }

                gridViewer.setInput(data);
            }
        } else {
            if (gridViewer != null) {
                Iterator<GridViewerColumn> iterator = viewerColumns.values().iterator();

                while (iterator.hasNext())
                    iterator.next().getColumn().dispose();

                viewerColumns.clear();

                gridViewer.setInput(null);
            }
        }
    }

    private int guessColumnWidth(String columnHeader) {
        int size = 0;

        if (columnHeader != null && columnHeader.length() > 0)
            size = (columnHeader.length() * 10) + 10;

        if (size < MIM_COLUMN_SIZE)
            size = MIM_COLUMN_SIZE;

        return size;
    }
}
