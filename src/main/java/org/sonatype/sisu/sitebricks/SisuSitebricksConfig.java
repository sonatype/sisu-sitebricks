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

public abstract class SisuSitebricksConfig extends GuiceServletContextListener {

  public static final String INJECTOR_KEY = "@INJECTOR";
  public static List<Module> modules;
  private Injector injector;
  private Logger logger;
  private File webappDirectory;
  private SisuSitebricksModulesBuilder modulesBuilder;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {

    webappDirectory = new File(servletContextEvent.getServletContext().getRealPath("/"));
    //
    // We need to set the injector here first because super.contextInitialized( servletContext ) will call getInjector() so if we have not 
    // retrieved our injector created elsewhere, say from a testing environment, a new one will be created and cause inconsistencies.
    //
    injector = (Injector) servletContextEvent.getServletContext().getAttribute(INJECTOR_KEY);

    if (injector != null) {
      logger = injector.getInstance(Logger.class);
    }

    super.contextInitialized(servletContextEvent);
  }

  protected Injector getInjector() {
    //
    // If an injector has been created and added to the servlet context then the client 
    // has decided they have what they need already.
    //
    if (injector != null) {
      return injector;
    }

    installModules(modules);

    if (logger != null) {
      for (Module m : modules) {
        logger.info("Installing module from " + getClass().getName() + ": " + m);
      }
    }

    //
    // We use the WireModule to wrap all the other modules to perform some standard cleanup
    // and de-duping of bindings
    //    
    Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new WireModule(modules));
    //
    // The injector has now been created so if there are any operations that require the injector before
    // the system starts up completely here is where they have access to the injector
    //
    onInjectorConstruction(injector);
    
    return injector;
  }
  
  protected void installModules(List<Module> modules) { 
    File configurationDirectory = new File(getWebappDirectory(), "WEB-INF");
    modulesBuilder = new SisuSitebricksModulesBuilder(configurationDirectory, getApplicationId());
    //
    // At this point all of the standard modules have been created but we want to give the
    // client the opportunity to modify the SitebricksModule which is pretty common.
    //
    modulesBuilder.setSitebricksModule(createSitebricksModule());
    
    modulesBuilder.build();
    //
    // Take all the modules created by the modules builder and add them to the list, along with
    // any modules the user may have installed themselves.
    //
    for (Module module : modulesBuilder.getModules()) {
      addModule(module);
    }
  }
  
  protected abstract SisuSitebricksModule createSitebricksModule();
    
  //
  // At this point the Injector has been created so we can use it for anything
  // we need before the application starts up.
  //
  protected void onInjectorConstruction(Injector injector) {
    modulesBuilder.onInjectorConstruction(injector);
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
  
  protected String getApplicationId() {
    return null;
  }
}
