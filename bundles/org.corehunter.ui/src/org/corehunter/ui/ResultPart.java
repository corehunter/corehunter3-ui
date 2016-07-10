
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.data.CoreHunterData;
import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
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

import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;

public class ResultPart {

    public static final String ID = "org.corehunter.ui.part.result" ;

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

    @Inject
    public ResultPart() {

    }

    @PostConstruct
    public void postConstruct(Composite parent, MPart part, EPartService partService, EModelService modelService,
            MApplication application) {
        
        partUtilitiies = new PartUtilitiies(partService, modelService, application);
        shellUtilitiies = new ShellUtilitiies(parent.getShell()) ;
        
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));

        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

        TabItem runTabItem = new TabItem(tabFolder, SWT.NONE);
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
        
        TabItem logTabItem = new TabItem(tabFolder, SWT.NONE);
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
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(dataset, DatasetPart.ID));
    }
    
    private synchronized void updatePart() {

        if (coreHunterRun != null) {
            try {
                CoreHunterRunArguments arguments = 
                        Activator.getDefault().getCoreHunterRunServices().getArguments(coreHunterRun.getUniqueIdentifier()) ;
                dataset = Activator.getDefault().getDatasetServices().getDataset(arguments.getDatasetId());
                CoreHunterData coreHunterData = Activator.getDefault().getDatasetServices()
                        .getCoreHunterData(partInput.getUniqueIdentifier());
                headerViewer.setHeaders(getHeaders(coreHunterData));
            } catch (Exception e) {
                this.shellUtilitiies.handleError("Can not get dataset!", "Can not find the dataset for this result",
                        e);
            }
            
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
                    
                    break;
                case FINISHED:
                    btnViewError.setText("Finished!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(false);
                    btnViewDataset.setEnabled(true);
                    break;
                case NOT_STARTED:
                    btnViewError.setText("Not Started!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(true);
                    btnViewDataset.setEnabled(false);
                    
                    break;
                case RUNNING:
                    btnViewError.setText("Running!");
                    btnViewError.setEnabled(false);
                    btnRefreshResult.setEnabled(true);
                    btnViewDataset.setEnabled(false);
                    break;
                default:
                    break;

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