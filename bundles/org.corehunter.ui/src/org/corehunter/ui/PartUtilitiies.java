package org.corehunter.ui;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class PartUtilitiies {
    
    public static final String MAIN_STACK_ID = "org.corehunter.ui.mainPartStack";
    public static final String INPUT = "org.corehunter.ui.mainPartStack.input";
    
    private EPartService partService ;
    private EModelService modelService ;
    private MApplication application ;    

    public PartUtilitiies(EPartService partService, EModelService modelService, MApplication application) {
        super();
        this.partService = partService;
        this.modelService = modelService;
        this.application = application;
    }

    public MPart openPart(PartInput partInput)
    {
        MPart part = partService.createPart(partInput.getPartId());
        
        if (partInput instanceof Named)
                part.setLabel(((Named)partInput).getName()) ;
        
        part.getTransientData().put(INPUT, partInput) ;
        
        MPartStack stack = (MPartStack) getModelService().find(MAIN_STACK_ID, getApplication());

        stack.getChildren().add(part); // Add part to stack
                
        // show the part
        return getPartService().showPart(part, PartState.CREATE); 
    }

    public final EPartService getPartService() {
        return partService;
    }

    public final EModelService getModelService() {
        return modelService;
    }

    public final MApplication getApplication() {
        return application;
    }
}
