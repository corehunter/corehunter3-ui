
package org.corehunter.ui;

import java.util.Iterator;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartUtilitiies {

    public static final String MAIN_STACK_ID = "org.corehunter.ui.mainPartStack";
    public static final String INPUT = "org.corehunter.ui.mainPartStack.input";
    private static final Logger logger = LoggerFactory.getLogger(PartUtilitiies.class);

    private EPartService partService;
    private EModelService modelService;
    private MApplication application;

    public PartUtilitiies(EPartService partService, EModelService modelService, MApplication application) {
        super();
        this.partService = partService;
        this.modelService = modelService;
        this.application = application;

        partService.addPartListener(new IPartListener() {

            @Override
            public void partActivated(MPart part) {
            	logger.debug("partActivated part={}", part);
            }

            @Override
            public void partBroughtToTop(MPart part) {
            	logger.debug("partBroughtToTop part={}", part); 
            }

            @Override
            public void partDeactivated(MPart part) {
            	logger.debug("partDeactivated part={}", part);
            }

            @Override
            public void partHidden(MPart part) {
            	logger.debug("partHidden part={}", part);
            }

            @Override
            public void partVisible(MPart part) {
            	logger.debug("partVisible part={}", part);
            }});
    }

    public MPart openPart(PartInput partInput) {
        MPart part = null;
        
        Iterator<MPart> parts = getPartService().getParts().iterator() ;
        
        boolean found = false ;
        
		while (parts.hasNext() && !found) {
			part = parts.next() ;
			
			found = ObjectUtils.equals(partInput, getPartInput(part)) ;
        }
        
		if (found) {
            part = getPartService().showPart(part, PartState.ACTIVATE);
        } else {
            part = partService.createPart(partInput.getPartId());

            if (part != null) {
            
                if (partInput instanceof Named)
                    part.setLabel(((Named) partInput).getName());
    
                part.getTransientData().put(INPUT, partInput);
    
                MPartStack stack = (MPartStack) getModelService().find(MAIN_STACK_ID, getApplication());
    
                stack.getChildren().add(part); // Add part to stack
    
                // show the part
                part = getPartService().showPart(part, PartState.ACTIVATE);
            }
        }
		
        return part;
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
    
    private PartInput getPartInput(MPart part) {
        return (PartInput)part.getTransientData().get(INPUT);
    }

}
