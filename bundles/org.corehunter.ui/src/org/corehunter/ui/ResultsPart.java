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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.services.CorehunterRun;
import org.corehunter.services.CorehunterRunServices;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ResultsPart {
    private CorehunterRunServices corehunterRunServices;
    private CorehunterRunTable resultTable;
    private Button btnRemove;
    private Button btnView;
    private Button btnClear;
    private CorehunterRun selectedCorehunterRun;

    @Inject
    public ResultsPart() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<?> serviceReference = bundleContext.getServiceReference(CorehunterRunServices.class.getName());
        setResultClient((CorehunterRunServices) bundleContext.getService(serviceReference));
    }

    @PostConstruct
    public void postConstruct(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        Group grpResults = new Group(parent, SWT.NONE);
        grpResults.setText("Results");
        grpResults.setLayout(new GridLayout(1, false));
        grpResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        resultTable = new CorehunterRunTable();

        resultTable.createPartControl(grpResults);

        resultTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                resultsSelectionChanged();
            }
        });

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        composite.setLayout(new GridLayout(3, false));

        btnRemove = new Button(composite, SWT.NONE);
        btnRemove.setText("Remove");
        btnRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeResult();
            }
        });

        btnView = new Button(composite, SWT.NONE);
        btnView.setText("View");
        btnView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewResult();
            }
        });

        btnClear = new Button(composite, SWT.NONE);
        btnClear.setText("Clear");
        btnClear.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clearSelection();
            }
        });

        updateButtons();
    }

    protected void resultsSelectionChanged() {
        selectedCorehunterRun = resultTable.getSelectedCorehunterRun();

        updateButtons();
    }

    private void updateButtons() {
        btnRemove.setEnabled(selectedCorehunterRun != null);
        btnView.setEnabled(selectedCorehunterRun != null);
        btnClear.setEnabled(selectedCorehunterRun != null);
    }

    private void updateViewer() {
        resultTable.updateViewer();
    }

    protected void clearSelection() {
        resultTable.cleaerSelectedCorehunterRun();
        selectedCorehunterRun = resultTable.getSelectedCorehunterRun();
        updateButtons();
    }

    protected void removeResult() {
        try {
            corehunterRunServices.removeCorehunterRun(resultTable.getSelectedCorehunterRun().getUniqueIdentifier());
            updateViewer();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            handleException(e);
        }
    }

    protected void viewResult() {
        // TODO Auto-generated method stub

    }

    private void handleException(Exception e) {
        // TODO Auto-generated method stub

    }

    private synchronized final void setResultClient(CorehunterRunServices corehunterRunServices) {
        this.corehunterRunServices = corehunterRunServices;
    }

}