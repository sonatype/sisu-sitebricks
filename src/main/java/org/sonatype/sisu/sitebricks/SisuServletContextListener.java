package org.sonatype.sisu.sitebricks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.sonatype.guice.bean.binders.WireModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

public class SisuServletContextListener extends GuiceServletContextListener {
  
  public static final String INJECTOR_KEY = "@INJECTOR";
  public static List<Module> modules;
  private Injector injector;
  private Logger logger;
  private File webappDirectory;  

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    
    webappDirectory = new File(servletContextEvent.getServletContext().getRealPath("/"));

    //
    // We need to set the injector here first because super.contextInitialized( servletContext ) will call getInjector() so if we have not retrieved
    // our injector created elsewhere, say from a testing environment, a new one will be created and cause inconsistencies.
    //
    injector = (Injector) servletContextEvent.getServletContext().getAttribute(INJECTOR_KEY);

    if (injector != null) {
      logger = injector.getInstance(Logger.class);
    }
        
    super.contextInitialized(servletContextEvent);
  }
  
  protected void onInjectorConstruction(Injector injector) {
    //
    // At this point the Injector has been created so we can use it for anything
    // we need before the application starts up.
    //
  }
  
  protected Injector getInjector() {
    //
    // If an injector has been added to the servlet context then the client has decided they have what they need already.
    //
    if (injector != null) {
      return injector;
    }

    installModules(modules);

    if (logger != null) {
      for (Module m : modules) {
        logger.info("Installing module from SisuServletContextListener: " + m);
      }
    }

    Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new WireModule(modules));    
    onInjectorConstruction(injector);    
    return injector;
  }

  protected void installModules(List<Module> modules) {
  }

  protected void addModule(Module module) {
    //
    // If an injector has been added to the servlet context then the client has decided they have what they need already.
    //
    if (injector != null) {
      return;
    }

    if (modules == null) {
      modules = new ArrayList<Module>();
    }
    modules.add(module);
  }
    
  protected File getWebappDirectory() {
    return webappDirectory;
  }
}
