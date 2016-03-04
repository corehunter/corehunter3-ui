package org.corehunter.ui;

import javax.inject.Inject;

import org.corehunter.services.DatasetClient;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import uno.informatics.data.Dataset;

public class DatasetTable 
{

  private DatasetComparator comparator;

  private TableViewer viewer;

  private DatasetClient datasetClient;
  
  @Inject
  public DatasetTable() 
  {
    BundleContext bundleContext = 
        FrameworkUtil.
        getBundle(this.getClass()).
        getBundleContext(); 

    ServiceReference<?> serviceReference = bundleContext.
        getServiceReference(DatasetClient.class.getName());
    setDatasetClient((DatasetClient) bundleContext.
        getService(serviceReference)); 

  }

  public void createPartControl(Composite parent) {
    GridLayout layout = new GridLayout(2, false);
    parent.setLayout(layout);
    Label searchLabel = new Label(parent, SWT.NONE);
    searchLabel.setText("Search: ");
    final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
    searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
        | GridData.HORIZONTAL_ALIGN_FILL));
    createViewer(parent);
    // Set the sorter for the table
    comparator = new DatasetComparator();
    viewer.setComparator(comparator);

  }

  private void createViewer(Composite parent) {
    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
    createColumns(parent, viewer);
    final Table table = viewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    viewer.setContentProvider(new ArrayContentProvider());
    
    updateViewer() ;

    GridData gridData = new GridData();
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;
    viewer.getControl().setLayoutData(gridData);
  }

  private void updateViewer()
  {
    viewer.setInput(datasetClient.getAllDatasets());
  }

  // This will create the columns for the table
  private void createColumns(final Composite parent, final TableViewer viewer) {
    String[] titles = { "Name", "Description" };
    int[] bounds = { 100, 200};

    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
    col.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        Dataset dataset = (Dataset) element;
        return dataset.getName();
      }
    });

    col = createTableViewerColumn(titles[1], bounds[1], 1);
    col.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        Dataset dataset = (Dataset) element;
        return dataset.getDescription();
      }
    });

  }

  private TableViewerColumn createTableViewerColumn(String title, int bound,
      final int colNumber) {
    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
        SWT.NONE);
    final TableColumn column = viewerColumn.getColumn();
    column.setText(title);
    column.setWidth(bound);
    column.setResizable(true);
    column.setMoveable(true);
    column.addSelectionListener(getSelectionAdapter(column, colNumber));
    return viewerColumn;
  }

  private SelectionAdapter getSelectionAdapter(final TableColumn column,
      final int index) {
    SelectionAdapter selectionAdapter = new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        comparator.setColumn(index);
        int dir = comparator.getDirection();
        viewer.getTable().setSortDirection(dir);
        viewer.getTable().setSortColumn(column);
        viewer.refresh();
      }
    };
    return selectionAdapter;
  }

  
/**
   * Passing the focus request to the viewer's control.
   */

  public void setFocus() {
    viewer.getControl().setFocus();
  }

  private synchronized final void setDatasetClient(DatasetClient datasetClient)
  {
    this.datasetClient = datasetClient;
  } 
 
} 