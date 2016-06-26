package org.corehunter.ui;

import org.eclipse.jface.viewers.CellLabelProvider;

public class ObjectArrayMatrixDataViewer extends ArrayMatrixDataViewer<Object> {

    @Override
    protected CellLabelProvider createLabelProvider(int columnIndex) {
        return new ObjectMatrixDataLabelProvider(columnIndex);
    }  
}
