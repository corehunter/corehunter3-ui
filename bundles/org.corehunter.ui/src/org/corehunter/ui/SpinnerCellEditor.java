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
