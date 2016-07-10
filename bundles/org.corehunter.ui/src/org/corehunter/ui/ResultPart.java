
package org.corehunter.ui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.data.CoreHunterData;
import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jamesframework.core.subset.SubsetSolution;

import uno.informatics.data.Data;
import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;

public class ResultPart {

    public static final String ID = "org.corehunter.ui.part.result" ;

    private MPart part ;
    private PartInput partInput;
    private Text textName;
    private Text textStartDate;
    private Text textEndDate;

    private PartUtilitiies partUtilitiies;
    private ShellUtilitiies shellUtilitiies ;

    private HeaderViewer headerViewer;

    private Dataset dataset;

    private CoreHunterRun coreHunterRun;
    
    private Button btnRefreshResult;
    private Button btnViewDataset;
    private Button btnViewError;
    
    private Text logText;

    private TabFolder tabFolder;
    private TabItem runTabItem;
    private TabItem logTabItem;

    @Inject
    public ResultPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent, MPart part, EPartService partService, EModelService modelService,
            MApplication application) {
        
        this.part = part ;     
        partUtilitiies = new PartUtilitiies(partService, modelService, application);
        shellUtilitiies = new ShellUtilitiies(parent.getShell()) ;
        
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));

        tabFolder = new TabFolder(parent, SWT.NONE);

        runTabItem = new TabItem(tabFolder, SWT.NONE);
        runTabItem.setText("Result");

        Composite runComposite = new Composite(tabFolder, SWT.NONE);
        runTabItem.setControl(runComposite);
        runComposite.setLayout(new GridLayout(2, false));

        Label lblName = new Label(runComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        textName = new Text(runComposite, SWT.BORDER);
        textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblStartDate = new Label(runComposite, SWT.NONE);
        lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblStartDate.setText("Start");

        textStartDate = new Text(runComposite, SWT.BORDER);
        textStartDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblEndDate = new Label(runComposite, SWT.NONE);
        lblEndDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblEndDate.setText("End");

        textEndDate = new Text(runComposite, SWT.BORDER);
        textEndDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Composite buttonComposite = new Composite(runComposite, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        GridLayout gl_buttonComposite = new GridLayout(2, true);
        buttonComposite.setLayout(gl_buttonComposite);
        
        Composite leftComposite = new Composite(buttonComposite, SWT.NONE);
        leftComposite.setLayout(new GridLayout(2, false));
        
        Label lblStatusLabel = new Label(leftComposite, SWT.NONE);
        lblStatusLabel.setText("Status");
        
        btnViewError = new Button(leftComposite, SWT.NONE);
        btnViewError.setText("Error");

        btnViewError.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewError();
            }
        });

        Composite rightComposite = new Composite(buttonComposite, SWT.NONE);
        rightComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        rightComposite.setLayout(new GridLayout(2, false));

        btnViewDataset = new Button(rightComposite, SWT.NONE);
        btnViewDataset.setText("View Dataset");

        btnRefreshResult = new Button(rightComposite, SWT.NONE);
        btnRefreshResult.setText("Refresh");

        btnRefreshResult.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updatePart();
            }
        });

        btnViewDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewDataset();
            }
        });

        Group headerViewerComposite = new Group(runComposite, SWT.NONE);
        headerViewerComposite.setText("Selected");
        headerViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        headerViewer = new HeaderViewer();

        headerViewer.createPartControl(headerViewerComposite);

        partInput = (PartInput) part.getTransientData().get(PartUtilitiies.INPUT);
        
        logTabItem = new TabItem(tabFolder, SWT.NONE);
        logTabItem.setText("Log");

        Composite logComposite = new Composite(tabFolder, SWT.NONE);
        logTabItem.setControl(logComposite);
        logComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        logText = new Text(logComposite, SWT.BORDER);

        if (partInput != null) {
            coreHunterRun = Activator.getDefault().getCoreHunterRunServices()
                    .getCoreHunterRun(partInput.getUniqueIdentifier());
            
            updatePart() ;
        } else {
            shellUtilitiies.handleError("Can not find result", "The results was not found!");
        }
        
        tabFolder.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
                updatePart() ;
            }
          });
        
        partService.addPartListener(new IPartListener() {

            @Override
            public void partActivated(MPart part) {
                updatePart(part);
            }

            @Override
            public void partBroughtToTop(MPart part) {
                updatePart(part);
            }

            @Override
            public void partDeactivated(MPart part) {
            }

            @Override
            public void partHidden(MPart part) {
            }

            @Override
            public void partVisible(MPart part) {
                updatePart(part);
            }});
    }

    protected void updatePart(MPart part) {
       if (this.part == part) {
           updatePart(); 
       }
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(dataset, DatasetPart.ID));
    }
    
    private synchronized void updatePart() {

        if (tabFolder.getSelection().length > 0) {
            if (tabFolder.getSelection()[0].equals(runTabItem)) {
                updateMainTab() ;
            } else {
                updateLogTab() ;
            }        
        }
    }
    
    private void updateMainTab() {
        if (coreHunterRun != null) {
            
            CoreHunterData coreHunterData = null ;
            try {
                CoreHunterRunArguments arguments = 
                        Activator.getDefault().getCoreHunterRunServices().getArguments(coreHunterRun.getUniqueIdentifier()) ;
                dataset = Activator.getDefault().getDatasetServices().getDataset(arguments.getDatasetId());
                coreHunterData = Activator.getDefault().getDatasetServices()
                        .getCoreHunterData(arguments.getDatasetId());
                headerViewer.setHeaders(getHeaders(coreHunterData));
            } catch (Exception e) {
                this.shellUtilitiies.handleError("Can not get dataset!", "Can not find the dataset for this result",
                        e);
            }

            textName.setText(coreHunterRun.getName());

            if (coreHunterRun.getStartDate() != null) {
                textStartDate.setText(coreHunterRun.getStartDate().toString());
            }
            
            if (coreHunterRun.getEndDate() != null)
                textEndDate.setText(coreHunterRun.getEndDate().toString());
            
            switch (coreHunterRun.getStatus()) {
                case FAILED:
                    btnViewError.setText("Failed!");
                    btnViewError.setEnabled(true);
                    btnRefreshResult.setEnabled(false);
                    btnViewDataset.setEnabled(false);
                    headerViewer.clearChecked() ;
                    break;
                case FINISHED:
                    btnViewError.setText("Finished!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(false);
                    btnViewDataset.setEnabled(true);
                    SubsetSolution solution = Activator.getDefault().getCoreHunterRunServices().getSubsetSolution(coreHunterRun.getUniqueIdentifier()) ;

                    if (coreHunterData != null) {
                        headerViewer.setChecked(getHeadersFromIndices(coreHunterData, solution)) ;
                    }
                    break;
                case NOT_STARTED:
                    btnViewError.setText("Not Started!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(true);
                    btnViewDataset.setEnabled(false);
                    headerViewer.clearChecked() ;
                    break;
                case RUNNING:
                    btnViewError.setText("Running!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(true);
                    btnViewDataset.setEnabled(false);
                    headerViewer.clearChecked() ;
                    break;
                default:
                    break;

            }
        }
    }

    private static SimpleEntity[] getHeadersFromIndices(Data data, SubsetSolution solution) {
        
        Set<Integer> ids = solution.getSelectedIDs() ;
        
        SimpleEntity[] headers = new SimpleEntity[ids.size()] ;
        
        Iterator<Integer> iterator  = ids.iterator() ;
        
        int i = 0 ;
        
        while (iterator.hasNext()) {
            headers[i] = data.getHeader(iterator.next()) ;
            ++i ;
        }
        
        return headers ;
    }

    private void updateLogTab() {
        if (coreHunterRun != null) {        
            try {
                String outputStream = Activator.getDefault().getCoreHunterRunServices().getOutputStream(coreHunterRun.getUniqueIdentifier()) ;
                
                if (outputStream != null && !outputStream.isEmpty()) {
                    logText.setText(outputStream) ;
                } else {
                    logText.setText("No log for this run"); 
                }       
                
            } catch (Exception e) {
                logText.setText("Can not find the log for this result");
            }
            
        }
    }

    private void viewError() {
        
        if (coreHunterRun != null) { 
            String message = Activator.getDefault().getCoreHunterRunServices().getErrorMessage(coreHunterRun.getUniqueIdentifier()) ;
            String error = Activator.getDefault().getCoreHunterRunServices().getErrorStream(coreHunterRun.getUniqueIdentifier()) ;
            
            shellUtilitiies.handleError("Core Hunter failed!", message, error);
        }
    }

    // TODO replace with method in Data 
    private SimpleEntity[] getHeaders(CoreHunterData coreHunterData) {
        
        int size = coreHunterData.getSize() ;
        
        SimpleEntity[] headers = new SimpleEntity[size] ;
        
        for (int index = 0; index < size ; ++index) {
            headers[index] = coreHunterData.getHeader(index) ;
        }
            
        return headers ;
    }
}