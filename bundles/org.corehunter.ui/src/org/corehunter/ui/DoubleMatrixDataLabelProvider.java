package org.corehunter.ui;

public class DoubleMatrixDataLabelProvider extends ArrayMatrixDataLabelProvider<Double> {

    public DoubleMatrixDataLabelProvider(int columnIndex) {
        super(columnIndex);
    }
    
    public String getText(Object element) {
        return super.getText(((Double[]) element)[getColumnIndex()]);
    }
}
