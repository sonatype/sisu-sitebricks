package org.sonatype.sisu.sitebricks;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.sonatype.guice.bean.binders.WireModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class SisuServletContextListener
    extends GuiceServletContextListener
{
    public static final String INJECTOR_KEY = "@INJECTOR";
    
    private List<Module> modules;

    private Injector injector;        
    
    @Override
    public void contextInitialized( ServletContextEvent servletContextEvent )
    {
        //
        // We need to set the injector here first because super.contextInitialized( servletContext ) will call getInjector() so if we have not retrieved
        // our injector created elsewhere, say from a testing environment, a new one will be created and cause inconsistencies.
        //        
        injector = (Injector) servletContextEvent.getServletContext().getAttribute( INJECTOR_KEY );
        
        super.contextInitialized( servletContextEvent );        
    }

    protected Injector getInjector()
    {
        //
        // If an injector has been added to the servlet context then the client has decided they have what they need already.
        //
        if( injector != null )
        {
            return injector;
        }
        
        installModules( modules );
        for( Module m : modules )
        {
            System.out.println( "Installing module from SisuServletContextListener: " + m );
        }        
        return Guice.createInjector( new WireModule( modules ) );
    }
    
    protected void installModules( List<Module> modules )
    {        
    }
    
    protected void addModule( Module module )
    {
        //
        // If an injector has been added to the servlet context then the client has decided they have what they need already.
        //
        if( injector != null )
        {
            return;
        }
        
        if( modules == null )
        {
            modules = new ArrayList<Module>();
        }
        modules.add( module );
    }    
}
