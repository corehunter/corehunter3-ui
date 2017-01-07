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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jamesframework.core.subset.SubsetSolution;
import org.osgi.framework.Bundle;

import uno.informatics.data.SimpleEntity;

public class HeaderViewer implements SolutionContainer {
	private SimpleEntityComparator comparator;

	private TableViewer viewer;

	protected SimpleEntity[] headers;

	protected SubsetSolution solution;

	private boolean editable;

	private boolean[] selected;

	private ArrayList<Integer> identifiers;

	private Image selectedIcon;

	private Image unselectedIcon;

	private SolutionChangedEventHandler solutionChangedEventHandler;

	private String toolTipText;

	public static final int ASCENDING = -1;
	public static final int DESCENDING = 1;

	public HeaderViewer() {

		solutionChangedEventHandler = new SolutionChangedEventHandler(this);
	}

	public final void addSolutionChangedListener(SolutionChangedListener listener) {
		solutionChangedEventHandler.addSolutionChangedListener(listener);
	}

	public final void removeSolutionChangedListener(SolutionChangedListener listener) {
		solutionChangedEventHandler.removeSolutionChangedListener(listener);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		searchLabel.setToolTipText("Searches by entry name");
		final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		searchText.setToolTipText("Start typing here and the entry will be filtered by what you type");

		createViewer(parent);

		SimpleEntityFilter filter = new SimpleEntityFilter();

		searchText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				filter.setSearchText(searchText.getText());
				viewer.refresh();
			}
		});

		viewer.addFilter(filter);

		// Set the sorter for the table
		comparator = new SimpleEntityComparator(ASCENDING, 1);

		int dir = comparator.getSortIndicatorDirection();
		viewer.getTable().setSortDirection(dir);
		viewer.getTable().setSortColumn(viewer.getTable().getColumn(1));

		viewer.setComparator(comparator);

		Bundle bundle = Platform.getBundle("org.corehunter.ui");

		URL url = FileLocator.find(bundle, new Path("icons/unselected.png"), null);

		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);

		selectedIcon = imageDesc.createImage();

		url = FileLocator.find(bundle, new Path("icons/selected.png"), null);

		imageDesc = ImageDescriptor.createFromURL(url);

		unselectedIcon = imageDesc.createImage();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public final SimpleEntity[] getHeaders() {
		return headers;
	}

	public final void setHeaders(SimpleEntity[] headers) {
		if (headers == null) {
			throw new NullPointerException("Headers can not be null!");
		}

		this.headers = headers;

		clearSolution();
		updateViewer();
	}

	@Override
	public SubsetSolution getSolution() {
		return solution;
	}

	@Override
	public final void setSolution(SubsetSolution solution) {
		if (solution == null) {
			throw new NullPointerException("Solution can not be null!");
		}

		if (headers.length != solution.getTotalNumIDs()) {
			throw new NullPointerException("Total number of ids must be same length as number of headers!");
		}

		this.solution = new SubsetSolution(solution);
		identifiers = new ArrayList<Integer>(solution.getAllIDs());

		selected = new boolean[identifiers.size()];

		Iterator<Integer> selectedIds = identifiers.iterator();

		for (int i = 0; i < identifiers.size(); ++i)
			selected[i] = this.solution.getSelectedIDs().contains(selectedIds.next());

		updateViewer();
	}

	@Override
	public void updateSelection(SolutionChangedEvent event) {

		Iterator<Integer> selectedIds = event.getSelected().iterator();

		while (selectedIds.hasNext()) {
			updateSelection(selectedIds.next(), true);
		}

		Iterator<Integer> unselectedIds = event.getUnselected().iterator();

		while (unselectedIds.hasNext()) {
			updateSelection(unselectedIds.next(), false);
		}

		updateViewer();
	}

	private void updateSelection(Integer id, boolean select) {
		int index = identifiers.indexOf(id);

		if (index >= 0 && index < selected.length) {
			selected[index] = select;
			if (select) {
				solution.deselect(id);
			} else {
				solution.select(id);
			}
		}
	}

	public final void setHeadersWithSolution(SimpleEntity[] headers, SubsetSolution solution) {
		if (headers == null) {
			throw new NullPointerException("Headers can not be null!");
		}

		if (solution == null) {
			throw new NullPointerException("Selected can not be null!");
		}

		if (headers.length != solution.getTotalNumIDs()) {
			throw new NullPointerException("Total number of ids must be same length as number of headers!");
		}

		this.headers = headers;
		this.solution = new SubsetSolution(solution);
		identifiers = new ArrayList<Integer>(solution.getAllIDs());

		selected = new boolean[identifiers.size()];

		Iterator<Integer> selectedIds = identifiers.iterator();

		for (int i = 0; i < identifiers.size(); ++i)
			selected[i] = this.solution.getSelectedIDs().contains(selectedIds.next());

		updateViewer();
	}

	public final boolean isEditable() {
		return editable;
	}

	public final void setEditable(boolean editable) {
		this.editable = editable;
	}

	public final void clearSolution() {
		Set<Integer> allIDs = new HashSet<Integer>(headers.length);

		for (int index = 0; index < headers.length; ++index) {
			allIDs.add(index);
		}

		solution = new SubsetSolution(allIDs);
		identifiers = new ArrayList<Integer>(solution.getAllIDs());
		selected = new boolean[headers.length];
	}

	/*
	 * public final void addSelectionChangedListener(ISelectionChangedListener
	 * listener) { if (viewer != null)
	 * viewer.addSelectionChangedListener(listener); }
	 * 
	 * public final void addDoubleClickListener(IDoubleClickListener listener) {
	 * if (viewer != null) viewer.addDoubleClickListener(listener); }
	 */

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.getTable().setToolTipText(toolTipText);

		viewer.setContentProvider(new ArrayContentProvider());

		updateViewer();

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public final void updateViewer() {
		viewer.setInput(identifiers);
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;

		if (viewer != null) {
			viewer.getTable().setToolTipText(toolTipText);
		}
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "X", "#", "ID", "Name" };
		String[] toolTipText = {
				"Black indicates the entry is selected, white it is unselected. Click to change selection (if editable)",
				"The entry number", "The ID of the entry", "The name of entry" };
		int[] bounds = { 40, 40, 150, 150 };

		TableViewerColumn col = createTableViewerColumn(titles[0], toolTipText[0], bounds[0], 0);

		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (selected[identifiers.indexOf(element)]) {
					return selectedIcon;
				} else {
					return unselectedIcon;
				}
			}
		});

		col.setEditingSupport(new SelectionEditingSupport(viewer));

		col = createTableViewerColumn(titles[1], toolTipText[1], bounds[1], 1);

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});

		col = createTableViewerColumn(titles[2], toolTipText[2], bounds[2], 2);

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return headers[identifiers.indexOf(element)].getUniqueIdentifier();
			}
		});

		col = createTableViewerColumn(titles[3], toolTipText[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return headers[identifiers.indexOf(element)].getName();
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, String toolTipText, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		column.setToolTipText(toolTipText);
		return viewerColumn;
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getSortIndicatorDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private class SimpleEntityFilter extends ViewerFilter {

		private String searchString;

		public void setSearchText(String s) {
			// ensure that the value can be used for matching
			this.searchString = ".*" + s + ".*";
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}

			SimpleEntity entity = headers[identifiers.indexOf(element)];
			if (entity.getName() != null && entity.getName().matches(searchString)) {
				return true;
			}

			if (entity.getUniqueIdentifier() != null && entity.getUniqueIdentifier().matches(searchString)) {
				return true;
			}

			return false;
		}
	}

	private class SelectionEditingSupport extends EditingSupport {

		private final TableViewer viewer;
		private final CheckboxCellEditor editor;

		public SelectionEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
			this.editor = new CheckboxCellEditor(viewer.getTable());
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected boolean canEdit(Object element) {
			return editable;
		}

		@Override
		protected Object getValue(Object element) {

			return selected[identifiers.indexOf(element)];
		}

		@Override
		protected void setValue(Object element, Object userInputValue) {
			Boolean select = (Boolean) userInputValue;
			selected[identifiers.indexOf(element)] = (Boolean) userInputValue;

			if (select) {
				solution.select((Integer) element);
				solutionChangedEventHandler.fireSelectionEvent((Integer) element);
			} else {
				solution.deselect((Integer) element);
				solutionChangedEventHandler.fireUnselectionEvent((Integer) element);
			}

			viewer.update(element, null);
		}
	}

	public class SimpleEntityComparator extends ViewerComparator {
		private int propertyIndex;

		private int direction;
		private int defaultDirection;

		public SimpleEntityComparator() {
			this(ASCENDING, 0);
		}

		public SimpleEntityComparator(int defaultDirection, int propertyIndex) {
			this.propertyIndex = propertyIndex;
			this.defaultDirection = defaultDirection;
			setDirection(defaultDirection);
		}

		public int getSortIndicatorDirection() {
			return direction == ASCENDING ? SWT.DOWN : SWT.UP;
		}

		private int setDirection(int direction) {
			if (direction < 0) {
				this.direction = ASCENDING;
			} else {
				this.direction = DESCENDING;
			}

			return getSortIndicatorDirection();
		}

		/**
		 * Sets the column to to sorted on and changes sort direction if the
		 * same column as last time is set.
		 * 
		 * @param column
		 */
		public void setColumn(int column) {
			if (column == this.propertyIndex) {
				// Same column as last sort; toggle the direction
				direction = -1 * direction;
			} else {
				// New column; do a sort on default direction
				this.propertyIndex = column;
				direction = defaultDirection;
			}
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				Boolean entityB1 = selected[identifiers.indexOf(e1)];
				Boolean entityB2 = selected[identifiers.indexOf(e2)];
				rc = entityB1.compareTo(entityB2);
				break;
			case 1:
				Integer entityI1 = identifiers.indexOf(e1);
				Integer entityI2 = identifiers.indexOf(e2);
				rc = entityI1.compareTo(entityI2);
				break;
			case 2:
				SimpleEntity entity1 = headers[identifiers.indexOf(e1)];
				SimpleEntity entity2 = headers[identifiers.indexOf(e2)];
				rc = entity1.getUniqueIdentifier().compareTo(entity2.getUniqueIdentifier());
				break;
			case 3:
				entity1 = headers[identifiers.indexOf(e1)];
				entity2 = headers[identifiers.indexOf(e2)];
				rc = entity1.getName().compareTo(entity2.getName());
				break;
			default:
				rc = 0;
			}
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;
		}

	}
}