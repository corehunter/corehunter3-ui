
package org.corehunter.ui;

import java.util.HashMap;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;

public class PartUtilitiies {

    public static final String MAIN_STACK_ID = "org.corehunter.ui.mainPartStack";
    public static final String INPUT = "org.corehunter.ui.mainPartStack.input";

    private EPartService partService;
    private EModelService modelService;
    private MApplication application;

    private HashMap<String, MPart> parts;

    public PartUtilitiies(EPartService partService, EModelService modelService, MApplication application) {
        super();
        this.partService = partService;
        this.modelService = modelService;
        this.application = application;

        parts = new HashMap<String, MPart>();
        
        partService.addPartListener(new IPartListener() {

            @Override
            public void partActivated(MPart part) {
                //System.out.println("partActivated part="+part);
            }

            @Override
            public void partBroughtToTop(MPart part) {
                //System.out.println("partBroughtToTop part="+part);    
            }

            @Override
            public void partDeactivated(MPart part) {
                //System.out.println("partDeactivated part="+part);
            }

            @Override
            public void partHidden(MPart part) {
                //System.out.println("partHidden part="+part);
                
                parts.remove(getPartInput(part)) ;
            }

            @Override
            public void partVisible(MPart part) {
                //System.out.println("partVisible part="+part);  
            }});
    }

    public MPart openPart(PartInput partInput) {
        MPart part = null;

        if (parts.containsKey(partInput.getUniqueIdentifier())) {
            part = parts.get(partInput.getUniqueIdentifier());

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
    
                parts.put(partInput.getUniqueIdentifier(), part);
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
