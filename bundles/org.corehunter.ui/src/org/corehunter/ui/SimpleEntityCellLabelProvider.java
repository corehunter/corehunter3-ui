package org.corehunter.ui;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class SimpleEntityCellLabelProvider<ElementType> extends CellLabelProvider {

	@Override
	public void update(ViewerCell cell) {
		@SuppressWarnings("unchecked")
		String name = ((DataGridViewerRow<ElementType>)cell.getElement()).getHeader().getName() ;
        if (name != null) {
            cell.setText(name);
        }
	}
}
