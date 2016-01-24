package org.corehunter.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import uno.informatics.common.model.Dataset;

public class DatasetComparator extends ViewerComparator {
  private int propertyIndex;
  private static final int DESCENDING = 1;
  private int direction = DESCENDING;

  public DatasetComparator() {
    this.propertyIndex = 0;
    direction = DESCENDING;
  }

  public int getDirection() {
    return direction == 1 ? SWT.DOWN : SWT.UP;
  }

  public void setColumn(int column) {
    if (column == this.propertyIndex) {
      // Same column as last sort; toggle the direction
      direction = 1 - direction;
    } else {
      // New column; do an ascending sort
      this.propertyIndex = column;
      direction = DESCENDING;
    }
  }

  @Override
  public int compare(Viewer viewer, Object e1, Object e2) {
    Dataset dataset1 = (Dataset) e1;
    Dataset dataset2 = (Dataset) e2;
    int rc = 0;
    switch (propertyIndex) {
    case 0:
      rc = dataset1.getName().compareTo(dataset2.getName());
      break;
    case 1:
      rc = dataset1.getDescription().compareTo(dataset2.getDescription());
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