package com.sap.integration.utils.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Class, which loads/saves properties from/to the configuration file. <br>
 */
public class PropertyLoader {

    /** Variable stores path and name of configuration file */
    private static String configurationFile = "config.properties";
    private static PropertiesConfiguration propertiesConfiguarion = null;

    /**
     * Method, which returns name or path and name of configuration file (it depends on input parameter to the application)
     * 
     * @return name or path and name of configuration file
     */
    public static String getConfigurationFileName() {
        return PropertyLoader.configurationFile;
    }

    /**
     * Method which set name or path and name to configuration file. When no path, but only name will be entered, application will
     * try to find configuration file on its classpath.
     * 
     * @param configurationFile - name or path and name to the configuration file.
     */
    public static void setConfigurationFileName(String configurationFile) {
        PropertyLoader.configurationFile = configurationFile;
    }

    /**
     * Method which loads values of parameter with entered name. <br>
     * 
     * @param propertyName - name of property, which will be loaded <br>
     * @return value of entered parameter <br>
     * @throws ConfigurationException
     * @throws Exception possible exception during the processing
     */
    public static String loadProperty(final String propertyName) throws Exception {
        if (propertiesConfiguarion == null) {
            propertiesConfiguarion = new PropertiesConfiguration(PropertyLoader.configurationFile);
        }

        return propertiesConfiguarion.getString(propertyName);
    }

    /**
     * Method, which saves value of entered property. <br>
     * 
     * @param propertyName - property, which will be saved <br>
     * @param propertyValue - value of property, which will be saved <br>
     * @throws ConfigurationException
     */
    public static void saveProperty(String propertyName, String propertyValue) throws ConfigurationException {
        if (propertiesConfiguarion == null) {
            propertiesConfiguarion = new PropertiesConfiguration(PropertyLoader.configurationFile);
        }
        propertiesConfiguarion.setProperty(propertyName, propertyValue);
        propertiesConfiguarion.save();
    }

    /**
     * Method, which saves value of entered property. <br>
     * 
     * @param propertyName - property, which will be saved <br>
     * @param propertyValue - value of property, which will be saved <br>
     * @throws ConfigurationException
     */
    public static void saveProperty(String propertyName, Number propertyValue) throws ConfigurationException {
        saveProperty(propertyName, propertyValue.toString());
    }
}
