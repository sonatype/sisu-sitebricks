package org.sonatype.sisu.sitebricks.form;

public class Form
{
    private Class<?> type;
    
    public Form( Class<?> type )
    {        
        this.type = type;
    }
    
    public Class<?> getType()
    {
        return type;
    }    
}
