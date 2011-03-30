package org.sonatype.sitebricks.form;

import java.util.ArrayList;
import java.util.List;

public class FormDefinition
{
    String idProperty = "id";
    String title;
    String serviceUrl = "/json/person";
    List<FormField> createFormFields;
    List<ColumnDefinition> readFormColumns;
    List<ReaderFieldDefinition> readerFields = new ArrayList<ReaderFieldDefinition>();
    
    public FormDefinition( List<FormField> createFormFields, List<ColumnDefinition> readFormColumns, List<ReaderFieldDefinition> readerFields )
    {
        this.createFormFields = createFormFields;
        this.readFormColumns = readFormColumns;
        this.readerFields = readerFields;
    }

    public String getIdProperty()
    {
        return idProperty;
    }
    
    public String getTitle()
    {
        return "Yippy";
    }
    
    public String getServiceUrl()
    {
        return serviceUrl;
    }
    
    public List<FormField> getCreateFormFields()
    {
        return createFormFields;
    }

    public List<ColumnDefinition> getReadFormColumns()
    {
        return readFormColumns;
    }

    public List<ReaderFieldDefinition> getReaderFields()
    {
        return readerFields;
    }
}
