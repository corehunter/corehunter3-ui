package org.corehunter.ui.war;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;


public class BasicEntryPoint extends AbstractEntryPoint {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void createContents(Composite parent) {
        List list = new List( parent, SWT.NONE );
        list.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
        list.add( "Application is running! This link opens core hunter in <a href='corehunter' target='_blank'>a new tab</a>" );
    }

}
