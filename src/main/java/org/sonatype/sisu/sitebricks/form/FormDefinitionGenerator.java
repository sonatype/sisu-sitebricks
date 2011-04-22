package org.sonatype.sisu.sitebricks.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class FormDefinitionGenerator
{
    public FormDefinition generate( Form form )
    {
        List<FormField> formFields = new ArrayList<FormField>();
        List<ColumnDefinition> readFormColumns = new ArrayList<ColumnDefinition>();
        List<ReaderFieldDefinition> readerFields = new ArrayList<ReaderFieldDefinition>();

        Field[] fields = form.getType().getFields();
        for ( int i = 0; i < fields.length; i++ )
        {
            Field field = fields[i];

            String fieldName = field.getName();

            String fieldType = "text";

            //
            // Fields for the data reader: here we want the id to be read but not displayed
            //
            ReaderFieldDefinition rf = new ExtJsReaderFieldDefinition( fieldName );
            readerFields.add( rf );

            if ( fieldName.equals( "id" ) )
            {
                continue;
            }

            //
            // Create Form
            //
            FormField formField = new FormField( fieldName, fieldName, fieldType );
            formFields.add( formField );

            //
            // Read Column: as in a summary view
            //
            ExtjsColumnEditor columnEditor = new ExtjsColumnEditor( "textfield" );
            ColumnDefinition cd = new ExtJsColumnDefinition( fieldName, true, fieldName, "gridcolumn", columnEditor );
            readFormColumns.add( cd );
        }

        return new FormDefinition( formFields, readFormColumns, readerFields );
    }
}
