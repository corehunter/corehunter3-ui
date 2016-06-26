package org.corehunter.ui;

import org.eclipse.jface.viewers.CellLabelProvider;

public class DoubleArrayMatrixDataViewer extends ArrayMatrixDataViewer<Double> {

    @Override
    protected CellLabelProvider createLabelProvider(int columnIndex) {
        return new DoubleMatrixDataLabelProvider(columnIndex);
    }  
}
