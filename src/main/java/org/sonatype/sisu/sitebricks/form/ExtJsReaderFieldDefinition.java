package org.sonatype.sisu.sitebricks.form;

public class ExtJsReaderFieldDefinition
    implements ReaderFieldDefinition
{
    String name;

    public ExtJsReaderFieldDefinition( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
