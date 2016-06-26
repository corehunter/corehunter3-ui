package org.corehunter.ui;

public class ObjectMatrixDataLabelProvider extends ArrayMatrixDataLabelProvider<String> {

    public ObjectMatrixDataLabelProvider(int columnIndex) {
        super(columnIndex);
    }
    
    public String getText(Object element) {
        return super.getText(((Object[]) element)[getColumnIndex()]);
    }
}
