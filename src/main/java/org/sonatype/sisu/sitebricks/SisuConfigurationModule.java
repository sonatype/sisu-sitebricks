package org.sonatype.sisu.sitebricks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.sonatype.guice.bean.binders.ParameterKeys;

import com.google.inject.AbstractModule;

public class SisuConfigurationModule extends AbstractModule {
  private File configurationDirectory;
  private String applicationId;

  public SisuConfigurationModule(File configurationDirectory, String applicationId) {
    this.configurationDirectory = configurationDirectory;
    this.applicationId = applicationId;
  }

  @Override
  protected void configure() {
    String basePropertyName = applicationId + ".properties";
    System.out.println(basePropertyName);
    String mode = System.getProperty("appMode");
    if (mode == null) {
      mode = "dev";
    }

    String runtimeProperties = basePropertyName + "." + mode;
    System.out.println(runtimeProperties);
    Properties properties = new Properties();
    File propertiesFile = new File(configurationDirectory, runtimeProperties);

    System.out.println(propertiesFile);
    
    if (propertiesFile.exists()) {
      System.out.println("Loading configuration properties: " + propertiesFile);
      try {
        properties.load(new FileInputStream(propertiesFile));        
        System.out.println("Properties we have loaded:");
        System.out.println();
        Map<String,String> m = (Map) properties;
        for(String key: m.keySet()) {
          System.out.println(key + " ==> " + m.get(key));
        }
        System.out.println();
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
      }
    } else {
      try {
        InputStream is = getClass().getClassLoader().getResourceAsStream(basePropertyName);
        if (is != null) {
          properties.load(is);
        }
      } catch (IOException e) {
      }
    }

    if (!properties.isEmpty()) {
      bind(ParameterKeys.PROPERTIES).toInstance((Map) properties);
    }
  }
}