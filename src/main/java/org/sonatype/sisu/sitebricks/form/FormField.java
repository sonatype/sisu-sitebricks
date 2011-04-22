package org.sonatype.sisu.sitebricks.form;

//
// A form field might need to be adapted for a target system, but for now we'll focus on getting
// the prototype to work with ExtJS
//

public class FormField
{
    String name;
    String fieldLabel;
    String type;
    
    public FormField( String name, String fieldLabel, String type )
    {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.type = type;
    }
    
    public String getName()
    {
        return name;            
    }
    
    public String getFieldLabel()
    {
        return fieldLabel;
    }

    public String getType()
    {
        return type;
    }
}