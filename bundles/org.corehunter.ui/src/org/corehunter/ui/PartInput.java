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

import uno.informatics.data.SimpleEntity;
import uno.informatics.data.pojo.SimpleEntityPojo;

public class PartInput extends SimpleEntityPojo implements Named {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String partId ;
    
    public PartInput(String uniqueIdentifier, String partId) {
        super(uniqueIdentifier);
        
        setPartId(partId) ;
    }
    
    public PartInput(String uniqueIdentifier, String name, String partId) {
        super(uniqueIdentifier, name);
        
        setPartId(partId) ;
    }
 
    public PartInput(SimpleEntity simpleEntity, String partId) {
        super(simpleEntity);
        
        setPartId(partId) ;
    }
    
    public PartInput(PartInput partInput) {
        super(partInput);
        
        setPartId(partInput.getPartId()) ;
    }

    public final String getPartId() {
        return partId;
    }

    public final void setPartId(String partId) {
        this.partId = partId;
    }
}
