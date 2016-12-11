
package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.corehunter.data.CoreHunterData;
import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.corehunter.services.simple.CoreHunterRunPojo;
import org.eclipse.e4.ui.di.Persist ;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import uno.informatics.data.Dataset;
import uno.informatics.data.SimpleEntity;

public class ResultPart {

    public static final String ID = "org.corehunter.ui.part.result" ;

    private MPart part ;
    private PartInput partInput;
	@Inject
	private MDirtyable dirty;
    
    private Text textName;
    private Text textStartDate;
    private Text textEndDate;

    private PartUtilitiies partUtilitiies;
    private ShellUtilitiies shellUtilitiies ;

    private HeaderViewer headerViewer;

    private String savedName;
    
    private Button btnRefreshResult;
    private Button btnViewDataset;
    private Button btnViewDetails;
    
    private Text logText;

    private TabFolder tabFolder;
    private TabItem runTabItem;
    private TabItem logTabItem;

	private Label lblStatus;

	private Button btnSaveButton;

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
        runComposite.setLayout(new GridLayout(3, false));

        Label lblName = new Label(runComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        textName = new Text(runComposite, SWT.BORDER);
        textName.addModifyListener(new ModifyListener(){
		      public void modifyText(ModifyEvent event) {
		    	  textNameChanged() ;
		      }
		});

        textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        btnSaveButton = new Button(runComposite, SWT.NONE);
        btnSaveButton.setText("Save");
        btnSaveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                saveName();
            }
        });

        Label lblStartDate = new Label(runComposite, SWT.NONE);
        lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblStartDate.setText("Start");

        textStartDate = new Text(runComposite, SWT.BORDER);
        textStartDate.setEditable(false);
        textStartDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(runComposite, SWT.NONE);

        Label lblEndDate = new Label(runComposite, SWT.NONE);
        lblEndDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblEndDate.setText("End");

        textEndDate = new Text(runComposite, SWT.BORDER);
        textEndDate.setEditable(false);
        textEndDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(runComposite, SWT.NONE);

        Composite buttonComposite = new Composite(runComposite, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        GridLayout gl_buttonComposite = new GridLayout(2, true);
        gl_buttonComposite.horizontalSpacing = 0;
        gl_buttonComposite.verticalSpacing = 0;
        gl_buttonComposite.marginWidth = 0;
        gl_buttonComposite.marginHeight = 0;
        buttonComposite.setLayout(gl_buttonComposite);
        
        Composite leftComposite = new Composite(buttonComposite, SWT.NONE);
        leftComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        leftComposite.setLayout(new GridLayout(3, false));
        
        Label lblStatusLabel = new Label(leftComposite, SWT.NONE);
        lblStatusLabel.setText("Status");
        
        lblStatus = new Label(leftComposite, SWT.NONE);
        lblStatus.setText("Unknown");
        
        btnViewDetails = new Button(leftComposite, SWT.NONE);
        btnViewDetails.setText("Error Detail");

        btnViewDetails.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewError();
            }
        });

        Composite rightComposite = new Composite(buttonComposite, SWT.NONE);
        rightComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        rightComposite.setLayout(new GridLayout(2, false));

        btnViewDataset = new Button(rightComposite, SWT.NONE);
        btnViewDataset.setText("View Dataset");

        btnRefreshResult = new Button(rightComposite, SWT.NONE);
        btnRefreshResult.setText("Refresh Result");

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
        new Label(runComposite, SWT.NONE);
        new Label(runComposite, SWT.NONE);
        new Label(runComposite, SWT.NONE);

        Group headerViewerComposite = new Group(runComposite, SWT.NONE);
        headerViewerComposite.setText("Selected");
        headerViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

        headerViewer = new HeaderViewer();
        headerViewer.setEditable(false);

        headerViewer.createPartControl(headerViewerComposite);
        new Label(runComposite, SWT.NONE);
        new Label(runComposite, SWT.NONE);
        new Label(runComposite, SWT.NONE);

        partInput = (PartInput) part.getTransientData().get(PartUtilitiies.INPUT);
        
        logTabItem = new TabItem(tabFolder, SWT.NONE);
        logTabItem.setText("Log");

        Composite logComposite = new Composite(tabFolder, SWT.NONE);
        logTabItem.setControl(logComposite);
        logComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        logText = new Text(logComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        logText.setEditable(false);

        if (partInput != null) {            
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
    
	@Persist
	public void save() {
		saveName() ;	
	} 

    private void saveName() {
    	
        try {
        	CoreHunterRun run = 
        			Activator.getDefault().getCoreHunterRunServices().getCoreHunterRun(partInput.getUniqueIdentifier()) ;
        	
        	CoreHunterRunPojo updatedCoreHunterRun = new CoreHunterRunPojo(run) ;
        	updatedCoreHunterRun.setName(textName.getText());
        	
        	Activator.getDefault().getCoreHunterRunServices().updateCoreHunterRun(updatedCoreHunterRun);

        	updatePart() ;
        } catch (Exception e) {
            this.shellUtilitiies.handleError("Can not save result name!", "Can not save result name!",
                    e);
        }
	}

	protected void updatePart(MPart part) {
       if (this.part == part) {
           updatePart(); 
       }
    }

    private void viewDataset() {
		CoreHunterRunArguments arguments = 
				Activator.getDefault().getCoreHunterRunServices().getArguments(partInput.getUniqueIdentifier()) ;
				
        MPart part = partUtilitiies.openPart(new PartInput(arguments.getDatasetId(), DatasetPart.ID));

        Object editor = part.getObject();
        
        if (editor instanceof DatasetPart) {
            SubsetSolution solution = Activator.getDefault().getCoreHunterRunServices().getSubsetSolution(partInput.getUniqueIdentifier()) ;

        	((DatasetPart)editor).setSolution(solution) ;
        }
    }
    
    // TODO break to sections (name only, dataset, logs) with flag to indicate which parts to update
    private synchronized void updatePart() {

		if (partInput != null) {
        	
			CoreHunterRun coreHunterRun = Activator.getDefault().getCoreHunterRunServices()
                    .getCoreHunterRun(partInput.getUniqueIdentifier());
            
            CoreHunterData coreHunterData = null ;
            try {
                CoreHunterRunArguments arguments = 
                        Activator.getDefault().getCoreHunterRunServices().getArguments(coreHunterRun.getUniqueIdentifier()) ;
                Dataset dataset = Activator.getDefault().getDatasetServices().getDataset(arguments.getDatasetId());
                coreHunterData = Activator.getDefault().getDatasetServices()
                        .getCoreHunterData(arguments.getDatasetId());
                
                SimpleEntity[] headers = Activator.getDefault().getDatasetServices().getHeaders(dataset.getUniqueIdentifier()) ;

                headerViewer.setHeaders(headers);

                savedName = coreHunterRun.getName() ;
                dirty.setDirty(false);
                
                textName.setText(savedName);
                partInput.setName(savedName);
                part.setLabel(savedName);
                updateSaveButton() ;

                if (coreHunterRun.getStartInstant() != null) {
                    textStartDate.setText(InstantFormatter.format(coreHunterRun.getStartInstant()));
                }
                
                if (coreHunterRun.getEndInstant() != null)
                    textEndDate.setText(InstantFormatter.format(coreHunterRun.getEndInstant()));
                
                switch (coreHunterRun.getStatus()) {
                    case FAILED:
                    	lblStatus.setText("Failed!");
                        btnViewDetails.setEnabled(true);
                        btnRefreshResult.setEnabled(false);
                        btnViewDataset.setEnabled(false);
                        headerViewer.clearSolution();
                        break;
                    case FINISHED:
                    	lblStatus.setText("Finished!");
                        btnViewDetails.setEnabled(false);
                        btnRefreshResult.setEnabled(false);
                        btnViewDataset.setEnabled(true);
                        SubsetSolution solution = Activator.getDefault().getCoreHunterRunServices().getSubsetSolution(coreHunterRun.getUniqueIdentifier()) ;

                        if (coreHunterData != null) {
                            headerViewer.setSolution(solution) ;
                        }
                        break;
                    case NOT_STARTED:
                    	lblStatus.setText("Not Started!");
                        btnViewDetails.setEnabled(false);
                        btnRefreshResult.setEnabled(true);
                        btnViewDataset.setEnabled(false);
                        headerViewer.clearSolution() ;
                        break;
                    case RUNNING:
                    	lblStatus.setText("Running!");
                        btnViewDetails.setEnabled(false);
                        btnRefreshResult.setEnabled(true);
                        btnViewDataset.setEnabled(false);
                        headerViewer.clearSolution() ;
                        break;
                    default:
                        break;

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
            } catch (Exception e) {
            	
                btnViewDetails.setEnabled(false);
                btnRefreshResult.setEnabled(false);
                btnViewDataset.setEnabled(false);
                
                this.shellUtilitiies.handleError("Can not get dataset!", "Can not find the dataset for this result",
                        e);
            }
        }
    }
    
	private void updateSaveButton() {
        btnSaveButton.setEnabled(dirty.isDirty());
	}

	private void textNameChanged() {
		
		if (savedName != null) {
			if (!savedName.equals(textName.getText())) {
				dirty.setDirty(true);
			} else {
				dirty.setDirty(false);
			}
		} else {
			dirty.setDirty(false);
		}
		
		updateSaveButton() ;
	}
    
    private void viewError() {
        
		if (partInput != null) {
        	
			CoreHunterRun coreHunterRun = Activator.getDefault().getCoreHunterRunServices()
                    .getCoreHunterRun(partInput.getUniqueIdentifier());
			
            String message = Activator.getDefault().getCoreHunterRunServices().getErrorMessage(coreHunterRun.getUniqueIdentifier()) ;
            String error = Activator.getDefault().getCoreHunterRunServices().getErrorStream(coreHunterRun.getUniqueIdentifier()) ;
            
            shellUtilitiies.handleError("Core Hunter failed!", message, error);
        }
    }
}