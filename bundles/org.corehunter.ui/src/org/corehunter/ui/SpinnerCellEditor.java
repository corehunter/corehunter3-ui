package org.corehunter.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

public class SpinnerCellEditor extends CellEditor {
	
	private Spinner spinner ;
	private int minimum;
	private int maximum;
	
	public SpinnerCellEditor(Composite parent, int maximum) {
		this(parent, 0, maximum);
	}
	
	public SpinnerCellEditor(Composite parent, int minimum, int maximum) {
		super(parent, SWT.NONE);
		
		if (minimum >= maximum) {
			throw new IllegalArgumentException("Minimum must be less than maxiumn") ;
		}
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	protected Control createControl(Composite parent) {
		
		spinner = new Spinner(parent, getStyle()) ;
		spinner.setMinimum(minimum);
		spinner.setMaximum(maximum);

		return spinner;
	}

	@Override
	protected Object doGetValue() {
		return spinner.getSelection() ;
	}

	@Override
	protected void doSetFocus() {
		spinner.setFocus() ;
	}

	@Override
	protected void doSetValue(Object value) {
		spinner.setSelection((int)value) ;
	}
}
