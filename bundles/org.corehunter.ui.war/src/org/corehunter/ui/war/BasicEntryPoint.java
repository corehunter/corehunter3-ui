package org.corehunter.ui.war;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class BasicEntryPoint extends AbstractEntryPoint {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void createContents(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        Label checkbox = new Label(parent, SWT.PUSH);
        checkbox.setText("Application is running! Push button to run CoreHunter");
        Button button = new Button(parent, SWT.PUSH);
        button.setText("Core Hunter");
        
        button.addSelectionListener(new SelectionListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}});
    }

}
