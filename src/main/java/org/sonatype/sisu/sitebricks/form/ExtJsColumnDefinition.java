package org.sonatype.sisu.sitebricks.form;

public class ExtJsColumnDefinition
    implements ColumnDefinition
{
    //  {header: "Email", width: 100, sortable: true, dataIndex: 'email', xtype: 'gridcolumn', editor: { xtype: 'textfield' } },
    //  {header: "First", width: 50, sortable: true, dataIndex: 'firstName', editor: { xtype: 'textfield' } },
    //  {header: "Last", width: 50, sortable: true, dataIndex: 'lastName', editor: { xtype: 'textfield' } }

    String header;
    boolean sortable;
    String dataIndex;
    String xtype;
    ExtjsColumnEditor editor;
    
    public ExtJsColumnDefinition( String header, boolean sortable, String dataIndex, String xtype, ExtjsColumnEditor editor )
    {
        this.header = header;
        this.sortable = sortable;
        this.dataIndex = dataIndex;
        this.xtype = xtype;
        this.editor = editor;
    }

    public String getHeader()
    {
        return header;
    }

    public boolean isSortable()
    {
        return sortable;
    }

    public String getDataIndex()
    {
        return dataIndex;
    }

    public String getXtype()
    {
        return xtype;
    }

    public ExtjsColumnEditor getEditor()
    {
        return editor;
    }
}
