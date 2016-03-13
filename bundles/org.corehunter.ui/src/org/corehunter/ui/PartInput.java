package org.corehunter.ui;

import uno.informatics.data.SimpleEntity;
import uno.informatics.data.pojo.SimpleEntityPojo;

public class PartInput extends SimpleEntityPojo implements Named {

    private String partId ;
    
    public PartInput(String name, String partId) {
        super(name);
        
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
