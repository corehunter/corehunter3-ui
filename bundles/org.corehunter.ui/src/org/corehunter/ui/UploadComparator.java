package org.corehunter.ui;

import org.corehunter.services.Upload;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class UploadComparator extends ViewerComparator {
  private int propertyIndex;
  private static final int DESCENDING = 1;
  private int direction = DESCENDING;

  public UploadComparator() {
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
    Upload upload1 = (Upload) e1;
    Upload upload2 = (Upload) e2;
    int rc = 0;
    switch (propertyIndex) {
    case 0:
      rc = upload1.getName().compareTo(upload2.getName());
      break;
    case 1:
      rc = upload1.getDate().compareTo(upload2.getDate());
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