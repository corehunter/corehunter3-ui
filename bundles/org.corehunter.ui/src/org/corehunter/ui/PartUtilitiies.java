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

import java.util.Iterator;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
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
    }
    
    /**
     * Tries to refresh the part. I
     * @param id the id of the part to be refreshed
     */
	public void refreshPart(String id) {
		MPart part = partService.findPart(id) ;
		       
		if (part != null && part.getObject() != null && part.getObject() instanceof Refreshable) {
			((Refreshable)part.getObject()).refresh();
		}
	}

    /**
     * Opens a part by part input, creating the part if needed. If a part with the
     * given input is alway created, this method will bring it to the top of the stack and give
     * it focus.
     * 
     * @param partInput the input of the part to be opened
     * @return the opened part
     */
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

    /**
     * Gets the part service.
     * 
     * @return gets the part service.
     */
    public final EPartService getPartService() {
        return partService;
    }

    /**
     * Gets the model service.
     * 
     * @return gets the model service.
     */
    public final EModelService getModelService() {
        return modelService;
    }

    /**
     * Gets the application.
     * 
     * @return gets the application.
     */
    public final MApplication getApplication() {
        return application;
    }
    
    /**
     * Gets the part input from part
     * @param part the part from which the input will be returned
     * @return part input from the given part
     */
    private PartInput getPartInput(MPart part) {
        return (PartInput)part.getTransientData().get(INPUT);
    }
}
