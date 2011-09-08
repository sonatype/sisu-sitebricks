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
  private File webAppDirectory;

  private String applicationId;

  public SisuConfigurationModule(File baseDirectory, String application) {
    this.webAppDirectory = baseDirectory;
    this.applicationId = application;
  }

  @Override
  protected void configure() {
    String basePropertyName = applicationId + ".properties";

    String mode = System.getProperty("appMode");

    if (mode == null) {
      mode = "prod";
    }

    String runtimeProperties = basePropertyName + "." + mode;

    Properties applicationConfigurationProperties = new Properties();

    File propertiesFile = new File(new File(webAppDirectory, "WEB-INF"), runtimeProperties);

    if (propertiesFile.exists()) {
      try {
        applicationConfigurationProperties.load(new FileInputStream(propertiesFile));
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
      }
    } else {
      try {
        InputStream is = getClass().getClassLoader().getResourceAsStream(basePropertyName);

        if (is != null) {
          applicationConfigurationProperties.load(is);
        }
      } catch (IOException e) {
      }
    }

    //
    // Derived properties
    //
    String applicationDirection = System.getProperty("application.data.directory");

    if (applicationDirection != null) {
      applicationConfigurationProperties.setProperty("application.data.directory", System.getProperty("application.data.directory"));
    }

    Properties sysProperties = System.getProperties();
    // copy System user properties
    for (Map.Entry<Object, Object> sysProperty : sysProperties.entrySet()) {
      String key = (String) sysProperty.getKey();
      if (key.startsWith("user.") && !applicationConfigurationProperties.containsKey(key)) {
        applicationConfigurationProperties.setProperty(key, (String) sysProperty.getValue());
      }
    }

    if (!applicationConfigurationProperties.isEmpty()) {
      bind(ParameterKeys.PROPERTIES).toInstance((Map) applicationConfigurationProperties);
    } else {
      //
      // We should bail because the app is not going to work otherwise
      //
    }
  }
}